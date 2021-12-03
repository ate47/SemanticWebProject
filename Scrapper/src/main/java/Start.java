import org.apache.jena.rdf.model.ModelFactory;

import fr.atesab.sw.project.scraper.ScraperException;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;

public class Start {
    public static void main(String[] args) {
        var scraper = new MeteoScraper(MeteoScraper.METEOCIEL_ID_SAINT_ETIENNE);

        var model = ModelFactory.createDefaultModel();

        // load triples from meteociel
        try {
            scraper.loadTriples(model);
        } catch (ScraperException e) {
            throw new RuntimeException(e);
        }

    }
}
