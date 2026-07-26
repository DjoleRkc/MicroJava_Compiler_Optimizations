package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.AddOperations;
import rs.ac.bg.etf.pp1.ast.AssignOperation;
import rs.ac.bg.etf.pp1.ast.Bool;
import rs.ac.bg.etf.pp1.ast.BracketAccessor;
import rs.ac.bg.etf.pp1.ast.Break;
import rs.ac.bg.etf.pp1.ast.Char;
import rs.ac.bg.etf.pp1.ast.ClassDeclaration;
import rs.ac.bg.etf.pp1.ast.CondFact;
import rs.ac.bg.etf.pp1.ast.CondFactor;
import rs.ac.bg.etf.pp1.ast.CondTerm;
import rs.ac.bg.etf.pp1.ast.Condition;
import rs.ac.bg.etf.pp1.ast.ConditionTerm;
import rs.ac.bg.etf.pp1.ast.Continue;
import rs.ac.bg.etf.pp1.ast.Decrement;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorName;
import rs.ac.bg.etf.pp1.ast.DesignatorWithParams;
import rs.ac.bg.etf.pp1.ast.Div;
import rs.ac.bg.etf.pp1.ast.DoStart;
import rs.ac.bg.etf.pp1.ast.DoWhile;
import rs.ac.bg.etf.pp1.ast.DotAccessor;
import rs.ac.bg.etf.pp1.ast.Else;
import rs.ac.bg.etf.pp1.ast.Equal;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Factor;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.Greater;
import rs.ac.bg.etf.pp1.ast.GreaterOrEqual;
import rs.ac.bg.etf.pp1.ast.HasElse;
import rs.ac.bg.etf.pp1.ast.Increment;
import rs.ac.bg.etf.pp1.ast.Less;
import rs.ac.bg.etf.pp1.ast.LessOrEqual;
import rs.ac.bg.etf.pp1.ast.MapExpression;
import rs.ac.bg.etf.pp1.ast.MethodDeclaration;
import rs.ac.bg.etf.pp1.ast.Minus;
import rs.ac.bg.etf.pp1.ast.Mod;
import rs.ac.bg.etf.pp1.ast.Mul;
import rs.ac.bg.etf.pp1.ast.MultiplicativeOperations;
import rs.ac.bg.etf.pp1.ast.NegativeTermOperation;
import rs.ac.bg.etf.pp1.ast.NewWithBrackets;
import rs.ac.bg.etf.pp1.ast.NewWithParams;
import rs.ac.bg.etf.pp1.ast.NoDesignatorParamsList;
import rs.ac.bg.etf.pp1.ast.NoElse;
import rs.ac.bg.etf.pp1.ast.NoOptionalRelop;
import rs.ac.bg.etf.pp1.ast.NoOptionalWidth;
import rs.ac.bg.etf.pp1.ast.NotEqual;
import rs.ac.bg.etf.pp1.ast.Num;
import rs.ac.bg.etf.pp1.ast.OptionalRelOperator;
import rs.ac.bg.etf.pp1.ast.ParenthesisExpression;
import rs.ac.bg.etf.pp1.ast.Plus;
import rs.ac.bg.etf.pp1.ast.Print;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Read;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.Return;
import rs.ac.bg.etf.pp1.ast.SetOperation;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Term;
import rs.ac.bg.etf.pp1.ast.TermOperation;
import rs.ac.bg.etf.pp1.ast.TypeMethod;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethod;
import rs.ac.bg.etf.pp1.ast.While;
import rs.ac.bg.etf.pp1.ast.Width;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc = 0;
	private int printSetPc = 0;
	private int calcMapResultPc = 0;
	private int mapsFunAdr = 0;
	private boolean virtualFunCall = false;
	private Stack<Integer> jumpCondFactStack = new Stack<>();
	private Stack<Integer> jumpConditionStack = new Stack<>();
	private Stack<Integer> skipInsideLoopStack = new Stack<>();
	private Stack<Integer> skipElseBranchStack = new Stack<>();
	private Stack<Integer> doStartStack = new Stack<>();
	private Stack<List<Integer>> jumpBreakAdrs = new Stack<>();
	private Stack<List<Integer>> jumpContinueAdrs = new Stack<>();
	private Map<Struct, Integer> tvfAdrs = new HashMap<>();
	private Obj programObj;
	private Optimizer optimizer;

	// Na skipInsideLoopStack: nema false-skoka (const true Condition)
	private static final int NO_FALSE_JUMP = -1;

	public CodeGenerator() {
		this(null);
	}

	public CodeGenerator(Optimizer optimizer) {
		this.optimizer = optimizer;

		// Chr
		Tab.chrObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(Tab.chrObj.getLevel());
		Code.put(Tab.chrObj.getLocalSymbols().size());
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);

		// Ord - Moglo je i da se obrise i da se hardcoduju broj fp i localSymbols na 1 cime ispada isti kod za Ord i Chr
		// ali ovako je lakse za citanje
		Tab.ordObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(Tab.ordObj.getLevel());
		Code.put(Tab.ordObj.getLocalSymbols().size());
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);

		// Len
		Tab.lenObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(Tab.lenObj.getLevel());
		Code.put(Tab.lenObj.getLocalSymbols().size());
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);

		/*
		 * Napomena za implementaciju: Metode addAll i Union su trebale biti implementirane tako da
		 * oponasaju HashSet iz Jave, tj da imaju "automatski" resizing na potrebnu
		 * velicinu, iako je set ovde napravljen kao fixed size, ali posto se radi pass by value nije moguce reassignovati set a
		 * .Zato je uradjeno tako da se addAll radi samo dok ima mesta u setu a.
		 *  Add je uradjen tako da ne oponasa HashSet vec samo obican array,pa ne moze da se dodaje preko predefinisane velicine seta.
		 */

		// Add
		Obj addObj = Tab.find("add");
		addObj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(addObj.getLevel());
		Code.put(addObj.getLocalSymbols().size() + 1); // +1 za iterator i

		// Dohvatanje CurrInsertionIndexa koji se cuva kao poslednji element seta
		Code.put(Code.load_n);
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload); //a[len(a) - 1] je CurrInsertionIndex

		//len(a)
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub); // len(a) - 1 jer se poslednji element ne racuna

		// if (currIndex >= len(a) - 1) return
		int jumpToReturn = Code.pc + 1; // +1 zbog putFalseJump
		Code.putFalseJump(Code.lt, 0);


		// i = 0
		Code.loadConst(0);
		Code.put(Code.store_2);

		int loopStart = Code.pc;

		// if (i > currIndex) break
		Code.put(Code.load_2); // i
		Code.put(Code.load_n);
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload); //a[len(a) - 1] je CurrInsertionIndex
		int jumpToAfterLoop = Code.pc+ 1; // +1 zbog putFalseJump
		Code.putFalseJump(Code.le, 0);


		// if (a[i] == b) return
		Code.put(Code.load_n); // a
		Code.put(Code.load_2); // i
		Code.put(Code.aload);
		Code.put(Code.load_1); // b
		int jumpToLoopContinue = Code.pc + 1; // +1 zbog putFalseJump
		Code.putFalseJump(Code.eq, 0);


		// b je vec u nizu
		Code.put(Code.exit);
		Code.put(Code.return_);

		// i++
		Code.fixup(jumpToLoopContinue);
		Code.put(Code.inc);
		Code.put(2); // locals[2] je i
		Code.put(1); // inkrement za 1



		Code.putJump(loopStart);

		int afterLoopPc = Code.pc;
		Code.fixup(jumpToAfterLoop);


		// if (b == 0) ne mora da se storuje vrednost
		Code.put(Code.load_1); // b
		Code.loadConst(0);
		int skipStorePc = Code.pc + 1; // +1 zbog putFalseJump
		Code.putFalseJump(Code.ne, 0);


		// a[currIndex] = b
		Code.put(Code.load_n); // a
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.load_n);
		Code.put(Code.dup_x1);
		Code.put(Code.pop); // zamena mesta adresi od a i currIndexa
		Code.put(Code.aload); //a[len(a) - 1] je CurrInsertionIndex
		Code.put(Code.load_1); // b
		Code.put(Code.astore);

		Code.fixup(skipStorePc);
		int skipStoreLabelPc = Code.pc;


		// currIndex++
		Code.put(Code.load_n); // a
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload); //a[len(a) - 1] je CurrInsertionIndex
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.load_n); // a
		Code.put(Code.dup_x1);
		Code.put(Code.pop); // zamena mesta val i adr
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup_x1);
		Code.put(Code.pop); // zamena mesta val i indexa


		Code.put(Code.astore); //a[len(a) - 1] je CurrInsertionIndex

		// return
		Code.fixup(jumpToReturn);
		Code.put(Code.exit);
		Code.put(Code.return_);




		// addAll

		Obj addAllObj = Tab.find("addAll");
		addAllObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(addAllObj.getLevel());
		Code.put(addAllObj.getLocalSymbols().size() + 1); // +1 za iterator i

		// i = 0
		Code.loadConst(0);
		Code.put(Code.store_2);

		int loopBStart = Code.pc;

		// if i >= len(b), break
		Code.put(Code.load_2); // i
		Code.put(Code.load_1); // b
		Code.put(Code.arraylength);
		int jumpAfterLoopB = Code.pc + 1;
		Code.putFalseJump(Code.lt, 0);

		// if currInsertionIndex >= len(a) - 1 break
		Code.put(Code.load_n); // a
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload); // a[len(a)-1] je currInsertionIndex

		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub); // len(a) - 1

		int jumpBreakLoop = Code.pc + 1;
		Code.putFalseJump(Code.lt, 0);

		// add(a, b[i])
		Code.put(Code.load_n); // a
		Code.put(Code.load_1); // b
		Code.put(Code.load_2); // i
		Code.put(Code.aload); // b[i]

		Code.put(Code.call);
		Code.put2(addObj.getAdr() - Code.pc + 1);

		// i++
		Code.put(Code.inc);
		Code.put(2); // locals[2] je i
		Code.put(1);

		Code.putJump(loopBStart);

		Code.fixup(jumpBreakLoop);
		Code.fixup(jumpAfterLoopB);

		Code.put(Code.exit);
		Code.put(Code.return_);



		// PrintSet

		printSetPc = Code.pc;
		Code.put(Code.enter);
		Code.put(1); // 1 parametar
		Code.put(2); // 1 lokalna promenljiva + 1 parametar
		Code.put(Code.store_n); //skinuti a sa steka i staviti u lokalnu promenljivu 0

		// i=0
		Code.loadConst(0);
		Code.put(Code.store_1);

		int loopStart2 = Code.pc;


		// currInsertionIndex = a[len(a) - 1]; adr(a) je vec na steku
		Code.put(Code.load_n); // a
		Code.put(Code.load_n); // a
		Code.put(Code.arraylength); // len(a)
		Code.loadConst(1);
		Code.put(Code.sub); // len(a) - 1
		Code.put(Code.aload); // a[len(a) - 1] je CurrInsertionIndex


		Code.put(Code.load_1); // i
		Code.put(Code.dup_x1);
		Code.put(Code.pop); // zamena i i CurrInsertionIndex

		// if (i >= CurrInsertionIndex) break
		int jumpToAfterLoop2 = Code.pc + 1;
		Code.putFalseJump(Code.lt, 0);

		// Print a[i]
		Code.put(Code.load_n); // a
		Code.put(Code.load_1); // i
		Code.put(Code.aload);
		Code.loadConst(0);
		Code.put(Code.print);
		Code.loadConst(' ');
		Code.loadConst(0);
		Code.put(Code.bprint);

		// i++
		Code.put(Code.load_1);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.store_1);
		Code.putJump(loopStart2);
		int afterLoop = Code.pc;
		Code.fixup(jumpToAfterLoop2);

		Code.put(Code.exit);
		Code.put(Code.return_);

		// CalcMapResult

		calcMapResultPc = Code.pc;
		Code.put(Code.enter);
		Code.put(1); // 1 parametar (arr[i)
		Code.put(3); // 2 lokalne promenljive + 1 parametar

		// suma = 0
		Code.loadConst(0);
		Code.put(Code.store_1); // suma neka bude u locals[2]

		// i=0
		Code.loadConst(0);
		Code.put(Code.store_2); // i neka bude u locals[3]


		int loopStart1 = Code.pc;



		// if (i >= len(arr)) break
		Code.put(Code.load_2); // i
		Code.put(Code.load_n); // arr
		Code.put(Code.arraylength);
		int jumpToAfterLoop1 = Code.pc + 1; // +1 zbog putFalseJump
		Code.putFalseJump(Code.lt, 0);

		// suma += func(arr[i])
		Code.put(Code.load_1); // suma
		Code.put(Code.load_n); // arr
		Code.put(Code.load_2); // i
		Code.put(Code.aload); // arr[i]
		mapsFunAdr = Code.pc; // cuvanje adrese funkcije za mapiranje
		Code.put(Code.call);
		Code.put2(mapsFunAdr - Code.pc + 1); // +1 zbog call (Moze bilo koja adr jer je mapsFunAdr nefixovan)
		Code.put(Code.add); // suma += func(arr[i])
		Code.put(Code.store_1); // suma se cuva u locals[2]

		// i++
		Code.put(Code.load_2); // i
		Code.loadConst(1);
		Code.put(Code.add); // i++
		Code.put(Code.store_2); // i se cuva u locals[3]

		Code.putJump(loopStart1);

		Code.fixup(jumpToAfterLoop1);

		Code.put(Code.load_1); // suma

		Code.put(Code.exit);
		Code.put(Code.return_);





	}

	public int mapRelopToCode(Relop r) {
		if (r instanceof Equal) return Code.eq;
		if (r instanceof NotEqual) return Code.ne;
		if (r instanceof Greater) return Code.gt;
		if (r instanceof GreaterOrEqual) return Code.ge;
		if (r instanceof Less) return Code.lt;
		if (r instanceof LessOrEqual) return Code.le;
		return -1;
	}

	public int getMainPc() {
		return mainPc;
	}

	public void setMainPc(int mainPc) {
		this.mainPc = mainPc;
	}

	/**
	 * Čvorovi koji nose "vrednost" izraza/uslova - tu Optimizer može da stavi const.
	 * Korak 7: ako je neki predak const, deca ne emituju kod.
	 */
	private boolean isValueNode(SyntaxNode node) {
		return node instanceof Expr
				|| node instanceof Term
				|| node instanceof Factor
				|| node instanceof AddOperations
				|| node instanceof MultiplicativeOperations
				|| node instanceof CondFact
				|| node instanceof CondTerm
				|| node instanceof Condition;
	}

	// Da li postoji const predak-vrednost (tada ovaj čvor ne sme da emituje ništa)
	private boolean hasConstValueAncestor(SyntaxNode node) {
		if (optimizer == null)
			return false;
		SyntaxNode parent = node.getParent();
		while (parent != null) {
			if (isValueNode(parent) && optimizer.isConst(parent))
				return true;
			parent = parent.getParent();
		}
		return false;
	}

	private boolean shouldSkipCodegen(SyntaxNode node) {
		return hasConstValueAncestor(node);
	}

	// Ako je ovaj čvor const i nema const pretka, emituj jedan loadConst
	private boolean tryEmitFoldedConst(SyntaxNode node) {
		if (optimizer == null || !optimizer.isConst(node) || hasConstValueAncestor(node))
			return false;
		Code.loadConst(optimizer.getConstValue(node));
		return true;
	}

	@Override
	public void visit(ProgName progName) {
		programObj = Tab.find(progName.getProgName());
	}



	@Override
	public void visit(TypeMethod methodTypeName){
		methodTypeName.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		int hasThis = 0;
		if(!methodTypeName.obj.getLocalSymbols().isEmpty() && methodTypeName.obj.getLocalSymbols().iterator().next().getName().equals("this"))
			hasThis = 1;
		Code.put(methodTypeName.obj.getLevel() + hasThis);
		Code.put(methodTypeName.obj.getLocalSymbols().size());



	}

	@Override
	public void visit(VoidMethod methodTypeName){
		methodTypeName.obj.setAdr(Code.pc);
		if (methodTypeName.getMethodName().equals("main")) {
			mainPc = Code.pc;





			for (Obj symbol : programObj.getLocalSymbols())
				if(symbol.getType().getKind() == Struct.Class || symbol.getType().getKind() == Struct.Interface) {

					tvfAdrs.put(symbol.getType(), Code.dataSize);
					for (Obj classSymbol : symbol.getType().getMembers())
						if(classSymbol.getKind() == Obj.Meth) {
							for(char c: classSymbol.getName().toCharArray()) {
								Code.loadConst(c);
								Code.put(Code.putstatic);
								Code.put2(Code.dataSize++);


							}
							Code.loadConst(-1);
							Code.put(Code.putstatic);
							Code.put2(Code.dataSize++);
							Code.loadConst(classSymbol.getAdr());
							Code.put(Code.putstatic);
							Code.put2(Code.dataSize++);


						}
					Code.loadConst(-2);
					Code.put(Code.putstatic);
					Code.put2(Code.dataSize++);
				}
		}

		Code.put(Code.enter);

		int hasThis = 0;
		if(!methodTypeName.obj.getLocalSymbols().isEmpty() && methodTypeName.obj.getLocalSymbols().iterator().next().getName().equals("this"))
			hasThis = 1;
		Code.put(methodTypeName.obj.getLevel() + hasThis);
		Code.put(methodTypeName.obj.getLocalSymbols().size());
	}

	@Override
	public void visit(MethodDeclaration methodDeclaration) {
		if(!methodDeclaration.getMethodSignature().getMethodTypeName().obj.getType().equals(Tab.noType)) {
			Code.put(Code.trap);
			Code.put(99);
		}
		else {
			Code.put(Code.exit);
			Code.put(Code.return_);
		}






	}

	@Override
	public void visit(ClassDeclaration classDeclaration) {
		if(classDeclaration.obj.getType().getElemType() == null) return;

		for (Obj o : classDeclaration.obj.getType().getMembersTable().symbols())
			if(o.getKind() == Obj.Meth && o.getAdr() == 0)
				for (Obj p : classDeclaration.obj.getType().getElemType().getMembersTable().symbols())
					if(p.getName().equals(o.getName()))
						o.setAdr(p.getAdr());
	}

	@Override
	public void visit(Print print) {
		if (print.getExpr().struct.getKind() == Struct.Char)
			Code.put(Code.bprint);
		else if(print.getExpr().struct.getKind() == Struct.Int)
			Code.put(Code.print);

		else if(print.getExpr().struct.getKind() == SemanticAnalyzer.setType.getKind()) {
			Code.put(Code.call);
			Code.put2(printSetPc - Code.pc + 1); // +1 zbog call
		}


	}


	@Override
	public void visit(NoOptionalWidth noOptionalWidth) {
		Code.loadConst(0);
	}


	@Override
	public void visit(Width w) {
		Code.loadConst(w.getN1());
	}

	@Override
	public void visit(Num number) {
		if (shouldSkipCodegen(number) || tryEmitFoldedConst(number))
			return;
		Code.loadConst(number.getN1());
	}

	@Override
	public void visit(Char c) {
		if (shouldSkipCodegen(c) || tryEmitFoldedConst(c))
			return;
		Code.loadConst(c.getC1());
	}

	@Override
	public void visit(Bool b) {
		if (shouldSkipCodegen(b) || tryEmitFoldedConst(b))
			return;
		Code.loadConst(b.getB1() ? 1 : 0);
	}

	@Override
	public void visit(AddOperations addOperations) {
		// ceo +/− korak je foldovan - rezultat emituje spoljašnji Term/Expr
		if (shouldSkipCodegen(addOperations) || (optimizer != null && optimizer.isConst(addOperations)))
			return;
		if (addOperations.getAddop() instanceof Plus)
			Code.put(Code.add);
		else if (addOperations.getAddop() instanceof Minus)
			Code.put(Code.sub);
	}

	@Override
	public void visit(MultiplicativeOperations mo) {
		if (shouldSkipCodegen(mo) || (optimizer != null && optimizer.isConst(mo)))
			return;
		if (mo.getMulop() instanceof Mul)
			Code.put(Code.mul);
		else if (mo.getMulop() instanceof Div)
			Code.put(Code.div);
		else if (mo.getMulop() instanceof Mod)
			Code.put(Code.rem);
	}

	/**
	 * "(1+1)" može biti najspoljašniji const Factor (npr x * (1+1)).
	 * Unutrašnji Expr se tada skipuje; ovde mora da se emituje loadConst
	 */
	@Override
	public void visit(ParenthesisExpression pe) {
		if (shouldSkipCodegen(pe))
			return;
		tryEmitFoldedConst(pe);
		// ako nije const, deca su već ostavila vrednost na steku
	}

	@Override
	public void visit(FuncCall funcCall) {
		if (shouldSkipCodegen(funcCall) || tryEmitFoldedConst(funcCall))
			return;

		if (funcCall.getDesignatorParamsList() instanceof NoDesignatorParamsList)
			Code.load(funcCall.getDesignator().obj);
		else {
			Obj func = funcCall.getDesignator().obj;

			if (func.getLocalSymbols().isEmpty() ||
					(!func.getLocalSymbols().iterator().next().getName().equals("this"))) {
				Code.put(Code.call);
				Code.put2(func.getAdr() - Code.pc + 1); // +1 zbog call

			}
			else {
				virtualFunCall = false;
				Code.put(Code.invokevirtual);

				for (char c : func.getName().toCharArray())
					Code.put4(c);

				Code.put4(-1);
			}

		}
	}

	@Override
	public void visit(DesignatorWithParams dwp) {
		Obj func = dwp.getDesignator().obj;

		if (func.getLocalSymbols().isEmpty() ||
				(!func.getLocalSymbols().iterator().next().getName().equals("this"))) {
			Code.put(Code.call);
			Code.put2(func.getAdr() - Code.pc + 1); // +1 zbog call

		}
		else {
			virtualFunCall = false;
			Code.put(Code.invokevirtual);

			for(char c: func.getName().toCharArray())
				Code.put4(c);

			Code.put4(-1);
		}

		if (!(func.getType().equals(Tab.noType)))
			Code.put(Code.pop);

	}

	@Override
	public void visit(SetOperation so) {
		// s1
		Code.load(so.getDesignator().obj);
		Code.load(so.getDesignator1().obj);
		Code.put(Code.call);
		Code.put2(Tab.find("addAll").getAdr() - Code.pc + 1); // +1 zbog call

		// s2
		Code.load(so.getDesignator().obj);
		Code.load(so.getDesignator2().obj);
		Code.put(Code.call);
		Code.put2(Tab.find("addAll").getAdr() - Code.pc + 1); // +1 zbog call
	}





	@Override
	public void visit(DesignatorName ba) {
		if(ba.obj.getKind() == Obj.Fld && !ba.obj.getName().equals("this"))
			Code.put(Code.load_n);


		if(ba.obj.getKind() == Obj.Meth)
			if(!ba.obj.getLocalSymbols().isEmpty() && ba.obj.getLocalSymbols().iterator().next().getName().equals("this")) {
				Code.put( Code.load_n);
				virtualFunCall = true;
				Code.put(Code.dup );

				Code.put(Code.getfield);
				Code.put2(0);
			}

	}




	@Override
	public void visit(BracketAccessor ba) {
		Designator des = (Designator)ba.getParent();
		Code.load(des.getDesignatorName().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);


		if(ba.getAccessorList() instanceof DotAccessor) {
			Code.load(ba.obj);

			if(ba.getAccessorList().obj.getKind() == Obj.Meth) {
				virtualFunCall = true;
				Code.put(Code.dup);
				Code.put(Code.getfield);
				Code.put2(0);
			}
		}
	}

	@Override
	public void visit(DotAccessor da) {
		if(da.getParent() instanceof BracketAccessor) return;

		Obj des =  ((Designator)da.getParent()).getDesignatorName().obj;
		if(des.getType().getKind() != Struct.Class && des.getType().getKind() != Struct.Interface) return;

		Code.load(des);

		if(da.obj.getKind() == Obj.Meth) {
			virtualFunCall = true;
			Code.put(Code.dup);
			Code.put(Code.getfield);
			Code.put2(0);
		}
	}



	@Override
	public void visit(AssignOperation assignOp) {
		Code.store(assignOp.getDesignator().obj);

	}

	@Override
	public void visit(Term t) {
		// npr 2*3 unutar x+(2*3) - Term je const, ali ceo Expr nije
		if (shouldSkipCodegen(t) || tryEmitFoldedConst(t))
			return;

		if (t.getParent() instanceof NegativeTermOperation)
			Code.put(Code.neg);
	}

	@Override
	public void visit(NewWithBrackets nwb) {

		if(nwb.getType().struct.equals(SemanticAnalyzer.setType)) {
			Code.loadConst(1);
			Code.put(Code.add);
		}

		Code.put(Code.newarray);
		if(nwb.getType().struct.equals(Tab.charType))
			Code.put(0);
		else
			Code.put(1);
	}

	@Override
	public void visit(NewWithParams nwp) {
		Code.put(Code.new_);
		Code.put2((nwp.getType().struct.getNumberOfFields() + 1) * 4); // +1 zbog tvf pokazivaca
		Code.put(Code.dup);

		Code.loadConst(tvfAdrs.get(nwp.getType().struct));
		Code.put(Code.putfield);
		Code.put2(0);
	}


	@Override
	public void visit(Increment inc) {
		if (inc.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		else if (inc.getDesignator().obj.getKind() == Obj.Fld)
			Code.put(Code.dup);
		Code.load(inc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(inc.getDesignator().obj);
	}

	@Override
	public void visit(Decrement dec) {
		if (dec.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		else if (dec.getDesignator().obj.getKind() == Obj.Fld)
			Code.put(Code.dup);
		Code.load(dec.getDesignator().obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(dec.getDesignator().obj);
	}

	@Override
	public void visit(Return ret) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(Read r) {
		if (r.getDesignator().obj.getType().equals(Tab.charType))
			Code.put(Code.bread);
		else
			Code.put(Code.read);
		Code.store(r.getDesignator().obj);

	}

	@Override
	public void visit(CondFactor cf) {
		if (shouldSkipCodegen(cf))
			return;

		// Ceo CondFact je konstanta - deca (Expr) su već preskočena
		if (optimizer != null && optimizer.isConst(cf)) {
			if (optimizer.getConstValue(cf) == 0) {
				// false -> skok na kraj ovog && lanca
				Code.putJump(0);
				jumpCondFactStack.push(Code.pc - 2);
			}
			// true -> samo nastavi (nema false-skoka)
			return;
		}

		if (cf.getOptionalRelop() instanceof NoOptionalRelop) {
			Code.loadConst(0);
			int jumpToFalse = Code.pc + 1; // +1 zbog putFalseJump
			Code.putFalseJump(Code.ne, 0);
			jumpCondFactStack.push(jumpToFalse);
		} else {
			jumpCondFactStack.push(Code.pc + 1);
			if (cf.getOptionalRelop() instanceof OptionalRelOperator) {
				OptionalRelOperator orl = (OptionalRelOperator) cf.getOptionalRelop();
				Code.putFalseJump(mapRelopToCode(orl.getRelop()), 0);
			}
		}
	}

	@Override
	public void visit(ConditionTerm ct) {
		if (shouldSkipCodegen(ct))
			return;

		// Ceo && lanac je konstanta
		if (optimizer != null && optimizer.isConst(ct)) {
			if (optimizer.getConstValue(ct) != 0) {
				// true -> kao uspešan CondTerm: skok na then
				Code.putJump(0);
				jumpConditionStack.push(Code.pc - 2);
			}
			// false -> ništa (ne ide na then)
			return;
		}

		Code.putJump(0); // Ovde znamo da je uslov tacan jer su netacni uslovi izbaceni
		int jumpToFalse = Code.pc - 2; // -2 zbog putJump;
		jumpConditionStack.push(jumpToFalse);

		while (jumpCondFactStack.size() > 0) {
			int jumpToFalse1 = jumpCondFactStack.pop();
			Code.fixup(jumpToFalse1);
		}
	}

	@Override
	public void visit(Condition cond) {
		if (optimizer != null && optimizer.isConst(cond)) {
			if (optimizer.getConstValue(cond) == 0) {
				// uvek false -> bezuslovni skok na else / izlaz iz petlje
				Code.putJump(0);
				skipInsideLoopStack.push(Code.pc - 2);
			} else
				// uvek true -> nema false-skoka
				skipInsideLoopStack.push(NO_FALSE_JUMP);
			return;
		}

		Code.putJump(0); // netacne na else
		skipInsideLoopStack.push(Code.pc - 2); // -2 zbog putJump;

		while (jumpConditionStack.size() > 0)
			Code.fixup(jumpConditionStack.pop());
	}

	@Override
	public void visit(NoElse noElse) {
		int adr = skipInsideLoopStack.pop();
		if (adr != NO_FALSE_JUMP)
			Code.fixup(adr);
	}

	@Override
	public void visit(Else e) {
		Code.putJump(0); // tacni uslovi ne ulaze u else
		skipElseBranchStack.push(Code.pc - 2);
		int adr = skipInsideLoopStack.pop();
		if (adr != NO_FALSE_JUMP)
			Code.fixup(adr); // fixujemo jump na else
	}

	@Override
	public void visit(HasElse hasElse) {
		Code.fixup(skipElseBranchStack.pop());

	}

	@Override
	public void visit(DoStart doStart) {
		doStartStack.push(Code.pc);
		jumpBreakAdrs.push(new ArrayList<>());
		jumpContinueAdrs.push(new ArrayList<>());
	}

	@Override
	public void visit(DoWhile doWhile) {
		Code.putJump(doStartStack.pop()); // jump na doStart
		if (!skipInsideLoopStack.isEmpty()) {
			int adr = skipInsideLoopStack.pop();
			if (adr != NO_FALSE_JUMP)
				Code.fixup(adr);
		}

		List<Integer> adrs = jumpBreakAdrs.pop();
		for (Integer adr : adrs)
			Code.fixup(adr);
	}

	@Override
	public void visit(While w) {

		List<Integer> adrs = jumpContinueAdrs.pop();
		for (Integer adr : adrs)
			Code.fixup(adr);
	}

	@Override
	public void visit(Break b) {

		Code.putJump(0); // jump na kraj petlje
		jumpBreakAdrs.peek().add(Code.pc - 2); // -2 zbog putJump - dodaje u listu najugnjezdenije petlje
	}

	@Override
	public void visit(Continue c) {

		Code.putJump(0); // jump na pocetak petlje
		jumpContinueAdrs.peek().add(Code.pc - 2); // -2 zbog putJump - dodaje u listu najugnjezdenije petlje
	}

	@Override
	public void visit(NegativeTermOperation no) {
		if (shouldSkipCodegen(no))
			return;
		if (tryEmitFoldedConst(no)) {
		}

		if (virtualFunCall) {
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
		}
	}

	@Override
	public void visit(TermOperation to) {
		if (shouldSkipCodegen(to))
			return;
		if (tryEmitFoldedConst(to)) {
		}

		if (virtualFunCall) {
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
		}
	}

	@Override
	public void visit(MapExpression me) {

		int adr = me.getDesignator().obj.getAdr(); // adr(func)
		Code.put2(mapsFunAdr+1, adr - mapsFunAdr);

		Code.load(me.getDesignator1().obj); // arr
		Code.put(Code.call);
		Code.put2(calcMapResultPc - Code.pc + 1); // +1 zbog call


		if(virtualFunCall) {
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
		}

	}






}