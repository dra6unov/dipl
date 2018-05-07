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
	<form method="POST" action="/link">
		<input type="submit" value="Гугыл">
	</form>
	<div>
		<form method="POST" enctype="multipart/form-data" action="/up">
			File uploaded: <input type="file" name="file" /> <br> <input
				type="submit" value="Upload">
		</form>
	</div>

	<a th:href="${UploadedFile}" th:text="${ UploadedFile}">Ссылка на
		файл</a>

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
</body>
</html>