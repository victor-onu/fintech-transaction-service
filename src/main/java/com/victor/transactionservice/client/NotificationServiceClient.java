package com.victor.transactionservice.client;

import com.victor.transactionservice.config.LoadBalancerConfiguration;
import com.victor.transactionservice.service.dto.AccountOwnerDTO;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "notification-service", configuration = UserFeignClientInterceptor.class)
@LoadBalancerClient(name = "notification-service", configuration = LoadBalancerConfiguration.class)
public interface NotificationServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/emails/send-mails")
    Boolean sendCreateAccountMail(@RequestBody AccountOwnerDTO accountOwnerDTO);
}
