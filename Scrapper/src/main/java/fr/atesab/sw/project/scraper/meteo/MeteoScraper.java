package fr.atesab.sw.project.scraper.meteo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.jsoup.Jsoup;

import fr.atesab.sw.project.scraper.Scraper;
import fr.atesab.sw.project.scraper.ScraperException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeteoScraper implements Scraper {
    public static final String METEOCIEL_PAGE = "https://www.meteociel.fr/temps-reel/obs_villes.php?code2=";
    public static final int METEOCIEL_ID_SAINT_ETIENNE = 7475;

    @Getter
    private final int cityId;

    @Override
    public String getName() {
        return "meteo_html";
    }

    @Override
    public void loadTriples(Model model) throws ScraperException {
        try {
            var doc = Jsoup.connect(METEOCIEL_PAGE + getCityId()).get();
            doc.select("td").forEach(System.out::println);
            // TODO: complete that

        } catch (IOException e) {
            throw new ScraperException(this, e);
        }
    }
}
