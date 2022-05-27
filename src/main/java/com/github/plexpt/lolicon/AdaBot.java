package com.github.plexpt.lolicon;

import com.github.plexpt.lolicon.lolicon.aop.TimeLog;
import com.github.plexpt.lolicon.lolicon.entity.Setu;
import com.github.plexpt.lolicon.lolicon.service.ISetuService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@DependsOn
@Slf4j
@RequiredArgsConstructor
public class AdaBot implements ApplicationRunner {

    final ISetuService setuService;
    final MQService mqService;
    ExecutorService executor = Executors.newFixedThreadPool(5);

    @Value("${api}")
    boolean api = false;

    int max = 48446;

    int se = 12164;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!api) {
            return;
        }
        executor.submit(() -> {

            for (int i = 0; i < 2000; i++) {
                log.info("开始第" + i + "轮");
                for (int j = 0; j < 10; j++) {
                    start();
                }
                long count = setuService.count();
                log.info("已经爬取：" + count);

                if (count > 48200) {
                    break;
                }
            }
            log.info("结束爬取");


        });
    }

    private void start() {

        List<LoliconData.DataDTO> list = new ArrayList<>();
        try {
            list = LoliconBotApi.get();
        } catch (Exception e) {
            log.error("" + e.toString());
        }

        LoliconData data = new LoliconData();
        data.setData(list);
        mqService.send(data);
//        saveAll(list);
    }

    @TimeLog
    private void saveAll(List<LoliconData.DataDTO> list) {
        List<Setu> setus = convert(list);
        for (Setu setu : setus) {
            saveOne(setu);
        }
    }

    @TimeLog
    private List<Setu> convert(List<LoliconData.DataDTO> list) {
        List<Setu> setus = Dozer.convert(list, Setu.class);
        return setus;
    }

    @TimeLog
    private void saveOne(Setu setu) {
        try {
            setuService.save(setu);
        } catch (Exception e) {
//            log.warn("" + e.toString());
        }
    }
}
