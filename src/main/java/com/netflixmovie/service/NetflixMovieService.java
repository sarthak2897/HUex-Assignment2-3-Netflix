package com.netflixmovie.service;


import com.netflixmovie.domain.NetflixMovie;

import java.util.List;
import java.util.Map;

public interface NetflixMovieService {

    List<NetflixMovie> findByCriteria(Map<String,String> params);

    NetflixMovie findMovieById(String id);

    NetflixMovie postNetflixMovie(NetflixMovie netflixMovie);

}
