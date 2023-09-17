package org.primefaces.paradise.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sh_form_service")
public class FormService implements Serializable {

	private static final long serialVersionUID = -6280465386650043243L;

	@Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Column
	private Boolean sendMessage;
	
	@Column
	private Boolean active;
	
	@Column(name = "sh_user_id", columnDefinition = "NUMERIC", length = 20)	
	private Long idUser;
	
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;
	
	@Column
	private Date deletedDate;
	
	@Transient
	private List<FormServiceHistory> formServiceHistory;

	public FormService() {
		super();
	}

	public FormService(UUID id, String name, String description, Boolean sendMessage, Boolean active, Long idUser,
			Date createDate, Date updateDate, Date deletedDate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.sendMessage = sendMessage;
		this.active = active;
		this.idUser = idUser;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.deletedDate = deletedDate;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FormServiceHistory> getFormServiceHistory() {
		return formServiceHistory;
	}

	public void setFormServiceHistory(List<FormServiceHistory> formServiceHistory) {
		this.formServiceHistory = formServiceHistory;
	}

	public Boolean getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(Boolean sendMessage) {
		this.sendMessage = sendMessage;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}
	
}
