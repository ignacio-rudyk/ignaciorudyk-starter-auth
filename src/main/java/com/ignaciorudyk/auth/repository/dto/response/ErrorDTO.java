package com.ignaciorudyk.auth.repository.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ErrorDTO {

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public ErrorDTO(int code, String msg, LocalDateTime timestamp) {
        this.code = code;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public ErrorDTO(int code, String msg) {
        this(code, msg, LocalDateTime.now());
    }

    public ErrorDTO() {
        this(0, null, LocalDateTime.now());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}