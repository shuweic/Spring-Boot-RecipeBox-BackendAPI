package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.entity.Tags;
import com.springboot.recipebox.entity.Tags.TagKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.HTML;
import java.util.List;

public interface TagRepository extends JpaRepository<Tags, TagKey>
{
    @Query("select t.tagKey.name from Tags t where t.tagKey.user.username = ?1")
    List<String> getTagNamesOfUser(String username);

    @Query("select t from Tags t where t.tagKey.user.username = ?1 and t.tagKey.name = ?2")
    Tags getTagsByTagKeyUserUsernameAndTagKeyName(String username, String tagName);

}
