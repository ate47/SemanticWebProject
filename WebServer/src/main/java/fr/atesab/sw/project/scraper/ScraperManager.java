package fr.atesab.sw.project.scraper;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.stereotype.Component;

import fr.atesab.sw.project.scraper.meteo.MeteoCielLocation;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;
import fr.atesab.sw.project.scraper.territoire.TerritoireScraper;
import lombok.Getter;

@Component
public class ScraperManager {
    public static final String DATASET_URL = "http://localhost:3030/dataset";
    private Model model = ModelFactory.createDefaultModel();

    public ScraperManager() {
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("meteo", MeteoScraper.METEOCIEL_INDEX);
    }

    @Getter
    private TerritoireScraper territoire = new TerritoireScraper(TerritoireScraper.TERRITOIRE_URL);

    public MeteoScraper getMeteoScraper(MeteoCielLocation location) {
        return new MeteoScraper(location);
    }

    public <T> T executeModel(Function<Model, T> action) {
        T t;
        synchronized (model) {
            t = action.apply(model);
            try (RDFConnection conneg = RDFConnectionFactory.connect(DATASET_URL)) {
                conneg.load(model);
            }
            model.removeAll();
        }
        return t;
    }
}
