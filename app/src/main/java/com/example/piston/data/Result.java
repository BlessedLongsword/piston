package com.example.piston.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, UsernameError)
    private Result() {
    }

    @Override
    public String toString() {
        if (this instanceof com.example.piston.data.Result.Success) {
            com.example.piston.data.Result.Success<T> success = (com.example.piston.data.Result.Success<T>) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof com.example.piston.data.Result.Error) {
            com.example.piston.data.Result.Error error = (com.example.piston.data.Result.Error) this;
            return "UsernameError[exception=" + error.getError().getMessage() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends com.example.piston.data.Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // UsernameError sub-class
    public final static class Error extends com.example.piston.data.Result {
        private final Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}