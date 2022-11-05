package com.tts.OAuth;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.management.DefaultLoaderRepository;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Profile("database-loader")
public class DatabaseLoader {
	
	@SuppressWarnings("deprecation")
	@Autowired
	private DefaultLoaderRepository repository;
	
	@PostConstruct
	public void init() {
		if (repository.findByUserName("admin") == null) {
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			user.setRoles(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			repository.save(user)user;
		}
	}
}
