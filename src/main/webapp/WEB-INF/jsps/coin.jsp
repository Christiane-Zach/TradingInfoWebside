<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<script type="text/javascript" src="https://d3js.org/d3.v3.min.js"></script>
<title>trader information</title>
<style>
	.float-left{
	float: left;
	width: 14%;
	}
	.float-right{
	float: left;
	width: 85%;
	}
	.line {
  	fill: none;
  	stroke: #ffffff;      // starts invisible, made visible with transition
  	stroke-width: 1px;  // half is inside the margin, half is outside
	}
	.axis path, .axis line {
  	fill: none;
  	stroke: #ffffff;
  	shape-rendering: crispEdges;
	}
</style>
</head>
<body bgcolor="#000000" text="#ffffff">
	<h3>${oldPairName}</h3>
	
	<div class="container">
		<div class="float-left">
			<br></br>
			<br></br>
			<p>Available currency pairs:</p>
			<c:forEach items="${pairNames}" var="pairName">
				<form action="/${pairName}" method="post">
					<input type="hidden" value="${pairName}" name="pairName"/><br/>
					<input type="submit" value="${pairName}"/>
				</form>
			</c:forEach>
			<br></br>
			<form action="/${pairName}/${interval}/zoomedtime" method="post">
				<input type="hidden" value="${oldPairName}" name="pairName"/><br/>
				<input type="hidden" value="${oldInterval}" name="interval"/><br/>
				<p>Enter start time:</p>
				<input type="text" placeholder="Hour" name="startHour"/>
				<input type="text" placeholder="Minute" name="startMinute"/>
				<input type="text" placeholder="Day" name="startDay"/>
				<input type="text" placeholder="Month" name="startMonth"/>
				<input type="text" placeholder="Year" name="startYear"/>
				<p>Enter end time:</p>
				<input type="text" placeholder="Hour" name="endHour"/>
				<input type="text" placeholder="Minute" name="endMinute"/>
				<input type="text" placeholder="Day" name="endDay"/>
				<input type="text" placeholder="Month" name="endMonth"/>
				<input type="text" placeholder="Year" name="endYear"/>
				<br></br>
				<input type="submit" value="set time"/>
			</form>
		</div>
		<div class="float-right" style="display: flex;">
			<c:forEach items="${intervals}" var="interval">
				<form action="/${oldPairName}/${interval.key}" method="post">
					<input type="hidden" value="${oldPairName}" name="pairName"/><br/>
					<input type="hidden" value="${interval.key}" name="interval"/><br/>
					<input type="submit" value="${interval.key}"/>
				</form>
			</c:forEach>
		</div>
		<div class="float-right">
			<p>${oldInterval} Chart</p>
			<br></br>
			<p>Binance</p>
		</div>
		<div class="float-right">
			<script>
			function xyDiagramm(data) {
				var outerWidth = 960, outerHeight = 500;
				var margin = {top: 100, right: 20, bottom: 80, left: 120};
				var width = outerWidth - margin.left - margin.right, 
		    		height = outerHeight - margin.top - margin.bottom;  

				function xValue(d) { return d.x; }
				function yValue(d) { return d.y; }
			
				var x = d3.scale.linear()                // interpolator for X axis -- inner plot region
		    		.domain(d3.extent(data,xValue))
		    		.range([0,width]);

				var y = d3.scale.linear()                // interpolator for Y axis -- inner plot region
		    		.domain(d3.extent(data,yValue))
		    		.range([height,0]);                  // remember, (0,0) is upper left -- this reverses "y"

				var line = d3.svg.line()                 // SVG line generator
		    		.x(function(d) { return x(d.x); } )
		    		.y(function(d) { return y(d.y); } );

				var xAxis = d3.svg.axis()                // x Axis
		    		.scale(x)
		    		.ticks(10)                          
		    		.orient("bottom");

				var yAxis = d3.svg.axis()                // y Axis
		    		.scale(y)
		    		.ticks(10)
		    		.orient("left");

				var svg = d3.select("body").append("svg")
		    		.attr("width",  outerWidth)
		    		.attr("height", outerHeight);        // Note: ok to leave this without units, implied "px"

				var g = svg.append("g")                  // <g> element is the inner plot area (i.e., inside the margins)
		    		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

				g.append("g")                            // render the Y axis in the inner plot area
					.style("stroke", "white")
					.attr("class", "y axis")
		    		.call(yAxis);

				g.append("g")                            // render the X axis in the inner plot area
					.style("stroke", "white")
					.attr("class", "x axis")
		    		.attr("transform", "translate(0," + height + ")")  // axis runs along lower part of graph
		    		.call(xAxis);

				g.append("text")                         // outer x-axis label
					.style("stroke", "white")
					.attr("class", "x label") 
		    		.attr("text-anchor", "end") 
		    		.attr("x", width/2) 
		    		.attr("y", height + 2*margin.bottom/3 + 6) 
		    		.text("time");

				g.append("text")                         // outer y-axis label
					.style("stroke", "white")
					.attr("class", "x label") 
		    		.attr("text-anchor", "middle") 
		    		.attr("x", -height/2)
		    		.attr("y", -6 - margin.left/3)
		    		.attr("dy", "-.75em") 
		    		.attr("transform", "rotate(-90)") 
		    		.text("${oldPairName}");

				g.append("path")                         // plot the data as a line
		    		.datum(data)
		    		.attr("class", "line")
		    		.attr("d", line);

				g.selectAll(".dot")                      // plot a circle at each data location
		    		.data(data)
		  			.enter().append("circle")
		    		.style("stroke", "white")
		  			.attr("class", "dot")
		    		.attr("cx", function(d) { return x(d.x); } )
		    		.attr("cy", function(d) { return y(d.y); } )
		    		.attr("r", 1);
			}
			
			var data = [
				<c:forEach items="${binancePrices}" var="binancePrice" varStatus="status">  
				    {x: new Date('${binancePrice.epochTime}'),
				    y: '${binancePrice.price}'}
				    <c:if test="${!status.last}">,    
				    </c:if>  
				    </c:forEach>  
				];
			
			xyDiagramm(data);
			</script>
		</div>
	</div>
	<div class="container">
		<div class="float-left">
			<br></br>
			<br></br>
		</div>
		<div class="float-right">
			<br></br>
			<p>Bitfinex</p>
		</div>
	</div>
	<div class="container">
		<div class="float-left">
			<br></br>
			<br></br>
		</div>
		<div class="float-right">
			<script>
			var data = [
				<c:forEach items="${bitfinexPrices}" var="bitfinexPrice" varStatus="status">  
				    {x: new Date('${bitfinexPrice.epochTime}'),
				    y: '${bitfinexPrice.price}'}
				    <c:if test="${!status.last}">,    
				    </c:if>  
				    </c:forEach>  
				];
			
			xyDiagramm(data);
			</script>
		</div>
	</div>
	<div class="container">
		<div class="float-left">
			<br></br>
			<br></br>
		</div>
		<div class="float-right">
			<br></br>
			<p>Difference</p>
		</div>
	</div>
	<div class="container">
		<div class="float-left">
			<br></br>
			<br></br>
		</div>
		<div class="float-right">
			<script>
			var data = [
				<c:forEach items="${bbDifferences}" var="bbDifference" varStatus="status">  
				    {x: new Date('${bbDifference.key}'),
				    y: '${bbDifference.value}'}
				    <c:if test="${!status.last}">,    
				    </c:if>  
				    </c:forEach>  
				];
			
			xyDiagramm(data);
			</script>
		</div>
	</div>

</body>
</html>