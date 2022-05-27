package com.github.plexpt.lolicon;

import com.alibaba.fastjson.JSON;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * TaskMessageConsumer
 *
 * @author chensuyi
 * @date 2020-10-19 18:50
 */
@RocketMQMessageListener(
        topic = "${rocket.queue.lolicon.pull.topic}",
        consumerGroup = "${rocket.queue.lolicon.pull.group}")
@Component
@Slf4j
public class TaskMessageConsumer implements RocketMQListener<String> {


    @Override
    public void onMessage(String content) {
        LoliconData data;
        try {
            data = JSON.parseObject(content, LoliconData.class);
            process(data);
        } catch (UnsupportedOperationException e) {
            log.warn("消息执行顺序异常或已经处理过");
        } catch (Exception e) {
            log.error("消息解析失败", e);

        }
    }

    private void process(LoliconData list) {
        log.info("收到消息" + list.getData().size());
    }
}
