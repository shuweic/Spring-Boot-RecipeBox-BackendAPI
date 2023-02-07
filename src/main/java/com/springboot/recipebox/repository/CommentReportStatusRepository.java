package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.CommentReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportStatusRepository extends JpaRepository<CommentReportStatus, Integer> {
}
