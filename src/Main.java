import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 * Our Main class that contains the main function will be responsible of the execution of the whole code.
 * In this class, we majorly deal with the options that the user can enter, in fact :
 * -v will give him more details on the rules that were used
 * -wt filename.tex will write down (in latex) the parsetree in the .tex file given
 *
 * @author Salma and Naim
 */

public class Main{
	public static void main(String[] args)throws IOException, SecurityException, ParserException {

		if (args.length == 0) {
			wrongArguments();
		}
		String tex = ".tex";
		int argNumber = args.length-1;
		FileReader Source = null;
		Parser parse = null;
		ParseTree parseTree=null;

		try {
			Source = new FileReader(args[argNumber]);
		} catch (IOException ioe) {
			System.out.println("File not found: check your path");
		}
		parse = new Parser(Source);
		parseTree = parse.start();
        parse.printRules();
		 for(int i = 0; i < args.length; ++i){
			if(args[i].equals("-v")){
				parse.printVerboseRules();
			}

			if (args[i].equals("-wt") && args[i+1].toLowerCase().contains(tex) ){

				String content = parseTree.toLaTeX();
				try {
					Files.write(Paths.get(args[i+1]), content.getBytes());

				} catch (IOException e) {
						e.printStackTrace();
				}
			}


		 }


	}


	public static void wrongArguments(){
		System.out.println("Usage:  java -jar Part2.jar [OPTIONS] [FILES]\n\tOPTIONS:\n\t -v prints out a more verbose description of the rules" +
				"\n\t -wt writes down the parsetree to the file .tex given in arguments " +
				"\n\tA .tex file to write the parsetree on it\n" +
				" \n\tFILES:\n\tA .txt file containing a grammar");
		System.exit(0);
	}



}


