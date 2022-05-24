package com.github.plexpt.lolicon;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应
 * 字段名	数据类型	说明
 * error	string	错误信息
 * data	setu[]	色图数组
 * setu
 * 字段名	数据类型	说明
 * pid	int	作品 pid
 * p	int	作品所在页
 * uid	int	作者 uid
 * title	string	作品标题
 * author	string	作者名（入库时，并过滤掉 @ 及其后内容）
 * r18	boolean	是否 R18（在库中的分类，不等同于作品本身的 R18 标识）
 * width	int	原图宽度 px
 * height	int	原图高度 px
 * tags	string[]	作品标签，包含标签的中文翻译（有的话）
 * ext	string	图片扩展名
 * uploadDate	int	作品上传日期；时间戳，单位为毫秒
 * urls	object	包含了所有指定size的图片地址
 */
@NoArgsConstructor
@Data
public class LoliconData {

    private String error;
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Long pid;
        private Long p;
        private Long uid;
        private String title;
        private String author;
        private Boolean r18;
        private Long width;
        private Long height;
        private List<String> tags;
        private String ext;
        private Long uploadDate;
        private UrlsDTO urls;

        @NoArgsConstructor
        @Data
        public static class UrlsDTO {
            private String original;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }
    }

}
