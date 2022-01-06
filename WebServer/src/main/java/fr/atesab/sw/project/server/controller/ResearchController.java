package fr.atesab.sw.project.server.controller;

import java.util.Collections;
import java.util.List;

import org.apache.jena.query.ParameterizedSparqlString;
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

    public static record RoomAnswerElement(String iri, String label) {
    };

    public static record RoomAnswer(List<RoomAnswerElement> rooms) {
    };

    public static record FloorAnswerElement(String iri, String label, List<RoomAnswerElement> rooms) {
    };

    public static record FloorAnswer(List<FloorAnswerElement> floors) {
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

    @GetMapping("/floors")
    public FloorAnswer territoireFloors(
            @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        return new FloorAnswer(scraperManager.select(
                () -> {
                    ParameterizedSparqlString pss = new ParameterizedSparqlString();
                    pss.append("SELECT ?etage ?label ");
                    pss.append("WHERE { ");
                    pss.append("?etage a <https://w3id.org/bot#Storey> . ");
                    pss.append("?etage <http://www.w3.org/2000/01/rdf-schema#label> ?label . ");
                    pss.append("FILTER(LANG(?label) = \"\" || LANGMATCHES(LANG(?label), ");
                    pss.appendLiteral(lang);
                    pss.append("))");
                    pss.append("}");
                    return pss.asQuery();
                },
                solu -> new FloorAnswerElement(solu.get("etage").asResource().getURI(),
                        solu.get("label").asLiteral().getString(), Collections.emptyList())));
    }

    @GetMapping("/floor")
    public FloorAnswerElement floor(
            @RequestParam("floor") String floor,
            @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        return scraperManager.selectOne(() -> {
            ParameterizedSparqlString pss = new ParameterizedSparqlString();
            pss.append("SELECT ?label ");
            pss.append("WHERE { ");
            pss.appendIri(floor);
            pss.append(" <http://www.w3.org/2000/01/rdf-schema#label> ?label .");
            pss.append("FILTER(LANG(?label) = \"\" || LANGMATCHES(LANG(?label), ");
            pss.appendLiteral(lang);
            pss.append("))");
            pss.append("}");
            return pss.asQuery();
        }, solu -> new FloorAnswerElement(floor, solu.get("label").asLiteral().getString(),
                territoireRooms(floor, lang).rooms()));
    }

    @GetMapping("/room")
    public RoomAnswerElement room(
            @RequestParam("room") String room,
            @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        return scraperManager.selectOne(() -> {
            ParameterizedSparqlString pss = new ParameterizedSparqlString();
            pss.append("SELECT ?label ");
            pss.append("WHERE { ");
            pss.appendIri(room);
            pss.append(" <http://www.w3.org/2000/01/rdf-schema#label> ?label .");
            pss.append("FILTER(LANG(?label) = \"\" || LANGMATCHES(LANG(?label), ");
            pss.appendLiteral(lang);
            pss.append("))");
            pss.append("}");
            return pss.asQuery();
        }, solu -> new RoomAnswerElement(room, solu.get("label").asLiteral().getString()));
    }

    @GetMapping("/rooms")
    public RoomAnswer territoireRooms(
            @RequestParam("floor") String floor,
            @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        return new RoomAnswer(scraperManager.select(() -> {
            ParameterizedSparqlString pss = new ParameterizedSparqlString();
            pss.append("SELECT ?room ?label ");
            pss.append("WHERE { ");
            pss.appendIri(floor);
            pss.append(" <https://w3id.org/bot#hasSpace> ?room .");
            pss.append("?room <http://www.w3.org/2000/01/rdf-schema#label> ?label .");
            pss.append("FILTER(LANG(?label) = \"\" || LANGMATCHES(LANG(?label), ");
            pss.appendLiteral(lang);
            pss.append("))");
            pss.append("}");
            return pss.asQuery();
        }, solu -> new RoomAnswerElement(solu.get("room").asResource().getURI(),
                solu.get("label").asLiteral().getString())));
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
