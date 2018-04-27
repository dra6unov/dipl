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
		<form method="POST" enctype="multipart/form-data" action="/">
			<p>
				File uploaded: <input type="file" name="file" />
			</p>
			<br> <input type="submit" value="Upload">
		</form>
	</div>
<div>
<ul>
<li th:each="file : ${files}"> <a th:href="${file}" th:text="${file }"></a></li>
</ul>
</div>
</body>
</html>