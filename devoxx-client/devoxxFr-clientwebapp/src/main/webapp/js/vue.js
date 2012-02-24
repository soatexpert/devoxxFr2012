function sendResponse(id){
		$.ajax({
			type: "POST",
			url: "GiveResponse",
			data: $("#replyQuestionForm-"+id).serialize(),
			success: function (result) {
				//alert(result);
				updateScore(result);
				$("#content_answer .close").click();
				$("#content_answer").hide();
				//Rappeler allquestions pour obtenir la maj des statuts
			}
		});
	};
	
var score = 0;
//var userName = document.getElementsByName("userName").value;
	
function updateScore(response){
		var userName = document.getElementsByName("userName").value;
		$("#head_content").empty();		
		$("#head_content").append('<label id="pseudo">'+userName+'</label>\n');		
		if (response.responseType=='SUCCESS'){
			score = score + 5;		
		}else{
			score = score -5;		
		}
		$("#head_content").append('SCORE : '+score+'\n<br/>\n');
		$("#head_content").append('CLASSEMENT : <label id="classement">10</label>  / 105');		
};

$(function() {
	//overlay (jquerytools)
	// positions for each overlay
	var positions = [ 
		[0,530],
		[400,20],
		[400,530],
		[0,20],
		[0,20]	
	];	
	var questions;
	
	$("li[rel]:not(#play)").each(function(i) {
			var monLi	 = $(this);
			$(this).overlay({
				// common configuration for each overlay
				oneInstance: true, 
				closeOnClick: true, 
				 
				// setup custom finish position	
				left: $("#logon").offsetLeft + 105,
				top:$("#logon").offsetTop +105,
				onBeforeLoad:function(){monLi.addClass('liHighlight');},
				onClose:function(){monLi.removeClass('liHighlight');}			
			});		
	});
	
	
			
	$("#play").overlay({

			// common configuration for each overlay
			oneInstance: true, 
			closeOnClick: true, 
			 
			// setup custom finish position
			 
			/*top:this.offsetTop + 95,*/
			left: $("#logon").offsetLeft + 105,
			top:$("#logon").offsetTop +105,
			onBeforeLoad:function(){
				$("#loading").hide();				
				$.ajax({
					type: "GET",
					url: "AllQuestions",	
					success: function (data) {
						
		                   if (data.ArrayList!== null) {
		                	   questions = data;
		                	   $("#ul_play").empty();
		                           $(data.ArrayList).each(function (i, question) {
		                        	   //alert(question.label);		                        	   
		                        	   $("#ul_play").append("<li class='li_question' id='"+question.id+"' >Question n°"+question.id+"</li>");		                        	   
		                           });
		                   }	
		                   reloadLi();
					}
				});
			}
			
			// use apple effect
			/*effect: 'apple'*/
			
	});
	
	
	
	function reloadLi(){
	$("li.li_question").each(function(i) {
		
		$(this).overlay({
		target:"#content_answer",
		oneInstance: false, 
			closeOnClick: true, 
		left: $("#logon").width()+$("#logon").offset().left +$("#content_play").width() + 65 ,
		top: $("#logon").offset().top  ,
		onBeforeLoad: function() {
			if (this.getOverlay("#content_answer")){
				$("#content_answer .close").click();
				$("#content_answer").hide();
			}
			if(questions != null){				
				var question = questions.ArrayList[i];
				var htmlstring = '<h4>'+question.label+'</h4><hr>';
				htmlstring+='<form action="" name="replyQuestionForm" id="replyQuestionForm-' + question.id + '">';
				if (question.questionType=='FREE'){
					$(question.questions).each(function(j,answer){
						htmlstring+='<input type="text" name="answers" value="'+answer+'">'+answer+'<br>';
					});
				}
				else if (question.questionType=='MULTIPLE_CHOICE'){
					$(question.questions).each(function(j,answer){
						htmlstring+='<input type="checkbox" name="answers" value="'+answer+'">'+answer+'<br>';
					});
				}
				else{
					$(question.questions).each(function(j,answer){
						htmlstring+='<input type="radio" name="answers" value="'+answer+'">'+answer+'<br>';
					});
				}				
				htmlstring+='<hr><input type="button" value="VALIDER" id="answer_send" onClick=sendResponse("'+question.id+'"); />';
				htmlstring+='<input type="hidden" value="'+question.id+'" id="questionId" name="questionId">';
				htmlstring+='</form>'
				$("#content_answer .overlay_content").html(htmlstring);
			}
		}
		});
	});
	}
	
	
});