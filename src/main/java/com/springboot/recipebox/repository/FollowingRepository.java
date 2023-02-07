package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Following;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, Integer> {

    //
    List<Following> findFollowingByCurrentUserId(int currentUserID);

    @Query("SELECT f FROM Following f WHERE f.followedUser.id = :#{#currentUserID}")
    List<Following> getFollowersOfCurrentUserId(int currentUserID);

    @Query("SELECT f FROM Following f WHERE f.currentUser.id = :#{#currentUserID} AND f.followedUser.id = :#{#followingUserID}")
    List<Following> getFollowingFromCurrentUserIDAndFollowingUserID(int currentUserID, int followingUserID);

}
