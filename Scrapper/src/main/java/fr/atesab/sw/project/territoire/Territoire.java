package fr.atesab.sw.project.territoire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;

import org.apache.jena.rdf.model.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fr.atesab.sw.project.scraper.Scraper;
import fr.atesab.sw.project.scraper.ScraperException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Territoire extends Scraper {
    public static final String TERRITOIRE_URL = "https://territoire.emse.fr/kg/";

    private void allFiles0(String url, List<String> files, Pattern regex) {
        Document d;
        try {
            d = Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new ScraperException(this, "can't connect and get to url '" + url + "'", e);
        }
        d.select("a[href]").forEach(a -> {
            String link = a.attr("href");
            if (link.startsWith("/"))
                return;

            if (link.endsWith("/")) {
                allFiles0(a.absUrl("href"), files, regex);
                return;
            }

            if (regex.matcher(link).matches()) {
                files.add(a.absUrl("href"));
            }
        });
    }

    public List<String> allFiles(String pattern) {
        Pattern regex = Pattern.compile(pattern);
        List<String> files = new ArrayList<>();
        allFiles0(this.url, files, regex);
        return files;
    }

    @NonNull
    private final String url;

    @Override
    public String getName() {
        return "territoire";
    }

    @Override
    public void loadTriples(Model model) throws ScraperException {
        List<String> ttls = allFiles(".*[.]ttl");

        for (String ttl : ttls) {
            model.read(ttl); // read the ttl file to the graph
        }
    }
}
