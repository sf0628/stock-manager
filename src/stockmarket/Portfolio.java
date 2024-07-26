package stockmarket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import data.AlphaVantage;

/**
 * Represents a portfolio of stocks with a name, list of stocks, and latest date.
 */
@XmlRootElement(name = "Portfolio")
public class Portfolio {
  private String name;
  private List<Stock> stocks;
  private String latestDate;

  /**
   * Default constructor required for JAXB.
   */
  public Portfolio() {
    this.name = "";
    this.stocks = new ArrayList<>();
    this.latestDate = "";
  }

  /**
   * Constructs a Portfolio with the given name, an empty list of stocks, and unset latest date.
   *
   * @param name the name of the portfolio
   */
  public Portfolio(String name) {
    this.name = name;
    this.stocks = new ArrayList<>();
    this.latestDate = "";
  }

  /**
   * Constructs a Portfolio with the given name and stocks and unset latest date.
   *
   * @param name the name of the portfolio
   * @param stocks the stocks in the portfolio
   */
  private Portfolio(String name, List<Stock> stocks) {
    this.name = name;
    this.stocks = stocks;
    this.latestDate = "";
  }

  /**
   * Gets the name of a portfolio.
   * @return the name
   */
  @XmlElement
  public String getName() {
    return name;
  }

  /**
   * Gets the list of stocks of a portfolio.
   * @return a list of stocks
   */
  @XmlElementWrapper(name = "stocks")
  @XmlElement(name = "stock")
  public List<Stock> getStocks() {
    return stocks;
  }

  /**
   * Gets the latest date of a portfolio.
   * @return the latest date
   */
  @XmlElement
  public String getLatestDate() {
    return latestDate;
  }

  /**
   * Updates the globally most recent date if the provided date is more recent.
   * Otherwise, throws exception.
   *
   * @param newDate the new date to check and potentially update
   * @throws IllegalArgumentException if new date is before latestDate
   */
  private void updateLatestDate(String newDate) {
    if (latestDate == null || latestDate.isEmpty()
            || newDate.compareTo(latestDate) >= 0) {
      latestDate = newDate;
    } else {
      throw new IllegalArgumentException("Portfolio operations must be performed chronologically. "
              + "The date " + newDate + " is before the more recent date " + latestDate
              + " previously entered.");
    }
  }

  /**
   * Buys or sells a stock to/from the portfolio given the ticker,
   * number of shares to buy/sell, and date bought/sold.
   *
   * @param ticker   the stock ticker symbol to add/sell
   * @param shares   the number of shares of the stock to add/sell
   * @param date     the date the stock is added/sold
   * @param isAdding whether the stock is being bought or sold
   * @throws IllegalArgumentException when stock market isn't open on that date for that stock or
   *         if new date is before latestDate
   */
  public void updateStock(String ticker, double shares, String date, boolean isAdding) {
    boolean containsStock = false;
    int stockIndex = -1;
    //find stock in stocks list if it's there already
    for (int i = 0; i < stocks.size(); i++) {
      if (stocks.get(i).getTicker().equals(ticker)) {
        containsStock = true;
        stockIndex = i;
        break;
      }
    }

    //throws exception when invalid date
    Utils.checkDate(date, ticker);

    //updates latest date or throws exception if before latest
    updateLatestDate(date);

    if (containsStock) {
      double newShares = stocks.get(stockIndex).getShares();
      if (isAdding) { // buying
        newShares += shares;
      } else { // selling
        if (shares <= newShares) {
          newShares -= shares;
        } else {
          throw new IllegalArgumentException("Cannot sell more shares (" + shares
                  + ") than existing (" + newShares + ")");
        }
      }
      if (newShares > 0) {
        //update the stock with the new shares count
        stocks.set(stockIndex, new Stock(ticker, newShares, date));
      } else {
        //remove the stock if no shares are left
        stocks.remove(stockIndex);
      }
    } else {
      if (isAdding) {
        stocks.add(new Stock(ticker, shares, date));
      } else {
        throw new IllegalArgumentException("Cannot sell a stock (" + ticker + ") that doesn't" +
                " exist in this portfolio '" + name + "'");
      }
    }
  }

  /**
   * Calculates the individual value of a stock in the portfolio on a given date.
   *
   * @param stock     the stock to calculate the value for
   * @param givenDate the date to calculate the value on
   * @return the value of the stock on the given date
   * @throws IllegalArgumentException if date is in the future, invalid, or before latestDate
   */
  private double calculateStockValue(Stock stock, LocalDate givenDate) {
    if (givenDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Date cannot be in the future.");
    }

    //updates latest date or throws exception if before latest
    updateLatestDate(givenDate.toString());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate stockDateAdded = LocalDate.parse(stock.getDateAdded(), formatter);
    //value of stock is 0 if added after given date
    if (stockDateAdded.isAfter(givenDate)) {
      return 0.0;
    }

    //gets list of stock info for a stock in the portfolio
    List<StockInfo> stockInfoList = AlphaVantage.getStock(stock.getTicker());
    boolean dateFound = false;
    LocalDate min = new Utils().minMaxDate(stockInfoList, true);
    LocalDate max = new Utils().minMaxDate(stockInfoList, false);

    if (givenDate.isBefore(min) || givenDate.isAfter(max)) {
      throw new IllegalArgumentException("Date " + givenDate + " is invalid.");
    }

    double closingPrice = 0.0;
    //parse through list to add closing price for the given date if the date is found in the list
    for (StockInfo info : stockInfoList) {
      if (info.getTimestamp().equals(givenDate.toString())) {
        closingPrice = info.getClose();
        dateFound = true;
        break;
      }
    }
    if (!dateFound) {
      throw new IllegalArgumentException(
              "Cannot find stock " + stock.getTicker() + " with this date:" + givenDate);
    }

    return closingPrice * stock.getShares();
  }

  /**
   * Calculates the total value of the portfolio on a given date.
   *
   * @param date the date in the format "YYYY-MM-DD"
   * @return the value of the portfolio on the given date
   * @throws IllegalArgumentException if date is in the future, invalid, or before latestDate
   */
  public double calculateTotalValue(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate givenDate = LocalDate.parse(date, formatter);
    double totalValue = 0.0;

    if (stocks.isEmpty()) {
      if (givenDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Date cannot be in the future.");
      }
      return 0.0;
    }

    for (Stock stock : stocks) {
      totalValue += calculateStockValue(stock, givenDate);
    }

    return totalValue;
  }

  /**
   * Gets the distribution of values of stocks in the portfolio on a specific date.
   *
   * @param date the date in the format "YYYY-MM-DD"
   * @return the distribution of values in the format of a map object with stock ticker
   *         and the stock's individual value
   * @throws IllegalArgumentException if date is in future or invalid
   */
  public Map<String, Double> getDistribution(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate givenDate = LocalDate.parse(date, formatter);
    Map<String, Double> distribution = new HashMap<>();

    if (stocks.isEmpty()) {
      if (givenDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Date cannot be in the future.");
      }
      return distribution;
    }

    for (Stock stock : stocks) {
      double stockValue = calculateStockValue(stock, givenDate);
      if (stockValue > 0) {
        distribution.put(stock.getTicker(), stockValue);
      }
    }

    return distribution;
  }

  /**
   * Gets the composition of shares of stocks in the portfolio on a specific date.
   *
   * @param date the date in the format "YYYY-MM-DD"
   * @return the composition of shares in the format of a map object with stock ticker
   *         and the stock's number of shares
   * @throws IllegalArgumentException if date is in the future, invalid, or before latestDate
   */
  public Map<String, Double> getComposition(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate givenDate = LocalDate.parse(date, formatter);
    Map<String, Double> composition = new HashMap<>();

    if (givenDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Date cannot be in the future.");
    }

    updateLatestDate(date);

    if (stocks.isEmpty()) {
      return composition;
    }

    for (Stock stock : stocks) {
      LocalDate stockDateAdded = LocalDate.parse(stock.getDateAdded(), formatter);
      //put stocks added on or before the given date
      if (!stockDateAdded.isAfter(givenDate)) {
        composition.put(stock.getTicker(), stock.getShares());
      }
    }

    return composition;
  }

  /**
   * Rebalances the shares of stocks in the portfolio on a specific date
   * so distribution of value of the portfolio matches the intended weights.
   *
   * @param percentages the intended distribution percentages
   * @param date the date  in the format "YYYY-MM-DD"
   * @throws IllegalArgumentException if percentage list is empty or doesn't match portfolio size
   *         or if date is in future, invalid, or before latestDate
   */
  public void rebalance(List<Integer> percentages, String date) {
    if (percentages == null || percentages.isEmpty() || percentages.size() != stocks.size()) {
      throw new IllegalArgumentException("Percentages list cannot be empty " +
              "and must match the number of stocks in the portfolio.");
    }

    double totalValue = calculateTotalValue(date);

    for (int i = 0; i < percentages.size(); i++) {
      Stock stock = this.stocks.get(i);
      double intendedStockValue = totalValue * (percentages.get(i) / 100.0);
      double actualStockValue = calculateStockValue(stock,
              LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      double pricePerShare = actualStockValue / stock.getShares();

      double valueDiff = Math.abs(actualStockValue - intendedStockValue);
      double updateSharesBy = valueDiff / pricePerShare;
      //double newShares = intendedStockValue / pricePerShare;

      if (actualStockValue > intendedStockValue) { //sell to rebalance
        updateStock(stock.getTicker(), updateSharesBy, date, false);
      } else if (actualStockValue < intendedStockValue) { //buy to rebalance
        updateStock(stock.getTicker(), updateSharesBy, date, true);
      }
    }
  }

  /**
   * Overrides the equals method and evaluates a portfolio with the same stocks in
   * the same order and the same portfolio name as equal to on another.
   *
   * @param o an object
   * @return a boolean representing equality
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Portfolio)) {
      return false;
    }
    Portfolio portfolio = (Portfolio) o;

    //check name equality first
    if (!Objects.equals(name, portfolio.getName())) {
      return false;
    }

    //check stocks size first for early return
    if (stocks.size() != portfolio.stocks.size()) {
      return false;
    }

    //check latest date
    if (!Objects.equals(latestDate, portfolio.latestDate)) {
      return false;
    }

    //check each stock
    for (int i = 0; i < stocks.size(); i++) {
      if (!stocks.get(i).equals(portfolio.stocks.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Overrides the hashcode of portfolio to be equal.
   *
   * @return the hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, stocks, latestDate);
  }

  /**
   * Builder class for Portfolio. This builds a Portfolio object that takes in fields
   * incrementally.
   */
  public static class PortfolioBuilder {
    private String name;
    private List<Stock> stocks = new ArrayList<>();
    private String latestDate;

    /**
     * Constructs a PortfolioBuilder with fields initialized to an empty
     * string and array.
     */
    public PortfolioBuilder() {
      this.name = "";
      this.stocks = new ArrayList<>();
      this.latestDate = "";
    }

    /**
     * Adds a name to the Portfolio being built.
     * @param name name of the portfolio
     * @return the current updated builder
     */
    public PortfolioBuilder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Adds stocks to the list of stocks in the portfolio builder.
     * @param stocks the list of stocks of a portfolio
     * @return the current updated builder
     */
    public PortfolioBuilder addAllStock(List<Stock> stocks) {
      this.stocks.addAll(stocks);
      return this;
    }

    /**
     * Builds a Portfolio object with the previously inputted fields of the
     * portfolio builder.
     * @return a portfolio
     */
    public Portfolio build() {
      return new Portfolio(name, stocks);
    }
  }
}
