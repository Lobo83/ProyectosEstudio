package org.pruebaspringsecurity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pruebaspringsecurity.bean.UserLoginBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;


@Controller
public class LoginController {
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			UserLoginBean usuario, BindingResult result) {
		
		AuthenticationManager manager=(AuthenticationManager)WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean("authenticationManager");
	
		String login=usuario.getLogin();
		String pass=usuario.getPassword();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login,pass);
		try{
		Authentication arg0 = manager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(arg0);//Esta linea establece las credenciales que permitiran al usuario moverse por las páginas definidas con su acceso (ACCESS="ROLE_USER")
		
		}  catch (Exception e){
			
			
			return "/youRNotAllowed";
		}
		
		return "/user/recursoProtegido";

	}
	


}
