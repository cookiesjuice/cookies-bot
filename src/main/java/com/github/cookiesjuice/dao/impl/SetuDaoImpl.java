package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.SetuDao;
import com.github.cookiesjuice.entity.Setu;
import com.github.cookiesjuice.entity.Tag;

import java.util.List;

/**
 * 涩图数据访问接口实现类
 */
public class SetuDaoImpl implements SetuDao {
    @Override
    public long add(Setu setu) {
        return 0;
    }

    @Override
    public int addAll(List<Setu> setuList) {
        return 0;
    }

    @Override
    public int update(Setu setu) {
        return 0;
    }

    @Override
    public int delete(Setu setu) {
        return 0;
    }

    @Override
    public int deleteById(long id) {
        return 0;
    }

    @Override
    public Setu findSetuById(long id) {
        return null;
    }

    @Override
    public List<Setu> findAllSetu() {
        return null;
    }

    @Override
    public List<Setu> findAllSetuByTag(Tag tag) {
        return null;
    }

    @Override
    public List<Setu> findAllSetuByTagList(List<Tag> tagList) {
        return null;
    }
}
