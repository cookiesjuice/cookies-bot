package com.github.cookiesjuice.cookiesbot.module.setu.dao;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetuRepository extends JpaRepository<Setu, Long> {
}
