package io.github.leoniedermeier.utils.excecption;

public class StringErrorCode implements ErrorCode {

    private final String errorCode;

    public StringErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String code() {
        return errorCode;
    }

}
