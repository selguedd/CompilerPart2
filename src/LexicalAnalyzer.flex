import java.util.regex.PatternSyntaxException;

%%// Options of the scanner

%class LexicalAnalyzer	//Name
%unicode						//Use unicode
%line							//Use line counter (yyline variable)
%column						//Use character counter by line (yycolumn variable)
%function nextToken
%type Symbol
%yylexthrow PatternSyntaxException

%eofval{
	return new Symbol(LexicalUnit.END_OF_STREAM,yyline, yycolumn);
%eofval}

//Extended Regular Expressions

AlphaUpperCase	= [A-Z]
AlphaLowerCase	= [a-z]
Alpha		= {AlphaUpperCase}|{AlphaLowerCase}
Numeric		= [0-9]
AlphaNumeric	= {Alpha}|{Numeric}

Integer		= [0-9]+
Identifier		= (({Alpha})({AlphaNumeric}*))
LineFeed		= "\n"
CarriageReturn	= "\r"
EndOfLine		= ({LineFeed}{CarriageReturn}?) | ({CarriageReturn}{LineFeed}?)
Space		= (\t | \f | " ")
Separator		= ({Space}) | ({EndOfLine})
Any                   = ([^"\n""\r"])*
UpToEnd		= ({Space}{Any}{EndOfLine}) | ({EndOfLine})

//Declare exclusive states
%xstate YYINITIAL, COMMENTS

%%//Identification of tokens

<COMMENTS> {
	[^]			{}//ignore all character
	"CO"{Separator}			{yybegin(YYINITIAL);} // go back to analysis
  	<<EOF>>        		{throw new PatternSyntaxException("A comment is never closed.",yytext(),yyline);}
}

<YYINITIAL> {
	"CO"{Separator}			{yybegin(COMMENTS);} // go to ignore mode
	"co"{UpToEnd}		{} //ignore all

	"<="			{return new Symbol(LexicalUnit.SMALLEREQUAL,yyline, yycolumn,yytext());}
	">="			{return new Symbol(LexicalUnit.GREATEREQUAL,yyline, yycolumn,yytext());}
	"="			{return new Symbol(LexicalUnit.EQUAL,yyline, yycolumn,yytext());}
	"/="			{return new Symbol(LexicalUnit.DIFFERENT,yyline, yycolumn,yytext());}
	"<"			{return new Symbol(LexicalUnit.SMALLER,yyline, yycolumn,yytext());}
	">"			{return new Symbol(LexicalUnit.GREATER,yyline, yycolumn,yytext());}
	
	"("			{return new Symbol(LexicalUnit.LEFTPARENTHESIS,yyline, yycolumn,yytext());}
	")"			{return new Symbol(LexicalUnit.RIGHTPARENTHESIS,yyline, yycolumn,yytext());}
	"-"			{return new Symbol(LexicalUnit.MINUS,yyline, yycolumn,yytext());}
	"+"			{return new Symbol(LexicalUnit.PLUS,yyline, yycolumn,yytext());}
	":="			{return new Symbol(LexicalUnit.ASSIGN,yyline, yycolumn,yytext());}
	"*"			{return new Symbol(LexicalUnit.TIMES,yyline, yycolumn,yytext());}
	"/"			{return new Symbol(LexicalUnit.DIVIDE,yyline, yycolumn,yytext());}
	
	"begin"			{return new Symbol(LexicalUnit.BEG,yyline, yycolumn,yytext());}
	"end"			{return new Symbol(LexicalUnit.END,yyline, yycolumn,yytext());}
	"if"			{return new Symbol(LexicalUnit.IF,yyline, yycolumn,yytext());}
	"then"			{return new Symbol(LexicalUnit.THEN,yyline, yycolumn,yytext());}
	"endif"			{return new Symbol(LexicalUnit.ENDIF,yyline, yycolumn,yytext());}
	"else"			{return new Symbol(LexicalUnit.ELSE,yyline, yycolumn,yytext());}
	"not"			{return new Symbol(LexicalUnit.NOT,yyline, yycolumn,yytext());}
	"and"			{return new Symbol(LexicalUnit.AND,yyline, yycolumn,yytext());}
	"or"			{return new Symbol(LexicalUnit.OR,yyline, yycolumn,yytext());}
	"while"			{return new Symbol(LexicalUnit.WHILE,yyline, yycolumn,yytext());}
	"do"			{return new Symbol(LexicalUnit.DO,yyline, yycolumn,yytext());}
	"endwhile"			{return new Symbol(LexicalUnit.ENDWHILE,yyline, yycolumn,yytext());}
	"for"			{return new Symbol(LexicalUnit.FOR,yyline, yycolumn,yytext());}
	"from"			{return new Symbol(LexicalUnit.FROM,yyline, yycolumn,yytext());}
	"by"			{return new Symbol(LexicalUnit.BY,yyline, yycolumn,yytext());}
	"to"			{return new Symbol(LexicalUnit.TO,yyline, yycolumn,yytext());}
	"print"			{return new Symbol(LexicalUnit.PRINT,yyline, yycolumn,yytext());}
	"read"			{return new Symbol(LexicalUnit.READ,yyline, yycolumn,yytext());}

	{Integer}			{return new Symbol(LexicalUnit.NUMBER,yyline, yycolumn,new Integer(yytext()));}
	
	{Identifier}		{return new Symbol(LexicalUnit.VARNAME,yyline, yycolumn,yytext());}
	{Separator}		{}// ignore spaces
	";"			{return new Symbol(LexicalUnit.SEMICOLON,yyline, yycolumn,yytext());}
	[^]			{throw new PatternSyntaxException("Unmatched token, out of symbols",yytext(),yyline);}	//unmatched token gives an error
}
