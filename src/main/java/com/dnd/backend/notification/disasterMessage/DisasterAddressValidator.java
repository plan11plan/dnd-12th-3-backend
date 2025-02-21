// package com.dnd.backend.notification.disasterMessage;
//
// import java.util.List;
//
// import com.dnd.backend.user.entity.Address;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// public class DisasterAddressValidator {
//
// 	/**
// 	 * 공공데이터 재난문자에 기재된 주소 문자열(rawRegionStr)과 유저 주소를 비교하여,
// 	 * 유저가 알림 대상인지(포함되는지) 판별.
// 	 *
// 	 * @param userAddress   (유저의 주소 엔티티: sido, sgg, emd)
// 	 * @param rawRegionStr  (예: "경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,임진강 수계지역(경기도 연천군,파주시)")
// 	 * @return 매칭되면 true, 아니면 false
// 	 */
// 	public static boolean canNotifyUser(Address userAddress, String rawRegionStr) {
// 		if (userAddress == null || rawRegionStr == null || rawRegionStr.isBlank()) {
// 			return false;
// 		}
//
// 		// 1) 재난문자 주소 문자열을 복수 Address로 파싱
// 		List<Address> disasterAddresses = AddressParser.parseMultiAddress(rawRegionStr);
//
// 		// 2) 하나라도 매칭되면 알림 대상
// 		for (Address disasterAddr : disasterAddresses) {
// 			if (isMatched(userAddress, disasterAddr)) {
// 				return true;
// 			}
// 		}
// 		return false;
// 	}
//
// 	/**
// 	 * 매칭 규칙 (예시):
// 	 * 1) 시도(sido) 일치 (null-safe 비교)
// 	 * 2) 시군구(sgg) 일치 OR 재난문자에 sgg가 "전체"
// 	 * 3) 읍면동(emd)은 여기서는 생략 (정책에 따라 추가 가능)
// 	 */
// 	private static boolean isMatched(Address user, Address disaster) {
// 		if (!equalsIgnoreNull(user.getSido(), disaster.getSido())) {
// 			return false;
// 		}
//
// 		// "전체"면 해당 시도 전체를 의미한다고 가정
// 		if ("전체".equals(disaster.getSgg())) {
// 			return true;
// 		}
//
// 		// 정확히 일치하는지
// 		return equalsIgnoreNull(user.getSgg(), disaster.getSgg());
// 		// 필요시, user.getEmd() 와 disaster.getEmd() 비교를 추가
// 	}
//
// 	/**
// 	 * null-safe string equals
// 	 */
// 	private static boolean equalsIgnoreNull(String a, String b) {
// 		if (a == null || b == null) {
// 			return false;
// 		}
// 		return a.trim().equals(b.trim());
// 	}
// }
