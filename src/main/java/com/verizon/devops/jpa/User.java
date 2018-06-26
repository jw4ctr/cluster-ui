package com.verizon.devops.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="User")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class User  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5194236660936508865L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;	
	
    @Column
    private String name;
    @Column
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
            		CascadeType.PERSIST, CascadeType.MERGE
            })
    @JoinTable(name = "User_Service_Role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_role_id") })
    private Set<ServiceRole> roles = new HashSet<ServiceRole>();
    
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
            		CascadeType.PERSIST, CascadeType.MERGE
                })
    @JoinTable(name = "User_Project",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") })
    private Set<Project> projects = new HashSet<Project>();

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date createdAt;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
	private Date updatedAt;
	
	public User() {}
	
	public User(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public Set<ServiceRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ServiceRole> roles) {
		Set<Project> newProjects = new HashSet<Project>();
	
		//consolidate project refs
		Map<String, Service> uniqServiceRefs = new HashMap<String,Service>();
		for (ServiceRole sr : roles) {
			if (!uniqServiceRefs.containsKey(sr.getTheService().getName())) {
				uniqServiceRefs.put(sr.getTheService().getName(), sr.getTheService());
			} else {
				sr.setTheService(uniqServiceRefs.get(sr.getTheService().getName()));
			}
		}
		
		Map<String, Project> uniqProjectRefs = new HashMap<String,Project>();
		for (ServiceRole sr : roles) {
			if (!uniqProjectRefs.containsKey(sr.getTheService().getTheProject().getName())) {
				uniqProjectRefs.put(sr.getTheService().getTheProject().getName(), sr.getTheService().getTheProject());
			} else {
				sr.getTheService().setTheProject(uniqProjectRefs.get(sr.getTheService().getTheProject().getName()));
			}
		}
		
		Map<String, Role> uniqRoleRefs = new HashMap<String,Role>();
		for (ServiceRole sr : roles) {
			if (!uniqRoleRefs.containsKey(sr.getTheRole().getName())) {
				uniqRoleRefs.put(sr.getTheRole().getName(), sr.getTheRole());
			} else {
				sr.setTheRole(uniqRoleRefs.get(sr.getTheRole().getName()));
			}
		}
		
		Map<String, Stage> uniqStageRefs = new HashMap<String,Stage>();
		for (ServiceRole sr : roles) {
			if (!uniqStageRefs.containsKey(sr.getTheEnv().getName())) {
				uniqStageRefs.put(sr.getTheEnv().getName(), sr.getTheEnv());
			} else {
				sr.setTheEnv(uniqStageRefs.get(sr.getTheEnv().getName()));
			}
		}
		
		
		for (ServiceRole sr : roles) {
			boolean projectExists = false;		
			for (Project proj : projects) {
	    	   if (proj.equals(sr.getTheService().getTheProject())) {
	    		   projectExists = true;
	    		   break;
	    	   }
	       }
			if (!projectExists) newProjects.add(sr.getTheService().getTheProject());
		}
		this.projects.addAll(newProjects);
		this.roles = roles;
	}	
	
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		Set<Project> noDupsProjects = new HashSet<Project>();
		for (Project proj : projects) {
			boolean dups = false;
	       for (ServiceRole sr : this.roles) {
	    	   if (proj.equals(sr.getTheService().getTheProject())) {
	    		   noDupsProjects.add(sr.getTheService().getTheProject());
	    		   dups = true;
	    		   break;
	    	   }
	       }
	       if (!dups) noDupsProjects.add(proj);
		}
		this.projects = noDupsProjects;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
