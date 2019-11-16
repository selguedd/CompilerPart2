import java.io.*;
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
		try {
			parse.start();
		} catch (ParseException e) {

		}
		parse.printRules();
	}
	public static void usage(){
		System.out.println("java "+Main.class.getSimpleName()+" [file.il]");
		System.out.println("\tWithout the source file, the program read from the standard input stream.");
		System.exit(0);
	}
}