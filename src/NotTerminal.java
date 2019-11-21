/**
 * The goal of this class is to enumerate the list of None-terminal variables
 * that are in our grammar.
 * We indeed use that list to build the parsetree
 *
 * @author Salma and Naim
 */
public enum NotTerminal {
    Program,
    Code,
    InstList,
    Inst,
    Instruction,
    Assign,
    ExprArith,
    ExprTail,
    ProdEx, ProdTail, ProdAtom, AddSous, ProdDiv, If, IfTail, Cond, OrExp, AndEpx,
    CondTail, Condis, Comp, While, For, Print, Read;


    private NotTerminal(){

    }

}
