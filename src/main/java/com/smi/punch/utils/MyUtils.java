package com.smi.punch.utils;

public class MyUtils {
	public static String filterCRLF(String input) {
		return input.replaceAll("\r\n", "");
	}
}
