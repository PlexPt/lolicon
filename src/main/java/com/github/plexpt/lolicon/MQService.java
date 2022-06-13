package com.github.plexpt.lolicon;

import com.alibaba.fastjson.JSON;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MQService {

    @Autowired
    private RocketMQTemplate rocketTemplate;
    @Value("${rocket.queue.lolicon.pull.topic}")
    public String topic;

    public void send(LoliconData data) {
        try {
            SendResult result = rocketTemplate.syncSend(topic, JSON.toJSONString(data));
            if (!isSuccess(result)) {
                log.warn("发送消息不成功, 消息投递失败 ===> topic:{} result:{}", topic, result);
                throw new UnsupportedOperationException("发送消息不成功, 消息投递失败:" + result.toString());
            }
        } catch (Exception e) {
            log.error("投递消息失败: " + e.toString());
            throw new UnsupportedOperationException("投递消息失败, " + e.getMessage());
        }
    }


    private boolean isSuccess(SendResult result) {
        return Objects.nonNull(result) && (result.getSendStatus() == SendStatus.SEND_OK);
    }


}
