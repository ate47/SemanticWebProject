import fr.atesab.sw.project.scraper.ScraperException;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;

public class Start {
    public static void main(String[] args) {
        var scraper = new MeteoScraper(MeteoScraper.METEOCIEL_ID_SAINT_ETIENNE);

        try {
            scraper.loadTriples(null);
        } catch (ScraperException e) {
            e.printStackTrace();
        }
    }
}
