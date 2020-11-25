package com.github.cookiesjuice.cookiesbot.module.setu.dao;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
