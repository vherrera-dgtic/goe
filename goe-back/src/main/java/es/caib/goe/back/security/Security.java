package es.caib.goe.back.security;

import es.caib.goe.commons.utils.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * Proveeix una forma centralitzada de comprovar els permisos de l'usuari dins l'aplicació web.
 * 
 * @author areus
 */
@Named
@ApplicationScoped
public class Security {

    @Inject
    private HttpServletRequest request;

    public boolean isAdmin() {
        return request.isUserInRole(Constants.GOE_ADMIN);
    }
    
    public boolean isUser() {
        return request.isUserInRole(Constants.GOE_USER);
    }
    
    public boolean isUserOrAdmin() {
        return isUser() || isAdmin();
    }

}
