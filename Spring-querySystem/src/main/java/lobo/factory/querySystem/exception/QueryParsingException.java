package lobo.factory.querySystem.exception;

public class QueryParsingException extends Exception {
	public QueryParsingException(Exception e){
		super(e);
	}
	public QueryParsingException(String msg){
		super(msg);
	}
}
