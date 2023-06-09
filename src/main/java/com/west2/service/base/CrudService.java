package com.west2.service.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.west2.entity.base.BaseEntity;
import com.west2.entity.base.DataEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class CrudService<M extends BaseMapper<T>,T extends DataEntity<T>> {

    @Autowired
    protected M mapper;

    public CrudService() {
    }

    public T get(String id) {
        QueryWrapper<T> wrapper = new QueryWrapper<T>().eq("id", id);
        List<T> list = this.findList(wrapper);
        if (0 < list.size()) {
            return list.get(0);
        } else {
            return null;
        }
//        return this.mapper.selectById(id);
    }

    public IPage<T> findListPage(int pageNum, int pageSize, QueryWrapper<T> wrapper) {
        return this.findListPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public IPage<T> findAllListPage(int pageNum, int pageSize, QueryWrapper<T> wrapper) {
        return this.findAllListPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public IPage<T> findListPage(Page<T> page, QueryWrapper<T> wrapper) {
        wrapper.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        return mapper.selectPage(page, wrapper);
    }

    public IPage<T> findAllListPage(Page<T> page, QueryWrapper<T> wrapper) {
        return mapper.selectPage(page, wrapper);
    }


    public List<T> findList(QueryWrapper<T> wrapper) {
        wrapper.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        return this.mapper.selectList(wrapper);
    }

    public List<T> findAllList(QueryWrapper<T> wrapper) {
        return this.mapper.selectList(wrapper);
    }

    public T findOne(QueryWrapper<T> wrapper) {
        return this.mapper.selectOne(wrapper);
    }

    public int save(T entity) {
        int result = 0;
        if (entity.isNewRecord()) {
            entity.preInsert();
            result = this.mapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateById(entity);
        }
        return result;
    }

    public int delete(T entity) {
        int result = 0;
        if (entity != null) {
            entity.setDelFlag("1");
            entity.preUpdate();
            result = this.mapper.updateById(entity);
        }
        return result;
    }


}
