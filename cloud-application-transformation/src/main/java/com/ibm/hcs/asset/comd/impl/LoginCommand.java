package com.ibm.hcs.asset.comd.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class LoginCommand<T> extends AbstractCommand<T> {

	private static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);
	private final LoginRenderer loginRenderer;

	public LoginCommand(LoginRenderer loginRenderer) {
		this.loginRenderer = loginRenderer;
	}

	@ShellMethod(key = "cat", value = "Process cat command")
	public void cat(@ShellOption(value = "-u", help = "Username") String username,
			@ShellOption(value = "-p", help = "Password") String password) {

		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			execute();
		} else {
			logger.info("Invalid command format. Usage: cat -u <username> -p <password>");
		}
	}

	@Override
	public List<T> execute() {
		loginRenderer.render(null);
		return null;
	}
}
