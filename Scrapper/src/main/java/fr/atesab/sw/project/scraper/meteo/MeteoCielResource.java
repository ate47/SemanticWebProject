package fr.atesab.sw.project.scraper.meteo;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MeteoCielResource {
    @Getter
    private int code, day, month, year, hour;

    /**
     * get the resource of type meteo:Location for this Location
     * 
     * @param model the model to create Resources
     * @return the Resource
     */
    public Resource getResourceForLocation(Model model) {
        return model.createResource(
                MeteoScraper.METEOCIEL_INDEX + "L" + getCode() + "_" + getDay() + "_" + getMonth() + "_" + getYear()
                        + "T"
                        + getHour(),
                model.createResource(MeteoScraper.METEOCIEL_INDEX + "Location"));
    }
}
