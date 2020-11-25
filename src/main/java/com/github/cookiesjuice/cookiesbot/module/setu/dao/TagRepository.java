package com.github.cookiesjuice.cookiesbot.module.setu.dao;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
}
