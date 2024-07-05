package com.ibm.hcs.asset.comd.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProjectRenderer {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRenderer.class);
	private ConsolePrinter console;

	public ProjectRenderer(ConsolePrinter console) {
		this.console = console;
	}

	public <T> void render(List<T> list) {

		// This method normalizes a path to a standard format.
		String baseDirectory = (String) list.get(0);
		String normalizedPath = FilenameUtils.normalizeNoEndSeparator(baseDirectory);
		if (normalizedPath == null) {
			throw new InvalidPathException(baseDirectory, "Invalid path format");
		}

		Path path = Paths.get(normalizedPath).toAbsolutePath();
		// Check if the path exists
		if (!Files.exists(path)) {
			throw new InvalidPathException(normalizedPath, "Path does not exist");
		}

		try {
			System.out.println("Scanning directory: " + path);

			List<String> directories = findProjectFolders(path);

			// Print directories in the specified format
			IntStream.range(0, directories.size()).forEach(
					idx -> System.out.println((idx + 1) + ". " + directories.get(idx)));

		} catch (Exception e) {
			System.out.println("Error scanning directory: " + e.getMessage());
		}
	}

	private List<String> findProjectFolders(Path directoryPath) {
		try {
			if (!Files.isDirectory(directoryPath)) {
				logger.warn("Provided path is not a directory: {}", directoryPath);
				return Collections.emptyList();
			}

			try (Stream<Path> paths = Files.walk(directoryPath, 2)) {
				List<String> projectFolders = paths.filter(Files::isDirectory)
						.filter(path -> !path.equals(directoryPath)) // Exclude the root directory itself
						.filter(this::isMavenProject).map(path -> path.getFileName().toString())
						.collect(Collectors.toList());

				if (!projectFolders.isEmpty()) {
					logger.info("Maven project folders found: {}", projectFolders);
					return projectFolders;
				} else {
					logger.warn("No Maven project folders found in directory: {}", directoryPath);
					return Collections.emptyList();
				}
			} catch (IOException e) {
				logger.error("Error while searching for project folders.", e);
				return Collections.emptyList();
			}
		} catch (InvalidPathException e) {
			logger.error("Invalid path provided: {}", e.getMessage());
			return Collections.emptyList();
		}
	}

	private boolean isMavenProject(Path directoryPath) {
		if (!Files.isDirectory(directoryPath)) {
			logger.warn("Provided path is not a directory: {}", directoryPath);
			return false;
		}

		Path pomFilePath = directoryPath.resolve("pom.xml");
		if (!Files.exists(pomFilePath)) {
			// logger.warn("pom.xml not found in directory: {}", directoryPath);
			return false;
		}

		Path srcMainJava = directoryPath.resolve("src").resolve("main").resolve("java");
		Path srcMainResources = directoryPath.resolve("src").resolve("main").resolve("resources");
		Path srcTestJava = directoryPath.resolve("src").resolve("test").resolve("java");

		boolean hasMavenStructure = Files.isDirectory(srcMainJava) && Files.isDirectory(srcMainResources)
				&& Files.isDirectory(srcTestJava);

		if (hasMavenStructure) {
			logger.info("Valid Maven project found at: {}", directoryPath);
		} else {
			logger.warn("Directory does not follow Maven structure: {}", directoryPath);
		}

		return hasMavenStructure;
	}
}
