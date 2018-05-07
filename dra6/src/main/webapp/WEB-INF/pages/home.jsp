<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>dra6</h1>
	<div>
		<form method="POST" enctype="multipart/form-data" action="/up">
				File uploaded: <input type="file" name="file" />
			<br> <input type="submit" value="Upload">
		</form>
	</div>

 <a th:href="${UploadedFile}" th:text="${ UploadedFile}">Ссылка на файл</a>
</body>
</html>