<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Главная страница</title>
<script
  src="https://code.jquery.com/jquery-3.3.1.min.js"
  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
  crossorigin="anonymous"></script>
  <script src="/js/table.js" type="text/javascript"></script>
  <link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous">
</head>
<body>
	<div class="container-fluid">
	<div class="row">
	<div class="col-9">
	<h2>Добро пожаловать, ${username}</h2>
	</div>
	<div class="col-3">
	<form method="get" action="/logout">
			<button type="submit">Выйти из профиля</button>
		</form>
	</div>
	</div>
	<div class="row">
	<div class="col-1"></div>
	<div class="col-9">
		<form method="POST" enctype="multipart/form-data" action="/upload">
			Загрузка файла(ов): <input type="file" name="file" /> <br> 
			<div class="form-check">
				<input type="checkbox" class="form-check-input" id="GD" name="GD">
				<label class="form-check-label" for="GD">Google Drive</label>
			</div>
			<div class="form-check">
				<input type="checkbox" class="form-check-input" id="DP" name="DP">
				<label class="form-check-label" for="DP">Dropbox</label>
			</div>
			<input type="submit" value="Upload" class="btn btn-primary btn-lg">
		</form>
		<label>${upload_res }</label>
	</div>
	</div>
	<div class="row">
	<div class="col-9">
	<h2 align="left">Ваши файлы</h2>
	</div>
	</div>
	<div class="row">
	<table class="table table-striped">
		<thead>
		<tr>
			<th hidden="true">id</th>
			<th scope="col">Наименование файла</th>
			<th id="${google}" scope="col">Google Drive</th>
			<th id="${dropbox}" scope="col">Dropbox</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${map }" var="map">
			<tr id="${map.key}">
				<td hidden="true">${map.key}</td>
				<td>${map.value[0]}</td>
				<td id="${map.value[1]}">
				<c:choose> 
				<c:when test="${map.value[1]==null}"></c:when>
				<c:otherwise> 
				<form action="/google/download?id=${map.key}" method="post">
				<input type="submit" class="google">
				</form>
				</c:otherwise>
				</c:choose>
				</td>
				<td id="${map.value[1]}">
				<c:choose> 
				<c:when test="${map.value[2]==null}"></c:when>
				<c:otherwise> 
				<form action="/dropbox/download?id=${map.key}" method="post">
				<input type="submit" class="dropbox">
				</form>
				</c:otherwise>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
	</div>
	<script type="text/javascript">
	$('input').on('click',function(){
	    var id = $(this).closest('tr').attr('id');
	    
	    console.log(id);
	    if ($(this).hasClass("google")){
	    	$.ajax({
	    
	    	// dataType: "text",
	    	type: "POST",
	    	url: "/google/download",
	    	data: {id: id}
	    });}
	    if ($(this).hasClass("dropbox")){
	    	$.ajax({
	    	// dataType: "text",
	    	type: "POST",
	    	url: "/dropbox/download",
	    	data: {id: id}
	    });}
	});
	</script>
</body>
</html>