// package com.dnd.backend.notification.disasterMessage;
//
// import java.time.format.DateTimeFormatter;
// import java.util.List;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
//
// import com.dnd.backend.notification.notification.UserDisasterNotification;
// import com.dnd.backend.notification.notification.UserDisasterNotificationRepository;
// import com.dnd.backend.user.entity.Address;
// import com.dnd.backend.user.repository.AddressRepository;
// import com.dnd.backend.user.repository.MemberRepository;
// import com.fasterxml.jackson.databind.JsonNode;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Service
// @Slf4j
// @RequiredArgsConstructor
// public class DisasterMessageService {
//
// 	private static final String DISASTER_API_URL = "https://www.safetydata.go.kr/V2/api/DSSP-IF-00247";
//
// 	private final DisasterMessageRepository disasterMessageRepository;
// 	private final UserDisasterNotificationRepository userDisasterNotificationRepository;
//
// 	private final MemberRepository memberRepository;
// 	private final AddressRepository addressRepository;
//
// 	private final RestTemplate restTemplate;
//
// 	@Value("${disaster.api.serviceKey}")
// 	private String disasterServiceKey;
//
// 	/**
// 	 * (1) 주소 목록이 비어있으면 API 요청을 스킵
// 	 * (2) DB에서 최대 SN을 구해, 그보다 큰 SN만 DB에 새로 저장
// 	 * (3) 저장된 메시지별로 유저 주소 매핑 -> 알림 생성
// 	 */
// 	public void fetchAndSaveDisasterMessagesAndNotify() {
// 		// [A] 주소 정보가 없으면 API 호출 스킵
// 		List<Address> allAddresses = addressRepository.findAll();
// 		if (allAddresses.isEmpty()) {
// 			log.info("💡 주소 정보가 하나도 없습니다. 재난문자 API 호출을 스킵합니다.");
// 			return;
// 		}
//
// 		// [B] DB에 이미 저장된 SN의 최대값 조회
// 		Long maxSn = disasterMessageRepository.findMaxSn();
// 		log.info("현재 DB에 저장된 최대 SN = {}", maxSn);
//
// 		int pageNo = 1;
// 		int numOfRows = 20;
// 		boolean hasMore = true;
//
// 		while (hasMore) {
// 			String url = DISASTER_API_URL + "?serviceKey=" + disasterServiceKey
// 				+ "&pageNo=" + pageNo
// 				+ "&numOfRows=" + numOfRows
// 				+ "&returnType=json";
//
// 			try {
// 				ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
//
// 				if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
// 					JsonNode bodyArray = response.getBody().get("body");
//
// 					if (bodyArray != null && bodyArray.isArray()) {
// 						int size = bodyArray.size();
// 						log.info("✅ [DisasterMsg] pageNo={}, 응답 {}건 수신", pageNo, size);
//
// 						int insertedCount = 0;
//
// 						for (JsonNode msgNode : bodyArray) {
// 							Long sn = msgNode.get("SN").asLong();
//
// 							// [C] SN이 현재 maxSn보다 작거나 같으면 skip (이미 처리했을 가능성 큼)
// 							if (sn <= maxSn) {
// 								continue;
// 							}
//
// 							// SN이 DB에 없으면 새로 저장
// 							DisasterMessage message = parseDisasterMessage(msgNode);
// 							// DB 저장
// 							disasterMessageRepository.save(message);
//
// 							// [D] SN 최신화
// 							// 만약 sn이 maxSn보다 크면 maxSn 갱신
// 							if (sn > maxSn) {
// 								maxSn = sn;
// 							}
//
// 							// [E] 유저 주소와 매핑 -> 알림 생성
// 							processNotificationForMessage(message, allAddresses);
//
// 							insertedCount++;
// 						}
//
// 						log.info("▶ pageNo={} -> {}건 신규 저장", pageNo, insertedCount);
//
// 						if (size < numOfRows) {
// 							hasMore = false;
// 						} else {
// 							pageNo++;
// 						}
// 					} else {
// 						log.warn("⚠ 'body' 필드가 없거나 배열이 아닙니다. (pageNo={})", pageNo);
// 						break;
// 					}
// 				} else {
// 					log.error("❌ 재난문자 API 호출 실패: HTTP={} (pageNo={})",
// 						response.getStatusCode(), pageNo);
// 					break;
// 				}
// 			} catch (Exception e) {
// 				log.error("⚠️ 재난문자 API 호출 중 오류 (pageNo={}): {}", pageNo, e.getMessage(), e);
// 				break;
// 			}
// 		}
//
// 		log.info("✅ 재난문자 데이터 저장 및 알림 매핑 완료");
// 	}
//
// 	/**
// 	 * JSON -> DisasterMessage 변환
// 	 */
// 	private DisasterMessage parseDisasterMessage(JsonNode msgNode) {
// 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//
// 		DisasterMessage message = new DisasterMessage();
// 		message.setSn(msgNode.get("SN").asLong());
// 		message.setMessageContent(msgNode.get("MSG_CN").asText());
// 		message.setRegionName(msgNode.get("RCPTN_RGN_NM").asText());
// 		message.setCreatedAt(msgNode.get("CRT_DT").asText());
// 		message.setRegisteredAt(msgNode.get("REG_YMD").asText());
// 		message.setEmergencyLevel(msgNode.get("EMRG_STEP_NM").asText());
// 		message.setDisasterType(msgNode.get("DST_SE_NM").asText());
// 		message.setModifiedAt(msgNode.get("MDFCN_YMD").asText());
//
// 		return message;
// 	}
//
// 	/**
// 	 * 저장된 DisasterMessage + 전체 주소 목록 -> 알림 매핑
// 	 */
// 	private void processNotificationForMessage(DisasterMessage message, List<Address> allAddresses) {
// 		String regionName = message.getRegionName();
//
// 		for (Address userAddress : allAddresses) {
// 			boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, regionName);
// 			if (canNotify) {
// 				UserDisasterNotification notification = UserDisasterNotification.builder()
// 					.userId(userAddress.getMemberEntity().getId())
// 					.disasterMessageId(message.getId())
// 					.messageContent(message.getMessageContent())
// 					.emd(message.getRegionName())
// 					.build();
//
// 				userDisasterNotificationRepository.save(notification);
//
// 				log.info(" - 알림 대상 생성 userId={}, messageId={}, region='{}'",
// 					userAddress.getMemberEntity().getId(),
// 					message.getId(),
// 					message.getRegionName());
// 			}
// 		}
// 	}
//
// 	public List<String> getDistinctRegionNames() {
// 		return disasterMessageRepository.findDistinctRegionNames();
// 	}
// }
