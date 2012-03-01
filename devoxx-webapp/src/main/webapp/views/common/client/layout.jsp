<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="author" content="" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<meta name="robots" content="all" />
        <title><tiles:insertAttribute name="pageTitle" ignore="true" /></title>
        <link type="text/css" rel="stylesheet" href="<c:url value='/css/base.css' />" />   
        <link rel="Shortcut Icon" type="image/x-icon" href="<c:url value='/img/favicon.ico' />" />
        <script type="text/javascript" src="<c:url value='/js/jquery-1.7.1.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/js/jquery.tools.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/js/vue.js' />"></script>
		<tiles:insertAttribute name="specificHead" ignore="true" />
	</head>
	<body>
	    <header></header>
		<div id="container">
			<div id="content">
			    <div id="head">
				    <tiles:insertAttribute name="header" />
				</div>

				<div id="menu">
					<ul id="menu_content">
						<li id="logon" rel="#content_connect">LOG</li>
						<li id="play" rel="#content_play">PLAY</li>
						<li id="howto" rel="#content_rules">RULES</li>
						<li id="cat" rel="#content_cat">CATZ</li>
						<li id="about" rel="#content_about">ABOUT</li>
					</ul>
				</div>
				<div id="body">
					<tiles:insertAttribute name="content" />
				</div>
	
				<tiles:insertAttribute name="footer" />
			</div>
		</div>
	</body>
</html>