package com.ibm.hcs.asset.comd.impl;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRenderer {
	
	private ConsolePrinter console;

	public <T> void render(List<T> list) {

	}

}
