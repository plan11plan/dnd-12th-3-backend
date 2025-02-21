package com.dnd.backend.notification.disasterMessage;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DisasterMessageScheduler {

	private final DisasterMessageService disasterMessageService;

	public DisasterMessageScheduler(DisasterMessageService disasterMessageService) {
		this.disasterMessageService = disasterMessageService;
	}

	// 매 1분마다 실행 (cron 표현식 예시)
	// 초 분 시 일 월 요일
	@Scheduled(cron = "0 */30 * * * *")
	public void scheduledDisasterFetch() {
		disasterMessageService.fetchAndSaveDisasterMessagesAndNotify();
	}
}
