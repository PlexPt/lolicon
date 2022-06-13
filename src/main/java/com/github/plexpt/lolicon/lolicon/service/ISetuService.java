package com.github.plexpt.lolicon.lolicon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.plexpt.lolicon.lolicon.entity.Setu;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author plexpt
 * @since 2022-05-24
 */
public interface ISetuService extends IService<Setu> {

    void dled(Setu setu);
}
