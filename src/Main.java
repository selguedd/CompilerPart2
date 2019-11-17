import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class Main{
	public static void main(String[] args)throws FileNotFoundException, IOException, SecurityException{
		if(args.length > 1){
			usage();
		}
        
        
        if (args.length == 0)
            System.exit(-1) ;
        
        FileReader Source = new FileReader(args[0]);
        Parser parse = new Parser(Source);
		ParseTree parseTree=null;
		try {
			parseTree=parse.start();
		} catch (ParseException e) {

		}
		parse.printRules();
		if (parseTree != null) {
			String content =parseTree.toLaTeX();

			try {

				Files.write(Paths.get("filename.tex"), content.getBytes());

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	public static void usage(){
		System.out.println("java "+Main.class.getSimpleName()+" [file.il]");
		System.out.println("\tWithout the source file, the program read from the standard input stream.");
		System.exit(0);
	}
}