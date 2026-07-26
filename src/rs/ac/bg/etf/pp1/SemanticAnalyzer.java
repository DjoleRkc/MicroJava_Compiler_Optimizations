package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.AccessorList;
import rs.ac.bg.etf.pp1.ast.ActPar;
import rs.ac.bg.etf.pp1.ast.ActualParameter;
import rs.ac.bg.etf.pp1.ast.ActualParametersList;
import rs.ac.bg.etf.pp1.ast.AddOperations;
import rs.ac.bg.etf.pp1.ast.AssignOperation;
import rs.ac.bg.etf.pp1.ast.Bool;
import rs.ac.bg.etf.pp1.ast.BoolLiteral;
import rs.ac.bg.etf.pp1.ast.BracketAccessor;
import rs.ac.bg.etf.pp1.ast.Break;
import rs.ac.bg.etf.pp1.ast.Char;
import rs.ac.bg.etf.pp1.ast.CharLiteral;
import rs.ac.bg.etf.pp1.ast.ClassDeclaration;
import rs.ac.bg.etf.pp1.ast.ClassName;
import rs.ac.bg.etf.pp1.ast.ClassVarDeclaration;
import rs.ac.bg.etf.pp1.ast.CondFact;
import rs.ac.bg.etf.pp1.ast.CondFactor;
import rs.ac.bg.etf.pp1.ast.CondTerm;
import rs.ac.bg.etf.pp1.ast.Condition;
import rs.ac.bg.etf.pp1.ast.ConditionFactorsList;
import rs.ac.bg.etf.pp1.ast.ConditionTerm;
import rs.ac.bg.etf.pp1.ast.ConditionTermList;
import rs.ac.bg.etf.pp1.ast.ConstDeclList;
import rs.ac.bg.etf.pp1.ast.ConstDeclaration;
import rs.ac.bg.etf.pp1.ast.ConstDeclarationList;
import rs.ac.bg.etf.pp1.ast.Continue;
import rs.ac.bg.etf.pp1.ast.Decrement;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorName;
import rs.ac.bg.etf.pp1.ast.DesignatorParam;
import rs.ac.bg.etf.pp1.ast.DesignatorWithParams;
import rs.ac.bg.etf.pp1.ast.DoStart;
import rs.ac.bg.etf.pp1.ast.DoWhile;
import rs.ac.bg.etf.pp1.ast.DotAccessor;
import rs.ac.bg.etf.pp1.ast.Equal;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Extends;
import rs.ac.bg.etf.pp1.ast.Factor;
import rs.ac.bg.etf.pp1.ast.FormalParameter;
import rs.ac.bg.etf.pp1.ast.FormalParameters;
import rs.ac.bg.etf.pp1.ast.FormalParametersList;
import rs.ac.bg.etf.pp1.ast.FormalParamsList;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.GlobalVarDeclaration;
import rs.ac.bg.etf.pp1.ast.Increment;
import rs.ac.bg.etf.pp1.ast.InterfaceDeclaration;
import rs.ac.bg.etf.pp1.ast.InterfaceMethodSignature;
import rs.ac.bg.etf.pp1.ast.InterfaceName;
import rs.ac.bg.etf.pp1.ast.IsArray;
import rs.ac.bg.etf.pp1.ast.Literal;
import rs.ac.bg.etf.pp1.ast.MapExpression;
import rs.ac.bg.etf.pp1.ast.MethodDeclaration;
import rs.ac.bg.etf.pp1.ast.MethodSignature;
import rs.ac.bg.etf.pp1.ast.MultiplicativeOperations;
import rs.ac.bg.etf.pp1.ast.MultiplicativeSequenceList;
import rs.ac.bg.etf.pp1.ast.NegativeTermOperation;
import rs.ac.bg.etf.pp1.ast.NewWithBrackets;
import rs.ac.bg.etf.pp1.ast.NewWithParams;
import rs.ac.bg.etf.pp1.ast.NoAccessorList;
import rs.ac.bg.etf.pp1.ast.NoOptionalRelop;
import rs.ac.bg.etf.pp1.ast.NoReturnValue;
import rs.ac.bg.etf.pp1.ast.NotEqual;
import rs.ac.bg.etf.pp1.ast.Num;
import rs.ac.bg.etf.pp1.ast.NumberLiteral;
import rs.ac.bg.etf.pp1.ast.OperationList;
import rs.ac.bg.etf.pp1.ast.OptionalRelOperator;
import rs.ac.bg.etf.pp1.ast.ParenthesisExpression;
import rs.ac.bg.etf.pp1.ast.Print;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.Read;
import rs.ac.bg.etf.pp1.ast.Return;
import rs.ac.bg.etf.pp1.ast.ReturnVal;
import rs.ac.bg.etf.pp1.ast.ReturnValue;
import rs.ac.bg.etf.pp1.ast.SetOperation;
import rs.ac.bg.etf.pp1.ast.SetType;
import rs.ac.bg.etf.pp1.ast.StatementList;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Term;
import rs.ac.bg.etf.pp1.ast.TermOperation;
import rs.ac.bg.etf.pp1.ast.TypeMethod;
import rs.ac.bg.etf.pp1.ast.TypeOf;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VarDeclList;
import rs.ac.bg.etf.pp1.ast.VarDeclarations;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethod;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;


public class SemanticAnalyzer extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean mainFound=false;
	boolean returnFound = false;
	boolean errorDetected = false;
	Obj currentClass = Tab.noObj;
	int doWhileCount = 0;
	int nVars;
	Obj addObj;
	Obj addAllObj;
	int fpPosCnt=0;

	Logger log = Logger.getLogger(getClass());



	public static final Struct booleanType = new Struct(Struct.Bool);
	public static final Struct setType= new Struct(Struct.Enum);
	public static final Struct classType = new Struct(Struct.Class);

	public SemanticAnalyzer() {

		Tab.init();

		for(Obj local: Tab.chrObj.getLocalSymbols())
			local.setFpPos(++fpPosCnt);

		fpPosCnt = 0;

		for(Obj local: Tab.ordObj.getLocalSymbols())
			local.setFpPos(++fpPosCnt);



		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", booleanType));
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "set", setType));
		Tab.currentScope.addToLocals(addObj = new Obj(Obj.Meth, "add", Tab.noType,0,2));
		{
			Tab.openScope();
			Obj a, b;
			Tab.currentScope.addToLocals(a = new Obj(Obj.Var, "a", setType, 0, 1));
			a.setFpPos(1);
			Tab.currentScope.addToLocals(b = new Obj(Obj.Var, "b", Tab.intType, 0, 1));
			b.setFpPos(2);
			addObj.setLocals(Tab.currentScope.getLocals());
			Tab.closeScope();
		}
		Tab.currentScope.addToLocals(addAllObj = new Obj(Obj.Meth, "addAll", Tab.noType, 0, 2));
		{
			Tab.openScope();
			Obj a,b;
			Tab.currentScope.addToLocals(a = new Obj(Obj.Var, "a", setType, 0, 1));
			a.setFpPos(1);
			Tab.currentScope.addToLocals(b = new Obj(Obj.Var, "b", new Struct(Struct.Array, Tab.intType), 0, 1));
			b.setFpPos(2);
			addAllObj.setLocals(Tab.currentScope.getLocals());
			Tab.closeScope();
		}


	}

	public void print() {
		Tab.dump(new MySymbolTableVisitor());
	}



	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}

	public String getTypeName(Struct type) {
		switch(type.getKind()) {
		case 0:
			return "void";

		case 1:
			return "int";

		case 2:
			return "char";

		case 3:
			return "niz tipa " + getTypeName(type.getElemType());

		case 4:
			return type.getClass().getName() + " klase";

		case 5:
			return "bool";

		case 6:
			return "set";

		case 7:
			return "interfejsa";

		default:
			return "";

		}


	}

	private boolean checkValidExpression(Expr e) {
		if (e.struct.equals(Tab.intType) || e.struct.equals(Tab.charType) || e.struct.equals(booleanType) || e.struct.equals(setType))
			return true;


		return false;


	}




	public boolean passed() {
		return !errorDetected;
	}

	public Obj bracketAccessorCheck(Obj inner, BracketAccessor accessor) {

		if(inner.getKind() != Obj.Var || inner.getType().getKind() != Struct.Array) {
			report_error("Greska: " +inner.getName()+" nije adekvatna promenljiva niza", accessor);
			return Tab.noObj;
		}
		if (!accessor.getExpr().struct.equals(Tab.intType)) {
			report_error("Greska: indeks niza " + inner.getName() + " nije tipa int", accessor);
			return Tab.noObj;
		}
		report_info("Pristup elementu niza " + inner.getName(), accessor);
		return new Obj(Obj.Elem, inner.getName() + "[$]", inner.getType().getElemType());

	}

	private Obj dotAccessorCheck(Obj inner, DotAccessor accessor) {
		Obj obj = inner.getType().getMembersTable().searchKey(accessor.getI1());
		if (obj == null) {
			report_error("Greska: Ime " + accessor.getI1() + " nije polje klase", accessor);
			return Tab.noObj;
		}
		if (obj.getKind() == Obj.Fld)
			report_info("Pristup polju klase " + accessor.getI1(), accessor);
		else if (obj.getKind() == Obj.Meth)
			report_info("Poziv metoda klase " + accessor.getI1(), accessor);

		return obj;


	}



	@Override
	public void visit(Print print) {
		Expr e = print.getExpr();
		if(!checkValidExpression(e))
			report_error("Greska: Izraz unutar printa nije validan", e);
		else
			printCallCount++;
	}

	@Override
	public void visit(Return ret) {
		ReturnValue returnValue = ret.getReturnValue();

		if(currentMethod.equals(Tab.noObj)) {
			report_error("Greska: Return iskaz se nalazi van metode", returnValue);
			return;

		}

		if (returnValue instanceof NoReturnValue) {
			if (currentMethod.getType() != Tab.noType)
				report_error("Greska: Funkcija " + currentMethod.getName() + " treba da vrati vrednost, ali je prazan return.", returnValue);
		} else if (returnValue instanceof ReturnVal) {
			ReturnVal val = (ReturnVal) returnValue;

			if (!currentMethod.getType().equals(val.getExpr().struct))
				report_error("Greska: Povratna vrednost funkcije " + currentMethod.getName() + " nije ekvivalentna sa vracenim tipom", returnValue);
			else
				returnFound = true;
		}
	}

	@Override
	public void visit(ProgName progName){
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}

	@Override
	public void visit(Program program){
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		program.getProgName().obj.setLevel(nVars);
		Tab.closeScope();
	}

	@Override
	public void visit(NumberLiteral numberLiteral) {
		numberLiteral.struct = Tab.intType;
	}

	@Override
	public void visit(CharLiteral charLiteral) {
		charLiteral.struct = Tab.charType;
	}

	@Override
	public void visit(BoolLiteral boolLiteral) {
		boolLiteral.struct = booleanType;

	}

	@Override
	public void visit(Read r) {
		Designator des = r.getDesignator();


		if(!des.obj.getType().equals(Tab.intType) && !des.obj.getType().equals(Tab.charType) && !des.obj.getType().equals(booleanType))
			report_error("Greska: Ime nad kojim se radi read, " + des.getDesignatorName().obj.getName() + ", nije tipa int, char ili bool", des);


		else if(des.obj.getKind() != Obj.Var && des.obj.getKind() != Obj.Elem && des.obj.getKind() != Obj.Fld)
			report_error("Greska: Ime nad kojim se radi read, " + des.getDesignatorName().obj.getName() + ", nije promenljiva, element niza ili polje klase", des);
		else
			report_info("Koriscenje imena " + des.getDesignatorName().obj.getName() + " u read operaciji", des);
	}


	@Override
	public void visit(DesignatorName dn) {
		Obj obj = Tab.find(dn.getName());
		if (obj == Tab.noObj) {
			report_error("Greska: ime " + dn.getName() + " nije deklarisano", dn);
			dn.obj = Tab.noObj;
			return;
		}
		if (obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem && obj.getKind() != Obj.Fld
				&& obj.getKind() != Obj.Con && obj.getKind() != Obj.Meth) {
			report_error("Greska: ime " + dn.getName() + " nije adekvatnog tipa", dn);
			dn.obj = Tab.noObj;
			return;
		}
		dn.obj = obj;
	}





	@Override
	public void visit(GlobalVarDeclaration varDecl) {
		Struct varType = varDecl.getType().struct;
		if (varDecl.getArraySpecifier() instanceof IsArray)
			varType = new Struct(Struct.Array, varType);

		Obj existing = Tab.find(varDecl.getVarName());
		if (existing != Tab.noObj)
			report_error("Greska: Globalna promenljiva sa imenom " + varDecl.getVarName() + " vec postoji", varDecl);
		else {
			report_info("Deklarisana globalna promenljiva " + varDecl.getVarName(), varDecl);
			Tab.insert(Obj.Var, varDecl.getVarName(), varType);
		}

		VarDeclList varDeclList = varDecl.getVarDeclList();
		while (varDeclList instanceof VarDeclarations) {
			VarDeclarations varDeclarations = (VarDeclarations) varDeclList;
			varType = varDecl.getType().struct;
			if (varDeclarations.getArraySpecifier() instanceof IsArray)
				varType = new Struct(Struct.Array, varType);

			existing = Tab.find(varDeclarations.getVarName());
			if (existing != Tab.noObj)
				report_error("Greska: Globalna promenljiva sa imenom " + varDeclarations.getVarName() + " vec postoji", varDecl);
			else {
				report_info("Deklarisana globalna promenljiva " + varDeclarations.getVarName(), varDecl);
				Tab.insert(Obj.Var, varDeclarations.getVarName(), varType);
			}

			varDeclList = varDeclarations.getVarDeclList();
		}
	}

	@Override
	public void visit(Increment inc) {
		Designator operand = inc.getDesignator();


		if(!(operand.obj.getKind() == Obj.Var || operand.obj.getKind() == Obj.Elem || operand.obj.getKind() == Obj.Fld))
			report_error("Greska: ime " + operand.getDesignatorName().getName() + " nije promenljiva, element niza ili polje klase, pa se ne moze inkrementirati", operand);

		else if(!operand.obj.getType().equals(Tab.intType))
			report_error("Greska: promenljiva sa imenom " + operand.getDesignatorName().getName() + " nije tipa int, pa se ne moze inkrementirati", operand);



		else
			report_info("Koriscenje imena " + operand.getDesignatorName().getName() + " u operaciji inkrementiranja", operand);
	}

	@Override
	public void visit(Decrement dec) {
		Designator operand = dec.getDesignator();


		if(!(operand.obj.getKind() == Obj.Var || operand.obj.getKind() == Obj.Elem || operand.obj.getKind() == Obj.Fld))
			report_error("Greska: ime " + operand.getDesignatorName().getName() + " nije promenljiva, element niza ili polje klase, pa se ne moze dekrementirati", operand);

		else if(!operand.obj.getType().equals(Tab.intType))
			report_error("Greska: promenljiva sa imenom " + operand.getDesignatorName().getName() + " nije tipa int, pa se ne moze dekrementirati", operand);



		else
			report_info("Koriscenje imena " + operand.getDesignatorName().getName() + " u operaciji dekrementiranja", operand);
	}

	@Override
	public void visit(VarDecl varDecl) {
		Struct varType = varDecl.getType().struct;
		if (varDecl.getArraySpecifier() instanceof IsArray)
			varType = new Struct(Struct.Array, varType);

		Obj existing = Tab.currentScope.findSymbol(varDecl.getVarName());
		if (existing != null)
			report_error("Greska: Lokalna promenljiva sa imenom " + varDecl.getVarName() + " vec postoji", varDecl);
		else {
			report_info("Deklarisana lokalna promenljiva " + varDecl.getVarName(), varDecl);
			Tab.insert(Obj.Var, varDecl.getVarName(), varType);
		}

		VarDeclList varDeclList = varDecl.getVarDeclList();
		while (varDeclList instanceof VarDeclarations) {
			VarDeclarations varDeclarations = (VarDeclarations) varDeclList;
			varType = varDecl.getType().struct;
			if (varDeclarations.getArraySpecifier() instanceof IsArray)
				varType = new Struct(Struct.Array, varType);

			existing = Tab.currentScope.findSymbol(varDeclarations.getVarName());
			if (existing != null)
				report_error("Greska: Lokalna promenljiva sa imenom " + varDeclarations.getVarName() + " vec postoji", varDecl);
			else {
				report_info("Deklarisana lokalna promenljiva " + varDeclarations.getVarName(), varDecl);
				Tab.insert(Obj.Var, varDeclarations.getVarName(), varType);
			}

			varDeclList = varDeclarations.getVarDeclList();
		}
	}


	@Override
	public void visit(ConstDeclaration constDecl) {

		Obj existing = Tab.find(constDecl.getConstName());
		Struct declaredType = constDecl.getType().struct;
		Struct valueType = constDecl.getLiteral().struct;

		if (existing != Tab.noObj)
			report_error("Greska: Konstanta sa imenom " + constDecl.getConstName() + " vec postoji", constDecl);



		else if (declaredType.equals(SemanticAnalyzer.booleanType) && valueType != SemanticAnalyzer.booleanType)
			report_error("Greska: Ocekivana je bool vrednost za konstantu " + constDecl.getConstName() + " a dobijena je " + getTypeName(valueType), constDecl);

		else if (!declaredType.compatibleWith(valueType))
			report_error("Greska: Vrednost konstante " + constDecl.getConstName() +
					" nije kompatibilna sa tipom " + getTypeName(declaredType), constDecl);

		else {
			report_info("Deklarisana konstanta " + constDecl.getConstName(), constDecl);
			Obj varNode = Tab.insert(Obj.Con, constDecl.getConstName(), constDecl.getType().struct);
			Literal l = constDecl.getLiteral();

			if (l instanceof NumberLiteral) {
				NumberLiteral n = (NumberLiteral) l;
				varNode.setAdr(n.getN1());
			}

			else if(l instanceof CharLiteral) {
				CharLiteral c = (CharLiteral) l;
				varNode.setAdr(c.getC1());
			}

			else {
				BoolLiteral b = (BoolLiteral) l;

				if(b.getB1())
					varNode.setAdr(1);
				else
					varNode.setAdr(0);
			}
		}


		ConstDeclList constDeclList = constDecl.getConstDeclList();
		while (constDeclList instanceof ConstDeclarationList) {
			ConstDeclarationList constDeclarations = (ConstDeclarationList) constDeclList;
			valueType = constDeclarations.getLiteral().struct;
			existing = Tab.find(constDeclarations.getConstName());
			if (existing != Tab.noObj)
				report_error("Greska: Konstanta sa imenom " + constDeclarations.getConstName() + " vec postoji", constDecl);

			else if (declaredType.equals(SemanticAnalyzer.booleanType) && valueType != SemanticAnalyzer.booleanType)
				report_error("Greska: Ocekivana je bool vrednost za konstantu " + constDeclarations.getConstName() + " a dobijena je " + getTypeName(valueType), constDecl);

			else if (!declaredType.compatibleWith(valueType))
				report_error("Greska: Vrednost konstante " + constDeclarations.getConstName() +
						" nije kompatibilna sa tipom " + getTypeName(declaredType), constDecl);
			else {
				report_info("Deklarisana konstanta " + constDeclarations.getConstName(), constDecl);
				Obj varNode = Tab.insert(Obj.Con, constDeclarations.getConstName(), constDecl.getType().struct);
				Literal l = constDeclarations.getLiteral();

				if (l instanceof NumberLiteral) {
					NumberLiteral n = (NumberLiteral) l;
					varNode.setAdr(n.getN1());
				}

				else if(l instanceof CharLiteral) {
					CharLiteral c = (CharLiteral) l;
					varNode.setAdr(c.getC1());
				}

				else {
					BoolLiteral b = (BoolLiteral) l;

					if(b.getB1())
						varNode.setAdr(1);
					else
						varNode.setAdr(0);
				}
			}

			constDeclList = constDeclarations.getConstDeclList();
		}
	}

	@Override
	public void visit(TypeOf type){
		Obj typeNode = Tab.find(type.getTypeName());
		if(typeNode == Tab.noObj){
			report_error("Greska: Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else if(Obj.Type == typeNode.getKind())
			type.struct = typeNode.getType();
		else{
			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
			type.struct = Tab.noType;
		}
	}

	@Override
	public void visit(SetType type){
		Obj typeNode = Tab.find("set");
		if(typeNode == Tab.noObj){
			report_error("Greska: Nije pronadjen tip set u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else if(Obj.Type == typeNode.getKind())
			type.struct = typeNode.getType();
		else{
			report_error("Greska: Ime set ne predstavlja tip!", type);
			type.struct = Tab.noType;
		}
	}

	@Override
	public void visit(Term term){

		Factor left = term.getFactor();



		MultiplicativeSequenceList msl = term.getMultiplicativeSequenceList();

		while (msl instanceof MultiplicativeOperations) {
			if (!left.struct.equals(Tab.intType)) {
				report_error("Greska: Izraz sa leve strane operacije mnozenja nije tipa int", term);
				term.struct = Tab.noType;
				return;
			}
			MultiplicativeOperations mo = (MultiplicativeOperations) msl;
			Factor right = mo.getFactor();
			if (!right.struct.equals(left.struct)) {
				report_error("Greska: Izraz sa desne strane operacije mnozenja nije tipa int", term);
				term.struct = Tab.noType;
				break;
			}
			msl = mo.getMultiplicativeSequenceList();
		}

		if (term.struct == null)
			term.struct = left.struct;

	}

	@Override
	public void visit(AssignOperation assignOp) {
		Designator assignedTo = assignOp.getDesignator();
		Expr expr = assignOp.getExpr();
		if (assignedTo.obj.getKind() != Obj.Var && assignedTo.obj.getKind() != Obj.Elem && assignedTo.obj.getKind() != Obj.Fld)
			report_error("Greska: ime " + assignedTo.getDesignatorName().getName() + " nije promenljiva, polje klase ili element niza",assignedTo);


		else if (expr.struct.assignableTo(assignedTo.obj.getType()))

			report_info("Dodela vrednosti promenljivoj " + assignedTo.obj.getName(), assignedTo);

		else if (expr.struct.getKind() == Struct.Class) {
			List<Struct> parents = new ArrayList<>();

			Struct current = expr.struct;

			while (current != null) {
				parents.add(current);
				current = current.getElemType();
			}

			if(!(parents.contains(expr.struct)))
				report_error("Greska: Klase nisu kompatibilne pri dodeli",assignedTo);
			else
				report_info("Instanciranje klase" , assignedTo);
		}

		else
			report_error("Greska: Tip izraza nije kompatibilan pri dodeli sa imenom " + assignedTo.obj.getName(),assignedTo);
	}



	@Override
	public void visit(NegativeTermOperation expr) {
		Term first = expr.getTerm();

		if (!first.struct.equals(Tab.intType)) {
			report_error("Greska: Izraz unutar negacije nije tipa int", expr);
			expr.struct = Tab.noType;
		} else {
			OperationList ol = expr.getOperationList();

			while (ol instanceof AddOperations) {
				AddOperations ao = (AddOperations) ol;
				if (!ao.getTerm().struct.equals(Tab.intType)) {
					report_error("Greska: Izraz unutar negacije nije tipa int", expr);
					expr.struct = Tab.noType;
					break;
				}
				ol = ao.getOperationList();
			}

		}



		if (expr.struct == null)
			expr.struct = Tab.intType;

	}

	@Override
	public void visit(TermOperation expr) {
		OperationList ol = expr.getOperationList();

		while (ol instanceof AddOperations) {
			AddOperations ao = (AddOperations) ol;
			if (!ao.getTerm().struct.equals(expr.getTerm().struct)) {
				report_error("Greska: Izraz nije validan", expr);
				expr.struct = Tab.noType;
				break;
			}
			ol = ao.getOperationList();
		}

		if (expr.struct == null)
			expr.struct = expr.getTerm().struct;
	}

	@Override
	public void visit(MapExpression mapExpr) {
		boolean found = false;

		Designator left = mapExpr.getDesignator();

		Designator right = mapExpr.getDesignator1();

		if((left.obj.getKind() == Obj.Meth && left.obj.getLevel() == 1 && left.obj.getType().equals(Tab.intType))) {
			Collection<Obj> locals = left.obj.getLocalSymbols();
			for (Obj o : locals)
				if (o.getFpPos() == 1 && o.getType().equals(Tab.intType)) {
					found=true;
					break;
				}


		}

		if(!found) {
			report_error("Greska: Leva strana izraza map nije funkcija koja prima jedan parametar tipa int i vraca int", left);
			mapExpr.struct = Tab.noType;
		}

		if (!(right.obj.getType().getKind() == Struct.Array && right.obj.getType().getElemType().equals(Tab.intType))) {
			report_error("Greska: Desna strana izraza map nije niz celih brojeva", right);
			mapExpr.struct = Tab.noType;
		}

		mapExpr.struct = Tab.intType;

	}







	@Override
	public void visit(Designator designator) {

		designator.obj = designator.getDesignatorName().obj;

		Obj prev = null;

		AccessorList accessorList = designator.getAccessorList();

		while(!(accessorList instanceof NoAccessorList)) {
			prev=designator.obj;

			if(accessorList instanceof BracketAccessor) {
				designator.obj = bracketAccessorCheck(prev, (BracketAccessor) accessorList);
				accessorList.obj = designator.obj;
				if (designator.obj == Tab.noObj)
					break;

				accessorList = ((BracketAccessor) accessorList).getAccessorList();
			}


			else if(accessorList instanceof DotAccessor) {
				designator.obj = dotAccessorCheck(prev, (DotAccessor) accessorList);
				accessorList.obj = designator.obj;
				if (designator.obj == Tab.noObj)
					break;
				accessorList = ((DotAccessor) accessorList).getAccessorList();
			}

		}



	}

	@Override
	public void visit(ActPar ap) {
		ap.struct = ap.getExpr().struct;
	}

	@Override
	public void visit(DesignatorWithParams funcCall) {
		Obj func = funcCall.getDesignator().obj;

		if(func == Tab.noObj)
			report_error("Greska: funkcija " + funcCall.getDesignator().getDesignatorName().getName() + " nije definisana", funcCall);
		else if(Obj.Meth == func.getKind()) {
			List<Struct> formPars = new ArrayList<Struct>();
			for (Obj local : func.getLocalSymbols())
				if (local.getFpPos() > 0 && local.getKind() == Obj.Var && !local.getName().equals("this"))
					formPars.add(local.getType());



			List<Struct> actPars = new ArrayList<Struct>();

			if(funcCall.getActualParamsList() instanceof ActualParametersList) {
				ActualParametersList apl = (ActualParametersList) funcCall.getActualParamsList();


				actPars.add(apl.getActPar().struct);

				if(apl.getActualParams() instanceof ActualParameter) {
					ActualParameter ap = (ActualParameter) apl.getActualParams();

					while(ap instanceof ActualParameter) {
						actPars.add(ap.getActPar().struct);
						if(!(ap.getActualParams() instanceof ActualParameter))
							break;
						ap = (ActualParameter) ap.getActualParams();
					}
				}

			}



			if(actPars.size() == formPars.size()) {
				boolean flag=false;
				for(int i=0;i<actPars.size();i++)
					if (!actPars.get(i).assignableTo(formPars.get(i))) {
						flag=true;
						report_error("Greska: Stvarni parametar funkcije " + func.getName() + " na poziciji " + i + " nije kompatibilan pri dodeli sa formalnim parametrom na poziciji " + i, funcCall);
						break;
					}

				if(!flag)
					report_info("Pronadjen poziv funkcije " + func.getName(), funcCall);
			} else
				report_error("Greska: Broj formalnih i stvarnih parametara funkcije " + func.getName() + " nije isti", funcCall);

		}

		else if(func.getKind() != Obj.Meth)
			report_error("Greska: ime " + func.getName() + " nije funkcija", funcCall);
	}

	@Override
	public void visit(SetOperation	so) {
		Designator left = so.getDesignator();
		Designator right1 = so.getDesignator1();
		Designator right2 = so.getDesignator2();

		if(!left.obj.getType().equals(setType) || !right1.obj.getType().equals(setType) || !right2.obj.getType().equals(setType))
			report_error("Greska: Operacija union moze se vrsiti samo nad skupovima", so);
		else
			report_info("Poziv operacije union nad skupovima " + right1.getDesignatorName().getName() + " i " + right2.getDesignatorName().getName() + " i dodela skupu " + left.getDesignatorName().getName(), so);

	}

	@Override
	public void visit(DoStart doStart) {
		doWhileCount++;
	}

	@Override
	public void visit(DoWhile doWhile) {
		doWhileCount--;
	}

	@Override
	public void visit(Break b) {
		if (doWhileCount == 0)
			report_error("Greska: Break naredba se nalazi van do-while petlje", b);
	}

	@Override
	public void visit(Continue c) {
		if (doWhileCount == 0)
			report_error("Greska: Continue naredba se nalazi van do-while petlje", c);
	}

	@Override
	public void visit(CondFactor cf) {
		if(cf.getOptionalRelop() instanceof NoOptionalRelop) {
			if (!cf.getExpr().struct.equals(booleanType)) {
				report_error("Greska: Izraz unutar uslova nije tipa bool", cf);
				cf.struct = Tab.noType;
			}

			else
				cf.struct = booleanType;

		}
		else if(cf.getOptionalRelop() instanceof OptionalRelOperator) {
			OptionalRelOperator or = (OptionalRelOperator) cf.getOptionalRelop();
			if((cf.getExpr().struct.isRefType() || or.getExpr().struct.isRefType()) && !(or.getRelop() instanceof Equal || or.getRelop() instanceof NotEqual)) {
				report_error("Nedozvoljen relacioni operator za tipove klase ili niza", or);
				cf.struct = Tab.noType;
			}
			else if (!or.getExpr().struct.compatibleWith(cf.getExpr().struct)) {
				report_error("Greska: Izrazi unutar uslova nisu kompatibilnog tipa", cf);
				cf.struct = Tab.noType;
			}
			cf.struct = booleanType;
		}

	}

	@Override
	public void visit(ConditionTerm ct) {
		CondFact cf = ct.getCondFact();

		if (!(cf.struct.getKind() == Struct.Bool)) {
			ct.struct = Tab.noType;
			return;
		}
		if(ct.getCondFactList() instanceof ConditionFactorsList) {
			ConditionFactorsList cfl = (ConditionFactorsList) ct.getCondFactList();
			while (cfl instanceof ConditionFactorsList) {
				CondFact cf1 = cfl.getCondFact();
				if (!cf1.struct.equals(booleanType)) {
					ct.struct = Tab.noType;
					break;
				}
				if (!(cfl.getCondFactList() instanceof ConditionFactorsList))
					break;
				cfl = (ConditionFactorsList) cfl.getCondFactList();
			}
		}

		if (ct.struct == null)
			ct.struct = booleanType;

	}

	@Override
	public void visit(Condition cond) {
		CondTerm term = cond.getCondTerm();

		if(term.struct.getKind() != Struct.Bool) {
			cond.struct = Tab.noType;
			return;
		}
		if(cond.getCondTermList() instanceof ConditionTermList) {
			ConditionTermList ctl = (ConditionTermList) cond.getCondTermList();
			while (ctl instanceof ConditionTermList) {
				CondTerm ct = ctl.getCondTerm();
				if (!ct.struct.equals(booleanType)) {
					cond.struct = Tab.noType;
					break;
				}
				if (!(ctl.getCondTermList() instanceof ConditionTermList))
					break;
				ctl = (ConditionTermList) ctl.getCondTermList();
			}
		}

		if (cond.struct == null)
			cond.struct = booleanType;

	}



	@Override
	public void visit(FuncCall funcCall){



		if(funcCall.getDesignatorParamsList() instanceof DesignatorParam) {
			Obj func = funcCall.getDesignator().obj;

			if(func == Tab.noObj) {
				report_error("Greska: funkcija " + funcCall.getDesignator().getDesignatorName().getName() + " nije definisana", funcCall);
				funcCall.struct = Tab.noType;
			}

			else if(Obj.Meth == func.getKind()){

				List<Struct> formPars = new ArrayList<Struct>();
				for (Obj local : func.getLocalSymbols())
					if ((local.getFpPos() > 0 && local.getKind() == Obj.Var && !local.getName().equals("this")) || func.getName().equals("len"))
						formPars.add(local.getType());

				List<Struct> actPars = new ArrayList<Struct>();

				if(funcCall.getDesignatorParamsList() instanceof DesignatorParam) {
					DesignatorParam dp = (DesignatorParam) funcCall.getDesignatorParamsList();

					if(dp.getActualParamsList() instanceof ActualParametersList) {
						ActualParametersList apl = (ActualParametersList) dp.getActualParamsList();

						actPars.add(apl.getActPar().struct);

						if(apl.getActualParams() instanceof ActualParameter) {
							ActualParameter ap = (ActualParameter) apl.getActualParams();

							while(ap instanceof ActualParameter) {
								actPars.add(ap.getActPar().struct);
								if(!(ap.getActualParams() instanceof ActualParameter))
									break;
								ap = (ActualParameter) ap.getActualParams();
							}
						}

					}

					if(actPars.size() == formPars.size()) {
						boolean flag=false;
						for(int i=0;i<actPars.size();i++)
							if (!actPars.get(i).assignableTo(formPars.get(i))) {
								flag=true;
								report_error("Greska: Stvarni parametar funkcije " + func.getName() + " na poziciji " + i + " nije kompatibilan pri dodeli sa formalnim parametrom na poziciji " + i, funcCall);
								break;
							}

						if(!flag) {
							report_info("Pronadjen poziv funkcije " + func.getName(), funcCall);
							funcCall.struct = func.getType();
						}

					} else
						report_error("Greska: Broj formalnih i stvarnih parametara funkcije " + func.getName() + " nije isti", funcCall);



				}


			}else if(func.getKind() != Obj.Meth){
				report_error("Greska: ime " + func.getName() + " nije funkcija", funcCall);
				funcCall.struct = Tab.noType;
			}
		} else
			funcCall.struct = funcCall.getDesignator().obj.getType();

	}

	@Override
	public void visit(ParenthesisExpression pe) {
		pe.struct = pe.getExpr().struct;
	}


	@Override
	public void visit(Num number) {
		number.struct = Tab.intType;
	}

	@Override
	public void visit(Char c) {
		c.struct = Tab.charType;
	}

	@Override
	public void visit(Bool b) {
		b.struct = booleanType;
	}

	@Override
	public void visit(NewWithBrackets n) {
		if(!n.getExpr().struct.equals(Tab.intType)) {
			report_error("Greska: Izraz za velicinu niza nije tipa int", n);
			n.struct = Tab.noType;
		} else
			if(getTypeName(n.getType().struct).equals("set"))
				n.struct = setType;
			else
				n.struct = new Struct(Struct.Array, n.getType().struct);

	}

	@Override
	public void visit(NewWithParams n) {
		if(n.getType().struct.getKind() == Struct.Class)
			n.struct = new Struct(Struct.Class, n.getType().struct.getMembersTable());

		else {
			report_error("Greska: Nije moguce inicijalizovati objekat necega sto nije klasa", n);
			n.struct = Tab.noType;
		}
	}


	@Override
	public void visit(ClassVarDeclaration cvd) {
		Struct varType = cvd.getType().struct;
		if (cvd.getArraySpecifier() instanceof IsArray)
			varType = new Struct(Struct.Array, varType);

		Obj existing = Tab.currentScope.findSymbol(cvd.getI2());
		if (existing != null)
			report_error("Greska: Polje klase sa imenom " + cvd.getI2() + " vec postoji", cvd);
		else {
			report_info("Deklarisano polje klase " + cvd.getI2(), cvd);
			Tab.insert(Obj.Fld, cvd.getI2(), varType);
		}

		VarDeclList varDeclList = cvd.getVarDeclList();
		while (varDeclList instanceof VarDeclarations) {
			VarDeclarations varDeclarations = (VarDeclarations) varDeclList;
			varType = cvd.getType().struct;
			if (varDeclarations.getArraySpecifier() instanceof IsArray)
				varType = new Struct(Struct.Array, varType);

			existing = Tab.currentScope.findSymbol(varDeclarations.getVarName());
			if (existing != null)
				report_error("Greska: Polje klase sa imenom " + varDeclarations.getVarName() + " vec postoji", cvd);
			else {
				report_info("Deklarisano polje klase " + varDeclarations.getVarName(), cvd);
				Tab.insert(Obj.Fld, varDeclarations.getVarName(), varType);
			}

			varDeclList = varDeclarations.getVarDeclList();
		}

	}

	@Override
	public void visit(ClassName className) {
		Obj classObj = Tab.find(className.getClassName());



		if (classObj != Tab.noObj && classObj.getKind() == Obj.Type) {
			report_error("Greska: Klasa " + className.getClassName() + " vec postoji", className);
			className.obj = Tab.noObj;
		}

		else {
			className.obj = Tab.insert(Obj.Type, className.getClassName(), new Struct(Struct.Class));
			currentClass = className.obj;
			report_info("Definisana klasa " + className.getClassName(), className);
			Tab.openScope();

		}
	}

	@Override
	public void visit(Extends ext) {


		if (ext.getType().struct.getKind() != Struct.Class && ext.getType().struct.getKind() != Struct.Interface) {
			if(ext.getType() instanceof TypeOf) {
				TypeOf to = (TypeOf) ext.getType();
				report_error("Greska: Ime " + to.getTypeName()+ " nije klasa ili interfejs", ext);
			} else
				report_error("Greska: Ime Set nije klasa ili interfejs", ext);

			report_error("Greska: Definicija klase nije uspesna", ext);
			ext.struct = Tab.noType;
		} else {

			SymbolDataStructure base = ext.getType().struct.getMembersTable();
			currentClass.getType().setElementType(ext.getType().struct);

			for (Obj o : base.symbols()) {
				Obj curr = Tab.insert(o.getKind(), o.getName(), o.getType());

				if (o.getKind() == Obj.Meth) {

					Tab.openScope();
					for (Obj local : o.getLocalSymbols()) {
						Obj  l = Tab.insert(local.getKind(), local.getName(), local.getType());
						l.setFpPos(local.getFpPos());

					}

					Tab.chainLocalSymbols(curr);
					Tab.closeScope();
					curr.setAdr(o.getAdr());
					curr.setLevel(o.getLevel());
				}

			}
		}


	}


	@Override
	public void visit(InterfaceName interfaceName) {


		Obj interfaceObj = Tab.find(interfaceName.getInterfaceName());



		if (interfaceObj != Tab.noObj && interfaceObj.getKind() == Obj.Type) {
			report_error("Greska: Interfejs " + interfaceObj.getName() + " vec postoji", interfaceName);
			interfaceName.obj = Tab.noObj;
		}

		else {
			interfaceName.obj = Tab.insert(Obj.Type, interfaceName.getInterfaceName(), new Struct(Struct.Interface));
			currentClass = interfaceName.obj;
			report_info("Definisan interfejs " + interfaceName.getInterfaceName(), interfaceName);
			Tab.openScope();

		}



	}

	@Override
	public void visit(ClassDeclaration classDecl) {

		Tab.chainLocalSymbols(currentClass.getType());
		for (Obj o : currentClass.getType().getMembersTable().symbols()) {
			if (o.getKind() == Obj.Fld) o.setAdr(o.getAdr() + 1);
			if (o.getKind() == Obj.Meth && o.getAdr() == -1)
				report_error("Greska: Metoda " + o.getName() + " klase " + currentClass.getName()
				+ " nema implementaciju", classDecl);
		}

		
		Tab.closeScope();
		classDecl.obj = currentClass;
		currentClass = Tab.noObj;
	}

	@Override
	public void visit(InterfaceDeclaration interfaceDecl) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		currentClass = Tab.noObj;
	}

	@Override
	public void visit(InterfaceMethodSignature ims) {
		currentMethod.setAdr(-1);

		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod = null;

	}


	@Override
	public void visit(TypeMethod methodTypeName){



		StringBuilder message;

		if(currentClass != Tab.noObj) {
			if (currentClass.getType().getKind() == Struct.Class)
				message = new StringBuilder("Definicija metode klase ");
			else
				message = new StringBuilder("Definicija metoda interfejsa ");
			Obj existing = Tab.currentScope.findSymbol(methodTypeName.getMethodName());
			if(existing != null) {
				Tab.currentScope.getLocals().deleteKey(methodTypeName.getMethodName());

				report_info("Redefinisana metoda klase " + methodTypeName.getMethodName(), methodTypeName);
			}

		}

		else
			message = new StringBuilder("Definicija funkcije ");

		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), methodTypeName.getType().struct);


		message.append(methodTypeName.getMethodName() + ", povratnog tipa " + getTypeName(methodTypeName.getType().struct));

		methodTypeName.obj = currentMethod;
		Tab.openScope();
		if (currentClass != Tab.noObj) {
			Struct thisStruct = new Struct(Struct.Class);

			thisStruct.setMembers(Tab.currentScope.getOuter().getLocals());
			Tab.insert(Obj.Var, "this", thisStruct);
		}

		report_info(message.toString(), methodTypeName);
	}

	@Override
	public void visit(VoidMethod methodTypeName){

		StringBuilder message;

		if(currentClass != Tab.noObj) {
			if (currentClass.getType().getKind() == Struct.Class)
				message = new StringBuilder("Definicija metode klase ");
			else
				message = new StringBuilder("Definicija metoda interfejsa ");
			Obj existing = Tab.currentScope.findSymbol(methodTypeName.getMethodName());
			if(existing != null) {
				Tab.currentScope.getLocals().deleteKey(methodTypeName.getMethodName());
				report_info("Redefinisana metoda klase " + methodTypeName.getMethodName(), methodTypeName);
			}



		}

		else
			message = new StringBuilder("Definicija funkcije ");

		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), Tab.noType);
		message.append(methodTypeName.getMethodName() + ", povratnog tipa void");

		methodTypeName.obj = currentMethod;



		Tab.openScope();

		if (currentClass != Tab.noObj) {
			Struct thisStruct = new Struct(Struct.Class);
			thisStruct.setMembers(Tab.currentScope.getOuter().getLocals());
			Tab.insert(Obj.Var, "this", thisStruct);
		}

		report_info(message.toString(), methodTypeName);
	}



	@Override
	public void visit(MethodSignature methodSignature) {
		FormalParameters formPars;
		Obj existing;
		int params = 0;
		int fpPos=1;
		Obj forPar = null;

		if (methodSignature.getOptionalFormalParameters() instanceof FormalParameters) {
			formPars = (FormalParameters) methodSignature.getOptionalFormalParameters();
			FormalParameter formPar = formPars.getFormalParameter();

			Struct paramType = formPar.getType().struct;
			if (formPar.getArraySpecifier() instanceof IsArray)
				paramType = new Struct(Struct.Array, paramType);

			existing = Tab.currentScope.findSymbol(formPar.getParamName());
			if (existing != null)
				report_error("Greska: Formalni parametar sa imenom " + formPar.getParamName() + " vec definisan", methodSignature);
			else {
				report_info("Deklarisan formalni parametar " + formPar.getParamName(), methodSignature);
				forPar = Tab.insert(Obj.Var, formPar.getParamName(), paramType);
				forPar.setFpPos(fpPos);
				fpPos++;
				params++;
			}

			FormalParametersList formParamsList = formPar.getFormalParametersList();
			while (formParamsList instanceof FormalParamsList) {
				FormalParamsList formalParamsList = (FormalParamsList) formParamsList;

				paramType = formalParamsList.getType().struct;
				if (formalParamsList.getArraySpecifier() instanceof IsArray)
					paramType = new Struct(Struct.Array, paramType);

				existing = Tab.currentScope.findSymbol(formalParamsList.getParamName());
				if (existing != null)
					report_error("Greska: Formalni parametar sa imenom " + formalParamsList.getParamName() + " vec definisan", methodSignature);
				else {
					report_info("Deklarisan formalni parametar " + formalParamsList.getParamName(), methodSignature);
					forPar = Tab.insert(Obj.Var, formalParamsList.getParamName(), paramType);
					forPar.setFpPos(fpPos);
					fpPos++;
					params++;
				}

				formParamsList = formalParamsList.getFormalParametersList();
			}
		}

		currentMethod.setLevel(params);
		if(!mainFound && currentMethod.getName().equals("main") && currentMethod.getLevel() == 0)
			mainFound = true;


	}





	@Override
	public void visit(MethodDeclaration methodDeclaration) {


		StatementList statements = methodDeclaration.getStatementList();



		if (!returnFound && currentMethod.getType() != Tab.noType)
			report_error("Semanticka greska na liniji " + methodDeclaration.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);

		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		returnFound = false;
		currentMethod = null;
	}




}
