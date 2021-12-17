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
    public static final String METEOCIEL_PAGE = "https://www.meteociel.fr/temps-reel/obs_villes.php";

    @Getter
    private final MeteoCielLocation location;

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
            var doc = Jsoup.connect(METEOCIEL_PAGE + getLocation().toGetQuery()).get();

            var days = Integer.valueOf(doc.select("select[name=jour2] option[selected]").first().attr("value"));
            var month = Integer.valueOf(doc.select("select[name=mois2] option[selected]").first().attr("value")) + 1;
            var year = Integer.valueOf(doc.select("select[name=annee2] option[selected]").first().attr("value"));

            System.out.println(days + "/" + month + "/" + year);

            if (days != null)
                return;

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
