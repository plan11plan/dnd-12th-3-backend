package com.dnd.backend.domain.incident.service;

import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentLikeReadService {

	private final IncidentLikeRepository incidentLikeRepository;

	public int getLikeCount(Long incidentId) {
		return incidentLikeRepository.countByIncidentId(incidentId);
	}
}
