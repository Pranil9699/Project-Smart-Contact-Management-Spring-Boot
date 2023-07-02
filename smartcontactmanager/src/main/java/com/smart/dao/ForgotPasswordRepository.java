package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.ForgotPassword;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String>{

	@Query("from ForgotPassword as p where p.email=:email and p.otp=:otp")
	ForgotPassword getByEmailAndOtp(@Param("email") String email,@Param("otp") Integer otp);

}
