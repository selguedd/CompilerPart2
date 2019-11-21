/**
 * We build this class to be able to print out a certain message
 * in case of an error while parsing
 *
 * @authors Salma El Gueddari, Naim Sassine
 */
public class ParserException extends Exception {
    public ParserException(String message){
        super(message);
    }
}
