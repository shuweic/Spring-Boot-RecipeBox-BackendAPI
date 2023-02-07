package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.UserSavedRecipeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSavedRecipeMappingRepository extends JpaRepository<UserSavedRecipeMapping, UserSavedRecipeMapping.UserSavedRecipeMappingKey>
{
    @Query("select u from UserSavedRecipeMapping u where u.userSavedRecipeMappingKey.user.username = ?1")
    List<UserSavedRecipeMapping> findSavedRecipeMappingsByUsername(String username);
}
