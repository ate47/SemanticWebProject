package fr.atesab.sw.project.server.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.apache.jena.query.ParameterizedSparqlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.atesab.sw.project.scraper.ScraperManager;
import fr.atesab.sw.project.scraper.ScrapingResult;
import fr.atesab.sw.project.scraper.meteo.MeteoCielLocation;
import fr.atesab.sw.project.scraper.meteo.MeteoScraper;
import fr.atesab.sw.project.scraper.territoire.DataTerritoireScraper;
import fr.atesab.sw.project.scraper.territoire.SensorData;

@RestController
@RequestMapping("/api/")

public class ResearchController {
    @JsonInclude(Include.NON_NULL)
    public static record ApiEndPoint(String message) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record RoomAnswerElement(String iri, String label, List<String> sensors, FloorAnswerElement floor) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record RoomAnswer(List<RoomAnswerElement> rooms) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record SensorAnswer(List<String> sensors) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record FloorAnswerElement(String iri, String label, List<RoomAnswerElement> rooms) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record FloorAnswer(List<FloorAnswerElement> floors) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record MeteoData(List<MeteoDataElement> elements) {
    };

    @JsonInclude(Include.NON_NULL)
    public static record MeteoDataElement(int hour, float temperature) {
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
                        solu.get("label").asLiteral().getString(), null)));
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
            pss.append("SELECT ?label ?floorlabel ?flooriri ");
            pss.append("WHERE { ");
            pss.appendIri(room);
            pss.append(" <http://www.w3.org/2000/01/rdf-schema#label> ?label .");
            pss.append("?flooriri <https://w3id.org/bot#hasSpace> ");
            pss.appendIri(room);
            pss.append(" . ");
            pss.append("?flooriri <http://www.w3.org/2000/01/rdf-schema#label> ?floorlabel . ");
            pss.append("FILTER(LANG(?label) = \"\" || LANGMATCHES(LANG(?label), ");
            pss.appendLiteral(lang);
            pss.append("))");
            pss.append("FILTER(LANG(?floorlabel) = \"\" || LANGMATCHES(LANG(?floorlabel), ");
            pss.appendLiteral(lang);
            pss.append("))");
            pss.append("}");
            return pss.asQuery();
        }, solu -> new RoomAnswerElement(room, solu.get("label").asLiteral().getString(),
                sensors(room, lang).sensors(), new FloorAnswerElement(solu.get("flooriri").asResource().getURI(),
                        solu.get("floorlabel").asLiteral().getString(), null)));
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
                solu.get("label").asLiteral().getString(), null, null)));
    }

    @GetMapping("/sensors")
    public SensorAnswer sensors(
            @RequestParam("room") String room,
            @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        return new SensorAnswer(scraperManager.selectUris("sensor", () -> {
            ParameterizedSparqlString pss = new ParameterizedSparqlString();
            pss.append("SELECT ?sensor ");
            pss.append("WHERE { ");
            pss.append("?sensor <" + SensorData.SENSOR_INDEX + "hasRoom> ");
            pss.appendIri(room);
            pss.append(". ");
            pss.append("}");
            return pss.asQuery();
        }));
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

    @GetMapping("meteo")
    MeteoData meteo(
            @RequestParam(name = "day", defaultValue = "0", required = false) int day,
            @RequestParam(name = "month", defaultValue = "0", required = false) int month,
            @RequestParam(name = "year", defaultValue = "0", required = false) int year) {
        meteosciel(MeteoCielLocation.METEOCIEL_ID_SAINT_ETIENNE, day, month, year);
        return new MeteoData(scraperManager.select(() -> {
            ParameterizedSparqlString pss = new ParameterizedSparqlString();
            pss.append("SELECT ?hour ?temp ");
            pss.append("WHERE { ");
            // TODO: add date (also in the scraper)
            pss.append(MeteoScraper.METEOCIEL_INDEX + "hasTemperature ? temp . ");
            pss.append("}");
            return null;
        }, solu -> new MeteoDataElement(solu.get("hour").asLiteral().getInt(),
                solu.get("temp").asLiteral().getFloat())));
    }
}
