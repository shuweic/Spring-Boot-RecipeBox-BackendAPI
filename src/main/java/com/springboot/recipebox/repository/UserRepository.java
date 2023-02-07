package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // create User Repository, whose PK type is String
    // Same as my post API, JapRepository already have @Transactional or @Repository... or other method and annotation, no need to add here
    Optional<User> findUserByUsername(String username);
    User findUserById(int id);

}
