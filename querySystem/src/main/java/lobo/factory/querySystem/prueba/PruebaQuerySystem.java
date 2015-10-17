package lobo.factory.querySystem.prueba;

import java.util.Vector;

import lobo.factory.querySystem.exception.QueryParsingException;
import lobo.factory.querySystemEngine.QueryEngine;
import lobo.factory.result.QueryResult;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PruebaQuerySystem {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new ClassPathXmlApplicationContext("DataSourceContext.xml");
		String queryFile="D:/proyectosSpring/proyectos importados/querySystem/src/main/resources/querys/queryPrueba.xml";
		Vector<Object> values =new Vector<Object>();
		values.add(new Integer(1));
		values.add("pepe");
		values.add("lala");
		QueryEngine querySystem;
		QueryResult result = null;
		try {
			//querySystem = new QueryEngine(queryFile,(BasicDataSource) ctx.getBean("dataSource"),values);
			querySystem = (QueryEngine) ctx.getBean("queryEngine");
			querySystem.initialize(queryFile, values);
			result=querySystem.executeQuery();

		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QueryParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    System.out.println(result.getHeaders().toString());
	    System.out.println(result.getResult().toString());
	}

}
