package com.ibm.hcs.asset.comd.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ScanCommand<T> extends AbstractCommand<T> {

	private static final Logger logger = LoggerFactory.getLogger(ScanCommand.class);

	private final ProjectRenderer projectRenderer;
	private String baseDirectoryPath;

	public ScanCommand(ProjectRenderer projectRenderer) {
		this.projectRenderer = projectRenderer;
	}

	@ShellMethod(key = "scan", value = "scan for valid project directories")
	public void scanBaseDirectory(@ShellOption(value = "--basedir", help = "Base Directory") String baseDirectory) {

		if (baseDirectory == null || baseDirectory.trim().isEmpty()) {
			throw new NullPointerException("Path cannot be null or empty");
		}

		if (null != baseDirectory && !baseDirectory.trim().isEmpty()) {
			baseDirectoryPath = baseDirectory;
			execute();
		} else {
			logger.info("Invalid command format. Usage: scan --basedir <path>");
		}
	}

	@Override
	public List<T> execute() {
		projectRenderer.render(Arrays.asList(baseDirectoryPath));
		return null;
	}
}