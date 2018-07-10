package com.tgj.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"sysUser", "handler", "hibernateLazyInitializer"})
@Entity
@Table(name = "d_sys_role", catalog = "rykj", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class SysRole implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private List<SysPermission> permissions = new ArrayList<>(0);
	private SysUser sysUser;
	
	@ManyToOne
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "sysRole")
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
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
	
	@PrePersist
	public void prePersist() {
		this.name = "ROLE_" + name;
	}
	
	@PreUpdate
	public void preUpdate() {
		if (!name.startsWith("ROLE_")) {
			this.name = "ROLE_" + name;
		}
	}
	
}
