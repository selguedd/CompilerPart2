public class Symbol {
	public static final int UNDEFINED_POSITION = -1;
	public static final Object NO_VALUE = null;

	private final LexicalUnit type;
	private final NotTerminal terminal;
	private final String value;
	private final int line, column;
	private  NotTerminal variable;

	public Symbol(LexicalUnit unit, int line, int column, String value) {
		this.type = unit;
		this.line = line + 1;
		this.column = column;
		this.value = value;
        this.variable = null;
        this.terminal=null;
	}

    public Symbol(NotTerminal variable){
	    this.terminal = variable;
	    this .value = null;
	    this.line = UNDEFINED_POSITION;
	    this.column = UNDEFINED_POSITION;
	    this.variable=variable;
		type = null;
	}

	public Symbol(LexicalUnit unit, int line, int column) {
		this(unit, line, column, null);
	}

	public Symbol(LexicalUnit unit, int line) {
		this(unit, line, UNDEFINED_POSITION, null);
	}

	public Symbol(LexicalUnit unit) {
		this(unit, UNDEFINED_POSITION, UNDEFINED_POSITION, null);
	}

	public Symbol(LexicalUnit unit, String value) {
		this(unit, UNDEFINED_POSITION, UNDEFINED_POSITION, value);
	}

	public boolean isTerminal() {
		return this.type != null;
	}

	public boolean isNonTerminal() {
		return this.type == null;
	}

	public LexicalUnit getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public int getLine() {
		return this.line;
	}

	public int getColumn() {
		return this.column;
	}

	public NotTerminal getVariable() {
	    return this.variable;
    }

	@Override
	public int hashCode() {
		final String value = this.value != null ? this.value.toString() : "null";
		final String type = this.type != null ? this.type.toString() : "null";
		return new String(value + "_" + type).hashCode();
	}

	@Override
	public String toString() {
		if (this.isTerminal()) {
			return type.toString();
		}
		else{
			return terminal.toString();
		}

	}


	public String toTexString() {
		if (this.isTerminal()) {
			String value = this.value != null ? this.value.toString() : "null";
			String type = this.type != null ? this.type.toString() : "null";
			if (type.contains("_")) {
				type = type.replace("_","'\\textunderscore ");
			}
			return "token: " + "$" + value + "$"+" " + "lexical unit: " + type;
		}
		return terminal.toString();
	}
}
