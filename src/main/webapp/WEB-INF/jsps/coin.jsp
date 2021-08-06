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
  	stroke: white;      // starts invisible, made visible with transition
  	stroke-width: 1px;  // half is inside the margin, half is outside
	}
	rect {
  	fill: none;
  	stroke: black;
  	stroke-width: 1px;  // half is inside the margin, half is outside
	}
	.axis path, .axis line {
  	fill: none;
  	stroke: #000;
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
		</div>
		<div class="float-right">
			<p>${candles}</p>
			<script>
			
			var outerWidth = 960, outerHeight = 500;
			
			var margin = {top: 100, right: 20, bottom: 80, left: 80};
			
			var width = outerWidth - margin.left - margin.right, 
		    	height = outerHeight - margin.top - margin.bottom;  
			
			document.body.style.margin="0px"; 
			
			var data = [ {x: 0, y: 00}, {x: 1, y: 30}, {x: 2, y: 40},
	             {x: 3, y: 20}, {x: 4, y: 90}, {x: 5, y: 70} ];

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
		    .ticks(5)                            // request 5 ticks on the x axis
		    .orient("bottom");

		var yAxis = d3.svg.axis()                // y Axis
		    .scale(y)
		    .ticks(4)
		    .orient("left");

		var svg = d3.select("body").append("svg")
		    .attr("width",  outerWidth)
		    .attr("height", outerHeight);        // Note: ok to leave this without units, implied "px"

		var g = svg.append("g")                  // <g> element is the inner plot area (i.e., inside the margins)
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		g.append("g")                            // render the Y axis in the inner plot area
		    .attr("class", "y axis")
		    .call(yAxis);

		g.append("g")                            // render the X axis in the inner plot area
		    .attr("class", "x axis")
		    .attr("transform", "translate(0," + height + ")")  // axis runs along lower part of graph
		    .call(xAxis);

		g.append("text")                         // outer x-axis label
		    .attr("class", "x label") 
		    .attr("text-anchor", "end") 
		    .attr("x", width/2) 
		    .attr("y", height + 2*margin.bottom/3 + 6) 
		    .text("outer x-axis label");

		g.append("text")                         // plot title
		    .attr("class", "x label") 
		    .attr("text-anchor", "middle") 
		    .attr("x", width/2)
		    .attr("y", -margin.top/2)
		    .attr("dy", "+.75em") 
		    .text("plot title");

		g.append("text")                         // outer y-axis label
		    .attr("class", "x label") 
		    .attr("text-anchor", "middle") 
		    .attr("x", -height/2)
		    .attr("y", -6 - margin.left/3)
		    .attr("dy", "-.75em") 
		    .attr("transform", "rotate(-90)") 
		    .text("outer y-axis label");

		g.append("path")                         // plot the data as a line
		    .datum(data)
		    .attr("class", "line")
		    .attr("d", line);

		g.append("rect")                         // plot a rectangle that encloses the inner plot area
		    .attr("width", width)
		    .attr("width", width)
		    .attr("height", height);

		g.selectAll(".dot")                      // plot a circle at each data location
		    .data(data)
		  .enter().append("circle")
		    .attr("class", "dot")
		    .attr("cx", function(d) { return x(d.x); } )
		    .attr("cy", function(d) { return y(d.y); } )
		    .attr("r", 5);

		d3.selectAll("path").transition()       // data transition
		    .style("stroke", "steelblue")
		    .delay(1000)
		    .duration(2000);

	</script>
		</div>
	</div>

</body>
</html>