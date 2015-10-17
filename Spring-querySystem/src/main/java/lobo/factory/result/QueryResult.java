package lobo.factory.result;


import java.util.List;


public class QueryResult<T> {

	private List<T> result;
	private List<String> headers;
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> results) {
		this.result =  results;
	}
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String>  headers) {
		this.headers = headers;
	}
	

}
