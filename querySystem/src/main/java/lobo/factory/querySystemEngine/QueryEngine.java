package lobo.factory.querySystemEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lobo.factory.queryStructure.QueryStructure;
import lobo.factory.querySystem.exception.QueryParsingException;
import lobo.factory.result.QueryResult;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class QueryEngine {
	private QuerySystem<?> querySystem;
	private static final String QUERY_TAG = "QUERY";
	private static final String SQL_QUERY_TAG = "SQL_QUERY";
	private static final String PARAMS_QUERY_TAG = "PARAMETER";
	private static final String PARAM_ATTRIBUTE_NAME = "NAME";

	private static final String PARAM_ATTRIBUTE_TYPE = "TYPE";
	private static final String SEPARATOR = "/";

	private static final String TYPE_INTEGER="INTEGER";
	private static final String TYPE_VARCHAR="VARCHAR";
	private static final String TYPE_DATE="DATE";
	private BasicDataSource ds;
	public BasicDataSource getDs() {
		return ds;
	}

	public void setDs(BasicDataSource ds) {
		this.ds = ds;
	}

	
	public QueryStructure initialize(String queryFile, Vector<Object> values) throws QueryParsingException{
		QueryStructure queryStructure = getQueryStructureFromFile(queryFile, values);
		querySystem = new QuerySystem(ds, queryStructure.getSqlQuery(),queryStructure.getParameterValues(),queryStructure.getParameterTypes());
		return queryStructure;
	}
	
	public QueryResult<?> executeQuery(){
		return querySystem.executeQuery();
	}

	private QueryStructure getQueryStructureFromFile(final String queryFile, Vector<Object> values) throws QueryParsingException {
		QueryStructure queryStructure = new QueryStructure();
		Map<String, Object> parameters = new HashMap<String,Object>();
		 Map<String, Integer> parameterType=new  HashMap<String, Integer> ();
		DocumentBuilder documentBuilder;
		Document doc;
		
		
		// Procesamos el fichero XML y obtenemos nuestro objeto Document
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = documentBuilder.parse(new InputSource(new FileInputStream(
					queryFile)));
			// Implementación de XPath por defecto en Java. Lectura de la
			// sentencia SQL
			Node sqlQuery = (Node) (XPathFactory.newInstance().newXPath()
					.evaluate(
							QUERY_TAG + SEPARATOR + SQL_QUERY_TAG,
							doc, XPathConstants.NODE));
			queryStructure.setSqlQuery(sqlQuery.getTextContent());

			// Lectura de la lista de parámetros
			NodeList listaParametros = doc
					.getElementsByTagName(PARAMS_QUERY_TAG);
			for (int i = 0; i < listaParametros.getLength(); i++) {
				Node nodo = listaParametros.item(i);
				if (nodo instanceof Element) {
					//setting parameter values
					parameters.put(":"+((Element) nodo)
							.getAttribute(this.PARAM_ATTRIBUTE_NAME),
							values.get(i));
					parameterType.put(":"+((Element) nodo)
							.getAttribute(this.PARAM_ATTRIBUTE_NAME),translateToSqlType(((Element) nodo)
							.getAttribute(this.PARAM_ATTRIBUTE_TYPE)));

				}
			}
			queryStructure.setParams(parameters);
			queryStructure.setParamsTypes(parameterType);
			replaceParameter(queryStructure);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new QueryParsingException(e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new QueryParsingException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new QueryParsingException(e);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new QueryParsingException(e);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new QueryParsingException(e);
		}

		return queryStructure;

	}
	public void replaceParameter(QueryStructure queryStructure){
		String sql= queryStructure.getSqlQuery();
		StringBuffer finalSql = new StringBuffer();
		Map<String, Vector<Integer>> parameterPositions = new HashMap<String,Vector<Integer>>();
		int position=0; //the position to put in the values, at the end, the size of the final parameter values vector
		int sqlLength=sql.length();
		//Search for the parameters inside the query	

		for(int i=0; i<sqlLength;i++){
			if(sql.charAt(i)==':'){//Parameter found

				
				for(int j=i; j<sqlLength;j++){//Search for the end of the parameter
					if(sql.charAt(j)==' '){
						String parameter = sql.substring(i,j);//get the parameter without blank space
						if(parameterPositions.containsKey(parameter)){
							((Vector<Integer>)parameterPositions.get(parameter)).add(position);//Save the position
						}else{//New parameter found.
							Vector<Integer> positions=new Vector<Integer>();
							positions.add(position);
							parameterPositions.put(parameter, positions);
						}//else
						//jump the parameter in outer for
						i=j-1;//Next value of i will be the position of the blank space after the parameter
						//break the inner for					
						break;
					}else{//if(sql.charAt(j)==' ')
						if(j==sql.length()-1){//this means that parameter is at the end of the query
							String parameter=sql.substring(i,j+1);
							if(parameterPositions.containsKey(parameter)){
								((Vector<Integer>)parameterPositions.get(parameter)).add(position);//Save the position
							}else{//New parameter found.
								Vector<Integer> positions=new Vector<Integer>();
								positions.add(position);
								parameterPositions.put(parameter, positions);
							}//else
							//jump the parameter in outer for
							i=j;//Next value of i will be the position of the blank space after the parameter
							//break the inner for					
							break;
						}//if(j==sql.length()-1)
							
					}//else
					
				}//for(int j...)
				finalSql.append('?');//the final sql will have ? instead of the parameter
				position++;
			}else{//if(sql.charAt(i)==':')
				finalSql.append(sql.charAt(i));//this position doesn´t mean a parameter, so concat to the final sql
			}
		}//for(int i=0;
		
		if(position==0){//No parameter in the query. Nothing to do
			return;
		}
		//Values found.
		queryStructure.setSqlQuery(finalSql.toString());//Save the final query
		Set<String> keySet=parameterPositions.keySet();
		Vector<Object> values = initVector(position);
		Vector<Integer> types = initVector(position);
		for(String key: keySet){//create the new parameter 
			Vector<Integer> vPosition = parameterPositions.get(key);
			Object value = queryStructure.getParams().get(key); //get the value of the parameter. This value will be put in the right position to be passed in to the query
			Integer type=queryStructure.getParamsTypes().get(key);
			for(int i=0; i<vPosition.size(); i++){
				int pos=vPosition.get(i);
				
				values.set(pos, value);
				types.set(pos,type);
			}
		}
		queryStructure.setParameterValues(values);
		queryStructure.setParameterTypes(types);
	}
	
	private Integer translateToSqlType(String type){
		if(type.equalsIgnoreCase(this.TYPE_INTEGER)){
			return java.sql.Types.INTEGER;
		}else if(type.equalsIgnoreCase(this.TYPE_VARCHAR)){
			return java.sql.Types.VARCHAR;
		}else if(type.equalsIgnoreCase(this.TYPE_DATE)){
			return java.sql.Types.DATE;
		}
		return 0;
	}
	private <T> Vector<T> initVector(int size){
		Vector<Object> v = new Vector<Object>();
		for(int i=0; i<size; i++){
			v.add(new Object());
		}
		return (Vector<T>) v;
	}
}
