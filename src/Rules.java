import java.util.HashMap;
import java.util.Map;

/**
 * This class enumerates all the rules of our grammar.
 * This class can be used for many reasons when manipulating rules but we specially used in the
 * class Parser, when we created the method printVerboseRules that will print out the complete
 * rules identified while parsing if the user uses -v as an option
 *
 * @authors Salma El Gueddari, Naim Sassine
 */
public class Rules {

    private Map<Integer, String> dictionary = new HashMap<Integer, String>();

    public Rules(){
        dictionary.put(1, "<Program> -->  BEG <Code> END");
        dictionary.put(2, "<Code> --> EPSILON");
        dictionary.put(3, "<Code> --> <InstList>");
        dictionary.put(4, "<InstList> --> <Instruction> <Inst>");
        dictionary.put(5, "<Inst> --> ; <InstList>");
        dictionary.put(6, "<Inst> --> EPSILON");
        dictionary.put(7, "<Instruction> --> <Assign>");
        dictionary.put(8, "<Instruction> --> <If>");
        dictionary.put(9, "<Instruction> --> <While>");
        dictionary.put(10, "<Instruction> --> <For>");
        dictionary.put(11, "<Instruction> --> <Print>");
        dictionary.put(12, "<Instruction> --> <Read>");
        dictionary.put(13, "<Assign> --> [VARNAME] ASSIGN <ExprArith>");
        dictionary.put(14, "<ExprArith> --> <ProdEx> <ExprTail>");
        dictionary.put(15, "<ExprTail>  --> <AddSous> <ProdEx> <ExprTail>");
        dictionary.put(16, "<ExprTail>  --> EPSILON");
        dictionary.put(17, "<ProdEx> --> <ProdAtom> <ProdTail> ");
        dictionary.put(18, "<ProdTail> --> <ProdDiv> <ProdAtom> <ProdTail> ");
        dictionary.put(19, "<ProdTail>  --> EPSILON");
        dictionary.put(20, "<ProdAtom> --> ( <ExprArith> )");
        dictionary.put(21, "<ProdAtom> --> - <ProdAtom>");
        dictionary.put(22, "<ProdAtom> --> [VARNAME]");
        dictionary.put(23, "<ProdAtom> --> [NUMBER]");
        dictionary.put(24, "<AddSous> --> +");
        dictionary.put(25, "<AddSous> --> -");
        dictionary.put(26, "<ProdDiv> --> *");
        dictionary.put(27, "<ProdDiv> --> /");
        dictionary.put(28, "<If> --> if <Cond> then <Code> <IfTail>");
        dictionary.put(29, "<IfTail> --> endif");
        dictionary.put(30, "<IfTail> --> else <Code> endif");
        dictionary.put(31, "<Cond> --> <AndExp> <OrExp>");
        dictionary.put(32, "<OrExp>--> or <AndExp> <OrExp>");
        dictionary.put(33, "<OrExp>--> EPSILON");
        dictionary.put(34, "<AndExp> --> <Condis> <CondTail>");
        dictionary.put(35, "<CondTail>--> and <Condis> <CondTail>");
        dictionary.put(36, "<CondTail>--> EPSILON");
        dictionary.put(37, "<Condis> --> not <Condis>");
        dictionary.put(38, "<Condis> --> <ExprArith> <Comp> <ExprArith>");
        dictionary.put(39, "<Comp> --> =");
        dictionary.put(40, "<Comp> --> >=");
        dictionary.put(41, "<Comp> --> >");
        dictionary.put(42, "<Comp> --> <=");
        dictionary.put(43, "<Comp> --> <");
        dictionary.put(44, "<Comp> --> /=");
        dictionary.put(45, "<While> --> while <Cond> do <Code> endwhile");
        dictionary.put(46, "<For> --> for [VARNAME] from <ExprArith> by <ExprArith> to <ExprArith> do <Code> endwhile");
        dictionary.put(47, "<Print> --> print([VARNAME])");
        dictionary.put(48, "<Read> --> read([VARNAME])");

    }

    public String getRule(int i){
        return dictionary.get(i);
    }
}
