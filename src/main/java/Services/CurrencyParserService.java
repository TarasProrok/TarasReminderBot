package Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyParserService {

    CurrencyParserService() {
    }

    public static List<String> getExchangeRates() throws IOException {

        List<String> exchangeRates;
            exchangeRates = new ArrayList<>();

            // зчитування вмісту сторінки
            Document doc = Jsoup.connect("http://rulya-bank.com.ua/").get();

            // знаходимо таблицю з курсами валют
            Element table = doc.select("div.col-md-3").first();

            // знаходимо всі рядки таблиці
            if (table != null) {
                Elements rows = table.select("tr");

                // проходимо по кожному рядку, витягуємо дані про курси валют
                for (int i = 1; i <= 2; i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    String currency = cols.get(0).text();
                    String buyRate = cols.get(1).text();
                    String sellRate = cols.get(2).text();
                    exchangeRates.add(currency + ": купівля " + buyRate + ", продаж " + sellRate);
                }
            }
        return exchangeRates;
    }
    public static String prettyRates() throws IOException {
        List<String> ratesList = getExchangeRates();
        String rates = "<b>Курси валют на сьогодні:</b>";
        String usd = "💵"+ratesList.get(0);
        String eur = "\uD83D\uDCB6"+ratesList.get(1);
        return String.format("%s\n%s\n%s",rates, usd, eur);
    }
}
