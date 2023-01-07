package br.com.sevenheads.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import br.com.sevenheads.manager.UserManager;

public final class AuthenticationSuccessHandlerManagerImpl implements AuthenticationSuccessHandler {

	private static final Log LOGGER = LogFactory.getLog(AuthenticationSuccessHandlerManagerImpl.class);
	
	private UserManager userManager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

//		if(user.getUser() != null && user.getUser().getId() != null) {	
//			
//			// Atualiza a data de acesso do sistema caso encontre um usuario ativo no banco
//			try {
//				User userUpdate = userManager.setLoginTime(user.getUser());
//			} catch (Exception e) {
//				LOGGER.error(e.getStackTrace());
//				LOGGER.error("Problema ao salvar a Data do Ultimo Login.");
//			}
//		
//			if( (user.getUser().getBlocked()!=null) && user.getUser().getBlocked().equals(true) ) {
//				
//				Policy policy = user.getUser().getPolicy();				
//				if (policy != null) {					
//					if(policy.getAccessDowntime() != null) {
//						HttpSession session = request.getSession();		     
//					    session.setMaxInactiveInterval(policy.getAccessDowntime()*60);
//					}					
//					if( (policy.getUnblockWithPwdChange() != null) && (policy.getUnblockWithPwdChange().equals(true)) ) {
//						LOGGER.info("Usuario Bloquado. Ele sera redirecionado para a tela de alteração de senha. Usuario["+user.getUser().getLogin()+"]" );
//						response.sendRedirect(request.getContextPath() + "/password.xhtml");
//						return;						
//					}else {					
//						response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");
//						return;
//					}					
//				}else {					
//					response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");
//					return;
//				}
//				
//			}
//			
//			Policy policy = user.getUser().getPolicy();			
//			if (policy != null) {				
//				if(policy.getAccessDowntime() != null) {
//					HttpSession session = request.getSession();		     
//				    session.setMaxInactiveInterval(policy.getAccessDowntime()*60);
//				}				
//				if( (policy.getChangeFirstLoginPwd() != null) && policy.getChangeFirstLoginPwd().equals(true) ) {				
//					if (user.getUser().getFirstAccess()!=null &&
//							user.getUser().getFirstAccess().equals(true) ) {											
//						LOGGER.info("Troca de Senha Primeiro Login. Sera redirecionado para a tela de alteração de senha. Usuario["+user.getUser().getLogin()+"]");
//						response.sendRedirect(request.getContextPath() + "/password.xhtml");
//						return;
//					}
//				}
//				
//				// Se o usuario possui a politica troca de senha apos a recuperacao seta a variavel de controle do usuario
//				// com falso porque ele conseguiu alterar a senha com sucesso
//				if( (policy.getChangePwdAfterRecover() != null) && policy.getChangePwdAfterRecover().equals(true)){
//					if ((user.getUser().getChangePwdAfterRecover()!=null) && (user.getUser().getChangePwdAfterRecover().equals(true))) {
//						LOGGER.info("Troca de senha apos a recuperacao da senha. Sera redirecionado para a tela de alteração de senha. Usuario["+user.getUser().getLogin()+"]");
//						response.sendRedirect(request.getContextPath() + "/password.xhtml");
//						return;
//					}
//				}
//				
//				// Verifica periodicidade da senha
//				if( (user.getUser().getPasswordUpdatetime() != null) && (policy.getPeriodPwd() != null) ) {
//					Date now = new Date();
//					Long dayInMs = 24*60*60*1000L;
//					if ( (now.getTime() - user.getUser().getPasswordUpdatetime().getTime()) > (policy.getPeriodPwd().longValue() * dayInMs.longValue()) ){
//						LOGGER.info("Troca Periodica de Senha. Sera redirecionado para a tela de alteração de senha. Usuario["+user.getUser().getLogin()+"]");
//						response.sendRedirect(request.getContextPath() + "/password.xhtml");
//						return;
//					}
//				}
//			}			
//		}
		
        response.sendRedirect(request.getContextPath() + "/init.xhtml");
	}
}
