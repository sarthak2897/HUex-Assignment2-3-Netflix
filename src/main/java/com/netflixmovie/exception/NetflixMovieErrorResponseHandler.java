package com.netflixmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NetflixMovieErrorResponseHandler{

    @ExceptionHandler
    public ResponseEntity<NetflixMovieErrorResponse> handleIdNotFoundException(NetflixIdNotFoundException exception){
        NetflixMovieErrorResponse error = new NetflixMovieErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<NetflixMovieErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException exception){
        NetflixMovieErrorResponse error = new NetflixMovieErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage("Unauthorized to fetch data!");
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<NetflixMovieErrorResponse> handleAllExceptions(Exception exception){
        NetflixMovieErrorResponse error = new NetflixMovieErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
