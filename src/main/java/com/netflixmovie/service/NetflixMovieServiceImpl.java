package com.netflixmovie.service;

import com.netflixmovie.dao.NetflixMovieRepository;
import com.netflixmovie.domain.NetflixMovie;
import com.netflixmovie.exception.NetflixIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NetflixMovieServiceImpl implements NetflixMovieService{

    private NetflixMovieRepository netflixMovieRepository;

    @Autowired
    public NetflixMovieServiceImpl(NetflixMovieRepository netflixMovieRepository){
        this.netflixMovieRepository = netflixMovieRepository;
    }

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
    public NetflixMovie postNetflixMovie(NetflixMovie netflixMovie){
        long movieCount = netflixMovieRepository.count();
        //Setting up show ID
        netflixMovie.setShowId("s"+(movieCount+1));
        NetflixMovie movie = netflixMovieRepository.save(netflixMovie);
        return movie;
    }

}
