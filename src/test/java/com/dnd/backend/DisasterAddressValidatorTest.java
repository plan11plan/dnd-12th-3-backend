// package com.dnd.backend;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// import com.dnd.backend.notification.disasterMessage.DisasterAddressValidator;
// import com.dnd.backend.user.entity.Address;
//
// class DisasterAddressValidatorTest {
//
// 	@Test
// 	@DisplayName("1) 울산광역시 전체 vs 유저주소(울산광역시 남구 삼산동) -> True")
// 	void testUlsanAll() {
// 		// 유저 주소: 울산광역시 남구 삼산동
// 		Address userAddress = new Address();
// 		userAddress.setSido("울산광역시");
// 		userAddress.setSgg("남구");
// 		userAddress.setEmd("삼산동");
//
// 		// 재난문자 주소: "울산광역시 전체"
// 		String rawRegionStr = "울산광역시 전체";
//
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		assertTrue(canNotify, "같은 시도 + '전체' 이므로 TRUE");
// 	}
//
// 	@Test
// 	@DisplayName("2) 울산광역시 중구 vs 유저주소(울산광역시 남구 삼산동) -> False")
// 	void testUlsanMismatch() {
// 		Address userAddress = new Address();
// 		userAddress.setSido("울산광역시");
// 		userAddress.setSgg("남구");
//
// 		String rawRegionStr = "울산광역시 중구";
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		assertFalse(canNotify, "sgg가 중구 vs 남구 -> 불일치");
// 	}
//
// 	@Test
// 	@DisplayName("3) '경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,충청남도 전체 ,충청북도 전체 ,세종특별자치시 ' vs 유저주소(세종특별자치시 조치원읍)")
// 	void testMultipleAllSejong() {
// 		// 유저 주소: 세종특별자치시 (조치원읍)
// 		Address userAddress = new Address();
// 		userAddress.setSido("세종특별자치시");
// 		userAddress.setSgg("조치원읍");
//
// 		// 재난문자 주소(다중)
// 		String rawRegionStr = "경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,충청남도 전체 ,충청북도 전체 ,세종특별자치시";
//
// 		// parseMultiAddress 로는 6개 Address 로 파싱될 것이며,
// 		// 마지막 "세종특별자치시" => sido="세종특별자치시", sgg=null
// 		// => isMatched => sgg=null vs user sgg="조치원읍" => false, BUT
// 		// 이 경우 "sgg"가 없는데 "전체"도 아닌...
// 		// => 현재 로직상 sgg=null != "조치원읍" 이므로 False.
// 		// => 실제로 "세종특별자치시" 라면 굳이 sgg 없이 "전체"와 비슷한 의미로 봐야 할 수도 있음(정책).
// 		// => 필요하다면 "if sgg is null => treat as '전체'?" 등의 추가 로직이 필요.
//
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		// 현재 로직대로라면: false
// 		// => "세종특별자치시" == userAddress.sido("세종특별자치시") match "시도",
// 		//    but sgg=null != "조치원읍"
// 		// => "전체" 키워드가 아니므로 false.
// 		//
// 		// 만약 "시도만 같으면 알림" 정책을 원한다면, isMatched()를 수정해야 함.
// 		// 여기선 그대로 두고, false 로 확인.
// 		assertFalse(canNotify, "sgg가 없어서 매칭 실패(기본 로직). 정책에 따라 달라질 수 있음");
// 	}
//
// 	@Test
// 	@DisplayName("4) '부산광역시 전체' vs 유저주소(부산광역시 해운대구)")
// 	void testBusanAll() {
// 		Address userAddress = new Address();
// 		userAddress.setSido("부산광역시");
// 		userAddress.setSgg("해운대구");
//
// 		String rawRegionStr = "부산광역시 전체";
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		assertTrue(canNotify, "시도 동일 + '전체' => TRUE");
// 	}
//
// 	@Test
// 	@DisplayName("5) 복합: '경상남도 고성군 ,경상남도 진주시' vs 유저주소(경상남도 고성군)")
// 	void testMultiCommaMatch() {
// 		Address userAddress = new Address();
// 		userAddress.setSido("경상남도");
// 		userAddress.setSgg("고성군");
//
// 		String rawRegionStr = "경상남도 고성군 ,경상남도 진주시";
// 		// parseMultiAddress =>
// 		//   1) "경상남도 고성군" -> (sido="경상남도", sgg="고성군")
// 		//   2) "경상남도 진주시" -> (sido="경상남도", sgg="진주시")
// 		// user와 첫 번째가 매칭 -> TRUE
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		assertTrue(canNotify);
// 	}
//
// 	@Test
// 	@DisplayName("6) 괄호 예시: '임진강 수계지역(경기도 연천군,파주시)  ,경기도 임진강' vs 유저가 '경기도 파주시'")
// 	void testParentheses() {
// 		Address userAddress = new Address();
// 		userAddress.setSido("경기도");
// 		userAddress.setSgg("파주시");
//
// 		// 괄호 내 추가 파싱
// 		String rawRegionStr = "임진강 수계지역(경기도 연천군,파주시)  ,경기도 임진강";
//
// 		// parseMultiAddress steps:
// 		//   콤마 split =>
// 		//     1) "임진강 수계지역(경기도 연천군,파주시)"
// 		//     2) "경기도 임진강"
// 		//   첫 번째 => expandParentheses:
// 		//     outside="임진강 수계지역"
// 		//     inside="경기도 연천군,파주시"
// 		//        -> "경기도 연천군", "파주시"
// 		//     => 총 3개로 확장: ["임진강 수계지역", "경기도 연천군", "파주시"]
// 		//   각각 parseSingleAddress:
// 		//     - "임진강 수계지역" => tokens=["임진강","수계지역"] => sido="임진강", sgg="수계지역"
// 		//     - "경기도 연천군" => sido="경기도", sgg="연천군"
// 		//     - "파주시" => sido="파주시" (하나뿐이라 sido만 채워짐)
// 		//
// 		//   두 번째: "경기도 임진강"
// 		//     => sido="경기도", sgg="임진강"
// 		//
// 		// 최종 4개 Address가 생성됨:
// 		//   (1) sido="임진강", sgg="수계지역"
// 		//   (2) sido="경기도", sgg="연천군"
// 		//   (3) sido="파주시", sgg=null
// 		//   (4) sido="경기도", sgg="임진강"
// 		//
// 		// user: sido="경기도", sgg="파주시"
// 		// => none of them match under the default logic:
// 		//   - (2) sgg="연천군" != "파주시"
// 		//   - (3) sido="파주시" != "경기도" (sgg=null)
// 		//   - (4) sgg="임진강" != "파주시"
// 		//
// 		// => 결과: false (정확 일치 로직 상)
// 		//
// 		// 실제로 "파주시"가 시도인지, "경기도 파주시"가 되어야 하는지 등
// 		// 굉장히 복잡한 케이스.
// 		// 이대로면 NO,
// 		// 하지만 현실적으로는 "파주시"는 경기도의 시이므로
// 		//   sido="경기도", sgg="파주시" 라고 파싱하고 싶을 수 있음.
// 		// => 괄호 파싱의 세부 로직을 더 정교화해야 하거나,
// 		//    "파주시" 단독일 때 => sido="경기도", sgg="파주시"?
// 		//     이런 식의 규칙이 필요.
// 		// 여기서는 시연용으로 false 확인.
//
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress, rawRegionStr);
// 		assertFalse(canNotify, "현재 로직대로면 정확 매칭 실패 -> false");
// 	}
//
// 	@Test
// 	@DisplayName("7) 실제 예시: '경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,충청남도 전체 ,충청북도 전체 ,세종특별자치시' vs 유저주소(충청북도 청주시 흥덕구 복대동) -> True or False?")
// 	void testMultiAllChungbuk() {
// 		// 유저: 충청북도 청주시 흥덕구 복대동
// 		Address userAddress = new Address();
// 		userAddress.setSido("충청북도");
// 		userAddress.setSgg("청주시 흥덕구");
// 		userAddress.setEmd("복대동");
//
// 		// 재난문자: "경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,충청남도 전체 ,충청북도 전체 ,세종특별자치시"
// 		// parseMultiAddress => 6개 주소
// 		//   1) 경상남도 전체 -> sido="경상남도", sgg="전체"
// 		//   2) 전라남도 전체 -> sido="전라남도", sgg="전체"
// 		//   3) 전라북도 전체 -> sido="전라북도", sgg="전체"
// 		//   4) 충청남도 전체 -> sido="충청남도", sgg="전체"
// 		//   5) 충청북도 전체 -> sido="충청북도", sgg="전체"
// 		//   6) 세종특별자치시 -> sido="세종특별자치시"
// 		//
// 		// user: sido="충청북도", sgg="청주시 흥덕구"
// 		// 5)와 비교 -> 시도 일치(충청북도), sgg="전체" => match => true
// 		boolean canNotify = DisasterAddressValidator.canNotifyUser(userAddress,
// 			"경상남도 전체 ,전라남도 전체 ,전라북도 전체 ,충청남도 전체 ,충청북도 전체 ,세종특별자치시"
// 		);
// 		assertTrue(canNotify, "충청북도 + 전체 => 당연히 userAddress 포함");
// 	}
// }
