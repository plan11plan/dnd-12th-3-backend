package com.dnd.backend.user.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

// RestTemplate 직접 실행 예시
public class KakaoApiTest {
	private static final String KAKAO_COORD2ADDRESS_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
	private static final String KAKAO_REST_API_KEY = "ea43c7eae200179717c6a8f83f4974cc";

	public static void main(String[] args) {
		double lat = 37.715133;
		double lng = 126.734086;

		// x=경도(lng), y=위도(lat)
		String requestUrl = String.format("%s?x=%f&y=%f", KAKAO_COORD2ADDRESS_URL, lng, lat);

		// 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + KAKAO_REST_API_KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<JsonNode> response = restTemplate.exchange(
				requestUrl,
				HttpMethod.GET,
				entity,
				JsonNode.class
			);

			if (response.getStatusCode() == HttpStatus.OK) {
				JsonNode body = response.getBody();
				System.out.println("Kakao Response Body = " + body);

				if (body != null && body.has("documents") && body.get("documents").size() > 0) {
					JsonNode firstDoc = body.get("documents").get(0);

					// 도로명 주소
					JsonNode roadAddressNode = firstDoc.get("road_address");
					// 지번(주소) 정보
					JsonNode addressNode = firstDoc.get("address");

					System.out.println("roadAddressNode = " + roadAddressNode);
					System.out.println("addressNode = " + addressNode);

					if (addressNode != null && !addressNode.isNull()) {
						// 여기서 region_3depth_name이 대부분 "~~동" 형태의 값이 됩니다
						String dongName = addressNode.get("region_3depth_name").asText();
						System.out.println("▶ 행정동 (region_3depth_name) = " + dongName);
					}
				}
			} else {
				System.out.println("Request failed. Status code = " + response.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
