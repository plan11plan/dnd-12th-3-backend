package com.dnd.backend.incident.service;

public interface GeocodingService {
	String getRoadNameAddress(double pointX, double pointY);
}
