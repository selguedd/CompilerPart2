

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private LexicalAnalyzer scanner;
    private Symbol current;
    private ArrayList<Integer> rules = new ArrayList<Integer>();

 public Parser(FileReader source)throws FileNotFoundException, IOException, SecurityException{
     //final Scanner scan = new Scanner(iliusSource);
     this.scanner= new LexicalAnalyzer(source);
     this.current= scanner.nextToken();
 }
    private void consume() throws IOException{
        // We tweak consume() so that is groups the ENDLINE

            current = scanner.nextToken();

    }

    private ParseTree match(LexicalUnit l) throws IOException, ParseException {
        if(!current.getType().equals(l)){
            // There is a parsing error
            throw new ParseException(current.getValue(),-1);
        }
        else {
            Symbol cur = current;
            consume();
            return new ParseTree(cur);
        }
    }

    public ParseTree start() throws IOException, ParseException{
        // Program is the initial symbol of the Imp grammar
        return this.program();

    }


    public ParseTree program() throws IOException, ParseException{
        // Program is the initial symbol of the grammar
        // <Program> -->  BEG <Code> END
        rules.add(1);

        ParseTree parseTree = new ParseTree(new Symbol(NotTerminal.Program), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.BEG),this.code(),this.match(LexicalUnit.END)}));
        return parseTree;
    }

    public ParseTree code() throws IOException, ParseException{
     //<Code> --> EPSILON
     //<Code> --> <InstList>
      switch(current.getType()){
          case IF:
          case VARNAME:
          case WHILE:
          case PRINT:
          case READ:
          case FOR:
              rules.add(3);
              ParseTree parseTree = new ParseTree(new Symbol(NotTerminal.Code), Arrays.asList(new ParseTree[]{this.instList()}));
              return parseTree;
          case END:
          case ENDWHILE:
          case ENDIF:
          case ELSE:
              rules.add(2);
              return new ParseTree(new Symbol(NotTerminal.Code), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
          default:
              throw new ParseException(current.getValue(),-1);
      }

    }

    public void rule4()throws ParseException, IOException{
        instruction();
        inst();
    }

    public ParseTree instList() throws ParseException, IOException {
     switch (current.getType()){
         case IF:
         case VARNAME:
         case WHILE:
         case PRINT:
         case READ:
         case FOR:
             rules.add(4);
             ParseTree parseTree = new ParseTree(new Symbol(NotTerminal.InstList), Arrays.asList(new ParseTree[]{this.instruction(),this.inst()}));
             return parseTree;
         default:
             throw new ParseException(current.getValue(),-1);
     }
    }

    private ParseTree inst() throws ParseException, IOException {

        switch(current.getType()){
            case END:
            case ENDWHILE:
            case ENDIF:
            case ELSE:
                rules.add(6);
                return new ParseTree(new Symbol(NotTerminal.Inst), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            case SEMICOLON:
                rules.add(5);
                ParseTree parseTree = new ParseTree(new Symbol(NotTerminal.Inst), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.SEMICOLON),this.instList()}));
                return parseTree;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    public ParseTree instruction() throws ParseException, IOException {
        switch (current.getType()){
            case IF:
                rules.add(8);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.ifP()}));
            case VARNAME:
                rules.add(7);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.assign()}));
            case WHILE:
                rules.add(9);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.whileP()}));
            case PRINT:
                rules.add(11);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.printP()}));
            case READ:
                rules.add(12);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.read()}));
            case FOR:
                rules.add(10);
                return new ParseTree(new Symbol(NotTerminal.Instruction), Arrays.asList(new ParseTree[]{this.forP()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree forP() throws ParseException, IOException{
     rules.add(46);
        return new ParseTree(new Symbol(NotTerminal.For), Arrays.asList(this.match(LexicalUnit.FOR),
                this.match(LexicalUnit.VARNAME),
                this.match(LexicalUnit.FROM),
                this.expArith(),
                this.match(LexicalUnit.BY),
                this.expArith(),
                this.match(LexicalUnit.TO),
                this.expArith(),
                this.match(LexicalUnit.DO),
                this.code(),
                this.match(LexicalUnit.ENDWHILE)));

    }

    private ParseTree read() throws ParseException, IOException {
     rules.add(48);
     return new ParseTree(new Symbol(NotTerminal.Read), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.READ),
        this.match(LexicalUnit.LEFTPARENTHESIS),
        this.match(LexicalUnit.VARNAME),
        this.match(LexicalUnit.RIGHTPARENTHESIS)}));
    }

    private ParseTree printP()throws ParseException, IOException {
     rules.add(47);
     return new ParseTree(new Symbol(NotTerminal.Print), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.PRINT),
        this.match(LexicalUnit.LEFTPARENTHESIS),
        this.match(LexicalUnit.VARNAME),
        this.match(LexicalUnit.RIGHTPARENTHESIS)}));
    }

    private ParseTree whileP() throws ParseException, IOException {
     rules.add(45);
     return new ParseTree(new Symbol(NotTerminal.While), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.WHILE),
        this.cond(),
        this.match(LexicalUnit.DO),
        this.code(),
        this.match(LexicalUnit.ENDWHILE)}));
    }

    private ParseTree assign() throws ParseException, IOException {
     rules.add(13);
     return new ParseTree(new Symbol(NotTerminal.Assign), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.VARNAME),
        this.match(LexicalUnit.ASSIGN),
        this.expArith()}));
    }

    public ParseTree ifP() throws ParseException, IOException {
        rules.add(28);
        return new ParseTree(new Symbol(NotTerminal.If), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.IF),
        this.cond(),
        this.match(LexicalUnit.THEN),
        this.code(),
        this.ifTail()}));
    }

    private ParseTree ifTail() throws ParseException, IOException {
        switch (current.getType()){
            case ENDIF:
                rules.add(29);
                return new ParseTree(new Symbol(NotTerminal.IfTail), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.ENDIF)}));
            case ELSE:
                rules.add(30);
                return new ParseTree(new Symbol(NotTerminal.IfTail), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.ELSE),
                this.code(),
                this.match(LexicalUnit.ENDIF)}));
            default:
                throw new ParseException(current.getValue(),-1);
        }

    }

    private ParseTree cond()  throws ParseException, IOException {
        switch (current.getType()){
            case NOT:
            case NUMBER:
            case LEFTPARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(31);
                return new ParseTree(new Symbol(NotTerminal.Cond), Arrays.asList(new ParseTree[]{this.andExp(), this.orExp()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree andExp() throws ParseException, IOException {
     switch (current.getType()){
        case NOT:
        case NUMBER:
        case LEFTPARENTHESIS:
        case MINUS:
        case VARNAME:
            rules.add(34);
            return new ParseTree(new Symbol(NotTerminal.AndEpx), Arrays.asList(new ParseTree[]{this.condis(), this.condtail()}));
        default:
            throw new ParseException(current.getValue(),-1);
    }
    }

    private ParseTree condtail()throws ParseException, IOException {
        switch (current.getType()){
            case OR:
            case DO:
            case THEN:
                rules.add(36);
                return new ParseTree(new Symbol(NotTerminal.CondTail), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            case AND:
                rules.add(35);
                return new ParseTree(new Symbol(NotTerminal.CondTail), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.AND),
                this.condis(),
                this.condtail()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }
    private ParseTree condis()throws  ParseException, IOException{
        switch (current.getType()){
            case NOT:
                rules.add(37);
                return new ParseTree(new Symbol(NotTerminal.Condis), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            case NUMBER:
            case LEFTPARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(38);
                return new ParseTree(new Symbol(NotTerminal.Condis), Arrays.asList(new ParseTree[]{this.expArith(),
            this.comp(),
            this.expArith()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree expArith() throws  ParseException, IOException{
        switch (current.getType()){
            case NUMBER:
            case LEFTPARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(14);
                return new ParseTree(new Symbol(NotTerminal.ExprArith), Arrays.asList(new ParseTree[]{this.prodEx(),
            this.expTail()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }

    }

    private ParseTree expTail() throws  ParseException, IOException {
        switch (current.getType()){
            case MINUS:
            case PLUS:
                rules.add(15);
                return new ParseTree(new Symbol(NotTerminal.ExprTail), Arrays.asList(new ParseTree[]{this.addSous(),
                        this.prodEx(), this.expTail()}));
            case DO:
            case OR:
            case END:
            case IF:
            case BY:
            case TO:
            case THEN:
            case ENDIF:
            case ELSE:
            case RIGHTPARENTHESIS:
            case AND:
            case ENDWHILE:
            case SEMICOLON:
            case GREATER:
            case GREATEREQUAL:
            case SMALLER:
            case SMALLEREQUAL:
            case DIFFERENT:
            case EQUAL:
                rules.add(16);
                return new ParseTree(new Symbol(NotTerminal.ExprTail), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
                
    }

    private ParseTree addSous() throws  ParseException, IOException {
        switch (current.getType()){
            case MINUS:
                rules.add(25);
                return new ParseTree(new Symbol(NotTerminal.AddSous), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.MINUS)}));
            case PLUS:
                rules.add(24);
                return new ParseTree(new Symbol(NotTerminal.AddSous), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.PLUS)}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree prodEx() throws  ParseException, IOException {
        switch (current.getType()){
            case NUMBER:
            case LEFTPARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(17);
                return new ParseTree(new Symbol(NotTerminal.ProdEx), Arrays.asList(new ParseTree[]{this.prodAtom(), this.prodTail()}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree prodAtom() throws  ParseException, IOException{
        switch (current.getType()){
            case NUMBER:
                rules.add(23);
                return new ParseTree(new Symbol(NotTerminal.ProdAtom), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.NUMBER)}));
            case LEFTPARENTHESIS:
                rules.add(20);
                return new ParseTree(new Symbol(NotTerminal.ProdAtom), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.LEFTPARENTHESIS),
                this.expArith(),
                this.match(LexicalUnit.RIGHTPARENTHESIS)}));
            case MINUS:
                rules.add(21);
                return new ParseTree(new Symbol(NotTerminal.ProdAtom), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.MINUS),
                this.prodAtom()}));
            case VARNAME:
                rules.add(22);
                return new ParseTree(new Symbol(NotTerminal.ProdAtom), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.VARNAME)}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }
    private ParseTree prodTail() throws  ParseException, IOException{
        switch (current.getType()){
            case TIMES:
            case DIVIDE:
                rules.add(18);
                return new ParseTree(new Symbol(NotTerminal.ProdTail), Arrays.asList(new ParseTree[]{this.prodDiv(),
                this.prodAtom(),
                this.prodTail()}));
            case DO:
            case OR:
            case END:
            case IF:
            case BY:
            case TO:
            case PLUS:
            case MINUS:
            case THEN:
            case ENDIF:
            case ELSE:
            case RIGHTPARENTHESIS:
            case AND:
            case ENDWHILE:
            case SEMICOLON:
            case GREATER:
            case GREATEREQUAL:
            case SMALLER:
            case SMALLEREQUAL:
            case DIFFERENT:
            case EQUAL:
                rules.add(19);
                return new ParseTree(new Symbol(NotTerminal.ProdTail), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree prodDiv() throws  ParseException, IOException {
        switch (current.getType()){
            case TIMES:
                rules.add(26);
                return new ParseTree(new Symbol(NotTerminal.ProdDiv), Arrays.asList(new ParseTree[]{match(LexicalUnit.TIMES)}));
            case DIVIDE:
                rules.add(27);
                return new ParseTree(new Symbol(NotTerminal.ProdDiv), Arrays.asList(new ParseTree[]{match(LexicalUnit.DIVIDE)}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private ParseTree comp()throws  ParseException, IOException {
        switch (current.getType()){
            case GREATER:
                rules.add(41);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.GREATER)}));
            case GREATEREQUAL:
                rules.add(40);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.GREATEREQUAL)}));
            case EQUAL:
                rules.add(39);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.EQUAL)}));
            case SMALLER:
                rules.add(43);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.SMALLER)}));
            case SMALLEREQUAL:
                rules.add(42);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.SMALLEREQUAL)}));
            case DIFFERENT:
                rules.add(44);
                return new ParseTree(new Symbol(NotTerminal.Comp), Arrays.asList(new ParseTree[]{match(LexicalUnit.DIFFERENT)}));
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }


    private ParseTree orExp()throws ParseException, IOException {
        switch (current.getType()){
            case OR:
                rules.add(32);
                return new ParseTree(new Symbol(NotTerminal.OrExp), Arrays.asList(new ParseTree[]{this.match(LexicalUnit.OR),
                this.andExp(),
                this.orExp()}));
            case DO:
            case THEN:
                rules.add(33);
                return new ParseTree(new Symbol(NotTerminal.OrExp), Arrays.asList(new ParseTree[]{new ParseTree(new Symbol(LexicalUnit.EPSILON))}));
            default:
            throw new ParseException(current.getValue(),-1);
        }
    }

    public void printRules(){
        System.out.println("Rules");
        for(int i = 0; i < rules.size(); i++) {
            System.out.print(rules.get(i)+" ");
        }
    }
}
