package com.tgj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * <p>
 *		Demand实体.
 * </p>
 * @className Demand
 * @author Server  
 * @date 2018年5月16日 下午4:33:46 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@JsonIgnoreProperties(value = "nameAndContent")
@ApiModel(value="demand对象", description="用户需求对像")
@Entity
@Table(schema = "rykj", name = "d_demand", indexes = {@Index(unique = true, columnList = "d_phone_num,d_email,d_name_content")})
public class Demand implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键自增
	 */
	@ApiModelProperty(hidden = true)
	private Long id;
	
	/**
	 * 需要 人名 
	 */
	@ApiModelProperty(required = true)
	private String name;
	
	/**
	 * 需求人手机号
	 */
	@ApiModelProperty(required = true)
	private String phoneNum;
	
	/**
	 * 需求类型
	 */
	@ApiModelProperty(required = true)
	private String content;
	
	/**
	 * 邮箱
	 */
	@ApiModelProperty(required = false)
	private String email = "-1";
	
	/**
	 * 用户与内容（方便检索）
	 */
	@ApiModelProperty(hidden = true)
	private String nameAndContent;
	
	/**
	 * 创建日间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@ApiModelProperty(hidden = true)
	private Date createDate;
	
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@ApiModelProperty(hidden = true)
	private Date modifyDate;
	
	/**
	 * 状态（是否处理）
	 */
	@ApiModelProperty(hidden = true)
	private Short status = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "d_id", unique = true, nullable = false, updatable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "d_name", length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "d_phone_num", length = 11, nullable = false)
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Column(name = "d_content", length = 600, nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "d_email", nullable = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "d_name_content", nullable = false, length = 620)
	public String getNameAndContent() {
		return nameAndContent;
	}

	public void setNameAndContent(String nameAndContent) {
		this.nameAndContent = nameAndContent;
	}

	@Column(name = "d_create_date", nullable = false, updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "d_modify_date", nullable = false)
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "d_status", columnDefinition = "TINYINT(1) NULL DEFAULT 0")
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	
	@PrePersist
	public void prePersist() {
		Date date = null;
		if (Objects.isNull(createDate)) {
			date = new Date();
			createDate = date;
		}
		if (Objects.isNull(modifyDate)) modifyDate = date == null ? new Date() : date;
		if (Objects.isNull(status)) status = 0;
		
		nameAndContent = name + content;
	}
	
	@PreUpdate
	public void preUpdate() {
		if (Objects.isNull(modifyDate)) modifyDate = new Date();
		if (Objects.isNull(status)) status = 0;
		
		nameAndContent = name + content;
	}
	
}
