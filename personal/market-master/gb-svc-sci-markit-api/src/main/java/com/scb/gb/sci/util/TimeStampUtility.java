package com.scb.gb.sci.util;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class TimeStampUtility {
	
	public String previousDayDate() {
		
		LocalDate localDate = LocalDate.now();
		String previousDate = localDate.minusDays(1).toString();
		
		return previousDate;
	}
	
	public String previousWeekDate() {
		
		LocalDate localDate = LocalDate.now();
		String previousWeek = localDate.minusDays(7).toString();
		
		return previousWeek;
	}

}
