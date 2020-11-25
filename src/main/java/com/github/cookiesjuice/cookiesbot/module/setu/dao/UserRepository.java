package com.github.cookiesjuice.cookiesbot.module.setu.dao;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
