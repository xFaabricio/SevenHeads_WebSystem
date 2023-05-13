$(document).ready(function() {
	// Verifica se o parâmetro 'error' existe na URL
	var errorParam = getParameterByName('error');
	
	// Verifica se a URL contém 'logout'
  	var url = window.location.href;
  	if (url.indexOf('logout') !== -1) {
   		// Exibe a mensagem Growl de logout
    	PF('growlMsg').renderMessage({"summary": "User Logout", "detail": "Success logout !!","severity": "info"});
  	}
	
	// Define a mensagem de acordo com o valor do parâmetro 'error'
	var errorMessage = 'User';
	if (errorParam === 'deletedUser') {
		detail = 'Deleted User!';
	} else if (errorParam === 'blockedUser') {
		detail = 'Blocked user!';
	} else if(errorParam === 'loginError'){
		detail = 'Username or password is invalid !';
	}
	  
	// Exibe a mensagem Growl
	PF('growlMsg').renderMessage({"summary": errorMessage, "detail": detail,"severity": "error"});

	// Função para obter um parâmetro de uma URL por nome
	function getParameterByName(name, url) {
	  if (!url) url = window.location.href;
	  name = name.replace(/[\[\]]/g, '\\$&');
	  var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
		  results = regex.exec(url);
	  if (!results) return null;
	  if (!results[2]) return '';
	  return decodeURIComponent(results[2].replace(/\+/g, ' '));
	}	
});