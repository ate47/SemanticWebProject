package fr.atesab.sw.project.scraper.territoire;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * add the triple (subject, predicate, value^^xsd:double) to the model if the
     * value is present
     * 
     * @param model      the model to add
     * @param value      the value
     * @param subject    the subject
     * @param predictate the predicate
     * @return 1 if a triple was added, 0 otherwise
     */
    private static int addIfPresent(Model model, OptionalDouble value, Resource subject, Property predictate) {
        if (value.isPresent()) {
            model.add(subject, predictate, String.valueOf(value.getAsDouble()), XSDDatatype.XSDdouble);
            return 1;
        }
        return 0;
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
        Property hasRoom = model.createProperty(SensorData.SENSOR_INDEX + "hasRoom");
        Property date = model.createProperty(SensorData.SENSOR_INDEX + "date");
        Property hasSensor = model.createProperty(SensorData.SENSOR_INDEX + "hasSensor");
        Property hasHumidity = model.createProperty(SensorData.SENSOR_INDEX + "hasHumidity");
        Property hasLuminosity = model.createProperty(SensorData.SENSOR_INDEX + "hasLuminosity");
        Property hasSnd = model.createProperty(SensorData.SENSOR_INDEX + "hasSnd");
        Property hasSndf = model.createProperty(SensorData.SENSOR_INDEX + "hasSndf");
        Property hasSndm = model.createProperty(SensorData.SENSOR_INDEX + "hasSndm");
        Property hasTemperature = model.createProperty(SensorData.SENSOR_INDEX + "hasTemperature");

        // number of triples added
        AtomicInteger triples = new AtomicInteger();

        reader.readFile((SensorData data) -> {
            Resource r = data.getResource(model);
            int t = 5; // number of triples added: 2 = (r, hasRoom, room) + (r, a, Sensor) * 2 + date

            model.add(r, hasRoom, model.createResource(data.location()));

            Resource sensorData = model.createResource(
                    SensorData.SENSOR_INDEX + "SD" + data.id().toString().replace("-", "") + "T"
                            + data.time().getTime(),
                    model.createResource(SensorData.SENSOR_INDEX + "SensorData"));

            model.add(r, hasSensor, sensorData);
            model.add(sensorData, date,
                    model.createTypedLiteral(data.time().toInstant().toString(), XSDDatatype.XSDdateTime));

            t += addIfPresent(model, data.humidity(), sensorData, hasHumidity);
            t += addIfPresent(model, data.luminosity(), sensorData, hasLuminosity);
            t += addIfPresent(model, data.snd(), sensorData, hasSnd);
            t += addIfPresent(model, data.sndf(), sensorData, hasSndf);
            t += addIfPresent(model, data.sndm(), sensorData, hasSndm);
            t += addIfPresent(model, data.temperature(), sensorData, hasTemperature);

            triples.addAndGet(t);
        });

        return createResult(triples.get());
    }

}
