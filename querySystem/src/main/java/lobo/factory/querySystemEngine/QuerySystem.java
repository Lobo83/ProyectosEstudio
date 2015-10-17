package lobo.factory.querySystemEngine;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;


import java.util.Vector;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import lobo.factory.result.QueryResult;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.MappingSqlQuery;

public class QuerySystem<T> extends MappingSqlQuery<T> {
	private QueryResult<T> queryResult = new QueryResult<T>();
	private Object[] values;
	private Log log = LogFactory.getLog(QuerySystem.class);
	
	public QuerySystem(final BasicDataSource ds, final String query, final Vector<Object>params, final Vector<Integer> types){
		
		super(ds,query);
		values=new Object[params.size()];
		params.copyInto(values);
		
		for(int i=0; i<types.size();i++){
			super.declareParameter(new SqlParameterValue(types.get(i),values[i]));//Hay que hacer un traductor de tipos java a sql
		}
		compile();
		//printQuery(query,params);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		// TODO Auto-generated method stub		
		ResultSetMetaData metaData = rs.getMetaData();
		List<String> headers = new Vector<String>();
		int columnNumber = metaData.getColumnCount();
		Vector<Object>record = new Vector<Object>();
		for (int i = 1; i <= columnNumber; i++) {//warning: columns array starts in 1 not in 0
			if (rowNum == 0) {// first iteration sets headers
				if(null==metaData.getColumnLabel(i)){
					headers.add(metaData.getColumnName(i));
				}else{
					headers.add(metaData.getColumnLabel(i));
				}
			}// if(rowNum==0)
			
			record.add(rs.getObject(i));
		}
		if (rowNum == 0) {// first iteration sets headers
			queryResult.setHeaders(headers);
		}// if(rowNum==0)
		
		return (T) record;
	}
	@SuppressWarnings("unchecked")
	public QueryResult executeQuery(){
	   
		List results=super.execute(values);
		this.queryResult.setResult(results);
		return this.queryResult;
	}
    
	
}
