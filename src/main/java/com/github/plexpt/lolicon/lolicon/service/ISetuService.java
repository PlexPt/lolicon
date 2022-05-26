package com.github.plexpt.lolicon.lolicon.service;

import com.github.plexpt.lolicon.lolicon.entity.Setu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author plexpt
 * @since 2022-05-24
 */
public interface ISetuService extends IService<Setu> {

    void dled(Setu setu);
}
