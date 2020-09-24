package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.SetuDao;
import com.github.cookiesjuice.entity.Setu;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 涩图数据访问接口实现类
 */
public class SetuDaoImpl extends DaoImpl<Setu> implements SetuDao {

    @Override
    public Setu findByLongId(Serializable id) {
        return super.findByLongId(Setu.class, id);
    }

    @Override
    public List<Setu> findAll() {
        return super.findAll(Setu.class);
    }
}
