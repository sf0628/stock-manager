package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockmarket.StockInfo;
import stockmarket.Timespan;
import stockmarket.Utils;

/**
 * The AlphaVantage class gets and stores stock price data based on the ticker
 * from the AlphaVantage API history.
 */
public class AlphaVantage implements StocksAPI {
  //TODO: make field private, fix all dependencies
  //the list of histories of stock price
  private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public final static Map<String, List<StockInfo>> STOCK_PRICE_HISTORY = new HashMap<>();

  /**
   * Gets the list of stock information given the ticker value. If stock information
   * isn't present in the stockPriceHistory, the program will attempt to parse a csv file.
   * If that doesn't work then the program calls the API. If a new stock has been added,
   * the tickers list is also
   * @param ticker the identifying letters of a particular stock
   * @return the list of stock information of the given stock
   */
  public static List<StockInfo> getStock(String ticker) {
    //validates if the ticker follows proper syntax
    Utils.validTicker(ticker);
    //gets the stock
    if (STOCK_PRICE_HISTORY.containsKey(ticker)) {
      return STOCK_PRICE_HISTORY.get(ticker);
    } else {
      return addStock(ticker);
    }
  }

  /**
   * Gets the specific stock information for a stock at a given date. If stock isn't found at that
   * date then the stock is incremented back one day until a valid date is found.
   * @param ticker the identifying letters of a particular stock
   * @param date the date of the stock information attempted to be found
   * @return a StockInfo with the valid date
   */
  public static StockInfo getStockInfo(String ticker, LocalDate date, Timespan timespan) {
    Utils.validTicker(ticker);
    List<StockInfo> stockInfo = null;
    LocalDate currDate = date;

    //gets the stock information if doesn't exists in the data
    if (!STOCK_PRICE_HISTORY.containsKey(ticker)) {
      AlphaVantage.getStock(ticker);
    }
    stockInfo = STOCK_PRICE_HISTORY.get(ticker);
    LocalDate min = Utils.minMaxDate(stockInfo, true);
    LocalDate max = Utils.minMaxDate(stockInfo, false);

    //increments the date back until a valid date is found
    if (timespan.isMonth() || timespan.isYear()) { //is month or year
      while (!currDate.isAfter(max) || !currDate.isBefore(min)) {
        try {
          return Utils.checkDate(currDate.format(formatter), ticker);
        }
        catch (Exception e) {
          currDate = currDate.plusDays(-1);
        }
      }
    } else { //is day
      while (!currDate.isAfter(max) || !currDate.isBefore(min)) {
        try {
          return Utils.checkDate(currDate.format(formatter), ticker);
        }
        catch (Exception e) {
          currDate = currDate.plusDays(1);
        }
      }
    }

    throw new IllegalArgumentException("Stock info not found");
  }

  /**
   * Adds the list of stock price information for the given ticker from a local CSV file or API.
   *
   * @param ticker the stock ticker symbol to get information for
   * @return the list of StockInfo objects for the given ticker
   */
  private static List<StockInfo> addStock(String ticker) {
    List<StockInfo> stocksList = new ArrayList<>();
    String filePath = "src/data/" + ticker + ".csv";
    Path path = Paths.get(filePath);

    if (Files.exists(path)) {
      //try reading from local CSV file
      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        //skip header line
        br.readLine();

        while ((line = br.readLine()) != null) {
          String[] fields = line.split(",");
          String date = fields[0];
          double open = Double.parseDouble(fields[1]);
          double high = Double.parseDouble(fields[2]);
          double low = Double.parseDouble(fields[3]);
          double close = Double.parseDouble(fields[4]);
          long volume = Long.parseLong(fields[6]);

          StockInfo stock = new StockInfo(date, open, high, low, close, volume);
          stocksList.add(stock);
        }
      } catch (IOException e) {
        // If the file is not found or there's an error, fall back to API call
        stocksList = getAPIStockData(ticker);
      }
    } else {
      // fall back to API call if file does not exist
      stocksList = getAPIStockData(ticker);
    }

    // Add the fetched stock prices to the map
    STOCK_PRICE_HISTORY.put(ticker, stocksList);
    return stocksList;
  }

  /**
   * Gets the list of stock price information for the given ticker from API.
   *
   * @param ticker the stock ticker symbol to get information for
   * @return the list of StockInfo objects for the given ticker
   */
  private static List<StockInfo> getAPIStockData(String ticker) {
    //the API key needed to use this web service.
    //Please get your own free API key here: https://www.alphavantage.co/
    //Please look at documentation here: https://www.alphavantage.co/documentation/
    //old api key: P9MAGNZA2TQOW1LG
    String apiKey = "6ZFNAYRHG2K7KINU"; //new high volume
    URL url = null;

    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + ticker + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    List<StockInfo> stocksList = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String line;
      //skip the header line
      br.readLine();

      while ((line = br.readLine()) != null) {
        String[] fields = line.split(",");
        String date = fields[0];
        double open = Double.parseDouble(fields[1]);
        double high = Double.parseDouble(fields[2]);
        double low = Double.parseDouble(fields[3]);
        double close = Double.parseDouble(fields[4]);
        Long volume = Long.parseLong(fields[5]);

        StockInfo stock = new StockInfo(date, open, high, low, close, volume);
        stocksList.add(stock);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }

    return stocksList;
  }
}
