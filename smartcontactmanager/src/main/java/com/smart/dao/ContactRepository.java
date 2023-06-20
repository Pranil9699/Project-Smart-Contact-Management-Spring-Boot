package com.smart.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

	

	@Query("from Contact as c where c.user.id=:userId")
	Page<Contact> findContactsByUser(@Param("userId")int userId,Pageable pageable);
// IN  the pageable variable having the two infomation
	// currentPage-page
	// contact per page
	
	
	
	
	
	
	// searching the contact method
	
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
}
