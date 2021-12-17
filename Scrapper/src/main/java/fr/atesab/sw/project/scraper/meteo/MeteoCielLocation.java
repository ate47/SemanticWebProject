package fr.atesab.sw.project.scraper.meteo;

import fr.atesab.sw.project.utils.ScraperUtils;
import lombok.Getter;
import lombok.Setter;

public class MeteoCielLocation {
    public static final int METEOCIEL_ID_SAINT_ETIENNE = 7475;

    @Getter
    @Setter
    private int code;
    private boolean hasDate;
    @Getter
    @Setter
    private int day;
    @Getter
    @Setter
    private int month;
    @Getter
    @Setter
    private int year;

    /**
     * get today's weather for a city
     * 
     * @param code the code of the city
     */
    public MeteoCielLocation(int code) {
        this.code = code;
        this.hasDate = false;
    }

    /**
     * get the weather for a city of a certain day
     * 
     * @param code  the code of the city
     * @param day   between 1 and 31
     * @param month between 1 and 12
     * @param year  greater than 1975
     */
    public MeteoCielLocation(int code, int day, int month, int year) {
        this.code = code;
        this.day = ScraperUtils.interval(day, 1, 31, "day");
        this.month = ScraperUtils.interval(month, 1, 12, "month");
        this.year = ScraperUtils.intervalMin(year, 1975, "year");
        this.hasDate = true;
    }

    /**
     * @return the get query to append to {@link MeteoScraper#METEOCIEL_PAGE}
     */
    public String toGetQuery() {
        StringBuilder bld = new StringBuilder();
        bld.append("?code2=").append(getCode());
        if (hasDate()) {
            bld.append("&jour2=").append(getDay());
            bld.append("&mois2=").append(getMonth() - 1);
            bld.append("&annee2=").append(getYear());
        }
        return bld.toString();
    }

    /**
     * @return false for today, true for other days
     */
    public boolean hasDate() {
        return hasDate;
    }
}
