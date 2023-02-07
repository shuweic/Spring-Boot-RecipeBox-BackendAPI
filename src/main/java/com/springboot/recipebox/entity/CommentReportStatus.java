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
@Table(name = "comment_report_status")
public class CommentReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // default false, update to true when admin flag a comment
    private boolean adminReviewStatus;

    // FetchType.Lazy tell to hibernate to only fetch the related entities from db when we use the relationship
//    @OneToOne(fetch = FetchType.LAZY)
//    // add foreign key
//    @JoinColumn(name = "commentId", nullable = false)
//    private Comment comment;
}
