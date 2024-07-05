package com.ibm.hcs.asset.comd.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LoginRenderer {

	private static final Logger logger = LoggerFactory.getLogger(LoginRenderer.class);
	private ConsolePrinter console;

	public LoginRenderer(ConsolePrinter console) {
		this.console = console;
	}

	public <T> void render(List<T> list) {
		Resource resource = new ClassPathResource("banner.txt");
		try {
			if (!resource.exists()) {
				logger.error("banner.txt not found");
				console.display("Error: banner.txt not found");
			}

			URI uri = resource.getURI();
			Path path = Paths.get(uri);
			List<String> lines = Files.readAllLines(path);
			console.display(lines.toString());
		} catch (IOException e) {
			logger.error("Error reading banner.txt", e);
			console.display("Error reading banner.txt: " + e.getMessage());
		}
	}

}
