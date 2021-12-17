import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import fr.atesab.sw.project.scraper.ScraperException;
import fr.atesab.sw.project.scraper.meteo.MeteoCielLocation;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;
import fr.atesab.sw.project.territoire.Territoire;

public class Start {
    public static final String DATASET_URL = "http://localhost:3030/dataset";
    public static final boolean SEND_DATASET = false;

    public static void main(String[] args) {
        var mscraper = new MeteoScraper(new MeteoCielLocation(MeteoCielLocation.METEOCIEL_ID_SAINT_ETIENNE));
        var tscraper = new Territoire(Territoire.TERRITOIRE_URL);

        var model = ModelFactory.createDefaultModel();

        try {

            System.out.println("load triples from " + tscraper.getName());
            // tscraper.loadTriples(model);

            System.out.println("load triples from " + mscraper.getName());
            mscraper.loadTriples(model);

        } catch (ScraperException e) {
            throw new RuntimeException(e);
        }

        if (SEND_DATASET)
            try (var conneg = RDFConnectionFactory.connect(DATASET_URL)) {
                // add the content of model to the triplestore
                conneg.load(model);
            }
    }
}
