package com.netflixmovie.service;


import com.netflixmovie.domain.NetflixMovie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NetflixMovieService {

    List<NetflixMovie> findByCriteria(Map<String,String> params);

    NetflixMovie findMovieById(String id);

    NetflixMovie insertNetflixMovieToDB(NetflixMovie netflixMovie);

    NetflixMovie insertNetflixMovieToCSV(NetflixMovie netflixMovie) throws IOException, Exception;

}
