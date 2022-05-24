package com.github.plexpt.lolicon;

import com.github.dozermapper.core.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 *
 * dozer转换类
 *
 *
 * @author liyue
 * @version v2
 * @create 2019-03-13 15:35:10
 * @copyright www.liderong.cn
 */
@Component
public class Dozer {

    /**
     * dozer转换的核心mapper对象
     */
    private static Mapper dozerMapper;

    @Autowired
    private Mapper mapper;

    @PostConstruct
    private void construct() {
        Dozer.setDozerMapper(mapper);
    }

    private static void setDozerMapper(Mapper dozerMapper) {
        Dozer.dozerMapper = dozerMapper;
    }

    /**
     * 转换实体为另一个指定的实体
     * 任意一个参数为NULL时 会抛出NPE
     * {@link com.github.dozermapper.core.util.MappingValidator#validateMappingRequest}
     *
     * @param source 源实体 不能为NULL
     * @param clazz 目标实体 不能为NULL
     * @param <T> 泛型
     * @return 转换后的结果
     */
    @NonNull
    public static <T> T convert(@NonNull Object source, @NonNull Class<T> clazz) {
        return (T) dozerMapper.map(source, clazz);
    }

    /**
     * 转换List实体为另一个指定的实体
     * source如果为NULL 会使用空集合
     * 在目标实体为NULL时 会抛出NPE
     * {@link com.github.dozermapper.core.util.MappingValidator#validateMappingRequest}
     *
     * @param source 源集合 可以为NULL
     * @param clazz 目标实体 不能为NULL
     * @param <T> 泛型
     * @return 转换后的结果
     */
    @Nullable
    public static <T> List<T> convert(@Nullable List<?> source, @NonNull Class<T> clazz) {
        return Optional.ofNullable(source)
                .orElse(Collections.emptyList())
                .stream()
                .map(bean -> dozerMapper.map(bean, clazz))
                .collect(Collectors.toList());
    }
}
