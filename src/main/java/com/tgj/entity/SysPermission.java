package com.tgj.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(value = {"sysRole", "handler", "hibernateLazyInitializer"})
@Entity
@Table(name = "d_sys_permission", catalog = "rykj", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class SysPermission implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String url;
//	private Long pid;
	private SysRole sysRole;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
//	public Long getPid() {
//		return pid;
//	}
//	public void setPid(Long pid) {
//		this.pid = pid;
//	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	
}
