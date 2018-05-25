<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>dra6</h1>
	<h2>Добро пожаловать, ${username}</h2>
	
	<div>
	<form method="POST" action="/gg">
	<input type="text" name="usergroup" placeholder="введите группу">
	<button type="submit">Добавить группу</button>
	</form>
	</div>
	<br>
	
	<label>${mes}</label>
	<br>
	
	
	
	<div>
	<form action="POST">
	<input type="text" name="login" placeholder="Введите логин">
	<input type="text" name="pass" placeholder="Введите пароль">
	<button type="submit">Добавить</button>
	</form>
	</div>
	<br>
	<br>
	
	
	<br>
	<br>
	<form method="GET" action="/drive">
		<input type="Button" value="drive">
	</form>
	
	<form method="POST" action="/link">
		<input type="submit" value="Гугыл">
	</form>
	
	<div>
		<form method="POST" enctype="multipart/form-data" action="/up">
			Загрузка файла(ов): <input type="file" name="file" /> <br> <input
				type="submit" value="Upload">
		</form>
	</div>

	<ul>
		<c:forEach items="${lists }" var="lists">
			<li>${lists}</li>
		</c:forEach>
	</ul>

	<ul>
		<c:forEach items="${filesName }" var="filesName">
			<li>${filesName}</li>
		</c:forEach>
	</ul>
	
	<br>
	<br>
	<div>
	<label>${aut}</label>
	</div>
	
	<div>
	<form method="get" action="/logout">
	<button  type="submit">Выйти из профиля</button>
	</form>
	</div>
</body>
</html>