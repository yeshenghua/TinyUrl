package com.wheelseye.tinyurl.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.wheelseye.tinyurl.entities.TinyLongMapping;

//Task 3: SQL/NoSQL database needs to be used
@Component
public interface TinyLongMappingRepository extends JpaRepository<TinyLongMapping, Long>{
	
	TinyLongMapping findBytinyURL(String tinyURL);
	
}

