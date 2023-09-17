package org.primefaces.paradise.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.paradise.controller.FormController;
import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.FormService;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.security.UserDetailsUtil;

@Named
@ViewScoped
public class FormServiceView implements Serializable {

	private static final long serialVersionUID = -8645506285021273066L;

	private List<FormService> selectedFormsService;
	
	private FormService selectedFormService;
	
	private List<FormService> formsService;	
	
	private Boolean newForm;
	
	@Inject
	FormController formController;
	
	@Inject
	UserController userController;
	
	@PostConstruct
    public void init() {
		formsService = new ArrayList<>();
		openNew();
		if(UserDetailsUtil.getLoggedUser() != null) {
			String login = UserDetailsUtil.getLoggedUser().getUsername();
			User loggedUser = userController.findByLogin(login);		
			formsService = formController.findByUser(loggedUser);
		}
	}
	
	public void openNew() {
        this.selectedFormService = new FormService();
        this.selectedFormService.setName("");
        this.selectedFormService.setDescription("");
        this.selectedFormService.setId(UUID.randomUUID());    
        this.newForm = true;
    }
	
	public void deleteSelectedFormsService() {
		for(FormService form : this.selectedFormsService) {
			form.setActive(false);
			formController.update(form);
		}		
        this.formsService.removeAll(this.selectedFormsService);
        this.selectedFormsService = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Forms Removed"));
        updateListAndMessages();
    }
	
	public String getDeleteButtonMessage() {
        if (hasSelectedFormsService()) {
            int size = this.selectedFormsService.size();
            return size > 1 ? size + " forms selected" : "1 form selected";
        }

        return "Delete";
    }
	
	public boolean hasSelectedFormsService() {
        return this.selectedFormsService != null && !this.selectedFormsService.isEmpty();
    }
	
	public List<FormService> getSelectedFormsService() {
		return selectedFormsService;
	}

	public void setSelectedFormsService(List<FormService> selectedFormsService) {
		this.selectedFormsService = selectedFormsService;
	}

	public FormService getSelectedFormService() {
		return selectedFormService;
	}

	public void setSelectedFormService(FormService selectedFormService) {
		this.selectedFormService = selectedFormService;
	}

	public List<FormService> getFormsService() {
		return formsService;
	}

	public void setFormsService(List<FormService> formsService) {
		this.formsService = formsService;
	}

	public Boolean getNewForm() {
		return newForm;
	}

	public void setNewForm(Boolean newForm) {
		this.newForm = newForm;
	}

	public User getLoggedUser() {
		String login = UserDetailsUtil.getLoggedUser().getUsername();		
		return userController.findByLogin(login);
	}
	
	public void edit() {
		this.newForm = false;
	}
	
	public void saveForm() {		
        if (Boolean.TRUE.equals(this.newForm)) {        	
        	this.selectedFormService.setIdUser(getLoggedUser().getId());
        	this.selectedFormService.setActive(true);
        	this.selectedFormService.setCreateDate(new Date());
        	this.selectedFormService.setSendMessage(true);
        	formController.update(this.selectedFormService);
            this.formsService.add(this.selectedFormService);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form Added"));
        } else {        	
        	formController.update(this.selectedFormService);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form Updated"));        
        }
        
        
        PrimeFaces.current().executeScript("PF('manageFormDialog').hide()");
        updateListAndMessages();
        
    }

	public void updateListAndMessages() {
		PrimeFaces.current().ajax().update("form:messages", "form:dt-forms");
	}
	
    public void deleteForm() {        
        this.selectedFormService.setActive(false);
        formController.update(this.selectedFormService);
        this.formsService.remove(this.selectedFormService);
        this.selectedFormService = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form Removed"));
        updateListAndMessages();
    }	
}
