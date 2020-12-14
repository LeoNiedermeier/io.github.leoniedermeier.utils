package io.github.leoniedermeier.utils.web.advice;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorInformation {

    private String cid;
    private List<String> errroCodes;

    private HttpStatus status;

    public ErrorInformation() {
        super();

    }

    public String getCid() {
        return cid;
    }

    public List<String> getErrroCodes() {
        return errroCodes;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setErrroCodes(List<String> errroCodes) {
        this.errroCodes = errroCodes;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}