package com.tgj.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgj.base.BaseJpaService;
import com.tgj.dao.SysUserDao;
import com.tgj.entity.SysPermission;
import com.tgj.entity.SysRole;
import com.tgj.entity.SysUser;

/**
 * 
 * <p>
 *		SysUserService.
 * </p>
 * <p>
 *		实现SysUserService相关业务处理
 * </p>
 * @className SysUserService
 * @author Server  
 * @date 2018年5月16日 下午4:36:36 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Service
public class SysUserService extends BaseJpaService<SysUser> implements UserDetailsService {

	@Autowired
	private SysUserDao sysUserDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = sysUserDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户名:" + username + "不存在");
		}
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(user));
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(SysUser user) {
		List<GrantedAuthority> auths = new ArrayList<>();
		for (SysRole role : user.getRoles()) {
			for (SysPermission permission : role.getPermissions()) {
				auths.add(new SimpleGrantedAuthority(permission.getName()));
			}
		}
		return auths;
	}
}
