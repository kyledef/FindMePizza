package org.kyledef.findmepizza.model;

/**
 * Created by kyle on 3/1/15.
 */
public class FranchiseModel {
    String name;
    String shortcode;
    String url;
    int logoR;

    public FranchiseModel(String name, String shortcode, String url, int logoR) {
        this.name = name;
        this.shortcode = shortcode;
        this.url = url;
        this.logoR = logoR;
    }


    public String getName() {
        return name;
    }

    public String getShortcode() {
        return shortcode;
    }

    public String getUrl() {
        return url;
    }

    public int getLogoR() {
        return logoR;
    }
}
