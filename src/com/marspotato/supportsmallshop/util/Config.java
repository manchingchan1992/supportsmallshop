package com.marspotato.supportsmallshop.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {
	public final static int EARTH_RADIUS = 6371000; 
	
	public final static int HK_ISLAND = 1;
	public final static int KOWL0ON = 2;	
	public final static int NEW_TERRITORIES = 3;
	
	public final static String[] shopTypes = new String[]{"食肆", "零售（食物）","零售（其他）", "服務"}; 
	
	
	//static resource object
	public static DateTimeFormatter defaultDateTimeFormatter = ISODateTimeFormat.basicDateTimeNoMillis();
	public static Gson defaultGSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

}
