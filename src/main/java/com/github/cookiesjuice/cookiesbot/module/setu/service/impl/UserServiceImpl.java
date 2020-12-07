package com.github.cookiesjuice.cookiesbot.module.setu.service.impl;

import com.github.cookiesjuice.cookiesbot.config.setu.UserProperties;
import com.github.cookiesjuice.cookiesbot.module.setu.dao.EverydayRepository;
import com.github.cookiesjuice.cookiesbot.module.setu.dao.UserRepository;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Everyday;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;
import com.github.cookiesjuice.cookiesbot.module.setu.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserProperties userProperties;
    private final UserRepository userRepository;
    private final EverydayRepository everydayRepository;

    public UserServiceImpl(UserProperties userProperties, UserRepository userRepository, EverydayRepository everydayRepository) {
        this.userProperties = userProperties;
        this.userRepository = userRepository;
        this.everydayRepository = everydayRepository;
    }

    @Override
    public User find(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public synchronized User findOrSave(long id) {
        User user = find(id);
        if (user == null) {
            user = new User();
            user.setId(id);
            return userRepository.saveAndFlush(user);
        }
        return user;
    }

    @Override
    public synchronized void changeExp(User user, long exp) {
        if (exp == 0) return;

        long addexp = user.getExp() + exp;
        int level = exp > 0 ? user.getLevel() : 0;
        int[] levelExp = userProperties.getLevelExp();

        for (int lv = level; lv < levelExp.length - 1; lv++) {
            if (addexp >= levelExp[lv]) {
                user.setLevel(lv);
            } else break;
        }

        user.setExp(addexp);
        userRepository.saveAndFlush(user);
    }

    @Override
    public synchronized Everyday getToday(User user) {
        List<Everyday> everydays = user.getEverydays();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        for (Everyday day : everydays) {
            cal.setTime(day.getDate());
            if (y == cal.get(Calendar.YEAR) && m == cal.get(Calendar.MONTH) && d == cal.get(Calendar.DATE))
                return day;
        }

        Everyday everyday = everydayRepository.save(new Everyday());
        everyday.setUser(user);
        return everydayRepository.saveAndFlush(everyday);
    }

    @Override
    public synchronized void addSpeakCount(Everyday everyday) {
        everyday.setSpeakCount(everyday.getSpeakCount() + 1);
        everydayRepository.saveAndFlush(everyday);
    }

    @Override
    public void addSetuCount(Everyday everyday) {
        addSetuCount(everyday, 1);
    }

    @Override
    public synchronized void addSetuCount(Everyday everyday, int num) {
        everyday.setSetuCount(everyday.getSetuCount() + num);
        everydayRepository.saveAndFlush(everyday);
    }

    @Override
    public synchronized boolean addFavorite(User user, Setu setu) {
        for (Setu s : user.getFavoriteSetus()) {
            if (s.getId().equals(setu.getId())) {
                return false;
            }
        }

        user.getFavoriteSetus().add(setu);
        userRepository.saveAndFlush(user);
        return true;
    }
}
