package lobo.factory.queryStructure;

import java.util.Map;
import java.util.Vector;

public class QueryStructure {

	private String sqlQuery;
	private Map<String, Object> params;

	
	
	
	public String getSqlQuery() {
		return sqlQuery;
	}
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
