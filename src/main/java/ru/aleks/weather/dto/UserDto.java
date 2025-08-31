package ru.aleks.weather.dto;

public class UserDto {
    private String username;
    private String password;
    private String repeatPassword;

    public UserDto(String username, String password, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
