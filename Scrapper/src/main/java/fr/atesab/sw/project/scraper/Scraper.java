package fr.atesab.sw.project.scraper;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.jena.rdf.model.Model;

import lombok.Getter;

public abstract class Scraper {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    @Getter
    private final int uid = ID_GENERATOR.getAndIncrement();

    /**
     * @return the name of the scraper
     */
    public abstract String getName();

    /**
     * load the triples to the model
     * 
     * @param model the triples container
     */
    public abstract void loadTriples(Model model) throws ScraperException;
}
