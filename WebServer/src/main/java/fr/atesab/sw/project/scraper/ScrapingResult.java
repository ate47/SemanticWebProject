package fr.atesab.sw.project.scraper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.MODULE)
public class ScrapingResult {
    private String usedScraper;
    private int newTriple;
}
