// package com.dnd.backend.notification.firebase;
//
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.dnd.backend.notification.notification.UserDisasterNotification;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/notifications")
// public class UserDisasterNotificationController {
//
// 	private final UserDisasterNotificationService notificationService;
//
// 	@PostMapping
// 	public DisasterNotiResponse createNotification(@RequestBody UserDisasterNotification notification) {
// 		return notificationService.saveAndNotify(notification);
// 	}
// }
