// package com.dnd.backend.notification.firebase;
//
// import org.springframework.stereotype.Service;
//
// import com.dnd.backend.notification.notification.UserDisasterNotification;
// import com.google.firebase.messaging.FirebaseMessaging;
// import com.google.firebase.messaging.Message;
// import com.google.firebase.messaging.Notification;
//
// @Service
// public class FCMService {
//
// 	public DisasterNotiResponse sendNotificationToTopic(UserDisasterNotification notification) {
// 		// 토픽 이름 생성 (예: "disaster_seoul")
// 		String topic = "disaster_message_" + notification.getEmd().replaceAll("\\s+", "_").toLowerCase();
// 		System.out.println(topic);
// 		// FCM 메시지 생성
// 		Message message = Message.builder()
// 			.setNotification(Notification.builder()
// 				.setTitle("재난문자 알림")
// 				.setBody(notification.getMessageContent())
// 				.build())
// 			.setTopic(topic) // 토픽 지정
// 			.build();
//
// 		try {
// 			// FCM 메시지 전송
// 			String response = FirebaseMessaging.getInstance().send(message);
// 			System.out.println("Successfully sent message: " + response);
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
// 		return new DisasterNotiResponse(
// 			topic,
// 			"재난문자 알림",
// 			notification.getMessageContent()
// 		);
// 	}
// }
