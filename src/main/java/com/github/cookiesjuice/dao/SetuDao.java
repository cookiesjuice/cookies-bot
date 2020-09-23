package com.github.cookiesjuice.dao;

import com.github.cookiesjuice.entity.Setu;

import java.util.List;

/**
 * 涩图数据访问接口
 */
public interface SetuDao {
    /**
     * 添加或更新一张涩图
     */
    void saveSetu(Setu setu);

    /**
     * 添加或更新一组涩图
     */
    void saveAllSetu(List<Setu> setuList);

    /**
     * 根据涩图对象的id删除一张涩图
     */
    void deleteSetu(Setu setu);

    /**
     * 根据id查找涩图
     */
    Setu findSetuById(long id);

    /**
     * 无条件查找所有涩图
     */
    List<Setu> findAllSetu();
}
