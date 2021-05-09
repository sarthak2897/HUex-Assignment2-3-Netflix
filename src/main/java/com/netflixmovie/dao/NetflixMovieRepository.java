package com.netflixmovie.dao;

import com.netflixmovie.domain.NetflixMovie;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetflixMovieRepository extends CrudRepository<NetflixMovie, String>
        , JpaSpecificationExecutor<NetflixMovie> {


}
