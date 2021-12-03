package fr.atesab.sw.project.scraper;

public class ScraperException extends Exception {
    public ScraperException(Scraper s) {
        super("Can't scrap with " + s.getName());
    }

    public ScraperException(Scraper s, Throwable e) {
        super("Can't scrap with " + s.getName(), e);
    }

    public ScraperException(Scraper s, String msg, Throwable e) {
        super("Can't scrap with " + s.getName() + ": " + msg, e);
    }

    public ScraperException(Scraper s, String msg) {
        super("Can't scrap with " + s.getName() + ": " + msg);
    }

}
