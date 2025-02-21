// package com.dnd.backend.notification.firebase;
//
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.dnd.backend.notification.notification.UserDisasterNotification;
// import com.dnd.backend.notification.notification.UserDisasterNotificationRepository;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class UserDisasterNotificationService {
//
// 	private final UserDisasterNotificationRepository notificationRepository;
// 	private final FCMService fcmService;
//
// 	@Transactional
// 	public DisasterNotiResponse saveAndNotify(UserDisasterNotification notification) {
// 		UserDisasterNotification savedNotification = notificationRepository.save(notification);
// 		DisasterNotiResponse disasterNotiResponse = fcmService.sendNotificationToTopic(savedNotification);
// 		return disasterNotiResponse;
// 	}
// }
