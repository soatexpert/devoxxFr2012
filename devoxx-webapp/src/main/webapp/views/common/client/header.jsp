<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<img src="<c:url value='/img/logo_soat.png' />" class="img_header">
<img src="<c:url value='/img/logo_devoxx.png' />" class="img_header">
<img src="<c:url value='/img/cloudbees.png' />" class="img_header">
<div id="head_content">
	<label id="pseudo">${userResponse.name}</label>
	<!--<input type="hidden" id="userName" name="userName" value="2"/>-->
	SCORE : 10 <br /> CLASSEMENT : <label id="classement">10</label> / 105
</div>