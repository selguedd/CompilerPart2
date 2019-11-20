import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class Main{
	public static void main(String[] args)throws IOException, SecurityException, ParserException {

		if (args.length == 0) {
			wrongArguments();
		}




		int argNumber = args.length-1;
		FileReader Source = null;
		Parser parse = null;
		ParseTree parseTree=null;

		try {
			Source = new FileReader(args[argNumber]);
		} catch (IOException ioe) {
			System.out.println("File not found: check your path");
		}

		 for(int i = 0; i < args.length; ++i){
			if(args[i].equals("-v")){

				parse = new Parser(Source);
				parseTree = parse.start();

				parse.printRules();
			}

			if (args[i].equals("-wt")){
					parse = new Parser(Source);
				    parseTree = parse.start();
				if (parseTree != null) {
					System.out.println("big lol");
					String content = parseTree.toLaTeX();
					try {
						Files.write(Paths.get("filename.tex"), content.getBytes());

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}

	}




	public static void usage(){
		System.out.println("java "+Main.class.getSimpleName()+" [file.il]");
		System.out.println("\tWithout the source file, the program read from the standard input stream.");
		System.exit(0);
	}

	public static void wrongArguments(){
		System.out.println("Usage:  java -jar Part2.jar [OPTIONS] [FILES]\n\tOPTIONS:\n\t -v prints out a more verbose description of the rules" +
				"\n\t -wt writes down the parsetree to the file .tex given in arguments \n\tFILES:\n\tA .txt file containing a grammar" +
				"\n\tA .tex file to write the parsetree on it\n");
		System.exit(0);
	}
}


