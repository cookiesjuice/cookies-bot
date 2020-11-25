package com.github.cookiesjuice.cookiesbot.module.setu.dao;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Everyday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EverydayRepository extends JpaRepository<Everyday, Long> {
}
