package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.Dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.util.List;

public abstract class DaoImpl<T> implements Dao<T> {
    private static final SessionFactory sessionFactory;
    private Session session;

    //创建sessionFactory
    static {
        try {
            // 采用默认的hibernate.cfg.xml来启动一个Configuration的实例
            Configuration cfg = new Configuration().configure();
            // 以Configuration实例来创建SessionFactory实例
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * 打开一个会话
     */
    public Session openSession() {
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;
    }

    /**
     * 关闭一个会话
     */
    public void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    /**
     * 保存或更新一个对象
     */
    public void saveOrUpdate(T object) {
        try {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        } finally {
            closeSession();
        }
    }

    /**
     * 保存或更新一组对象
     */
    public void saveAll(List<T> list) {
        try {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            for (Object obj : list) {
                session.saveOrUpdate(obj);
            }
            transaction.commit();
        } finally {
            closeSession();
        }
    }

    /**
     * 删除一个对象
     */
    public void delete(T object) {
        try {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();
        } finally {
            closeSession();
        }
    }

    /**
     * 根据long型主键查找对象
     */
    protected T findByLongId(Class<T> t, Serializable id) {
        T ret = null;
        try {
            Session session = openSession();
            ret = session.load(t, id);
        } catch (NullPointerException e) {
            System.err.println("not find setu by id " + id);
        } finally {
            closeSession();
        }

        return ret;
    }

    /**
     * 查找所有对象
     */
    protected List<T> findAll(Class<T> t) {
        List<T> list;
        try {
            Session session = openSession();
            Query<T> query = session.createQuery("from " + t.getName(), t);
            list = query.list();
        } finally {
            closeSession();
        }

        return list;
    }

}
