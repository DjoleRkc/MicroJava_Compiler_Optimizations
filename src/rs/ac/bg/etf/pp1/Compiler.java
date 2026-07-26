package rs.ac.bg.etf.pp1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;

class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}


	public static void main(String args[]) throws Exception {
		Logger log = Logger.getLogger(Compiler.class);
		FileReader r = new FileReader(args[0]);
		Yylex skener = new Yylex(r);
		MJParser p = new MJParser(skener);
		Symbol s = p.parse();
		Program prog = (Program)(s.value);



		// ispis sintaksnog stabla
		log.info(prog.toString(""));
		System.out.println("===================================");



		SemanticAnalyzer v = new SemanticAnalyzer();
		prog.traverseBottomUp(v);


		v.print();

		if (!p.greska && v.passed() && v.mainFound) {
			// Constant folding (i kasnije ostale optimizacije) - posle semantike, pre codegen-a
			Optimizer optimizer = new Optimizer();
			prog.traverseBottomUp(optimizer);

			File outputFile = new File(args[0].replace(".mj", ".obj"));
			Code.dataSize = v.nVars;
			CodeGenerator codeGen = new CodeGenerator(optimizer);
			prog.traverseBottomUp(codeGen);


			Code.mainPc = codeGen.getMainPc();
			try (OutputStream out = new FileOutputStream(outputFile)) {
				Code.write(out);
			}
			log.info("Generisanje koda uspesno zavrseno!");
		}

		else
			log.error("Parsiranje NIJE uspesno zavrseno!");

	}
}

