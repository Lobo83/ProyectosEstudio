package lobo.factory.queryStructure;

import java.util.Map;
import java.util.Vector;

public class QueryStructure {

	private String sqlQuery;
	private Map<String, Object> params;
	private Map<String, Integer> paramsTypes;
	private Vector<Object> parameterValues;
	private Vector<Integer> parameterTypes;
	
	
	public Vector<Integer> getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Vector<Integer> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Map<String, Integer> getParamsTypes() {
		return paramsTypes;
	}
	public void setParamsTypes(Map<String, Integer> paramsTypes) {
		this.paramsTypes = paramsTypes;
	}
	
	public Vector<Object> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(Vector<Object> parameterValues) {
		this.parameterValues = parameterValues;
	}
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
