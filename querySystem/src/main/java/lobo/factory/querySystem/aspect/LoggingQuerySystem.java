package lobo.factory.querySystem.aspect;

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
		Vector <Object> params= queryStructure.getParameterValues();
		StringBuffer sqlFinal = new StringBuffer();
		int nextValToBePrinted=0;
		for(int i=0; i<query.length();i++){
			if(query.charAt(i)=='?'){
				sqlFinal.append(params.get(nextValToBePrinted));
				nextValToBePrinted++;
			}else{
				sqlFinal.append(query.charAt(i));
			}
		}
		log.info("Query a ejecutarse");
		log.info(sqlFinal.toString());
	}
	
}
