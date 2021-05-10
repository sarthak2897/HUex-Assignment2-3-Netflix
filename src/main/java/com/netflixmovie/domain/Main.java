package com.netflixmovie.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yy");
    //public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        File csvFile = new File("C:\\Users\\hp\\Downloads\\netflix_title.csv");
        Scanner sc = new Scanner(System.in);
        LocalDate  startDate = LocalDate.parse(LocalDate.parse(args[0],dtf).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        LocalDate  endDate = LocalDate.parse(LocalDate.parse(args[1],dtf).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(startDate+" "+endDate);
        if(csvFile.exists() && csvFile.isFile()){
            List<NetflixMovie> netflixMovies = convertCSVToList(csvFile);
            logger.info("File read! Enter the number of records (n): ");
            int n = sc.nextInt();

            //TV Shows List
            long start = System.currentTimeMillis();
            List<NetflixMovie> movieTypeList = netflixMovies.stream().filter(movie -> movie.getDateAdded()!= null && movie.getDateAdded().isAfter(startDate) && movie.getDateAdded().isBefore(endDate))
                    .filter(movie -> movie.getType().equals("TV Show")).limit(n).collect(Collectors.toList());
            long end = System.currentTimeMillis();
            logger.info(n+" records with type : TV Show");
            logger.info(movieTypeList.toString());
            logger.info("Time taken : "+(end-start)+" milli seconds!");

            // Horror Movies List
            start = System.currentTimeMillis();
            List<NetflixMovie> listingList = netflixMovies.stream().filter(movie -> movie.getDateAdded()!= null && movie.getDateAdded().isAfter(startDate) && movie.getDateAdded().isBefore(endDate))
            .filter(movie -> movie.getListedIn().contains("Horror Movies")).limit(n).collect(Collectors.toList());
            end=System.currentTimeMillis();
            logger.info(n+" records with listing as Horror Movies");
            logger.info(listingList.toString());
            logger.info("Time taken : "+(end-start)+" milli seconds!");

            //Indian Movies List
            start= System.currentTimeMillis();
            List<NetflixMovie> indianMoviesList = netflixMovies.stream().filter(movie -> movie.getDateAdded()!= null && movie.getDateAdded().isAfter(startDate) && movie.getDateAdded().isBefore(endDate))
                    .filter(movie -> movie.getType().equals("Movie") && movie.getCountry().equals("India")).limit(n).collect(Collectors.toList());
            end=System.currentTimeMillis();
            logger.info(n+" records showing Indian Movies");
            logger.info(indianMoviesList.toString());
            logger.info("Time taken : "+(end-start)+" milli seconds!");

            //list.stream().map(movie -> movie.getDateAdded()).sorted((m1,m2) -> m1.compareTo(m2)).forEach(System.out::println);

        }
        else{
            //logger.error("Can't read the file!");
            logger.log(Level.parse("error"),"Can't read the file!");
        }
    }

    public static List convertCSVToList(File csvFile) {
        try{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
        String row = bufferedReader.readLine();
            List<NetflixMovie> moviesList = new ArrayList<NetflixMovie>();
        if(row.length() == 0 || row == null)
            throw new Exception("File empty");
        while((row = bufferedReader.readLine()) != null){
            String cells[] = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            //System.out.println(Arrays.asList(cells));

            NetflixMovie movie = new NetflixMovie();
            movie.setShowId(cells[0]);
            movie.setType(cells[1]);
            movie.setTitle(cells[2]);
            movie.setDirector(cells[3]);
            movie.setCast(cells[4]);
            movie.setCountry(cells[5]);
            movie.setDateAdded(dateFormatter(cells[6]));
            movie.setReleaseYear(Integer.parseInt(cells[7]));
            movie.setRating(cells[8]);
            movie.setDuration(cells[9]);
            movie.setListedIn(cells[10]);
            movie.setDescription(cells[11]);
            moviesList.add(movie);
        }

            return moviesList;
        }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
        return null;
    }

    private static LocalDate dateFormatter(String date){
        LocalDate formatterDate =  null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(date.equals(""))
            return formatterDate;
        if(!date.contains("-")){
            String[] modifiedDate  = date.split(" ");
            String finalDate = (modifiedDate[2].length() < 3 ? "0"+modifiedDate[2].charAt(0) : modifiedDate[2].substring(0,2))+"-"+modifiedDate[1].substring(0,3)+"-"+modifiedDate[3].substring(modifiedDate.length-2,modifiedDate.length);
            formatterDate = LocalDate.parse(LocalDate.parse(finalDate, dtf).format(formatter));
        }
        else if(date.indexOf("-")>-1 && date.indexOf("-") < 2){
            String modifiedDate = 0+date;
            DateTimeFormatter innerFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            formatterDate = LocalDate.parse(LocalDate.parse(modifiedDate, dtf).format(formatter));
        }
        else{
            formatterDate = LocalDate.parse(date, dtf);
        }

        return formatterDate;
    }
}
