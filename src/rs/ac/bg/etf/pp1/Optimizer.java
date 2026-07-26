package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.Map;

import rs.ac.bg.etf.pp1.ast.AddOperations;
import rs.ac.bg.etf.pp1.ast.Addop;
import rs.ac.bg.etf.pp1.ast.Bool;
import rs.ac.bg.etf.pp1.ast.Char;
import rs.ac.bg.etf.pp1.ast.CondFact;
import rs.ac.bg.etf.pp1.ast.CondFactList;
import rs.ac.bg.etf.pp1.ast.CondFactor;
import rs.ac.bg.etf.pp1.ast.CondTerm;
import rs.ac.bg.etf.pp1.ast.CondTermList;
import rs.ac.bg.etf.pp1.ast.Condition;
import rs.ac.bg.etf.pp1.ast.ConditionFactorsList;
import rs.ac.bg.etf.pp1.ast.ConditionTerm;
import rs.ac.bg.etf.pp1.ast.ConditionTermList;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.Div;
import rs.ac.bg.etf.pp1.ast.Equal;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Factor;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.Greater;
import rs.ac.bg.etf.pp1.ast.GreaterOrEqual;
import rs.ac.bg.etf.pp1.ast.Less;
import rs.ac.bg.etf.pp1.ast.LessOrEqual;
import rs.ac.bg.etf.pp1.ast.Minus;
import rs.ac.bg.etf.pp1.ast.Mod;
import rs.ac.bg.etf.pp1.ast.Mul;
import rs.ac.bg.etf.pp1.ast.Mulop;
import rs.ac.bg.etf.pp1.ast.MultiplicativeOperations;
import rs.ac.bg.etf.pp1.ast.MultiplicativeSequenceList;
import rs.ac.bg.etf.pp1.ast.NegativeTermOperation;
import rs.ac.bg.etf.pp1.ast.NoDesignatorParamsList;
import rs.ac.bg.etf.pp1.ast.NoMultiplicativeSequenceList;
import rs.ac.bg.etf.pp1.ast.NoOperations;
import rs.ac.bg.etf.pp1.ast.NoOptionalRelop;
import rs.ac.bg.etf.pp1.ast.NotEqual;
import rs.ac.bg.etf.pp1.ast.Num;
import rs.ac.bg.etf.pp1.ast.OperationList;
import rs.ac.bg.etf.pp1.ast.OptionalRelOperator;
import rs.ac.bg.etf.pp1.ast.ParenthesisExpression;
import rs.ac.bg.etf.pp1.ast.Plus;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Term;
import rs.ac.bg.etf.pp1.ast.TermOperation;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Obj;

/**
 * Optimizacioni prolaz posle semantičke analize — constant folding.
 * Korak 1: literali.
 * Korak 2: folding *, /, % u Term.
 * Korak 3: folding +, - i unarni minus u Expr.
 * Korak 4: zagrade — ParenthesisExpression.
 * Korak 5: simboličke konstante (Obj.Con) kao Factor.
 * Korak 6: uslovi — Relop, &&, ||.
 */
public class Optimizer extends VisitorAdaptor {

	/** Informacija o konstantnoj vrednosti nekog AST čvora. */
	public static class ConstValue {
		public final int value;

		public ConstValue(int value) {
			this.value = value;
		}
	}

	private final Map<SyntaxNode, ConstValue> constants = new HashMap<>();

	public boolean isConst(SyntaxNode node) {
		return constants.containsKey(node);
	}

	public int getConstValue(SyntaxNode node) {
		return constants.get(node).value;
	}

	private void setConst(SyntaxNode node, int value) {
		constants.put(node, new ConstValue(value));
	}

	// --- Korak 1: literali su uvek konstante (Num, Char, Bool) ---

	@Override
	public void visit(Num num) {
		setConst(num, num.getN1());
	}

	@Override
	public void visit(Char ch) {
		setConst(ch, ch.getC1());
	}

	@Override
	public void visit(Bool bool) {
		setConst(bool, bool.getB1() ? 1 : 0);
	}

	// --- Korak 2: * / % (MultiplicativeOperations + Term) ---

	/**
	 * Leva strana mulop:
	 * - ako postoji ugnježdeni MultiplicativeOperations, to je delimični proizvod (već foldovan);
	 * - ako je lista prazna, leva strana je Factor iz roditeljskog Term-a.
	 */
	private Integer getLeftMulOperand(MultiplicativeOperations mo) {
		MultiplicativeSequenceList left = mo.getMultiplicativeSequenceList();
		if (left instanceof MultiplicativeOperations)
			return isConst(left) ? getConstValue(left) : null;

		SyntaxNode parent = mo.getParent();
		while (parent instanceof MultiplicativeOperations)
			parent = parent.getParent();
		if (parent instanceof Term) {
			Factor factor = ((Term) parent).getFactor();
			return isConst(factor) ? getConstValue(factor) : null;
		}
		return null;
	}

	private Integer applyMulop(Mulop op, int left, int right) {
		if (op instanceof Mul)
			return left * right;
		if (op instanceof Div) {
			if (right == 0)
				return null; // ne foldujemo deljenje/mod nulom
			return left / right;
		}
		if (op instanceof Mod) {
			if (right == 0)
				return null;
			return left % right;
		}
		return null;
	}

	@Override
	public void visit(MultiplicativeOperations mo) {
		Integer left = getLeftMulOperand(mo);
		Factor rightFactor = mo.getFactor();
		if (left == null || !isConst(rightFactor))
			return;

		Integer result = applyMulop(mo.getMulop(), left, getConstValue(rightFactor));
		if (result != null)
			setConst(mo, result);
	}

	@Override
	public void visit(Term term) {
		MultiplicativeSequenceList msl = term.getMultiplicativeSequenceList();

		if (msl instanceof NoMultiplicativeSequenceList) {
			Factor factor = term.getFactor();
			if (isConst(factor))
				setConst(term, getConstValue(factor));
			return;
		}

		if (isConst(msl))
			setConst(term, getConstValue(msl));
	}

	// --- Korak 3: + / - (AddOperations) + unarni minus ---

	private Integer getLeftAddOperand(AddOperations ao) {
		OperationList left = ao.getOperationList();
		if (left instanceof AddOperations)
			return isConst(left) ? getConstValue(left) : null;

		SyntaxNode parent = ao.getParent();
		while (parent instanceof AddOperations)
			parent = parent.getParent();

		Term firstTerm = null;
		boolean negate = false;
		if (parent instanceof TermOperation)
			firstTerm = ((TermOperation) parent).getTerm();
		else if (parent instanceof NegativeTermOperation) {
			firstTerm = ((NegativeTermOperation) parent).getTerm();
			negate = true;
		}

		if (firstTerm == null || !isConst(firstTerm))
			return null;

		int value = getConstValue(firstTerm);
		return negate ? -value : value;
	}

	private Integer applyAddop(Addop op, int left, int right) {
		if (op instanceof Plus)
			return left + right;
		if (op instanceof Minus)
			return left - right;
		return null;
	}

	@Override
	public void visit(AddOperations ao) {
		Integer left = getLeftAddOperand(ao);
		Term rightTerm = ao.getTerm();
		if (left == null || !isConst(rightTerm))
			return;

		Integer result = applyAddop(ao.getAddop(), left, getConstValue(rightTerm));
		if (result != null)
			setConst(ao, result);
	}

	@Override
	public void visit(TermOperation expr) {
		foldExpr(expr, expr.getTerm(), expr.getOperationList(), false);
	}

	@Override
	public void visit(NegativeTermOperation expr) {
		foldExpr(expr, expr.getTerm(), expr.getOperationList(), true);
	}

	private void foldExpr(Expr expr, Term term, OperationList ops, boolean unaryMinus) {
		if (ops instanceof NoOperations) {
			if (!isConst(term))
				return;
			int value = getConstValue(term);
			setConst(expr, unaryMinus ? -value : value);
			return;
		}

		if (isConst(ops))
			setConst(expr, getConstValue(ops));
	}

	// --- Korak 4: zagrade (Factor ::= "(" Expr ")") ---

	@Override
	public void visit(ParenthesisExpression pe) {
		Expr inner = pe.getExpr();
		if (isConst(inner))
			setConst(pe, getConstValue(inner));
	}

	// --- Korak 5: simboličke konstante (Obj.Con) ---

	@Override
	public void visit(FuncCall funcCall) {
		if (!(funcCall.getDesignatorParamsList() instanceof NoDesignatorParamsList))
			return;

		Designator des = funcCall.getDesignator();
		if (des == null || des.obj == null)
			return;

		if (des.obj.getKind() == Obj.Con)
			setConst(funcCall, des.obj.getAdr());
	}

	// --- Korak 6: uslovi (CondFactor, &&, ||) ---

	private Integer applyRelop(Relop op, int left, int right) {
		if (op instanceof Equal)
			return left == right ? 1 : 0;
		if (op instanceof NotEqual)
			return left != right ? 1 : 0;
		if (op instanceof Greater)
			return left > right ? 1 : 0;
		if (op instanceof GreaterOrEqual)
			return left >= right ? 1 : 0;
		if (op instanceof Less)
			return left < right ? 1 : 0;
		if (op instanceof LessOrEqual)
			return left <= right ? 1 : 0;
		return null;
	}

	@Override
	public void visit(CondFactor cf) {
		if (cf.getOptionalRelop() instanceof NoOptionalRelop) {
			if (isConst(cf.getExpr()))
				setConst(cf, getConstValue(cf.getExpr()) != 0 ? 1 : 0);
			return;
		}

		OptionalRelOperator oro = (OptionalRelOperator) cf.getOptionalRelop();
		Expr left = cf.getExpr();
		Expr right = oro.getExpr();
		if (!isConst(left) || !isConst(right))
			return;

		Integer result = applyRelop(oro.getRelop(), getConstValue(left), getConstValue(right));
		if (result != null)
			setConst(cf, result);
	}

	/**
	 * CondTerm ::= CondFact { && CondFact }
	 * Short-circuit: false && x → false čak i ako x nije const.
	 */
	@Override
	public void visit(ConditionTerm ct) {
		CondFact first = ct.getCondFact();
		if (!isConst(first))
			return;

		int value = getConstValue(first) != 0 ? 1 : 0;
		CondFactList list = ct.getCondFactList();

		while (list instanceof ConditionFactorsList) {
			if (value == 0) {
				setConst(ct, 0);
				return;
			}

			ConditionFactorsList cfl = (ConditionFactorsList) list;
			CondFact next = cfl.getCondFact();
			if (!isConst(next))
				return;

			value = getConstValue(next) != 0 ? 1 : 0;
			list = cfl.getCondFactList();
		}

		setConst(ct, value);
	}

	/**
	 * Condition ::= CondTerm { || CondTerm }
	 * Short-circuit: true || x → true čak i ako x nije const.
	 */
	@Override
	public void visit(Condition cond) {
		CondTerm first = cond.getCondTerm();
		if (!isConst(first))
			return;

		int value = getConstValue(first) != 0 ? 1 : 0;
		CondTermList list = cond.getCondTermList();

		while (list instanceof ConditionTermList) {
			if (value != 0) {
				setConst(cond, 1);
				return;
			}

			ConditionTermList ctl = (ConditionTermList) list;
			CondTerm next = ctl.getCondTerm();
			if (!isConst(next))
				return;

			value = getConstValue(next) != 0 ? 1 : 0;
			list = ctl.getCondTermList();
		}

		setConst(cond, value);
	}
}
