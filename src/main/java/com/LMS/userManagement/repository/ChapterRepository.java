package com.LMS.userManagement.repository;

import com.LMS.userManagement.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,String> {
}
