//package com.fdm.pmstprocessor.listener;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import com.fdm.pmscommon.config.RabbitMQConfig;
//import com.fdm.pmscommon.dto.incoming.TradeUploadRequest;
//@Component
//public class TradeProcessorListener {
//    @RabbitListener(queues = RabbitMQConfig.TRADE_QUEUE)
//    public void receiveTrade(TradeUploadRequest request) {
//        System.out.println("Received trade request: " + request);
//    }
//}
