package os;

/**
 * The class OSException is the exception type that may be thrown from the OS
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	6.9.2005
 */
public class OSException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public OSException(){
		super();
	}
	
	public OSException(String s){
		super(s);
	}
	
	public OSException(Exception e){
		super(e);
	}
}
