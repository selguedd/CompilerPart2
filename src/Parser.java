

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

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

    private void match(LexicalUnit l) throws IOException, ParseException {
        if(!current.getType().equals(l)){
            // There is a parsing error
            throw new ParseException(current.getValue(),-1);
        }
        else {
            Symbol cur = current;
            consume();

        }
    }
    public void start() throws IOException, ParseException{
        // Program is the initial symbol of the Imp grammar
        program();

    }
    public void program() throws IOException, ParseException{
        // Program is the initial symbol of the Imp grammar
        rules.add(1);
        match(LexicalUnit.BEG);
        code();
        match(LexicalUnit.END);
    }
    public void code() throws IOException, ParseException{
      switch(current.getType()){
          case IF:
          case VARNAME:
          case WHILE:
          case PRINT:
          case READ:
          case FOR:
              rules.add(3);
              instList();
              break;
          case END:
          case ENDWHILE:
          case ENDIF:
          case ELSE:
              rules.add(2);
              break;
          default:
              throw new ParseException(current.getValue(),-1);
      }

    }
    public void rule4()throws ParseException, IOException{
        instruction();
        inst();
    }
    public void instList() throws ParseException, IOException {
     switch (current.getType()){
         case IF:
         case VARNAME:
         case WHILE:
         case PRINT:
         case READ:
         case FOR:
             rules.add(4);
             rule4();
             break;
         default:
             throw new ParseException(current.getValue(),-1);
     }
    }

    private void inst() throws ParseException, IOException {

        switch(current.getType()){
            case END:
            case ENDWHILE:
            case ENDIF:
            case ELSE:
                rules.add(6);
                break;
            case SEMICOLON:
                rules.add(5);
                match(LexicalUnit.SEMICOLON);
                instList();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    public void instruction() throws ParseException, IOException {
        switch (current.getType()){
            case IF:
                rules.add(8);
                ifP();
                break;
            case VARNAME:
                rules.add(7);
                assign();
                break;
            case WHILE:
                rules.add(9);
                whileP();
                break;
            case PRINT:
                rules.add(11);
                printP();
                break;
            case READ:
                rules.add(12);
                read();
                break;
            case FOR:
                rules.add(10);
                forP();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void forP() throws ParseException, IOException{
     rules.add(46);
     match(LexicalUnit.FOR);
     match(LexicalUnit.VARNAME);
     match(LexicalUnit.FROM);
     expArith();
     match(LexicalUnit.BY);
     expArith();
     match(LexicalUnit.TO);
     expArith();
     match(LexicalUnit.DO);
     code();
     match(LexicalUnit.ENDWHILE);

    }

    private void read() throws ParseException, IOException {
     rules.add(48);
     match(LexicalUnit.READ);
     match(LexicalUnit.LEFT_PARENTHESIS);
     match(LexicalUnit.VARNAME);
     match(LexicalUnit.RIGHT_PARENTHESIS);
    }

    private void printP()throws ParseException, IOException {
     rules.add(47);
     match(LexicalUnit.PRINT);
     match(LexicalUnit.LEFT_PARENTHESIS);
     match(LexicalUnit.VARNAME);
     match(LexicalUnit.RIGHT_PARENTHESIS);
    }

    private void whileP() throws ParseException, IOException {
     rules.add(45);
     match(LexicalUnit.WHILE);
     cond();
     match(LexicalUnit.DO);
     code();
     match(LexicalUnit.ENDWHILE);
    }

    private void assign() throws ParseException, IOException {
     rules.add(13);
     match(LexicalUnit.VARNAME);
     match(LexicalUnit.ASSIGN);
     expArith();
    }

    public void ifP() throws ParseException, IOException {
        match(LexicalUnit.IF);
        cond();
        match(LexicalUnit.THEN);
        code();
        ifTail();
        rules.add(28);

    }

    private void ifTail() throws ParseException, IOException {
        switch (current.getType()){
            case ENDIF:
                match(LexicalUnit.ENDIF);
                rules.add(29);
                break;
            case ELSE:
                match(LexicalUnit.ELSE);
                code();
                match(LexicalUnit.ENDIF);
                rules.add(30);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }

    }

    private void cond()  throws ParseException, IOException {
        switch (current.getType()){
            case NOT:
            case NUMBER:
            case LEFT_PARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(31);
                andExp();
                orExp();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void andExp() throws ParseException, IOException {
     switch (current.getType()){
        case NOT:
        case NUMBER:
        case LEFT_PARENTHESIS:
        case MINUS:
        case VARNAME:
            rules.add(34);
            condis();
            condtail();
            break;
        default:
            throw new ParseException(current.getValue(),-1);
    }
    }

    private void condtail()throws ParseException, IOException {
        switch (current.getType()){
            case OR:
            case DO:
            case THEN:
                rules.add(36);
                break;
            case AND:
                rules.add(35);
                match(LexicalUnit.AND);
                condis();
                condtail();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }
    private void condis()throws  ParseException, IOException{
        switch (current.getType()){
            case NOT:
                rules.add(37);
                break;
            case NUMBER:
            case LEFT_PARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(38);
                expArith();
                comp();
                expArith();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void expArith() throws  ParseException, IOException{
        switch (current.getType()){
            case NUMBER:
            case LEFT_PARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(14);
                prodEx();
                expTail();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }

    }

    private void expTail() throws  ParseException, IOException {
        switch (current.getType()){
            case MINUS:
            case PLUS:
                rules.add(15);
                addSous();
                prodEx();
                expTail();
                break;
            case DO:
            case OR:
            case END:
            case IF:
            case BY:
            case TO:
            case THEN:
            case ENDIF:
            case ELSE:
            case RIGHT_PARENTHESIS:
            case AND:
            case ENDWHILE:
            case SEMICOLON:
            case GREATER:
            case GREATER_EQUAL:
            case SMALLER:
            case SMALLER_EQUAL:
            case DIFFERENT:
            case EQUAL:
                rules.add(16);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
                
    }

    private void addSous() throws  ParseException, IOException {
        switch (current.getType()){
            case MINUS:
                rules.add(25);
                match(LexicalUnit.MINUS);
                break;
            case PLUS:
                rules.add(24);
                match(LexicalUnit.PLUS);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void prodEx() throws  ParseException, IOException {
        switch (current.getType()){
            case NUMBER:
            case LEFT_PARENTHESIS:
            case MINUS:
            case VARNAME:
                rules.add(17);
                prodAtom();
                prodTail();
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void prodAtom() throws  ParseException, IOException{
        switch (current.getType()){
            case NUMBER:
                rules.add(23);
                match(LexicalUnit.NUMBER);
                break;
            case LEFT_PARENTHESIS:
                rules.add(20);
                match(LexicalUnit.LEFT_PARENTHESIS);
                expArith();
                match(LexicalUnit.RIGHT_PARENTHESIS);
                break;
            case MINUS:
                rules.add(21);
                match(LexicalUnit.MINUS);
                prodAtom();
                break;
            case VARNAME:
                rules.add(22);
                match(LexicalUnit.VARNAME);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }
    private void prodTail() throws  ParseException, IOException{
        switch (current.getType()){
            case TIMES:
            case DIVIDE:
                rules.add(18);
                prodDiv();
                prodAtom();
                prodTail();
                break;
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
            case RIGHT_PARENTHESIS:
            case AND:
            case ENDWHILE:
            case SEMICOLON:
            case GREATER:
            case GREATER_EQUAL:
            case SMALLER:
            case SMALLER_EQUAL:
            case DIFFERENT:
            case EQUAL:
                rules.add(19);
                break;

            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void prodDiv() throws  ParseException, IOException {
        switch (current.getType()){
            case TIMES:
                match(LexicalUnit.TIMES);
                rules.add(26);
                break;
            case DIVIDE:
                match(LexicalUnit.DIVIDE);
                rules.add(27);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }

    private void comp()throws  ParseException, IOException {
        switch (current.getType()){
            case GREATER:
                match(LexicalUnit.GREATER);
                rules.add(41);
                break;
            case GREATER_EQUAL:
                match(LexicalUnit.GREATER_EQUAL);
                rules.add(40);
                break;
            case EQUAL:
                match(LexicalUnit.EQUAL);
                rules.add(39);
                break;
            case SMALLER:
                match(LexicalUnit.SMALLER);
                rules.add(43);
                break;
            case SMALLER_EQUAL:
                match(LexicalUnit.SMALLER_EQUAL);
                rules.add(42);
                break;
            case DIFFERENT:
                match(LexicalUnit.DIFFERENT);
                rules.add(44);
                break;
            default:
                throw new ParseException(current.getValue(),-1);
        }
    }


    private void orExp()throws ParseException, IOException {
        switch (current.getType()){
            case OR:
                match(LexicalUnit.OR);
                rules.add(32);
                andExp();
                orExp();
                break;
            case DO:
            case THEN:
                rules.add(33);
                break;
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
