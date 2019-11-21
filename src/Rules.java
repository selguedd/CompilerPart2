import java.util.HashMap;
import java.util.Map;

public class Rules {

    private Map<Integer, String> dictionary = new HashMap<Integer, String>();
    public Rules(){
        dictionary.put(1, "<Program> -->  BEG <Code> END");
        dictionary.put(2, "<Code> --> EPSILON");
        dictionary.put(3, "<Code> --> <InstList>");
        dictionary.put(4, "<InstList> --> <Instruction> <Ints>");
        dictionary.put(5, "<Inst> --> SEMICOLON <InstList>");
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
        dictionary.put(20, "<ProdAtom> --> LEFT_PARENTHESIS <ExprArith> RIGHT_PARENTHESIS");
        dictionary.put(21, "<ProdAtom> --> MINUS <ProdAtom>");
        dictionary.put(22, "<ProdAtom> --> [VARNAME]");
        dictionary.put(23, "<ProdAtom> --> [NUMBER]");
        dictionary.put(24, "<AddSous> --> PLUS");
        dictionary.put(25, "<AddSous> --> MINUS");
        dictionary.put(26, "<ProdDiv> --> TIMES");
        dictionary.put(27, "<ProdDiv> --> DIVIDE");
        dictionary.put(28, "<If> --> IF <Cond> THEN <Code> <IfTail>");
        dictionary.put(29, "<IfTail> --> ENDIF");
        dictionary.put(30, "<IfTail> --> ELSE <Code> ENDIF");
        dictionary.put(31, "<Cond> --> <AndExp> <OrExp>");
        dictionary.put(32, "<OrExp>--> OR <AndExp> <OrExp>");
        dictionary.put(33, "<OrExp>--> EPSILON");
        dictionary.put(34, "<AndExp> --> <Condis> <CondTail>");



    }
    public void init(){

    }
    public String getrule(){
        return null;
    }
}
