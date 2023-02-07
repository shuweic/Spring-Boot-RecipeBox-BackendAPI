package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>{
}

