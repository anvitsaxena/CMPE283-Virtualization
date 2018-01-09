$(document).ready(function() {
	$.ajax({
		type : "GET",
		cache : false,
		contentType : "application/json",
		url :"configuration.htm",
		datatype : "json",

		beforeSend : function(xhr) {
			xhr.setRequestHeader("Accept", "html/text");
			xhr.setRequestHeader("Content-Type", "application/json");				
		},
		success : function(response) {
		 alert(response);
			
		},
		error : function(xhr, status, errorThrown) {
			var err = xhr.responseText;
			console.log(err);
		}
	});
});