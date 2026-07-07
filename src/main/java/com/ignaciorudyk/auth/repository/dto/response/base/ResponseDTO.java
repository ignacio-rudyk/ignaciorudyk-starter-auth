package com.ignaciorudyk.auth.repository.dto.response.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO {

    @JsonProperty("metadata")
    private MetadataDTO metadata;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("errorList")
    private List<ErrorDTO> errorList;

    public ResponseDTO() {
        this.metadata = null;
        this.data = null;
        this.errorList = new ArrayList<>();
    }

    public ResponseDTO(MetadataDTO metadata, Object data, List<ErrorDTO> errorList) {
        this.metadata = metadata;
        this.data = data;
        this.errorList = errorList;
    }

    public MetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDTO metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<ErrorDTO> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ErrorDTO> errorList) {
        this.errorList = errorList;
    }

}