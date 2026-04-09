<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>welcome to trading informations</title>
<style>
.container {
	text-align: center}
</style>
</head>
<body bgcolor="#000000" text="#ffffff">
	<div class="container">
	
		<form action="/search" method="post">
			<input type="text" placeholder="Search for an item..." name="searchedFor" class="searchbar"/>
			<button type="submit">Search</button>
		</form>
		
		
		<br></br>
		<h1>Welcome</h1>
		<br></br>
		<h2>Here you get information for your trading</h2>
		<br></br>
		<p>Available currency pairs:</p>
		<c:forEach items="${pairNames}" var="pairName">
				<form action="/${pairName}" method="post">
					<input type="hidden" value="${pairName}" name="pairName"/><br/>
					<input type="submit" value="${pairName}"/>
				</form>
		</c:forEach>
	</div>
	
</body>
</html>