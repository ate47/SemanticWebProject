package fr.atesab.sw.project.scraper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.stereotype.Component;

import fr.atesab.sw.project.scraper.meteo.MeteoCielLocation;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;
import fr.atesab.sw.project.scraper.territoire.DataTerritoireScraper;
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

    public DataTerritoireScraper getDataTerritoireScraper(URL url) {
        return new DataTerritoireScraper(url);
    }

    public void acceptConnection(Consumer<RDFConnection> action) {
        executeConnection(conneg -> {
            action.accept(conneg);
            return null;
        });
    }

    public <T> T executeConnection(Function<RDFConnection, T> action) {
        try (RDFConnection conneg = RDFConnectionFactory.connect(DATASET_URL)) {
            return action.apply(conneg);
        }
    }

    public List<String> selectUris(String uriProp, String query) {
        return select(query, solu -> solu.get(uriProp).asResource().getURI());
    }

    public List<String> selectUris(String uriProp, Supplier<Query> query) {
        return select(query, solu -> solu.get(uriProp).asResource().getURI());
    }

    public <T> List<T> select(String query, Function<QuerySolution, T> selection) {
        return select(() -> QueryFactory.create(query), selection);
    }

    public <T> List<T> select(Supplier<Query> query, Function<QuerySolution, T> selection) {
        Query q = query.get();
        return executeConnection(conneg -> {
            List<T> uris = new ArrayList<>();
            try (QueryExecution exec = conneg.query(q)) {
                ResultSet set = exec.execSelect();
                while (set.hasNext()) {
                    uris.add(selection.apply(set.next()));
                }
            }
            return uris;
        });
    }

    public <T> T selectOne(String query, Function<QuerySolution, T> selection) {
        return selectOne(() -> QueryFactory.create(query), selection);
    }

    public <T> T selectOne(Supplier<Query> query, Function<QuerySolution, T> selection) {
        Query q = query.get();
        return executeConnection(conneg -> {
            try (QueryExecution exec = conneg.query(q)) {
                ResultSet set = exec.execSelect();
                if (set.hasNext()) {
                    return selection.apply(set.next());
                }
            }
            throw new NoSuchElementException();
        });
    }

    public <T> T executeModel(Function<Model, T> action) {
        T t;
        synchronized (model) {
            t = action.apply(model);
            System.out.println("executing model...");
            acceptConnection(conneg -> conneg.load(model));
            System.out.println("end executing model.");
            model.removeAll();
        }
        return t;
    }
}
