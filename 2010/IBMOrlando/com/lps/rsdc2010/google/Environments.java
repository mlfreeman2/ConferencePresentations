package com.lps.rsdc2010.google;

public enum Environments
{
    Main("http://www.google.com"), Finance("http://finance.google.com"), News("http://news.google.com");
    private String URL = "";
    
    private Environments(String url)
    {
        this.URL = url;
    }
    
    public String getURL()
    {
        return URL;
    }
}
