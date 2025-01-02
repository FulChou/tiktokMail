package org.edu.commom.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    private Result() {
    }

    private Result(Result<T> result) {
        this.code = result.code;
        this.msg = result.msg;
        this.data = result.data;
    }

    public static class Builder<T> {
        private Result<T> result;

        public Builder() {
            result = new Result<>();
        }

        public Builder code(int code) {
            result.code = code;
            return this;
        }

        public Builder msg(String msg) {
            result.msg = msg;
            return this;
        }

        public Builder data(T data) {
            result.data = data;
            return this;
        }

        public Result<T> build() {
            return new Result<>(result);
        }
    }

}
