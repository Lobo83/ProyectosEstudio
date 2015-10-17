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
	
	private static final String QUERY_TAG = "QUERY";
	private static final String SQL_QUERY_TAG = "SQL_QUERY";
	private static final String SQL_QUERY_NAME = "QUERY_NAME";
	private static final String PARAMS_QUERY_TAG = "PARAMETER";
	private static final String PARAM_ATTRIBUTE_NAME = "NAME";

	private static final String SEPARATOR = "/";

	

	private BasicDataSource ds;
	private QuerySystem querySystem;
	private String queriesDirectory;


	public String getQueriesDirectory() {
		return queriesDirectory;
	}

	public void setQueriesDirectory(String queriesDirectory) {
		this.queriesDirectory = queriesDirectory;
	}

	public BasicDataSource getDs() {
		return ds;
	}

	public void setDs(BasicDataSource ds) {
		this.ds = ds;
	}

	
	public QueryStructure initialize(String queryFile, Vector<Object> values) throws QueryParsingException{
		QueryStructure queryStructure = getQueryStructureFromFile(queryFile, values);
		querySystem = new QuerySystem(ds);
		return queryStructure;
	}
	
	public QueryResult<?> executeQuery(QueryStructure queryStructure){
		return querySystem.executeQuery(queryStructure);
	}



	private QueryStructure getQueryStructureFromFile(final String queryFile,
			Vector<Object> values) throws QueryParsingException {
		QueryStructure queryStructure = new QueryStructure();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Map<String, Integer> parameterType = new HashMap<String, Integer>();
		DocumentBuilder documentBuilder;
		Document doc;
		final String completeNameQueryFile=queriesDirectory+queryFile;

		// Procesamos el fichero XML y obtenemos nuestro objeto Document
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = documentBuilder.parse(new InputSource(new FileInputStream(
					completeNameQueryFile)));
			// Implementación de XPath por defecto en Java. Lectura de la
			// sentencia SQL
			Node sqlQuery = (Node) (XPathFactory.newInstance().newXPath()
					.evaluate(QUERY_TAG + SEPARATOR + SQL_QUERY_TAG, doc,
							XPathConstants.NODE));
			Node sqlQueryName = (Node) (XPathFactory.newInstance().newXPath()
					.evaluate(QUERY_TAG + SEPARATOR + SQL_QUERY_NAME, doc,
							XPathConstants.NODE));
			
			queryStructure.setSqlQuery("/*"+sqlQueryName.getTextContent()+" */ \n"+sqlQuery.getTextContent());

			// Lectura de la lista de parámetros
			NodeList listaParametros = doc
					.getElementsByTagName(PARAMS_QUERY_TAG);
			for (int i = 0; i < listaParametros.getLength(); i++) {
				Node nodo = listaParametros.item(i);
				if (nodo instanceof Element) {
					// setting parameter values
					parameters
							.put(((Element) nodo)
											.getAttribute(this.PARAM_ATTRIBUTE_NAME),
									values.get(i));
					
				}
			}
			queryStructure.setParams(parameters);
			
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

	

	
}
