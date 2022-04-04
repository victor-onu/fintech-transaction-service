package com.victor.transactionservice.service.fiegn;

import com.victor.transactionservice.client.NotificationServiceClient;
import com.victor.transactionservice.service.dto.AccountOwnerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    private final NotificationServiceClient notificationServiceClient;

    public NotificationService(NotificationServiceClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    public Boolean sendCreateAccountMail(AccountOwnerDTO accountOwnerDTO) {
        return notificationServiceClient.sendCreateAccountMail(accountOwnerDTO);
    }
}
