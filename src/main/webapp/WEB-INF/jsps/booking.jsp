<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Shazar Homepage</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
</head>
<body bgcolor="#000000" text="#ffffff">

	<script>
	var array = [
		<c:forEach items="${dates}" var="date" varStatus="status">  
		    "${date}"
		    <c:if test="${!status.last}">,    
		    </c:if>  
		    </c:forEach>  
		];
	//var array = ["2021-08-18"]
	document.write(array)
	$(function () {
	  $('input').datepicker({
	    dateFormat: 'yy-mm-dd',
	    beforeShowDay: function(date) {
	      var string = jQuery.datepicker.formatDate('yy-mm-dd', date);
	      return [array.indexOf(string) == -1]
	    }
	  });
	});
	</script>
	
	<p>How long do you want to book the "${item.name}"?</p>
	<form action="./booking/finished" method="post">
		<label for="startDate"><b>Start date:</b></label>
		<br></br>
		<input type="date" name="startDate" required/>
		<br></br>
		<label for="endDate"><b>End date:</b></label>
		<br></br>
		<input type="date" name="endDate" required/>
		<br></br>
    	<button type="submit">Book the item</button>
	</form>
</body>