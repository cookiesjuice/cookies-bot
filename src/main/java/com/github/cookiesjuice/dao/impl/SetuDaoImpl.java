package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.Dao;
import com.github.cookiesjuice.dao.SetuDao;
import com.github.cookiesjuice.entity.Setu;

import java.util.List;

/**
 * 涩图数据访问接口实现类
 */
public class SetuDaoImpl extends Dao implements SetuDao {
    @Override
    public void saveSetu(Setu setu) {
        saveOrUpdate(setu);
    }

    @Override
    public void saveAllSetu(List<Setu> setuList) {
        saveAll(setuList);
    }

    @Override
    public void deleteSetu(Setu setu) {
        delete(setu);
    }

    @Override
    public Setu findSetuById(long id) {
        return findByLongId(Setu.class, id);
    }

    @Override
    public List<Setu> findAllSetu() {
        return findAll(Setu.class);
    }
}
