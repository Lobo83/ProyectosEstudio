package lobo.factory.querySystem.aspect;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import lobo.factory.queryStructure.QueryStructure;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;



@Aspect
public class LoggingQuerySystem {

	
	private Log log = LogFactory.getLog(LoggingQuerySystem.class);
	//Para que se cumpla se tiene que llamar al método directo de la Bean, si no no pasa por el punto de corte ya que está dentro del flujo del método de la bean
	@AfterReturning(pointcut="execution(* lobo.factory.querySystemEngine.QueryEngine.initialize(..))",
			returning="queryStructure")
	private void printQuery( QueryStructure queryStructure){

		String query=queryStructure.getSqlQuery();
		Map<String, Object> mapParameter=queryStructure.getParams();
		Set<String> keySet=mapParameter.keySet();
		for(String key:keySet){
			query=query.replace(":"+key, mapParameter.get(key).toString());	
		}
		log.info("Query a ejecutarse");
		
		
		log.info(query);
	}
	
	
}
