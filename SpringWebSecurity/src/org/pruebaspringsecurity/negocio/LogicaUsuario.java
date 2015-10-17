package org.pruebaspringsecurity.negocio;

import org.springframework.security.access.annotation.Secured;


public class LogicaUsuario implements LogicaUsuarioInterface {//no hay que olvidar que esto se utiliza en Aspectos, por lo que se tienen que utilizar interfaces o CGLIB

	@Secured("ROLE_ADMIN")
	public  String getAdminMessage(){
		return "Hola soy Admin";
	}
	@Secured("ROLE_USER")
	public  String getUserMessage(){
		return "Hola soy User";
	}
}
