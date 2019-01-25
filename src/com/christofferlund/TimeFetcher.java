package com.christofferlund;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TimeFetcher {

    private String searchString;
    private String baseURLString;
    private URL searchURL;

    public TimeFetcher() {
        this.baseURLString = "http://google.com/search?q=time+in+";
    }

    public void setSearchURL(String location) {
        try {
            searchURL = new URL(baseURLString + location);
        } catch (MalformedURLException e) {
            System.err.println("Error in setSearchURL" + e.getMessage());
        }
    }

    public URL getSearchURL() {
        return searchURL;
    }

    public void setSearchString() {
        Scanner s = new Scanner(System.in);
        try {
            System.out.println("Enter a city or a country!");
            searchString = s.next();
            searchString = searchString.trim();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getSearchString(){
        return searchString;
    }
}
