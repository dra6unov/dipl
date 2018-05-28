<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous">
<link th:href="@{/css/style.css}" rel="stylesheet" />
</head>

<body class="bod">
	<h2>${grats}</h2>
	<%
		if (session.getAttribute("user") != null) {
			response.sendRedirect("/");
		}
	%>
	<div class="container-fluid">
		<div class="row">
			<div class="col-6"></div>
			<div class="col-6">
				<div>
					<h2>Регистрация</h2>
					<form method="post" action="/reg">
						<div class="form-group">
							<label for="forlogin1">Введите логин</label> <input type="text"
								name="login" class="form-control" id="gorlogin1"
								aria-describedby="loginHelp" placeholder="Логин">
						</div>
						<div class="form-group">
							<label for="forpass1">Введите пароль</label> <input
								type="password" name="pass" class="form-control" id="forpass1"
								placeholder="Пароль">
						</div>
						<div class="form-group">
							<label for="forFirstName">Введите имя</label> <input type="text"
								name="first_name" class="form-control" id="forFirstName"
								placeholder="Имя">
						</div>
						<div class="form-group">
							<label for="forLastName">Введите фамилию</label> <input
								type="text" name="last_name" class="form-control"
								id="forFirstName" placeholder="Фамилия">
						</div>
						<div class="form-group">
							<label for="forEmail">Введите электронную почту</label> <input
								type="email" name="email" class="form-control" id="forEmail"
								placeholder="email">
						</div>
						<button type="submit" class="btn btn-outline-primary">Зарегистрироваться</button>
					</form>
				</div>
				<br>
				<div>
					<h2>Авторизация</h2>
					<form method="post" action="au">
						<div class="form-group">
							<label for="forlogin2">Введите логин</label> <input type="text"
								name="login" class="form-control" id="gorlogin2"
								aria-describedby="loginHelp" placeholder="Логин">
						</div>
						<div class="form-group">
							<label for="forpass2">Введите пароль</label> <input
								type="password" name="pass" class="form-control" id="forpass2"
								placeholder="Пароль">
						</div>
						<button type="submit" class="btn btn-outline-primary">Войти</button>
					</form>
				</div>
				<br>
				<div>
					<label>${error}</label>
				</div>
			</div>
		</div>
	</div>
</body>
</html>