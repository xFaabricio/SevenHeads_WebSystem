package org.primefaces.paradise.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sh_form_service_history")
public class FormServiceHistory implements Serializable {
	
	private static final long serialVersionUID = 7963147802655889486L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_history_generator")
	@SequenceGenerator(name = "form_history_generator", sequenceName = "sh_form_service_history_seq", allocationSize = 1)
	private Long id;

	@Column(name = "form_service_id")
	private UUID uuidFormService;
	
	@Column
	private String message;
	
	@Column
	private Date createDate;

	@Column
	private Boolean sendMessage;
	
	public FormServiceHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormServiceHistory(Long id, UUID uuidFormService, String message, Date createDate, Boolean sendMessage) {
		super();
		this.id = id;
		this.uuidFormService = uuidFormService;
		this.message = message;
		this.createDate = createDate;
		this.sendMessage = sendMessage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getUuidFormService() {
		return uuidFormService;
	}

	public void setUuidFormService(UUID uuidFormService) {
		this.uuidFormService = uuidFormService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(Boolean sendMessage) {
		this.sendMessage = sendMessage;
	}
	
}
