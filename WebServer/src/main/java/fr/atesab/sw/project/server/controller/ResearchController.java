package fr.atesab.sw.project.server.controller;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.atesab.sw.project.scraper.ScraperManager;
import fr.atesab.sw.project.scraper.ScrapingResult;
import fr.atesab.sw.project.scraper.meteo.MeteoCielLocation;
import fr.atesab.sw.project.scraper.territoire.DataTerritoireScraper;

@RestController
@RequestMapping("/api/")

public class ResearchController {
    public static record ApiEndPoint(String message) {
    };

    @Autowired
    ScraperManager scraperManager;

    public ResearchController() {
        // Récuperer ici les résultats grace au scrapper et les mettre dans une liste
        /* private List <territoire> territoireList; */
        /* territoireList.add(...) */
    }

    @GetMapping("/")
    ApiEndPoint home() {
        return new ApiEndPoint("api");
    }

    /*
     * @GetMapping("/home/{id}")
     * public TerritoireScraper get(@PathVariable String id){
     * return territoireList.stream().filter(t ->
     * id.equals(t.getId()))/findAny().orElse(null)
     * }
     */

    @GetMapping("/territoire")
    ScrapingResult territoire() {
        return scraperManager.executeModel(scraperManager.getTerritoire()::loadTriples);
    }

    @GetMapping("/territoire/rooms")
    public void RecupRooms() {
        ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
        // queryStr.setNsPrefix("sw", "https://territoire.emse.fr/kg/);
        queryStr.append("SELECT ?etage");
        queryStr.append("WHERE {");
        queryStr.append(" ?etage a https://w3id.org/bot#Storey%22");
        queryStr.append("}");
        Query q = queryStr.asQuery();

    }

    @GetMapping("/dataterritoire")
    ScrapingResult dataTerritoire() {
        return scraperManager.executeModel(scraperManager.getDataTerritoireScraper(
                DataTerritoireScraper.DEMO_DAILY_SENSOR)::loadTriples);
    }

    @GetMapping("/meteociel")
    ScrapingResult meteosciel(
            @RequestParam(name = "city", defaultValue = "0", required = false) int city,
            @RequestParam(name = "day", defaultValue = "0", required = false) int day,
            @RequestParam(name = "month", defaultValue = "0", required = false) int month,
            @RequestParam(name = "year", defaultValue = "0", required = false) int year) {

        if (city == 0) {
            city = MeteoCielLocation.METEOCIEL_ID_SAINT_ETIENNE;
        } else if (city < 0) {
            throw new IllegalArgumentException("bad city code");
        }

        if (day == 0 && month == 0 && year == 0) {
            // today
            return scraperManager
                    .executeModel(scraperManager.getMeteoScraper(new MeteoCielLocation(city))::loadTriples);
        } else if (day > 0 && month > 0 && year >= 1975 && day <= 31 && month <= 12) {
            // date
            return scraperManager
                    .executeModel(
                            scraperManager.getMeteoScraper(new MeteoCielLocation(city, day, month, year))::loadTriples);
        } else {
            throw new IllegalArgumentException("bad date");
        }
    }
}
