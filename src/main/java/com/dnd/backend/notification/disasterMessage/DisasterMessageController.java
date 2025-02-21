// package com.dnd.backend.notification.disasterMessage;
//
// import java.util.List;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.dnd.backend.notification.notification.UserDisasterNotification;
// import com.dnd.backend.notification.notification.UserDisasterNotificationRepository;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// /**
//  * 재난문자 관련 API를 제공하는 컨트롤러
//  */
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/disaster-messages")
// @Slf4j
// public class DisasterMessageController {
//
// 	private final DisasterMessageService disasterMessageService;
// 	private final DisasterMessageRepository disasterMessageRepository;
// 	private final UserDisasterNotificationRepository userDisasterNotificationRepository;
//
// 	/**
// 	 * 1) 공공데이터 재난문자 API 호출
// 	 * 2) DB에 저장
// 	 * 3) 유저 주소와 매핑 -> 알림 대상(UserDisasterNotification) 생성
// 	 *
// 	 * @return 성공 메시지
// 	 */
// 	@PostMapping("/fetch-and-notify")
// 	public ResponseEntity<String> fetchAndNotify() {
// 		disasterMessageService.fetchAndSaveDisasterMessagesAndNotify();
// 		return ResponseEntity.ok("재난문자 데이터 갱신 및 알림대상 매핑 완료");
// 	}
//
// 	/**
// 	 * 저장된 재난문자 전체 조회
// 	 */
// 	@GetMapping
// 	public ResponseEntity<List<DisasterMessage>> getAllDisasterMessages() {
// 		List<DisasterMessage> messages = disasterMessageRepository.findAll();
// 		return ResponseEntity.ok(messages);
// 	}
//
// 	/**
// 	 * 특정 재난문자 조회
// 	 */
// 	@GetMapping("/{id}")
// 	public ResponseEntity<DisasterMessage> getDisasterMessageById(@PathVariable Long id) {
// 		DisasterMessage message = disasterMessageRepository.findById(id)
// 			.orElseThrow(() -> new IllegalArgumentException("해당 재난문자를 찾을 수 없습니다. id=" + id));
// 		return ResponseEntity.ok(message);
// 	}
//
// 	/**
// 	 * 알림 내역(매핑된 유저-재난문자) 전체 조회
// 	 */
// 	@GetMapping("/notifications")
// 	public ResponseEntity<List<UserDisasterNotification>> getAllNotifications() {
// 		List<UserDisasterNotification> notiList = userDisasterNotificationRepository.findAll();
// 		return ResponseEntity.ok(notiList);
// 	}
//
// 	/**
// 	 * 특정 유저의 알림 내역 조회 (예시)
// 	 */
// 	@GetMapping("/notifications/user/{userId}")
// 	public ResponseEntity<List<UserDisasterNotification>> getNotificationsByUser(@PathVariable Long userId) {
// 		// 간단히 findAll 후 필터링 (규모가 작다면 가능, 대규모 시엔 별도 쿼리 필요)
// 		List<UserDisasterNotification> all = userDisasterNotificationRepository.findAll();
// 		List<UserDisasterNotification> filtered = all.stream()
// 			.filter(n -> n.getUserId().equals(userId))
// 			.toList();
//
// 		return ResponseEntity.ok(filtered);
// 	}
// }
