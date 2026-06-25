package com.ignaciorudyk.auth.repository.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetadataDTO {

    @JsonProperty("path")
    private String path;

    @JsonProperty("method")
    private String method;

    @JsonProperty("code")
    private int code;

    public MetadataDTO(String path, String method, int code) {
        this.path = path;
        this.method = method;
        this.code = code;
    }

    public MetadataDTO() {
        this(null, null, 0);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}