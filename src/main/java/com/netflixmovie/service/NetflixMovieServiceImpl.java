package com.netflixmovie.service;

import com.netflixmovie.dao.NetflixMovieRepository;
import com.netflixmovie.domain.Main;
import com.netflixmovie.domain.NetflixMovie;
import com.netflixmovie.exception.NetflixIdNotFoundException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetflixMovieServiceImpl implements NetflixMovieService{

    private NetflixMovieRepository netflixMovieRepository;
    private static Logger logger = LoggerFactory.getLogger(NetflixMovieServiceImpl.class);
    File file = new File("./src/main/resources/netflix_titles1.csv");
    @Autowired
    public NetflixMovieServiceImpl(NetflixMovieRepository netflixMovieRepository){
        this.netflixMovieRepository = netflixMovieRepository;
    }
    public NetflixMovieServiceImpl(){}

    // Single method to handle all get requests concnerned with request parameters using Criteria API JPA 2.0
    public List<NetflixMovie> findByCriteria(Map<String,String> params) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<NetflixMovie> resultList = netflixMovieRepository.findAll((Specification<NetflixMovie>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.get("startDate") != null && params.get("endDate") != null) {
                System.out.println("Dates detected " + params.get("startDate"));
                LocalDate startDate = LocalDate.parse(params.get("startDate"), dtf);
                LocalDate endDate = LocalDate.parse(params.get("endDate"), dtf);
                System.out.println(startDate + " " + endDate);

                Predicate date = criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("dateAdded")), criteriaBuilder.between(root.get("dateAdded"), startDate, endDate));
                predicates.add(date);
            }
            if (params.get("movieType") != null) {
                String movieType = params.get("movieType");
                Predicate movieFilter = criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("listedIn")), criteriaBuilder.like(root.get("listedIn"), "%" + movieType + "%"));

                predicates.add(movieFilter);
            }

            if (params.get("country") != null) {
                String country = params.get("country");
                Predicate countryFilter = criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("country")), criteriaBuilder.equal(root.get("country"), country));
                predicates.add(countryFilter);
            }
            Predicate[] predicates1 = predicates.toArray(new Predicate[predicates.size()]);
            return criteriaBuilder.and(predicates1);

        });
        if(params.get("count") != null){
            resultList = resultList.stream().limit(Integer.parseInt(params.get("count"))).collect(Collectors.toList());
        }
        return resultList;
    }

    //Fetching movie/TV Show details by ID
    @Override
    public NetflixMovie findMovieById(String id) {
        Optional<NetflixMovie> netflixMovie = netflixMovieRepository.findById(id);
        netflixMovie.orElseThrow(() -> new NetflixIdNotFoundException("Entered ID "+id+" not found!"));
        return netflixMovie.get();
    }

    // Inserting new movie to the database
    @Override
    public NetflixMovie insertNetflixMovieToDB(NetflixMovie netflixMovie){
        logger.info(netflixMovie.toString());
        long movieCount = netflixMovieRepository.count();
        //Setting up show ID
        netflixMovie.setShowId("s"+(movieCount+1));
        NetflixMovie movie = netflixMovieRepository.save(netflixMovie);
        return movie;
    }

    //Inserting new movie to CSV file
    @Override
    public NetflixMovie insertNetflixMovieToCSV(NetflixMovie netflixMovie) throws Exception {
        //File file = new File("C:\\Users\\sartnagpal\\Downloads\\netflix_titles.csv");

        netflixMovie.setShowId("s"+(noOfRecords(file)+1));
        FileWriter csvWriter = new FileWriter(file,true);
        csvWriter.append(netflixMovie.getShowId()==null ? "" : netflixMovie.getShowId());
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getType()==null ? "" : netflixMovie.getType());
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getTitle()==null ? "" : netflixMovie.getTitle());
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getDirector()==null ? "" : "\""+netflixMovie.getDirector()+"\"");
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getCast()==null ? "" : "\""+netflixMovie.getCast()+"\"");
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getCountry()==null ? "" : "\""+netflixMovie.getCountry()+"\"");
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getDateAdded()==null ? "" : netflixMovie.getDateAdded().format(DateTimeFormatter.ofPattern("dd-MMM-yy")));
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getReleaseYear() == null ? "" : String.valueOf(netflixMovie.getReleaseYear()));
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getRating()==null ? "" : netflixMovie.getRating());
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getDuration()==null ? "" : netflixMovie.getDuration());
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getListedIn()==null ? "" : "\""+netflixMovie.getListedIn()+"\"");
        csvWriter.append(",");
        csvWriter.append(netflixMovie.getDescription()==null ? " " : "\""+netflixMovie.getDescription()+"\"");
        csvWriter.append("\n");
        csvWriter.close();
        logger.info("CSV file updated successfully!");
        return netflixMovie;
    }
    private int noOfRecords(File file) throws  Exception{

        FileReader fileReader = new FileReader(file);
        List<String[]> csvData = new CSVReaderBuilder(fileReader).withSkipLines(1).build().readAll();
        logger.info("No of records in csv file: "+csvData.size());
        return csvData.size();
    }

    //Adding cron job to sync any extra content in CSV file to DB
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncDBWithCSV() throws Exception {
        logger.info("Running cron job at "+ LocalDate.now());
        int csvRecords = noOfRecords(file);
        int dbRecords = (int) netflixMovieRepository.count();
        //int dbRecords = 19;
        if(csvRecords <= dbRecords)
            logger.info("Database is in sync with CSV file.");
        else{
            FileReader fileReader = new FileReader(file);
            List<String[]> csvData = new CSVReaderBuilder(fileReader).withSkipLines(dbRecords+1).build().readAll();
          //  csvData.stream().forEach(data -> Arrays.asList(data).forEach(System.out::println));
            csvData.stream().forEach(records -> {
                NetflixMovie movie = new NetflixMovie();
                movie.setShowId(records[0]);
                movie.setType(records[1]);
                movie.setTitle(records[2]);
                movie.setDirector(records[3]);
                movie.setCast(records[4]);
                movie.setCountry(records[5]);
                movie.setDateAdded(Main.dateFormatter(records[6]));
                movie.setReleaseYear(Integer.parseInt(records[7]));
                movie.setRating(records[8]);
                movie.setDuration(records[9]);
                movie.setListedIn(records[10]);
                movie.setDescription(records[11]);
                //insertNetflixMovieToDB(movie);
                netflixMovieRepository.save(movie);
                logger.info("Movie with ID "+movie.getShowId()+" synced to DB.");
            });
            }

    }
   /* public static void main(String args[]) throws Exception {
        NetflixMovieServiceImpl obj = new NetflixMovieServiceImpl();
        obj.syncDBWithCSV();
    }*/
}
