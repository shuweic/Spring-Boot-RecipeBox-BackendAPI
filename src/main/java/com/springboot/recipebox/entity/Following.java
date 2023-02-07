package com.springboot.recipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "followings")
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // user object of the user we are following
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id", nullable = false)
    private User followedUser;

    // logged-in user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_user_id", nullable = false)
    private User currentUser;
}
