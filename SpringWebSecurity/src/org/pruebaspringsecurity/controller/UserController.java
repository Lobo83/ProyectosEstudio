package org.pruebaspringsecurity.controller;

import javax.servlet.http.HttpSession;

import org.pruebaspringsecurity.bean.UsuarioMessageBean;
import org.pruebaspringsecurity.negocio.LogicaUsuarioInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Controller
public class UserController {
	
	@RequestMapping(value = "/user/recursoProtegido2", method = RequestMethod.GET)
	public String doSomething(Model model, HttpSession session){
		UsuarioMessageBean userMessage = new UsuarioMessageBean();
		WebApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
		LogicaUsuarioInterface logicaUsuario = (LogicaUsuarioInterface) ctx.getBean("logicaUsuario");
		try{
			userMessage.setMensaje(logicaUsuario.getAdminMessage());
		}catch(Exception e){
			userMessage.setMensaje("SORRY MEN, NO ERES ADMIN");
		}
		
		model.addAttribute("userMessage",userMessage);
		return "/user/recursoProtegido2";
	}
	
	@RequestMapping(value = "/cacaDeVaca", method = RequestMethod.GET)
	public String doSomethingElse(Model model){
		UsuarioMessageBean userMessage = new UsuarioMessageBean();
		userMessage.setMensaje("");
		
		model.addAttribute("userMessage",userMessage);
		return "/cacaDeVaca";
	}
}
