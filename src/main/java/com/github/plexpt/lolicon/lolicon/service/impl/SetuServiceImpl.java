package com.github.plexpt.lolicon.lolicon.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.plexpt.lolicon.lolicon.entity.Setu;
import com.github.plexpt.lolicon.lolicon.mapper.SetuMapper;
import com.github.plexpt.lolicon.lolicon.service.ISetuService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author plexpt
 * @since 2022-05-24
 */
@Service
public class SetuServiceImpl extends ServiceImpl<SetuMapper, Setu> implements ISetuService {

    @Override
    public void dled(Setu setu) {
        update(new UpdateWrapper<Setu>()
                .set("dl", 1)
                .eq("pid", setu.getPid())
        );

    }
}
