<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>${grats}</h2>
<%
if (session.getAttribute("user")!=null){
	response.sendRedirect("/");
}
%>

	<div>
	<h2>Зарегистрируйтесь!</h2>
		<form method="post" action="/reg">
			<input type="text" name="login" placeholder="Введите логин">
			<br>
			<input type="password" name="pass" placeholder="Введите пароль">
			<br>
			<input type="text" name="first_name" placeholder="Введите имя">
			<br>
			<input type="text" name="last_name" placeholder="Введите фамилию">
			<br>
			<input type="text" name="email" placeholder = "Введите электронную почту">
			<br>
			<button type="submit">Зарегистрироваться</button>
		</form>
	</div>
	
	<div>
	<h2>Уже зарегистрированы в системе? Авторизируйтесь!</h2>
	<form method="post" action="au">
	<input type="text" name="login" placeholder="Введите логин">
	<br>
	<input type="text" name="pass" placeholder="Введите пароль">
	<br>
	<button type="submit">Авторизироваться</button>
	</form>
	</div>
	<br>
	<div>
	<label>${error}</label>
	</div>

</body>
</html>