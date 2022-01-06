package fr.atesab.sw.project.scraper.territoire;

import java.io.File;
import java.net.URL;
import java.util.OptionalDouble;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import fr.atesab.sw.project.scraper.Scraper;
import fr.atesab.sw.project.scraper.ScraperException;
import fr.atesab.sw.project.scraper.ScrapingResult;
import fr.atesab.sw.project.utils.CSVReader;
import fr.atesab.sw.project.utils.ScraperUtils;
import fr.atesab.sw.project.utils.StreamSupplier;

public class DataTerritoireScraper extends Scraper {

    public static final URL DEMO_DAILY_SENSOR = ScraperUtils.errorIfException(() -> new URL(
            "https://seafile.emse.fr/d/710ced68c2894189a6f4/files/?p=%2F20211116-daily-sensor-measures.csv&dl=1"));

    private static void addIfPresent(Model model, OptionalDouble value, Resource subject, Property predictate) {
        if (value.isPresent()) {
            model.add(subject, predictate, String.valueOf(value.getAsDouble()), XSDDatatype.XSDdouble);
        }
    }

    private CSVReader<SensorData> reader;

    public DataTerritoireScraper(StreamSupplier stream) {
        this.reader = new CSVReader<>(stream, SensorData::of);
    }

    public DataTerritoireScraper(File file) {
        this.reader = new CSVReader<>(file, SensorData::of);
    }

    public DataTerritoireScraper(URL url) {
        this.reader = new CSVReader<>(url, SensorData::of);
    }

    @Override
    public String getName() {
        return "data territoire";
    }

    @Override
    public ScrapingResult loadTriples(Model model) throws ScraperException {
        Property hasHumidity = model.createProperty(SensorData.SENSOR_INDEX + "hasHumidity");
        Property hasLuminosity = model.createProperty(SensorData.SENSOR_INDEX + "hasLuminosity");
        Property hasSnd = model.createProperty(SensorData.SENSOR_INDEX + "hasSnd");
        Property hasSndf = model.createProperty(SensorData.SENSOR_INDEX + "hasSndf");
        Property hasSndm = model.createProperty(SensorData.SENSOR_INDEX + "hasSndm");
        Property hasTemperature = model.createProperty(SensorData.SENSOR_INDEX + "hasTemperature");

        return createResult(reader.readFile((SensorData data) -> {
            Resource r = data.getResource(model);

            addIfPresent(model, data.humidity(), r, hasHumidity);
            addIfPresent(model, data.luminosity(), r, hasLuminosity);
            addIfPresent(model, data.snd(), r, hasSnd);
            addIfPresent(model, data.sndf(), r, hasSndf);
            addIfPresent(model, data.sndm(), r, hasSndm);
            addIfPresent(model, data.temperature(), r, hasTemperature);
        }));
    }

}
