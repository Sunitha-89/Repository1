package com.ibm.hcs.asset.comd.impl;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter {

	public void display(String message) {
		System.out.println(message);
	}
}
