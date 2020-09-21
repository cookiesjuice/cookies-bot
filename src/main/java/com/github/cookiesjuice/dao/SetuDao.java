package com.github.cookiesjuice.dao;

import com.github.cookiesjuice.entity.Setu;
import com.github.cookiesjuice.entity.Tag;

import java.util.List;

/**
 * 涩图数据访问接口
 */
public interface SetuDao {
    /**
     * 添加一张涩图
     *
     * @param setu 涩图对象
     * @return 添加结果 <br/>
     * 涩图id -> 成功 <br/>
     * -1 -> 失败
     */
    long add(Setu setu);

    /**
     * 添加一组涩图
     *
     * @param setuList 涩图对象列表
     * @return 成功添加的数量
     */
    int addAll(List<Setu> setuList);

    /**
     * 更新一张涩图
     *
     * @param setu 涩图对象
     * @return 更新结果 <br/>
     * 0 -> 更新成功 <br/>
     * -1 -> 更新失败
     */
    int update(Setu setu);

    /**
     * 根据涩图对象的id删除一张涩图
     *
     * @param setu 涩图对象
     * @return 删除结果 <br/>
     * 0 -> 删除成功 <br/>
     * -1 -> 删除失败
     */
    int delete(Setu setu);

    /**
     * 根据涩图id删除一张涩图
     *
     * @param id 涩图id
     * @return 删除结果 <br/>
     * 0 -> 删除成功 <br/>
     * -1 -> 删除失败
     */
    int deleteById(long id);

    /**
     * 根据ID查找涩图
     *
     * @param id 涩图唯一id
     * @return 查找结果
     * 找到 -> 涩图对象 <br/>
     * 未找到 -> null <br/>
     */
    Setu findSetuById(long id);

    /**
     * 无条件查找所有涩图
     *
     * @return 涩图对象列表 <br/>
     */
    List<Setu> findAllSetu();

    /**
     * 根据一个标签的id查找所有涩图
     *
     * @param tag 标签对象
     * @return 包含此标签的涩图对象列表 <br/>
     */
    List<Setu> findAllSetuByTag(Tag tag);

    /**
     * 根据多个标签的id查找所有涩图
     *
     * @param tagList 标签对象列表
     * @return 包含这些标签的涩图对象列表 <br/>
     */
    List<Setu> findAllSetuByTagList(List<Tag> tagList);
}
