// package com.dnd.backend;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// import com.dnd.backend.notification.disasterMessage.AddressParser;
// import com.dnd.backend.user.entity.Address;
//
// class AddressParserTest {
//
// 	@Test
// 	@DisplayName("단일 주소 파싱 테스트 - 1개 토큰 (세종특별자치시)")
// 	void testSingleAddress_OneToken() {
// 		String raw = "세종특별자치시";
// 		Address address = AddressParser.parseSingleAddress(raw);
// 		System.out.println(address);
//
// 		assertNotNull(address, "파싱 결과는 null이 아니다.");
// 		assertEquals(raw, address.getAddressName());
// 		assertEquals("세종특별자치시", address.getSido());
// 		assertNull(address.getSgg(), "시군구는 지정되지 않아야 함");
// 		assertNull(address.getEmd(), "읍면동은 지정되지 않아야 함");
// 	}
//
// 	@Test
// 	@DisplayName("단일 주소 파싱 테스트 - 2개 토큰 (울산광역시 전체)")
// 	void testSingleAddress_TwoTokens() {
// 		String raw = "울산광역시 전체";
// 		Address address = AddressParser.parseSingleAddress(raw);
//
// 		System.out.println(address);
// 		assertNotNull(address);
// 		assertEquals("울산광역시 전체", address.getAddressName());
// 		assertEquals("울산광역시", address.getSido());
// 		assertEquals("전체", address.getSgg());
// 		assertNull(address.getEmd());
// 	}
//
// 	@Test
// 	@DisplayName("단일 주소 파싱 테스트 - 3개 토큰 (울산광역시 울주군 삼남면)")
// 	void testSingleAddress_ThreeTokens() {
// 		String raw = "울산광역시 울주군 삼남면";
// 		Address address = AddressParser.parseSingleAddress(raw);
//
// 		System.out.println(address);
// 		assertNotNull(address);
// 		assertEquals("울산광역시 울주군 삼남면", address.getAddressName());
// 		assertEquals("울산광역시", address.getSido());
// 		assertEquals("울주군", address.getSgg());
// 		assertEquals("삼남면", address.getEmd());
// 	}
//
// 	@Test
// 	@DisplayName("단일 주소 파싱 테스트 - 4개 토큰 (경기도 안산시 단원구 풍도동)")
// 	void testSingleAddress_FourTokens() {
// 		String raw = "경기도 안산시 단원구 풍도동";
// 		Address address = AddressParser.parseSingleAddress(raw);
// 		System.out.println(address);
// 		assertNotNull(address);
// 		assertEquals("경기도 안산시 단원구 풍도동", address.getAddressName());
// 		assertEquals("경기도", address.getSido());
// 		assertEquals("안산시 단원구", address.getSgg()); // 2번째 + 3번째 토큰 연결
// 		assertEquals("풍도동", address.getEmd());
// 	}
//
// 	@Test
// 	@DisplayName("단일 주소 파싱 테스트 - 5개 토큰 이상 (충청북도 청주시 상당구 율량동 무슨리)")
// 	void testSingleAddress_MoreTokens() {
// 		String raw = "충청북도 청주시 상당구 율량동 무슨리";
// 		Address address = AddressParser.parseSingleAddress(raw);
//
// 		System.out.println(address);
// 		assertNotNull(address);
// 		assertEquals("충청북도 청주시 상당구 율량동 무슨리", address.getAddressName());
// 		assertEquals("충청북도", address.getSido());
// 		// 중간 토큰("청주시 상당구 율량동")을 sgg로, 마지막("무슨리")를 emd로 할당
// 		assertEquals("청주시 상당구 율량동", address.getSgg());
// 		assertEquals("무슨리", address.getEmd());
// 	}
//
// 	@Test
// 	@DisplayName("쉼표로 구분된 여러 주소 파싱 테스트 (강원특별자치도 원주시, 강원특별자치도 홍천군 등)")
// 	void testMultipleAddressParsing() {
// 		// 원본 예시 중 하나
// 		String rawMultiple = "강원특별자치도 원주시 ,강원특별자치도 홍천군 ,강원특별자치도 횡성군";
//
// 		// 콤마(,) 기준으로 split
// 		String[] splitted = rawMultiple.split("\\s*,\\s*");
//
// 		assertEquals(3, splitted.length);
//
// 		Address addr1 = AddressParser.parseSingleAddress(splitted[0]);
// 		Address addr2 = AddressParser.parseSingleAddress(splitted[1]);
// 		Address addr3 = AddressParser.parseSingleAddress(splitted[2]);
//
// 		System.out.println(addr1);
// 		System.out.println(addr2);
// 		System.out.println(addr3);
// 		// 첫 번째 주소
// 		assertNotNull(addr1);
// 		assertEquals("강원특별자치도 원주시", addr1.getAddressName());
// 		assertEquals("강원특별자치도", addr1.getSido());
// 		assertEquals("원주시", addr1.getSgg());
// 		assertNull(addr1.getEmd());
//
// 		// 두 번째 주소
// 		assertNotNull(addr2);
// 		assertEquals("강원특별자치도 홍천군", addr2.getAddressName());
// 		assertEquals("강원특별자치도", addr2.getSido());
// 		assertEquals("홍천군", addr2.getSgg());
// 		assertNull(addr2.getEmd());
//
// 		// 세 번째 주소
// 		assertNotNull(addr3);
// 		assertEquals("강원특별자치도 횡성군", addr3.getAddressName());
// 		assertEquals("강원특별자치도", addr3.getSido());
// 		assertEquals("횡성군", addr3.getSgg());
// 		assertNull(addr3.getEmd());
// 	}
// }
