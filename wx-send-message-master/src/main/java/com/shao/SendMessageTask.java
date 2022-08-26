package com.shao;

import com.shao.service.SendMessage;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Describe：
 *
 * @author Administrator
 * @createTime 2022/08/22 21:29
 */

@Configuration
@EnableScheduling
public class SendMessageTask {
    
    @Resource
    private SendMessage sendMessage;
    
    @Scheduled(cron = "0 0 9 * * ?")
    private void configureTasks() {
        
        sendMessage.sendMessageByTemplate();
    }
    
}
