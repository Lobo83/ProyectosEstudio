package org.lobo.web.controller;

import java.util.Vector;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import lobo.factory.querySystemEngine.QueryEngine;
public class HelloWorldController extends AbstractController {
	private static final String DISPATCHER_SERVLET_CONTEXT_ATTR_NAME =
            "org.springframework.web.servlet.FrameworkServlet.CONTEXT.querySystem";
	
	@Override

	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView myModel = new ModelAndView("HolaHola");
		myModel.addObject("message", "hola hola");
		System.out.println("Me cago en to ya");
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		//Se carga una bean del contexto del root
		org.apache.commons.dbcp.BasicDataSource prueba1 = (org.apache.commons.dbcp.BasicDataSource) ctx.getBean("dataSource");
		//Se carga una bean del contexto servlet
		ctx=WebApplicationContextUtils.getWebApplicationContext(this.getServletContext(),DISPATCHER_SERVLET_CONTEXT_ATTR_NAME);
		//org.apache.commons.dbcp.BasicDataSource dataSource=(org.apache.commons.dbcp.BasicDataSource)ctx.getBean("tarugo"); //Devuelve la bean tarugo del container
		org.apache.commons.dbcp.BasicDataSource dataSource=(org.apache.commons.dbcp.BasicDataSource)ctx.getBean("dataSource");//Devuelve la bean dataSource del contexto padre pero accediendo a través del contexto hijo, es decir, querySystem-servlet.xml
		System.out.println("mira lo que tengo "+dataSource.toString());
		System.out.println("tomcat arreglado??SIIIIII.....EEEE...oououou");
		QueryEngine querySystem =(QueryEngine) ctx.getBean("queryEngine");
		
		//Probando invocacion seria de bean cuyo tipo está definida en el proyecto QuerySystem (dependencia añadida)
		String queryFile="queryPrueba.xml";
		Vector<Object> values =new Vector<Object>();
		Vector<Integer> ids=new Vector<Integer>();
		ids.add(new Integer(10));
		ids.add(new Integer(9));
		values.add(ids);
		values.add("pepe");
		values.add("lala");
		querySystem.executeQuery(querySystem.initialize(queryFile, values));
		return myModel;
	}

	

}
