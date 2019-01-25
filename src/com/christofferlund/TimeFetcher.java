package com.christofferlund;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a program that fetches the time and date of a city or a country and shows it to the user.
 */
public class TimeFetcher {

    private String userInput = "";
    private String searchString;
    private URL searchURL;
    private String htmlContent;
    private String time;
    private String date;

    private String separator = "----------------------------";


    public TimeFetcher() {
    }

    /**
     * Core program loop
     */
    public void goFetch() {
        showWelcomeMessage();
        while (!userInput.equals("q")) {
            takeUserInput();                    // Get input for location to search
            setSearchURL(searchString);         // Set the URL to parse
            storeHTML();                        // Store the HTML with info
            searchForTime();                    // Search for time
        }
    }


    public void takeUserInput() {
        Scanner s = new Scanner(System.in);
        try {
            // Take user input
            System.out.println("Please enter a location or 'q' to quit");
            userInput = s.nextLine().trim();

            // User quitting
            if (userInput.equals("q")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }

            // Replace spaces with plus signs for correct URL
            searchString = userInput.replaceAll(" ", "+");

            // Show user that search is being done
            System.out.println("Searching...");
        } catch (Exception e) {
            System.err.println("Error in setSearchString: " + e.getMessage());
        }
    }


    public void setSearchURL(String location) {
        try {
            String baseURLString = "https://www.google.com/search?q=time+in+";
            searchURL = new URL(baseURLString + location);
        } catch (MalformedURLException e) {
            System.err.println("Error in setSearchURL" + e.getMessage());
        }
    }


    public void storeHTML() {
        try {
            // Open input-stream to read from
            URLConnection uc = this.searchURL.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            // Loop through lines to build html into a string
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            htmlContent = sb.toString();

        } catch (Exception e) {
            System.err.println("Error in parseHTML: " + e.getMessage());
        }
    }

    public void searchForTime() {
        // Find pattern in html to locate time
        String lp = "<div class=\"gsrt vk_bk dDoNo\" aria-level=\"3\" role=\"heading\">";
        String rp = "</div>";
        Pattern pattern = Pattern.compile(lp + "(.*?)" + rp);
        Matcher matcher = pattern.matcher(htmlContent);

        // If time is found, print out time
        if (matcher.find()) {
            time = matcher.group(1);
            System.out.println(separator);
            System.out.println(userInput + "\n" + time);
            System.out.println(separator);
        }
    }

    /**
     * Welcome message at the start of the program
     */
    public void showWelcomeMessage() {
        System.out.println(separator);
        System.out.println("Welcome to TimeFetcher!\nIn this program, you can enter a city \n" +
                "or a country and I will give you the \n" +
                "current time and date");
        System.out.println(separator);
    }

}
