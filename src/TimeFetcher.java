import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a program that fetches the time and dayAndDate of a city or a country and shows it to the user.
 * Create a new TimeFetcher instance and call on the goFetch() method to run the software.
 */
public class TimeFetcher {

    private String userInput;
    private String searchString;
    private URL searchURL;
    private String htmlContent;
    private String time;
    private String dayAndDate;
    private String location;
    private boolean timeAndDateFound;

    // For "GUI" of console
    private String separator = "----------------------------";


    public TimeFetcher() {
        userInput="";
    }

    /**
     * Core program loop used to run the software.
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
            findDate();                         // Search and store dayAndDate
            findLocation();                     // Search and store location
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

            // Show user that search is being done
            System.out.println("Searching...");
        } catch (Exception e) {
            System.err.println("Error in setSearchString: " + e.getMessage());
        }
    }


    private void setSearchURL(String location) {
        try {
            String baseURLString = "https://www.google.com/search?hl=en&q=time+in+";
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
        // Pattern to find in html to locate time
        String lp = "<div class=\"gsrt vk_bk dDoNo\" aria-level=\"3\" role=\"heading\">";
        String rp = "</div>";

        time = patternSearch(lp, rp);
    }

    private void findDate() {
        // Pattern to find in html to locate day
        String lpDay = "<div class=\"vk_gy vk_sh\"> ";
        String rpDay = "<span class=\"KfQeJ\">";
        String day = patternSearch(lpDay, rpDay);


        // Pattern to find in html to locate date
        String lpDate = "<span class=\"KfQeJ\">";
        String rpDate = "</span>";
        String date = patternSearch(lpDate,rpDate);

        // Store in dayAndDate
        this.dayAndDate = day + date;
    }

    private void findLocation() {
        // Pattern to find in html to locate location
        String lp = "<span> Time in ";
        String rp = " </span>";

        location = patternSearch(lp, rp);
    }

    // Method used to search for content in string using regex patterns
    private String patternSearch(String leftPattern, String rightPattern) {
        Pattern pattern = Pattern.compile(leftPattern + "(.*?)" + rightPattern);
        Matcher matcher = pattern.matcher(htmlContent);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            timeAndDateFound = false;
            return "";
        }
    }

    private void printTimeAndDate() {
        if (timeAndDateFound) {
            System.out.println(separator);
            System.out.println(location);
            System.out.println(dayAndDate + ", " + time);
            System.out.println(separator);
        } else {
            System.out.println(separator);
            System.out.println("Could not retrieve time and dayAndDate. \n" +
                    "Please check spelling or try another location. \n" +
                    "If problem persists please contact \n" +
                    "software administrator");
            System.out.println(separator);
        }
    }

    private void showWelcomeMessage() {
        System.out.println(separator);
        System.out.println("Welcome to TimeFetcher!\nIn this program, you can enter a city \n" +
                "or a country and I will give you the \n" +
                "current time and date");
        System.out.println(separator);
    }
}
