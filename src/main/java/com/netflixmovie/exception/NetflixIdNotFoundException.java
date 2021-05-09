package com.netflixmovie.exception;

public class NetflixIdNotFoundException extends RuntimeException {

    public NetflixIdNotFoundException(String message) {
        super(message);
    }

    public NetflixIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetflixIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
