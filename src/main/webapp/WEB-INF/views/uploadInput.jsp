<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>upload test page</h1>

<form action="/upload" method="post" enctype="multipart/form-data"> <!-- 파일 업로드는 항상 포스트 -->
<input type="file" name="files" multiple="multiple">
<button>upload</button></form>
</body>
</html>