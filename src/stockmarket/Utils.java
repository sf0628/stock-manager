package stockmarket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import data.AlphaVantage;

/**
 * This class represents the utilities of our stocks program. They serve as various checks
 * that are used to prevent error and throw exceptions in the other parts of the program.
 */
public class Utils {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Given a list of stock information, the method finds either the most recent, or the
   * oldest date.
   * @param stockInfo a list of stock information including timestamps
   * @param isMin boolean value that represents either most recent or oldest
   * @return a date in string form
   */
  public static LocalDate minMaxDate(List<StockInfo> stockInfo, boolean isMin) {
    LocalDate result = LocalDate.parse(stockInfo.get(0).getTimestamp(), formatter);
    for (StockInfo stock : stockInfo) {
      LocalDate currentDate = LocalDate.parse(stock.getTimestamp(), formatter);
      if (isMin && currentDate.isBefore(result)) {
        result = currentDate;
      } else if (!isMin && currentDate.isAfter(result)) {
        result = currentDate;
      }
    }
    return result;
  }

  /**
   * Given a list of stock information, the method finds the stock with either the
   * highest or lowest closing value.
   * @param stockInfo a list of stock information including timestamps
   * @param isMin boolean value that represents either most recent or oldest
   * @return the stock of the minimum or mia
   */
  public static StockInfo minMaxStock(List<StockInfo> stockInfo, boolean isMin) {
    StockInfo result = stockInfo.get(0);
    for (StockInfo stock : stockInfo) {
      if (isMin && result.getClose() > stock.getClose()) {
        result = stock;
      } else if (!isMin && result.getClose() < stock.getClose()) {
        result = stock;
      }
    }
    return result;
  }

  /**
   * This method takes in two dates, an x-value, and a list of stocks to determine
   * if that form each date with x-day prior to that day is in range of all the date
   * values given.
   * @param startDate the start date
   * @param endDate the end date
   * @param x number of days away from any given day (x-value)
   * @param stockInfo list of stock information
   * @return
   */
  public static boolean inRange(String startDate, String endDate, int x,
                                List<StockInfo> stockInfo) {
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    LocalDate last = start.minusDays(x);
    boolean validLast = true;
    boolean validEnd = true;
    boolean validStart = true;
    //first checks if start date comes before end date
    Utils.checkDateStartEnd(startDate, endDate);
    if (last.isBefore(Utils.minMaxDate(stockInfo, true))) {
      validLast = false;
    }
    if (end.isAfter(Utils.minMaxDate(stockInfo, false))) {
      validEnd = false;
    }

    if (start.isAfter(end)) {
      validStart = false;
    }

    return validLast && validEnd && validStart;
  }

  /**
   * This method checks that given two dates, the start date goes before
   * the end date.
   * @param startDate a starting date in string form
   * @param endDate an end date in string form
   */
  public static void checkDateStartEnd(String startDate, String endDate) {
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
  }

  /**
   * This method checks that given the date and the ticker, date exists
   * in the stock data.
   * @param date any given date
   * @param ticker the ticker symbol for a stock
   *
   */
  public static StockInfo checkDate(String date, String ticker) {
    List<StockInfo> stockInfo = AlphaVantage.getStock(ticker);

    for (StockInfo stock : stockInfo) {
      if (stock.getTimestamp().equals(date)) {
        return stock;
      }
    }
    throw new IllegalArgumentException(date + " does not exist in this stock " + ticker);
  }

  /**
   * This method validates if a ticker is in the valid format. A valid ticker in one
   * that only contains capitalized letters (no symbols, no digits), and is maximum
   * four letters only.
   * @param ticker ticker symbol of a stock
   */
  public static void validTicker(String ticker) {
    if (ticker == null || ticker.isEmpty()
            || !ticker.matches("[A-Z]{1,4}")) {
      throw new IllegalArgumentException("Invalid ticker: " + ticker
              + ". Only capitalized letter, 4 letter maximum");
    }
  }

  /**
   * Validates that the year is in a correct four digit format.
   * @param year a given integer value
   */
  public static void validYear(int year) {
    String yearString = Integer.toString(year);
    if (yearString == null || yearString.isEmpty()) {
      throw new IllegalArgumentException("Invalid year: " + year);
    }
    if (!yearString.matches("[0-9]{4}")) {
      throw new IllegalArgumentException("Invalid year: " + year);
    }
  }

  /**
   * Validates that the month is valid. A valid month is positive and less than or equal
   * to 12.
   * @param month the given month integer
   */
  public static void validMonth(int month) {
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month: " + month);
    }
  }

  /**
   * Validates that the day value is valid. A valid day is positive and less than or equal
   * to 31.
   * @param day the given day integer
   */
  public static void validDay(int day) {
    if (day < 1 || day > 31) {
      throw new IllegalArgumentException("Invalid day: " + day);
    }
  }

}
