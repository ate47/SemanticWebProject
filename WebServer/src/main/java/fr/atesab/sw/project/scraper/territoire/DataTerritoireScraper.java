package fr.atesab.sw.project.scraper.territoire;

import java.io.File;
import java.net.URL;

import org.apache.jena.rdf.model.Model;

import fr.atesab.sw.project.scraper.Scraper;
import fr.atesab.sw.project.scraper.ScraperException;
import fr.atesab.sw.project.scraper.ScrapingResult;
import fr.atesab.sw.project.utils.CSVReader;
import fr.atesab.sw.project.utils.ScraperUtils;
import fr.atesab.sw.project.utils.StreamSupplier;

public class DataTerritoireScraper extends Scraper {

    public static final URL DEMO_DAILY_SENSOR = ScraperUtils.errorIfException(() -> new URL(
            "https://seafile.emse.fr/d/710ced68c2894189a6f4/files/?p=%2F20211116-daily-sensor-measures.csv&dl=1"));

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
        return createResult(reader.readFile((SensorData data) -> {
            System.out.println(data);
            // TODO: add triples
        }));
    }

}
