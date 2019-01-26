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

    private String userInput;
    private String searchString;
    private URL searchURL;
    private String htmlContent;
    private String time;
    private String date;
    private boolean timeAndDateFound;

    // For "GUI" of console
    private String separator = "----------------------------";


    public TimeFetcher() {
        userInput="";
    }

    /**
     * Core program loop
     */
    public void goFetch() {
        showWelcomeMessage();
        while (!userInput.equals("q")) {
            // Reset search
            timeAndDateFound = true;

            takeUserInput();                    // Get input for location to search
            setSearchURL(searchString);         // Set the URL to parse
            fetchHTML();                        // Fetch and store the HTML
            findTime();                         // Search and store time
            findDate();                         // Search and store date
            printTimeAndDate();                 // Show results to user
        }
    }


    private void takeUserInput() {
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
            // Makes TitleCase of user input
            userInput = toTitleCase(userInput);

            // Show user that search is being done
            System.out.println("Searching...");
        } catch (Exception e) {
            System.err.println("Error in setSearchString: " + e.getMessage());
        }
    }


    private void setSearchURL(String location) {
        try {
            String baseURLString = "https://www.google.com/search?q=time+in+";
            searchURL = new URL(baseURLString + location);
        } catch (MalformedURLException e) {
            System.err.println("Error in setSearchURL" + e.getMessage());
        }
    }


    private void fetchHTML() {
        try {
            // Open input-stream to read from
            URLConnection uc = this.searchURL.openConnection();

            // Make request look like a web-browser
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");

            // BufferReader to read from inputstream
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

    private void findTime() {
        // Find pattern in html to locate time
        String lp = "<div class=\"gsrt vk_bk dDoNo\" aria-level=\"3\" role=\"heading\">";
        String rp = "</div>";
        Pattern pattern = Pattern.compile(lp + "(.*?)" + rp);
        Matcher matcher = pattern.matcher(htmlContent);

        // If time is found, store time
        if (matcher.find()) {
            time = matcher.group(1);
        }
    }

    private void findDate() {
        String day = "";
        String date ="";

        // Find pattern in html to locate day
        String lp = "<div class=\"vk_gy vk_sh\"> ";
        String rp = "<span class=\"KfQeJ\">";
        Pattern pattern = Pattern.compile(lp + "(.*?)" + rp);
        Matcher matcher = pattern.matcher(htmlContent);

        // If day is found, store day
        if (matcher.find()) {
            day = matcher.group(1);
        } else {
            timeAndDateFound = false;
        }

        // Find pattern in html to locate date
        lp = "<span class=\"KfQeJ\">";
        rp = "</span>";
        pattern = Pattern.compile(lp + "(.*?)" + rp);
        matcher = pattern.matcher(htmlContent);

        // If date is found, store date
        if (matcher.find()) {
            date = matcher.group(1);
        } else {
            timeAndDateFound = false;
        }

        // Store in date
        this.date = day + date;
    }

    private void printTimeAndDate() {
        if (timeAndDateFound) {
            System.out.println(separator);
            System.out.println(userInput);
            System.out.println(date + ", " + time);
            System.out.println(separator);
        } else {
            System.out.println(separator);
            System.out.println("Could not retrieve time and date. \n" +
                    "Please check spelling or try another location. \n" +
                    "If problem persists please contact \n" +
                    "software administrator");
            System.out.println(separator);
        }
    }

    /**
     * Welcome message at the start of the program
     */
    private void showWelcomeMessage() {
        System.out.println(separator);
        System.out.println("Welcome to TimeFetcher!\nIn this program, you can enter a city \n" +
                "or a country and I will give you the \n" +
                "current time and date");
        System.out.println(separator);
    }

    private String toTitleCase(String s) {
        StringBuilder sb = new StringBuilder();
        boolean nextCharTitle = true;

        for (char c : s.toCharArray()) {
            if (Character.isSpaceChar(c)) {         // New word
                nextCharTitle = true;
            } else if (nextCharTitle) {             // First letter of new word
                c = Character.toTitleCase(c);
                nextCharTitle = false;
            } else if (!Character.isSpaceChar(c)){  // Regular lowercase character
                c = Character.toLowerCase(c);
            }

            sb.append(c);
        }
        return sb.toString();
    }
}
