package com.netflixmovie.controller;

import com.netflixmovie.domain.Main;
import com.netflixmovie.domain.NetflixMovie;
import com.netflixmovie.exception.UnauthorizedAccessException;
import com.netflixmovie.service.NetflixMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class NetflixMovieController {
    private static Logger logger = LoggerFactory.getLogger(NetflixMovieController.class);
    File csvFile = new File("C:\\Users\\hp\\Downloads\\netflix.csv");
    List<NetflixMovie> netflixMoviesList = Main.convertCSVToList(csvFile);

    @Autowired
    HttpServletRequest httpServletRequest;
    String token = UUID.randomUUID().toString();

    @Autowired
    private NetflixMovieService netflixMovieService;

    @PostMapping(path="/login")
    public ResponseEntity<NetflixMovie> login(@RequestParam("username") String username,@RequestParam("password") String password){
    HttpHeaders headers = new HttpHeaders();
            headers.add("X-AUTH-TOKEN", token);
            logger.info("Logged in! Now you can access the APIs!");
        return new ResponseEntity<NetflixMovie>(headers,HttpStatus.OK) ;
    }

    @GetMapping(path = "/tvshows/{id}")
    public ResponseEntity<NetflixMovie> findMovieById(@PathVariable String id){
        logger.info("Fetching Netflix Movie/TV Show by ID");
        NetflixMovie movie = netflixMovieService.findMovieById(id);
        return new ResponseEntity<>(movie,HttpStatus.OK);
    }

    @PostMapping("/tvshows")
    public ResponseEntity<NetflixMovie> insertMovieData(@RequestBody NetflixMovie netflixMovie,@RequestParam String datasource) throws Exception {
        //Sending JSON data for Netflix movies/ TV Shows to insert into the database
        if(datasource.equalsIgnoreCase("db")) {
            logger.info("Inserting data into db.");
            NetflixMovie movie = netflixMovieService.insertNetflixMovieToDB(netflixMovie);
            return new ResponseEntity<>(movie, HttpStatus.CREATED);
        }
        else{
            //Sending JSON data for Netflix movies/ TV Shows to insert into the csv file
            logger.info("Inserting data into CSV file.");
            NetflixMovie movie = netflixMovieService.insertNetflixMovieToCSV(netflixMovie);
            return new ResponseEntity<>(movie,HttpStatus.CREATED);
        }
    }

    @GetMapping(path="/tvshows")
    public ResponseEntity<List<NetflixMovie>> getMovieData(@RequestParam Map<String,String> params){
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        //A check if the X-AUTH-TOKEN is present or not before accessing APIs
        if (httpServletRequest.getHeader("X-AUTH-TOKEN") == null || !httpServletRequest.getHeader("X-AUTH-TOKEN").equals(token)) {
            logger.error("Unauthorized to fetch data!");
            throw new UnauthorizedAccessException();
            //return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else{
            //Data fetched for the database
            if(params.containsKey("db")) {
                logger.info("Fetching the data from database.");
                List<NetflixMovie> resultList = netflixMovieService.findByCriteria(params);
                headers.add("X-TIME-TO-EXECUTE", (System.currentTimeMillis() - start) + " millis");
                return new ResponseEntity<>(resultList, headers, HttpStatus.OK);
            }
            else{
                //Data fetched from the csv file
                logger.info("Fetching the data from the csv file.");
                return getTvShows(params);
            }
        }
    }


    // Scenario when the netflix_title.csv file is read and output is displayed through functional programming
    public ResponseEntity<List<NetflixMovie>> getTvShows(@RequestParam Map<String,String> params) {
        Stream<NetflixMovie> stream = netflixMoviesList.stream();
        List<NetflixMovie> aggregateList = null;
       //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yy");
        long start = System.currentTimeMillis();
        //Check if the X-AUTH-TOKEN is there in the request header before accessing the APIs
            if (params.get("startDate") != null && params.get("endDate") != null) {
                LocalDate startDate = LocalDate.parse(params.get("startDate"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate endDate = LocalDate.parse(params.get("endDate"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                stream = stream.filter(movie -> movie.getDateAdded() != null && movie.getDateAdded().isAfter(startDate) && movie.getDateAdded().isBefore(endDate));
            }
            if (params.get("movieType") != null) {
                String movieType = params.get("movieType");
                stream = stream.filter(netflixMovie -> netflixMovie.getListedIn() != null && netflixMovie.getListedIn().toLowerCase().contains(movieType.toLowerCase()));
            }
            if (params.get("country") != null) {
                String country = params.get("country");
                stream = stream.filter(netflixMovie -> netflixMovie.getCountry().toLowerCase().equals(country.toLowerCase()));
            }
            if (params.get("count") != null) {
                stream = stream.limit(Integer.parseInt(params.get("count")));
            }
            aggregateList = stream.collect(Collectors.toList());
            HttpHeaders headers = new HttpHeaders();
            //Adding a header to response depicting time taken to execute the query
            headers.add("X-TIME-TO-EXECUTE", (System.currentTimeMillis() - start) + " millis");

            return new ResponseEntity<>(aggregateList, headers, HttpStatus.OK);

    }
}
