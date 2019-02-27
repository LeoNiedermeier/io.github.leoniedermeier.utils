package io.github.leoniedermeier.utils.web.advice;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorInformation {

    private HttpStatus status;
    private List<String> errroCodes;

    private String cid;

    public ErrorInformation() {
        super();

    }

    public List<String> getErrroCodes() {
        return this.errroCodes;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public void setErrroCodes(List<String> errroCodes) {
        this.errroCodes = errroCodes;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return this.cid;
    }
}