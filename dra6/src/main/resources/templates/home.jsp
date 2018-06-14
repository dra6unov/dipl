<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script
  src="https://code.jquery.com/jquery-3.3.1.min.js"
  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
  crossorigin="anonymous"></script>
  <script src="/js/table.js" type="text/javascript"></script>
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
	<table >
		<caption>Ваши файлы</caption>
		<tr>
			<th hidden="true">id</th>
			<th>Наименование файла</th>
			<th>Google Drive</th>
			<th>Dropbox</th>
		</tr>


		<c:forEach items="${fileValues }" var="fileValue">
			<tr>
				<td hidden="true">${fileValue.key}</td>
				<td>${fileValue.value}</td>
				<td>download</td>
				<td>download</td>
			</tr>
		</c:forEach>
	</table>

	<br>
	<br>
	<div>
		<label>${aut}</label>
	</div>

	<div>
		<form method="get" action="/logout">
			<button type="submit">Выйти из профиля</button>
		</form>
	</div>

	<div>
		<form method="POST" enctype="multipart/form-data" action="/upload">
			Загрузка файла(ов): <input type="file" name="file" /> <br> <input
				type="submit" value="Upload">
			<div class="form-check">
				<input type="checkbox" class="form-check-input" id="GD" name="GD">
				<label class="form-check-label" for="GD">Google Drive</label>
			</div>
			<div class="form-check">
				<input type="checkbox" class="form-check-input" id="DP" name="DP">
				<label class="form-check-label" for="DP">Dropbox</label>

			</div>
		</form>
		<label>${upload_res }</label>
	</div>
	
	<table>
		<caption>Ваши файлы</caption>
		<tr>
			<th hidden="true">id</th>
			<th>Наименование файла</th>
			<th>Google Drive</th>
			<th>Dropbox</th>
		</tr>


		<c:forEach items="${map }" var="map">
			<tr id="${map.key}">
				<td hidden="true">${map.key}</td>
				<td>${map.value[0]}</td>
				<td>
				<c:choose> 
				<c:when test="${map.value[1]==null}"></c:when>
				<c:otherwise><a th:href="@{'/google/allow'}">download</a></c:otherwise>
				</c:choose>
				</td>
				<td>
				<c:choose> 
				<c:when test="${map.value[2]==null}"></c:when>
				<c:otherwise><a>download</a></c:otherwise>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<script type="text/javascript">
	$('td').on('click',function(){
	    var id = $(this).closest('tr').attr('id');
	    console.log(id);
	    $.ajax({
	    	type: "POST",
	    	url: "/google/download",
	    	data: {id: id}
	    });
	    $.ajax({
	    	type: "POST",
	    	url: "/dropbox/download",
	    	data: {id: id}
	    });
	});
	</script>
</body>
</html>