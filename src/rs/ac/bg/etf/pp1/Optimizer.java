package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rs.ac.bg.etf.pp1.ast.AddOperations;
import rs.ac.bg.etf.pp1.ast.Addop;
import rs.ac.bg.etf.pp1.ast.Bool;
import rs.ac.bg.etf.pp1.ast.Break;
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
import rs.ac.bg.etf.pp1.ast.Continue;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.Div;
import rs.ac.bg.etf.pp1.ast.DoWhile;
import rs.ac.bg.etf.pp1.ast.Equal;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Factor;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.Greater;
import rs.ac.bg.etf.pp1.ast.GreaterOrEqual;
import rs.ac.bg.etf.pp1.ast.HasElse;
import rs.ac.bg.etf.pp1.ast.IfStatement;
import rs.ac.bg.etf.pp1.ast.Less;
import rs.ac.bg.etf.pp1.ast.LessOrEqual;
import rs.ac.bg.etf.pp1.ast.MethodDeclaration;
import rs.ac.bg.etf.pp1.ast.Minus;
import rs.ac.bg.etf.pp1.ast.Mod;
import rs.ac.bg.etf.pp1.ast.Mul;
import rs.ac.bg.etf.pp1.ast.Mulop;
import rs.ac.bg.etf.pp1.ast.MultiplicativeOperations;
import rs.ac.bg.etf.pp1.ast.MultiplicativeSequenceList;
import rs.ac.bg.etf.pp1.ast.NegativeTermOperation;
import rs.ac.bg.etf.pp1.ast.NestedStatements;
import rs.ac.bg.etf.pp1.ast.NoDesignatorParamsList;
import rs.ac.bg.etf.pp1.ast.NoMultiplicativeSequenceList;
import rs.ac.bg.etf.pp1.ast.NoOperations;
import rs.ac.bg.etf.pp1.ast.NoOptionalRelop;
import rs.ac.bg.etf.pp1.ast.NotEqual;
import rs.ac.bg.etf.pp1.ast.Num;
import rs.ac.bg.etf.pp1.ast.OperationList;
import rs.ac.bg.etf.pp1.ast.OptionalCondition;
import rs.ac.bg.etf.pp1.ast.OptionalConditionList;
import rs.ac.bg.etf.pp1.ast.OptionalRelOperator;
import rs.ac.bg.etf.pp1.ast.ParenthesisExpression;
import rs.ac.bg.etf.pp1.ast.Plus;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.Return;
import rs.ac.bg.etf.pp1.ast.Statement;
import rs.ac.bg.etf.pp1.ast.StatementList;
import rs.ac.bg.etf.pp1.ast.StatementsList;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Term;
import rs.ac.bg.etf.pp1.ast.TermOperation;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Obj;

/**
 * Optimizacioni prolaz posle semantičke analize.
 * - Constant folding (koraci 1–6)
 * - Dead code:
 *   korak 1 = naredbe posle return u istom bloku
 *   korak 2 = mrtve then/else grane kod const Condition
 *   korak 3 = naredbe posle break/continue u istom bloku
 *   korak 4 = mrtav back-edge do-while petlje
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
	private final Set<Statement> unreachable = new HashSet<>();
	/** do-while petlje kojima ne treba jmp nazad na DoStart */
	private final Set<DoWhile> skipLoopBackEdge = new HashSet<>();

	public boolean isConst(SyntaxNode node) {
		return constants.containsKey(node);
	}

	public int getConstValue(SyntaxNode node) {
		return constants.get(node).value;
	}

	public boolean isUnreachable(Statement stmt) {
		return unreachable.contains(stmt);
	}

	public boolean shouldSkipLoopBackEdge(DoWhile dw) {
		return skipLoopBackEdge.contains(dw);
	}

	private void setConst(SyntaxNode node, int value) {
		constants.put(node, new ConstValue(value));
	}

	private void markUnreachable(Statement stmt) {
		unreachable.add(stmt);
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
	 * Short-circuit: false && x -> false čak i ako x nije const.
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
	 * Short-circuit: true || x -> true čak i ako x nije const.
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

	// =====================================================================
	// Dead code - koraci 1–4
	// =====================================================================

	/**
	 * Poziva se POSLE constant folding prolaza.
	 * Korak 1+3: posle return / break / continue u bloku.
	 * Korak 2: then/else kad je Condition konstanta.
	 * Korak 4: mrtav jmp nazad u do-while.
	 */
	public void markDeadCode(Program prog) {
		prog.traverseTopDown(new VisitorAdaptor() {
			@Override
			public void visit(MethodDeclaration md) {
				markDeadAfterTerminators(md.getStatementList());
			}

			@Override
			public void visit(NestedStatements ns) {
				markDeadAfterTerminators(ns.getStatementList());
			}

			@Override
			public void visit(IfStatement ifs) {
				markDeadIfBranches(ifs);
			}

			@Override
			public void visit(DoWhile dw) {
				if (loopNeverContinues(dw))
					skipLoopBackEdge.add(dw);
			}
		});
	}

	/** Skuplja naredbe sleva nadesno iz levo-rekurzivne liste. */
	private void collectStatements(StatementList list, List<Statement> out) {
		if (list instanceof StatementsList) {
			StatementsList sl = (StatementsList) list;
			collectStatements(sl.getStatementList(), out);
			out.add(sl.getStatement());
		}
		// NoStatements - prazan blok
	}

	/** return / break / continue prekidaju tok u tekućem bloku. */
	private boolean isBlockTerminator(Statement s) {
		return s instanceof Return || s instanceof Break || s instanceof Continue;
	}

	private void markDeadAfterTerminators(StatementList list) {
		List<Statement> stmts = new ArrayList<>();
		collectStatements(list, stmts);

		boolean dead = false;
		for (Statement s : stmts) {
			if (dead)
				markUnreachable(s);
			// terminator ostaje živ; mrtvo je sve POSLE njega u istom bloku
			if (isBlockTerminator(s))
				dead = true;
		}
	}

	/**
	 * Const true  -> else grana mrtva (ako postoji).
	 * Const false -> then grana mrtva.
	 * Zahteva da je folding već označio Condition kao const.
	 */
	private void markDeadIfBranches(IfStatement ifs) {
		Condition cond = ifs.getCondition();
		if (!isConst(cond))
			return;

		if (getConstValue(cond) != 0) {
			if (ifs.getOptionalElse() instanceof HasElse)
				markUnreachable(((HasElse) ifs.getOptionalElse()).getStatement());
		} else
			markUnreachable(ifs.getStatement());
	}

	// --- Korak 4: mrtav back-edge ---

	private Condition getDoWhileCondition(DoWhile dw) {
		OptionalConditionList ocl = dw.getOptionalConditionList();
		if (ocl instanceof OptionalCondition)
			return ((OptionalCondition) ocl).getCondition();
		return null; // while() bez uslova = uvek true
	}

	/**
	 * Poslednja "živa" naredba u telu (simulacija terminator prolaza).
	 */
	private Statement lastLiveInBody(Statement body) {
		if (body instanceof NestedStatements) {
			List<Statement> stmts = new ArrayList<>();
			collectStatements(((NestedStatements) body).getStatementList(), stmts);
			Statement lastLive = null;
			boolean dead = false;
			for (Statement s : stmts) {
				if (!dead)
					lastLive = s;
				if (isBlockTerminator(s))
					dead = true;
			}
			return lastLive;
		}
		return body;
	}

	/**
	 * Petlja se nikad ne vraća na DoStart:
	 * - while (false) / const false uslov, ili
	 * - telo uvek završi break/return (continue i dalje treba back-edge).
	 */
	private boolean loopNeverContinues(DoWhile dw) {
		Condition cond = getDoWhileCondition(dw);
		if (cond != null && isConst(cond) && getConstValue(cond) == 0)
			return true;

		Statement lastLive = lastLiveInBody(dw.getStatement());
		return lastLive instanceof Break || lastLive instanceof Return;
	}

	/**
	 * Da li je poslednja dostižna naredba u bloku return
	 * Ako jeste, codegen ne treba ponovo da emituje exit/return na kraju metode.
	 */
	public boolean endsWithLiveReturn(StatementList list) {
		List<Statement> stmts = new ArrayList<>();
		collectStatements(list, stmts);
		Statement lastLive = null;
		for (Statement s : stmts)
			if (!isUnreachable(s))
				lastLive = s;
		return lastLive instanceof Return;
	}
}
