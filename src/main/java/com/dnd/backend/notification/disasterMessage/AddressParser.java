// package com.dnd.backend.notification.disasterMessage;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import com.dnd.backend.user.entity.Address;
//
// import lombok.extern.slf4j.Slf4j;
//
// /**
//  * 재난문자 주소 파싱 로직
//  */
// @Slf4j
// public class AddressParser {
//
// 	/**
// 	 * 다수의 주소가 콤마로 구분되어 들어온 문자열을 파싱하여 Address 목록으로 반환.
// 	 * ex) "경상남도 고성군 ,경상남도 진주시 ,임진강 수계지역(경기도 연천군,파주시)  ,경기도 임진강"
// 	 */
// 	public static List<Address> parseMultiAddress(String multiAddressStr) {
// 		List<Address> result = new ArrayList<>();
// 		if (multiAddressStr == null || multiAddressStr.trim().isEmpty()) {
// 			return result;
// 		}
//
// 		// 1) 콤마로 1차 분할
// 		String[] splitted = multiAddressStr.split("\\s*,\\s*");
//
// 		for (String part : splitted) {
// 			// 2) 괄호 안에 추가 주소가 있는지 확인하여 확장
// 			//    ex) "임진강 수계지역(경기도 연천군,파주시)" ->
// 			//        -> "임진강 수계지역" + "경기도 연천군" + "파주시"
// 			List<String> expandedParts = expandParentheses(part);
//
// 			for (String exp : expandedParts) {
// 				Address parsed = parseSingleAddress(exp);
// 				if (parsed != null) {
// 					result.add(parsed);
// 				}
// 			}
// 		}
// 		return result;
// 	}
//
// 	/**
// 	 * 괄호 안에 별도 주소들이 들어있는 경우, 분할하여 확장하는 메서드
// 	 * 예) "임진강 수계지역(경기도 연천군,파주시)"
// 	 *   -> ["임진강 수계지역", "경기도 연천군", "파주시"]
// 	 *
// 	 * 간단히 한 쌍의 괄호만 처리. 괄호 중첩 등은 추가 로직 필요.
// 	 */
// 	private static List<String> expandParentheses(String input) {
// 		List<String> list = new ArrayList<>();
// 		if (input == null || input.isEmpty()) {
// 			return list;
// 		}
//
// 		// 괄호 시작 인덱스
// 		int startIdx = input.indexOf("(");
// 		// 괄호 종료 인덱스
// 		int endIdx = input.indexOf(")");
//
// 		if (startIdx < 0 || endIdx < 0 || endIdx < startIdx) {
// 			// 괄호가 유효하게 없으면, 통째로만 반환
// 			list.add(input);
// 			return list;
// 		}
//
// 		// (1) 괄호 밖 문자열 (앞부분 + 뒷부분)
// 		String outside = input.substring(0, startIdx).trim(); // "임진강 수계지역"
// 		if (!outside.isEmpty()) {
// 			list.add(outside);
// 		}
//
// 		// (2) 괄호 안 문자열
// 		//     예: "경기도 연천군,파주시" (중간 쉼표 구분 가능)
// 		String inside = input.substring(startIdx + 1, endIdx).trim();
// 		// 쉼표(,) 로 분리
// 		String[] insideSplit = inside.split("\\s*,\\s*");
// 		for (String s : insideSplit) {
// 			if (!s.isBlank()) {
// 				list.add(s.trim());
// 			}
// 		}
//
// 		// (3) 괄호 뒤에 남은 문자열 (예외적으로 있을 수 있음)
// 		//     예: "임진강 수계지역(경기도 연천군,파주시) 기타" -> "기타"
// 		if (endIdx + 1 < input.length()) {
// 			String remain = input.substring(endIdx + 1).trim();
// 			if (!remain.isEmpty()) {
// 				list.add(remain);
// 			}
// 		}
//
// 		return list;
// 	}
//
// 	/**
// 	 * 단일 주소 문자열을 (sido, sgg, emd)로 파싱
// 	 * ex) "경기도 안산시 단원구 풍도동"
// 	 * ex) "울산광역시 울주군"
// 	 * ex) "부산광역시 전체"
// 	 *
// 	 * 괄호나 쉼표가 처리된 뒤의 '단일' 문자열을 전제로 동작.
// 	 */
// 	public static Address parseSingleAddress(String addressStr) {
// 		if (addressStr == null || addressStr.trim().isEmpty()) {
// 			return null;
// 		}
// 		String trimmed = addressStr.trim();
//
// 		// 공백 분리
// 		String[] tokens = trimmed.split("\\s+");
// 		Address address = new Address();
// 		address.setAddressName(trimmed);
//
// 		int len = tokens.length;
// 		if (len == 1) {
// 			// ex) "세종특별자치시" or "전체" 단독
// 			address.setSido(tokens[0]);
// 		} else if (len == 2) {
// 			// ex) "울산광역시 전체" or "경기도 부천시"
// 			address.setSido(tokens[0]);
// 			address.setSgg(tokens[1]);
// 		} else if (len == 3) {
// 			// ex) "울산광역시 울주군 삼남면"
// 			address.setSido(tokens[0]);
// 			address.setSgg(tokens[1]);
// 			address.setEmd(tokens[2]);
// 		} else if (len == 4) {
// 			// ex) "경기도 안산시 단원구 풍도동"
// 			address.setSido(tokens[0]);
// 			address.setSgg(tokens[1] + " " + tokens[2]);
// 			address.setEmd(tokens[3]);
// 		} else {
// 			// 5개 이상
// 			address.setSido(tokens[0]);
// 			address.setEmd(tokens[len - 1]);
// 			// 중간 토큰들 이어붙여서 시군구 구성
// 			StringBuilder sggBuilder = new StringBuilder();
// 			for (int i = 1; i < len - 1; i++) {
// 				sggBuilder.append(tokens[i]);
// 				if (i != len - 2) {
// 					sggBuilder.append(" ");
// 				}
// 			}
// 			address.setSgg(sggBuilder.toString());
// 		}
//
// 		return address;
// 	}
// }
