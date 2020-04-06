package com.roy.im.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenlin
 */
@Service
public class PushMsgService {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    public void push(){
        kafkaTemplate.send("sendMsg","im-server","{\"data\":i am testing}");
    }
}
