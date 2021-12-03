package fr.atesab.sw.project.scraper;

import org.apache.jena.rdf.model.Model;

public interface Scraper {
    /**
     * @return the name of the scraper
     */
    String getName();

    /**
     * load the triples to the model
     * 
     * @param model the triples container
     */
    void loadTriples(Model model) throws ScraperException;
}
