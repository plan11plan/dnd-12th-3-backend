// package com.dnd.backend.notification.disasterMessage;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Lob;
// import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(
// 	name = "disaster_message",
// 	uniqueConstraints = {
// 		@UniqueConstraint(name = "uk_disaster_message_sn", columnNames = {"sn"})
// 	}
// )
// public class DisasterMessage {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	private Long id;
//
// 	// SN: 공공데이터에서 제공하는 고유번호
// 	@Column(nullable = false)
// 	private Long sn;
//
// 	@Lob
// 	@Column(nullable = false, columnDefinition = "TEXT")
// 	private String messageContent; // MSG_CN
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String regionName; // RCPTN_RGN_NM
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String createdAt; // CRT_DT
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String registeredAt; // REG_YMD
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String emergencyLevel; // EMRG_STEP_NM
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String disasterType; // DST_SE_NM
//
// 	@Lob
// 	@Column(nullable = false)
// 	private String modifiedAt; // MDFCN_YMD
//
// 	// 생성자 단축용 빌더 등 필요시
// }
