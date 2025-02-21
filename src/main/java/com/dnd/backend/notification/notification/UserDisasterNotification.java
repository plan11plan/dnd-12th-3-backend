// package com.dnd.backend.notification.notification;
//
// import com.dnd.backend.support.auditing.BaseTimeEntity;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Lob;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @Table(name = "user_disaster_notification")
// public class UserDisasterNotification extends BaseTimeEntity {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	private Long id;
//
// 	// 어떤 유저인지 (MemberEntity.id)
// 	private Long userId;
//
// 	// 어떤 재난문자인지 (DisasterMessage.id)
// 	private Long disasterMessageId;
//
// 	// 재난문자 내용 (MSG_CN)
// 	@Lob
// 	@Column(nullable = false)
// 	private String messageContent;
//
// 	// 재난문자 지역 (RCPTN_RGN_NM)
// 	@Lob
// 	@Column(nullable = false)
// 	private String emd;
//
// }
