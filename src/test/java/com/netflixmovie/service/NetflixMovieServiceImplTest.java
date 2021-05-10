package com.netflixmovie.service;

import com.netflixmovie.dao.NetflixMovieRepository;
import com.netflixmovie.domain.NetflixMovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NetflixMovieServiceImplTest {

    @Mock
    private NetflixMovieRepository netflixMovieRepository;
    @Autowired
    @InjectMocks
    private NetflixMovieServiceImpl netflixMovieService;
    NetflixMovie movie1;
    NetflixMovie movie2;
    List<NetflixMovie> list = new ArrayList<>();

    @BeforeEach
    void setUp(){
        //MockitoAnnotations.openMocks(this);
        movie1 = new NetflixMovie();
        movie1.setShowId("s1");
        movie1.setType("TV Show");
        movie1.setTitle("Altered Carbon");
        movie1.setCast("John Kinnaman");
        movie1.setCountry("USA");
        movie1.setDuration("2 seasons");
        movie1.setListedIn("Sci-fi,Action");
        movie1.setReleaseYear(2018);

        movie2 = new NetflixMovie();
        movie2.setShowId("s2");
        movie2.setType("Movie");
        movie2.setTitle("Tenet");
        movie2.setCast("Robert Pattinson");
        movie2.setCountry("USA");
        movie2.setDuration("150 min");
        movie2.setListedIn("Sci-fi,Action");
        movie2.setReleaseYear(2020);
        list.add(movie1);
        list.add(movie2);

    }

//    @Test
//    @Disabled
//    void findByCriteria() {
//        //Mockito.when(netflixMovieRepository.findAll()).thenReturn(list);
//        Map<String,String> map = new HashMap<>();
//        map.put("movieType","Action");
//        assertEquals(netflixMovieService.findByCriteria(map),list);
//    }

    @Test
    void findMovieById() {
        Mockito.when(netflixMovieRepository.findById("s2")).thenReturn(Optional.ofNullable(movie2));
        assertEquals(netflixMovieService.findMovieById(movie2.getShowId()), movie2);
    }



    @Test
    void postNetflixMovie() {
        Mockito.when(netflixMovieRepository.save(any())).thenReturn(movie1);
        netflixMovieService.insertNetflixMovieToDB(movie1);
        Mockito.verify(netflixMovieRepository,Mockito.times(1)).save(any());
    }
}