<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" >
<head>
	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
	<meta name="author" content="" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="robots" content="all" />

	<title>DevoxxFrance</title>

	
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/vue.js"></script>
	<style type="text/css" title="currentStyle">
		@import "css/base.css";
	</style>
	<link rel="Shortcut Icon" type="image/x-icon" href="img/favicon.ico" />	
</head>
<body>
<header></header>
<div id="container">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div id="content">
		<div id="head">
			<img src="img/logo_soat.png" class="img_header">
				<img src="img/logo_devoxx.png" class="img_header">
					<img src="img/cloudbees.png" class="img_header">
					<div id="head_content">						
						<label id="pseudo">${userResponse.name}</label>
						<input type="hidden" id="userName" name="userName" value="2"/>
						SCORE : 10
						<br/>
						CLASSEMENT : <label id="classement">10</label> / 105
					</div>
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
		
			<div id="content_connect" class="simple_overlay">
				<div class="overlay_content">
				<label for="login">LOGIN</label><input type="text" id="login" value=""/>
				<br/>
				<label for="password">MOT DE PASSE</label><input type="password" id="password" value=""/>
				<br/>
				<input type="button" value="ENVOYER" id="login_send"/>
				</div>
			</div>
			<div id="content_play" class="simple_overlay">
				<div class="overlay_content">
				<ul id="ul_play">
				</ul>
</div>
			</div>
			<div id="content_rules" class="simple_overlay">
			<div class="overlay_content">
			<ul>
<li>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</li>
<li>Mauris pharetra tortor vel dolor tristique vitae sollicitudin lacus posuere.</li>
<li>Sed auctor orci vitae turpis accumsan interdum.</li>
<li>Integer semper pulvinar lacus, eget imperdiet lorem volutpat ut.</li>
</ul>
<p></p>
<p>
</p><ul>
<li>Donec venenatis lorem ac nisl luctus vitae mollis quam porttitor.</li>
<li>Aenean accumsan leo volutpat elit varius lobortis.</li>
<li>Cras eget nisi eget nunc commodo posuere.</li>
<li>Duis ut mauris eu diam feugiat laoreet.</li>
<li>Pellentesque bibendum ornare dui, vitae molestie leo egestas vitae.</li>
<li>Praesent interdum lectus sed turpis tempor sagittis.</li>
</ul>
<p></p>
<p>
</p><ul>
<li>In at eros nisi, vitae porttitor ligula.</li>
<li>Duis varius tristique justo, ac ornare mauris interdum nec.</li>
<li>Quisque sed nisl sed metus interdum volutpat.</li>
<li>Praesent facilisis erat quis nulla laoreet aliquam aliquet ipsum lacinia.</li>
</ul>
<p></p>
<p>
</p><ul>
<li>Vivamus hendrerit nulla sit amet metus viverra varius.</li>
<li>Nam in arcu imperdiet leo luctus cursus quis non mi.</li>
<li>Sed auctor purus id ante accumsan ultrices eu sed tellus.</li>
<li>Nullam vitae lectus justo, id suscipit metus.</li>
<li>Quisque imperdiet elit et eros hendrerit pretium.</li>
</ul>
<p></p>
<p>
</p><ul>
<li>Etiam suscipit ante nec dui porta gravida.</li>
<li>Etiam fringilla eros id turpis mollis nec lobortis orci mattis.</li>
</ul>
<p></p>
</div>
			</div>
			<div id="content_cat" class="simple_overlay">
			<div class="overlay_content">
			<img src="img/chatvador.jpg"/>
			</div>
			</div>
			<div id="content_about" class="simple_overlay">
			<div class="overlay_content">
			<ul>
				<li>Game by Soat</li>
				<li>For DevoxxFr2012</li>				
			</ul>
			</div>
			</div>
			<div id="content_answer" class="simple_overlay">
				<div class="overlay_content">				
				</div>
			</div>
			</div>		
	<div>
</div>
<footer></footer>

</body>
</html>