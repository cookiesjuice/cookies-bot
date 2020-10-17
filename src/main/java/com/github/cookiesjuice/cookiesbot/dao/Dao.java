package com.github.cookiesjuice.cookiesbot.dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public interface Dao<T> {
    Session openSession();
    void closeSession();
    void saveOrUpdate(T object);
    void saveAll(List<T> list);
    void delete(T object);
    T findByLongId(Serializable id);
    abstract List<T> findAll();

}
