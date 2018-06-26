package com.verizon.devops.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="Service_Role")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class ServiceRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6418586486772681927L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
    private Long id;
		
	@Column
	private String name;
	
	@ManyToOne(cascade = {
			CascadeType.PERSIST, CascadeType.MERGE
    })
    @JoinColumn(name = "service_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Service theService;
	
	@ManyToOne(cascade = {
			CascadeType.PERSIST, CascadeType.MERGE
    })
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role theRole;
		
	@ManyToOne(cascade = {
			CascadeType.PERSIST, CascadeType.MERGE
        })
    @JoinColumn(name = "stage_id", nullable = false)
	private Stage theEnv;
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date createdAt;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
	private Date updatedAt;
		
	public ServiceRole() {}
	
	public ServiceRole(String name) {
		super();
		this.name = name;
	}

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

	public Service getTheService() {
		return theService;
	}

	public void setTheService(Service theService) {
		this.theService = theService;
	}

	public Role getTheRole() {
		return theRole;
	}

	public void setTheRole(Role theRole) {
		this.theRole = theRole;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Stage getTheEnv() {
		return theEnv;
	}

	public void setTheEnv(Stage theEnv) {
		this.theEnv = theEnv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((theEnv == null) ? 0 : theEnv.hashCode());
		result = prime * result + ((theRole == null) ? 0 : theRole.hashCode());
		result = prime * result + ((theService == null) ? 0 : theService.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceRole other = (ServiceRole) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (theEnv == null) {
			if (other.theEnv != null)
				return false;
		} else if (!theEnv.equals(other.theEnv))
			return false;
		if (theRole == null) {
			if (other.theRole != null)
				return false;
		} else if (!theRole.equals(other.theRole))
			return false;
		if (theService == null) {
			if (other.theService != null)
				return false;
		} else if (!theService.equals(other.theService))
			return false;
		return true;
	}

}
