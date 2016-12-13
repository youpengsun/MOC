<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Exception Occured</title>
<meta charset="UTF-8">
<title>Administrator Login</title>
<link rel="stylesheet" href="/moc/static/css/errorpage/font.css">
<link rel="stylesheet" href="/moc/static/css/errorpage/style.css">
</head>

<body>
	<hr id="headline" />
	<div class="container">	
		<img class="oops" alt="" src="/moc/static/image/oops.png">

		<div>
			<h1 class="title">Oops! Exception Occurred:</h1>
		</div>
		<div class="content">
			<p>
				${errorMessage}
			</p>
		</div>
		<div class="footer">
			<a href="/moc/index.html">Back Home</a>
		</div>
	</div>

</body>

</html>