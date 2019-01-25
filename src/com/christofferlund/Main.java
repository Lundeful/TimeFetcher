package com.christofferlund;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;


public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        TimeFetcher tf = new TimeFetcher();
        Desktop desktop = Desktop.getDesktop();

        tf.setSearchString();
        tf.setSearchURL(tf.getSearchString());
        System.out.println("----- Getting time in " + tf.getSearchString() + "-----");
        desktop.browse(tf.getSearchURL().toURI());
    }
}
