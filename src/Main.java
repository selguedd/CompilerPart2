import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main{
	public static void main(String[] args)throws FileNotFoundException, IOException, SecurityException{
		if(args.length > 1){
			usage();
		}
        
        
        if (args.length == 0)
            System.exit(-1) ;
        
        FileReader Source = new FileReader(args[0]);
      
		
		//final Scanner scan = new Scanner(iliusSource);
		final LexicalAnalyzer analyzer = new LexicalAnalyzer(Source);
		Map<String,Symbol> table = new HashMap<String,Symbol>();
		Symbol symbol;
		while(!(symbol = analyzer.nextToken()).getType().equals(LexicalUnit.END_OF_STREAM)){
			System.out.println(symbol.toString());
			if(symbol.getType().equals(LexicalUnit.VARNAME)){
				if(!table.containsKey(symbol.getValue())){
					table.put(symbol.getValue().toString(),symbol);
				}
			}
		}
		
		System.out.println("Identifiers");
		List<String> sorted = new ArrayList<String>(table.size());
		for(String identifier:table.keySet())
			sorted.add(identifier);
		Collections.sort(sorted);
		for(String identifier:sorted)
			System.out.println(identifier+"\t"+(table.get(identifier).getLine()));
	}
	public static void usage(){
		System.out.println("java "+Main.class.getSimpleName()+" [file.il]");
		System.out.println("\tWithout the source file, the program read from the standard input stream.");
		System.exit(0);
	}
}