package com.netflixmovie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflixmovie.domain.NetflixMovie;
import com.netflixmovie.service.NetflixMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NetflixMovieControllerTest {

    @Mock
    private NetflixMovieService netflixMovieService;
    @InjectMocks
    private NetflixMovieController netflixMovieController;
    @Autowired
    private MockMvc mockMvc;
    NetflixMovie movie1;
    NetflixMovie movie2;

    @BeforeEach
    public void setUp(){
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
        mockMvc = MockMvcBuilders.standaloneSetup(netflixMovieController).build();
    }

    @Test
    @Disabled
    void login() {
    }

    @Test
    void findMovieById() throws Exception {
        Mockito.when(netflixMovieService.findMovieById(movie1.getShowId())).thenReturn(movie1);
        mockMvc.perform(MockMvcRequestBuilders.get("/tvshows/s1").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(movie1))).
                andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

   // @Test
//    void insertMovieData() throws Exception {
//        //MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));
//        Mockito.when(netflixMovieService.postNetflixMovie(any())).thenReturn(movie1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/tvshows").contentType(MediaType.APPLICATION_JSON).contentType(new ObjectMapper().writeValueAsString(movie1))).andExpect(MockMvcResultMatchers.status().isOk());
//        Mockito.verify(netflixMovieService,Mockito.times(1)).postNetflixMovie(any());
//    }

    @Test
    @Disabled
    void getMovieData() {
    }
}