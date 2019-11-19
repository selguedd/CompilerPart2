import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class Main{
	public static void main(String[] args)throws FileNotFoundException, IOException, SecurityException{

        if (args.length == 0 || args.length == 1){
            System.exit(-1) ;}

        FileReader Source = new FileReader(args[0]);
        Parser parse = new Parser(Source);
		ParseTree parseTree=null;


		if(args.length == 2){
			try {
				parseTree=parse.start();
			} catch (ParseException e) {

			}
			parse.printRules();
		}
		else if(args.length == 3){
			if (args[0] == "-v"){
				try {
					parseTree=parse.start();
				} catch (ParseException e) {

				}
				parse.printRules();
			}
			else if (args[0] == "-wt"){
				try {
					parseTree=parse.start();
				} catch (ParseException e) {

				}
				parse.printRules();
				if (parseTree != null) {
					String content = parseTree.toLaTeX();

					try {

						Files.write(Paths.get("filename.tex"), content.getBytes());

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}
		else if (args.length == 4){

		}
		else {
			usage();
		}

	}
	public static void usage(){
		System.out.println("java "+Main.class.getSimpleName()+" [file.il]");
		System.out.println("\tWithout the source file, the program read from the standard input stream.");
		System.exit(0);
	}
}