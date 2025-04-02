package com.example.flexiMed.exceptions;

public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }

    public static class NoSuchUserExistsException extends RuntimeException{
        private String message;

        public NoSuchUserExistsException() {}

        public NoSuchUserExistsException(String msg) {
            super(msg);
            this.message = msg;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException{
        private String message;

        public UserAlreadyExistsException() {}

        public UserAlreadyExistsException(String msg) {
            super(msg);
            this.message = msg;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
