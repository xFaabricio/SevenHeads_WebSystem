package org.primefaces.paradise.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sh_user")
public class User implements Serializable {
	
	private static final long serialVersionUID = -4587481838374402425L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String login;
	
	private String password;
	
	private String email;
	
	private Date lastLogin;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Date inactiveDate;
	
	private Date blockedDate;
	
	private Boolean active;
	
	private Boolean blocked;
	
	private Integer tryQuantity;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "sh_user_roles",
			joinColumns= {@JoinColumn(name="user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")})
	private List<Role> roles;
		
	@Lob
	private byte[] userPhoto;
	
	/**
	 * Constructor
	 **/	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String name, String login, String password, String email, Date lastLogin, Date createDate,
			Date updateDate, Date inactiveDate, Date blockedDate, Boolean active, Boolean blocked, Integer tryQuantity,
			List<Role> roles, byte[] userPhoto) {
		super();
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;
		this.email = email;
		this.lastLogin = lastLogin;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.inactiveDate = inactiveDate;
		this.blockedDate = blockedDate;
		this.active = active;
		this.blocked = blocked;
		this.tryQuantity = tryQuantity;
		this.roles = roles;
		this.userPhoto = userPhoto;
	}

	/**
	 * Getters and Setter
	 **/
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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

	public Date getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public Date getBlockedDate() {
		return blockedDate;
	}

	public void setBlockedDate(Date blockedDate) {
		this.blockedDate = blockedDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public Integer getTryQuantity() {
		return tryQuantity != null ? tryQuantity : 0 ;
	}

	public void setTryQuantity(Integer tryQuantity) {
		this.tryQuantity = tryQuantity;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public byte[] getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(byte[] userPhoto) {
		this.userPhoto = userPhoto;
	}	
	
}
