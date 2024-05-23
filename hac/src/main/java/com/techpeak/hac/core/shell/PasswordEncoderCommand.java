package com.techpeak.hac.core.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PasswordEncoderCommand {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderCommand() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @ShellMethod(key="encodePassword")
    public String encodePassword(@ShellOption String password) {
        return passwordEncoder.encode(password);
    }
}
