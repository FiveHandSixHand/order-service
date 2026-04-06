package com.fhsh.daitda.order.infrastructure.external.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fhsh.daitda.order.infrastructure.external.slack.dto.CreateSlackRequest;
import com.fhsh.daitda.response.CommonResponse;

@FeignClient(name = "notification-service")
public interface SlackFeignClient {
	@PostMapping("/internal/v1/slackmessages")
	CommonResponse<Void> createSlack(@RequestBody CreateSlackRequest slackRequest);
}
