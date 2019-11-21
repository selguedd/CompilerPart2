import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

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
				"\n\t -wt writes down the parsetree to the file .tex given in arguments \n\tFILES:\n\tA .txt file containing a grammar" +
				"\n\tA .tex file to write the parsetree on it\n");
		System.exit(0);
	}



}


