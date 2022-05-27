package com.github.plexpt.lolicon;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.plexpt.lolicon.lolicon.entity.Setu;
import com.github.plexpt.lolicon.lolicon.service.ISetuService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class PicDownBot implements CommandLineRunner {

    ExecutorService executor = Executors.newFixedThreadPool(5);
    final ISetuService setuService;
    @Value("${dl}")
    boolean dl = false;


    @Override
    public void run(String... args) throws Exception {
        if (!dl) {
            return;
        }
        executor.submit(() -> {
            log.info("开始下载");

            List<Setu> list = setuService.list(new QueryWrapper<Setu>()
                    .eq("r18", "false")
                    .eq("dl", 0)
            );

            int size = list.size();
            AtomicInteger pro = new AtomicInteger(1);
            list.parallelStream()
                    .forEach(setu -> {
                        try {
                            downOne(setu);
                            log.info("进度：{}/{}", pro.get(), size);
                            pro.getAndIncrement();
                        } catch (Exception e) {
                            log.error(e.toString());
                        }
                    });
            log.info("结束下载");
        });
    }

    private void downOne(Setu setu) {
        //{"original":"https://i.pixiv.cat/img-original/img/2022/01/30/22/42/35/95887616_p0.png"}
        String urls = setu.getUrls();

        LoliconData.DataDTO.UrlsDTO dto = JSON.parseObject(urls, LoliconData.DataDTO.UrlsDTO.class);
//        String url = dto.getOriginal().replace("i.pixiv.cat", "proxy.pixiv.shojo.cn");
        String url = dto.getOriginal().replace("i.pixiv.cat", "i.pixiv.re");

        //https://pixiv.shojo.cn/34844544-0
//          url = String.format("https://pixiv.shojo.cn/%d-%d", setu.getPid(), setu.getP());

        File dir = new File("dl/normal/");
        FileUtil.mkdir(dir);

        log.info("下载：" + url);
        HttpUtil.downloadFile(url, dir);
        log.info("下载成功：" + url.substring(url.lastIndexOf("/") + 1));
        setuService.dled(setu);
    }


}
