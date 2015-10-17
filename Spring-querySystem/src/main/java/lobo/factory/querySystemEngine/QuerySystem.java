package lobo.factory.querySystemEngine;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Vector;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lobo.factory.queryStructure.QueryStructure;
import lobo.factory.result.QueryResult;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.object.MappingSqlQuery;

class QuerySystem {
	private QueryResult queryResult = new QueryResult();
	NamedParameterJdbcTemplate jdbTemplate;

	private Log log = LogFactory.getLog(QuerySystem.class);

	protected QuerySystem(BasicDataSource dataSource) {
		this.jdbTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	protected QueryResult<?> executeQuery(QueryStructure queryStructure){
		Map<String, Object> mapParameters=queryStructure.getParams();
		Set<String> keySet=mapParameters.keySet();
		MapSqlParameterSource namedParameter=new MapSqlParameterSource();
		for(String key: keySet){
			namedParameter.addValue(key, mapParameters.get(key));
		}
		QuerySystemRowMapper rowMapper=new QuerySystemRowMapper();
		
		 this.queryResult.setResult( this.jdbTemplate.query(queryStructure.getSqlQuery(),namedParameter,rowMapper));
		 
		 return this.queryResult;
	}

	@SuppressWarnings("unchecked")
	
	private class QuerySystemRowMapper implements RowMapper<Vector<Object>> {
		public Vector<Object> mapRow(final ResultSet rs, final int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			ResultSetMetaData metaData = rs.getMetaData();
			List<String> headers = new Vector<String>();
			int columnNumber = metaData.getColumnCount();
			Vector<Object> record = new Vector<Object>();
			for (int i = 1; i <= columnNumber; i++) {// warning: columns array
														// starts in 1 not in 0
				if (rowNum == 0) {// first iteration sets headers
					if (null == metaData.getColumnLabel(i)) {
						headers.add(metaData.getColumnName(i));
					} else {
						headers.add(metaData.getColumnLabel(i));
					}
				}// if(rowNum==0)

				record.add(rs.getObject(i));
			}
			if (rowNum == 0) {// first iteration sets headers
				queryResult.setHeaders(headers);
			}// if(rowNum==0)

			return  record;
		}
	}

	
}
