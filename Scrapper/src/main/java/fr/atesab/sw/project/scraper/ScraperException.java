package fr.atesab.sw.project.scraper;

public class ScraperException extends Exception {
    public ScraperException(Scraper s) {
        this("Can't scrap with " + s.getName());
    }

    public ScraperException(Scraper s, Throwable e) {
        this("Can't scrap with " + s.getName(), e);
    }

    public ScraperException(String msg, Throwable e) {
        super(msg, e);
    }

    public ScraperException(String msg) {
        super(msg);
    }
}
