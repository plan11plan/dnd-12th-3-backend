// package com.dnd.backend.notification.disasterMessage;
//
// import java.util.List;
// import java.util.Optional;
//
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;
//
// @Repository
// public interface DisasterMessageRepository extends JpaRepository<DisasterMessage, Long> {
//
// 	// 이미 저장된 SN인지 확인할 수 있도록
// 	Optional<DisasterMessage> findBySn(Long sn);
//
// 	// 중복되지 않은 지역명 목록 조회 (기존 메서드)
// 	@Query("SELECT DISTINCT dm.regionName FROM DisasterMessage dm")
// 	List<String> findDistinctRegionNames();
//
// 	// SN의 최대값 조회
// 	@Query("SELECT COALESCE(MAX(dm.sn), 0) FROM DisasterMessage dm")
// 	Long findMaxSn();
//
// }
