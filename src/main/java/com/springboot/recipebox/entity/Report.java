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
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reportTitle;
    private String reportDetail;
    private String adminReply = "Awaiting administrator check...";

    // FetchType.Lazy tell to hibernate to only fetch the related entities from db when we use the relationship
    @ManyToOne(fetch = FetchType.LAZY)
    // add foreign key
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // User who reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
