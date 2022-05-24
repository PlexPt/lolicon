package com.github.plexpt.lolicon;

import com.github.plexpt.lolicon.lolicon.entity.Setu;
import com.github.plexpt.lolicon.lolicon.service.ISetuService;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdaBot implements ApplicationRunner {

    final ISetuService setuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        for (int i = 0; i < 100; i++) {
            log.info("开始第" + i + "轮");
            start();
        }
        log.info("结束爬取");

    }

    private void start() {
        List<LoliconData.DataDTO> list = LoliconBotApi.get();

        List<Setu> setus = Dozer.convert(list, Setu.class);

        setuService.saveOrUpdateBatch(setus);
    }
}
