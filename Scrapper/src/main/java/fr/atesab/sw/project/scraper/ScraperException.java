package fr.atesab.sw.project.scraper;

public class ScraperException extends RuntimeException {
    private static String getMessageOf(Scraper s) {
        return "Can't scrap with " + s.getName() + "(" + s.getUid() + ")";
    }

    private static String getMessageOf(Scraper s, String msg) {
        return getMessageOf(s) + ": " + msg;
    }

    public ScraperException(Scraper s) {
        super(getMessageOf(s));
    }

    public ScraperException(Scraper s, Throwable e) {
        super(getMessageOf(s), e);
    }

    public ScraperException(Scraper s, String msg, Throwable e) {
        super(getMessageOf(s, msg), e);
    }

    public ScraperException(Scraper s, String msg) {
        super(getMessageOf(s, msg));
    }

}
