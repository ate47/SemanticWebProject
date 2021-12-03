package fr.atesab.sw.project.scraper.meteo;

import java.io.IOException;
import java.util.Iterator;

import org.apache.jena.rdf.model.Model;
import org.jsoup.Jsoup;

import fr.atesab.sw.project.scraper.Scraper;
import fr.atesab.sw.project.scraper.ScraperException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeteoScraper extends Scraper {
    public static final String METEOCIEL_PAGE = "https://www.meteociel.fr/temps-reel/obs_villes.php?code2=";
    public static final int METEOCIEL_ID_SAINT_ETIENNE = 7475;

    @Getter
    private final int cityId;

    @Override
    public String getName() {
        return "Meteo Ciel";
    }

    private static boolean passIt(Iterator<?> it, int count) {
        for (var i = 0; i < count; i++) {
            if (!it.hasNext())
                return true;
            it.next();
        }
        return false;
    }

    @Override
    public void loadTriples(Model model) throws ScraperException {
        try {
            var doc = Jsoup.connect(METEOCIEL_PAGE + getCityId()).get();
            var elements = doc
                    .select("tr td center table tr td");
            var it = elements.iterator();
            if (passIt(it, 28))
                throw new ScraperException(this, "Can't pass header td");
            while (it.hasNext()) {
                var hour = it.next().text();

                if (passIt(it, 3) || !it.hasNext())
                    throw new ScraperException(this, "Can't pass hour to temperature");

                var temp = it.next().text();

                System.out.println(hour + ": " + temp);
                // TODO: complete that to add triples

                passIt(it, 7);
            }

        } catch (IOException e) {
            throw new ScraperException(this, e);
        }
    }
}
