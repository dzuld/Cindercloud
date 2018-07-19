package cloud.cinder.web.notifications;

import cloud.cinder.common.notifications.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class Noty {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public void success(final String message) {
        messagingTemplate.convertAndSend("/topic/notifications",
                Notification.builder()
                        .type("success")
                        .text(message)
                        .build());
    }
}
