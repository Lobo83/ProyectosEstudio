<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script type="text/javascript">
		function validateLogin(loginForm){
			
			if(loginForm.login.value.length == 0 || loginForm.password.value.length == 0){
				alert("Debe rellenar login y password para continuar");
				return false;
			}
			return true;
		}
	</script>
<body>
<form name="login" action="login" method="post" onsubmit="return validateLogin(this);">

<table align="center"  bgcolor="#C0C0C0" style="width: 1220px; "> 
		<tr><td style="width: 350px; "></td><td>Login: <input alt="Nombre de usuario" type="text" id="login" name="login"/></td><td>Password: <input type="password" alt="Contrasenia" id="password" name="password"/></td><td><input value="Entrar" type="submit" name="buttonLogin" id="buttonLogin"/></td><td><input value="Crear Cuenta" type="submit" name="buttonLogin" id="buttonLogin" style="width: 91px; "/></td><td style="width: 259px; "></td></tr>
		</table>
	</form>
</body>
</html>