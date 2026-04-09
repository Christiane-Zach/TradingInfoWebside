<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Shazar Homepage</title>
<style><%@include file="/WEB-INF/css/style.css"%></style>
</head>
<body>
  <div class="container">
    <div class="row">
      <div class="logo col2">
        <br></br>
        <img src="ShaZar_logo-v1.png" width="125px">
      </div>
      <div class="col10 last-col empty">
        <br></br>
        <br></br>
        <br></br>
      </div>
    </div>
    <div class="row">
      <div class="col9 box">
        <a href="/">Home</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/newOffer">Create new offer</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/login">My account</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="text" placeholder="Search.." class="searchbar">
        <button type="submit">Submit</button>
      </div>
    </div>
    <div class="row">
      <div class="col2 categorybox">
          <br></br>
          <a href="/">Back</a>
      </div>
      <div class="col2 categorybox">
          <br></br>
          <h3>"${searchedFor}"</h3>
      </div>
      <div class="col8 empty last-col">
          <br></br>
      </div>
    </div>
    <c:forEach items="${items}" var="item">
    	<div class="row">
      		<div class="col12 categorybox">
        		<hr>
      		</div>
    	</div>
    	<div class="row">
      		<div class="col8 categorybox">
        		<p>"${item.name}"
          		<br></br>
          		<br></br>
          		Offer from: "${item.lender}"
          		<br></br>
          		<br></br>
          		"${item.description}"
          		</p>
      		</div>
      		<div class="col4 categorybox last-col">
        		<img src="chair.jfif">
        		<br></br>
        		<form action="./details" method="post">
        			<input type="hidden" value="${item.itemId}" name="itemId"/>
        			<input type="hidden" value="${category.name}" name="categoryName"/>
          			<input type="submit" value="More details"/>
        		</form>
      		</div>
    	</div>
    </c:forEach>
    <div class="row">
        <div class="col12 box">
          <a href="uber_uns.html">About us</a> |
          <a href="news.html">News</a> | 
          <a href="faq.html">FAQ</a> |
          <a href="impressum.html">Impressum</a>
        </div>
    </div> 
  </div>
</body>