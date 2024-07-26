package stockmarket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.AlphaVantage;

/**
 * Represents the model in the stock market application.
 * Handles functionality with core stock operations.
 */
public class StockModel implements Model {
  //formats a string into a date time
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final List<Portfolio> portfolios;
  private final Map<String, String> loadablePortfolios; //portfolio name -> file name

  /**
   * Manually constructs a stock model.
   * @param portfolios a list of portfolios that each have a string for the ticker symbol
   *                   and a list of the recent stocks.
   */
  public StockModel(List<Portfolio> portfolios) {
    this.portfolios = portfolios;
    this.loadablePortfolios = new HashMap<>();
    loadablePortfolios.put("Testing", "Testing.xml");
  }

  /**
   * Constructs a stock model with no portfolios yet.
   */
  public StockModel() {
    this.portfolios = new ArrayList<Portfolio>();
    this.loadablePortfolios = new HashMap<>();
    loadablePortfolios.put("Testing", "Testing.xml");
  }

  /**
   * Gets the list of portfolios of this stock model.
   * @return a list of portfolios
   */
  public List<Portfolio> getPortfolios() {
    return portfolios;
  }

  /**
   * Gets the map with the loadable portfolio names to the file name associated
   * with it.
   * @return the map of loadable portfolios
   */
  public Map<String, String> getLoadablePortfolios() {
    return loadablePortfolios;
  }

  @Override
  public double calculateGainLoss(String ticker, String startDate,
                                  String endDate) {
    List<StockInfo> stockInfo = AlphaVantage.getStock(ticker);
    //checks if dates and ticker are valid
    Utils.checkDateStartEnd(startDate, endDate);
    Utils.validTicker(ticker);

    Double startClose = null;
    Double endClose = null;

    //get the closing value for the start date and end date
    for (StockInfo stock : stockInfo) {
      if (stock.getTimestamp().equals(startDate)) {
        startClose = stock.getClose();
      }
      if (stock.getTimestamp().equals(endDate)) {
        endClose = stock.getClose();
      }
    }

    //throws exception if either dates are not found in stockInfo
    if (startClose == null) {
      throw new IllegalArgumentException("Start date was not found: " + startDate + ".");
    }
    if (endClose == null) {
      throw new IllegalArgumentException("End date was not found: " + endDate + ".");
    }

    //returns the different
    return endClose - startClose;
  }

  @Override
  public double calculateMovingAverage(String ticker, String date,
                                       int x) {
    Utils.validTicker(ticker);
    List<StockInfo> stockInfo = AlphaVantage.getStock(ticker);
    double total = 0.0;
    Integer dateIndex = null;
    LocalDate minDate = Utils.minMaxDate(stockInfo, true);
    boolean isReverseOrder = stockInfo.get(0).getTimestamp()
            .compareTo(stockInfo.get(stockInfo.size() - 1).getTimestamp()) > 0;
    //find the index of the stock with the given date
    for (int i = 0; i < stockInfo.size(); i++) {
      if (stockInfo.get(i).getTimestamp().equals(date)) {
        dateIndex = i;
      }
    }

    //throws exception if date index could not be found
    if (dateIndex == null) {
      throw new IllegalArgumentException("Date was not found: " + date + ".");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate current = LocalDate.parse(date , formatter);
    for (int i = 0; i < x; i ++) {
      boolean nextDateFound = false;
      while (!nextDateFound && minDate.isBefore(current)) {
        if (isReverseOrder) {
          if (stockInfo.get(dateIndex + i) != null) {
            total += stockInfo.get(dateIndex + i).getClose();
            nextDateFound = true;
          }
          current = current.plusDays(1);
        } else {
          if (stockInfo.get(dateIndex - i) != null) {
            total += stockInfo.get(dateIndex - i).getClose();
            nextDateFound = true;
          }
          current = current.plusDays(-1);
        }
      }
      if (!nextDateFound) {
        throw new IllegalArgumentException("Date was not found: "
                + current + ".");
      }
    }
    return total / x;
  }

  @Override
  public List<String> calculateXDayCrossover(String ticker, String startDate,
                                             String endDate, int x) {
    //checks for invalid inputs
    Utils.validTicker(ticker);
    Utils.checkDateStartEnd(startDate, endDate);
    List<String> crossoverDates = new ArrayList<>();
    List<StockInfo> stockInfo = AlphaVantage.getStock(ticker);
    if (!Utils.inRange(startDate, endDate, x, stockInfo)) {
      throw new IllegalArgumentException("Start date or end date was not in range.");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDate current = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    Map<String, Double> movingAverages = new HashMap<>();

    // Precompute moving averages for all dates in the range
    for (StockInfo stock : stockInfo) {
      LocalDate stockDate = LocalDate.parse(stock.getTimestamp(), formatter);
      if ((stockDate.isEqual(current) || stockDate.isAfter(current)) && (stockDate.isEqual(end)
              || stockDate.isBefore(end))) {
        double movingAverage = calculateMovingAverage(ticker, stock.getTimestamp(), x);
        movingAverages.put(stock.getTimestamp(), movingAverage);
      }
    }
    while (current.isBefore(end) || current.isEqual(end)) {
      String curr = current.format(formatter);
      if (movingAverages.containsKey(curr)) {
        double currentMovingAverage = movingAverages.get(curr);
        for (StockInfo stock : stockInfo) {
          if (stock.getTimestamp().equals(curr) && stock.getClose() > currentMovingAverage) {
            crossoverDates.add(curr);
          }
        }
      }
      current = current.plusDays(1);
    }
    return crossoverDates;
  }

  @Override
  public void createPortfolio(String portfolioName) {
    if (portfolioName == null || portfolioName.isEmpty()) {
      throw new IllegalArgumentException("Portfolio name was not provided.");
    }
    for (Portfolio portfolio : this.portfolios) {
      if (portfolio.getName().equals(portfolioName)) {
        throw new IllegalArgumentException("Portfolio already exists.");
      }
    }
    portfolios.add(new Portfolio(portfolioName));
  }

  @Override
  public Portfolio findPortfolio(String portfolioName) {
    for (Portfolio portfolio : this.portfolios) {
      if (portfolio.getName().equals(portfolioName)) {
        return portfolio;
      }
    }
    throw new IllegalArgumentException("Portfolio not found.");
  }

  @Override
  public void updateStockInPortfolio(String portfolioName, String ticker, double shares,
                                     String date, boolean isAdding) {
    //cannot buy fractional shares
    if (isAdding && shares % 1 != 0) {
      throw new IllegalArgumentException("Cannot buy fractional shares. " +
              "Please enter a whole number of shares.");
    }

    List<StockInfo> stockInfo = AlphaVantage.getStock(ticker);
    Portfolio portfolio = findPortfolio(portfolioName);

    //adds/deletes stock to/from portfolio
    portfolio.updateStock(ticker, shares, date, isAdding);
  }

  @Override
  public double calculatePortfolioValue(String portfolioName, String date) {
    Portfolio portfolio = findPortfolio(portfolioName);

    return portfolio.calculateTotalValue(date);
  }

  @Override
  public Map<String, Double> getPortfolioDistribution(String portfolioName, String date) {
    Portfolio portfolio = findPortfolio(portfolioName);

    return portfolio.getDistribution(date);
  }

  @Override
  public Map<String, Double> getPortfolioComposition(String portfolioName, String date) {
    Portfolio portfolio = findPortfolio(portfolioName);

    return portfolio.getComposition(date);
  }

  @Override
  public void rebalancePortfolio(String portfolioName, List<Integer> percentages, String date) {
    Portfolio portfolio = findPortfolio(portfolioName);

    int totalPercentage = 0;

    for (Integer percentage : percentages) {
      totalPercentage += percentage;
    }

    if (totalPercentage != 100) {
      throw new IllegalArgumentException("Percentages must add up to 100 to rebalance.");
    }

    portfolio.rebalance(percentages, date);
  }

  //between 5 and 30 lines, unless the time window is less than 5 days
  private Timespan getTimespan(String startDate, String endDate) {
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    Utils.checkDateStartEnd(startDate, endDate);
    int difference = (int) ChronoUnit.DAYS.between(start, end);
    //fields of timespan
    boolean isYear = false;
    boolean isMonth = false;
    boolean isDay = false;
    int count = 0;

    if (difference <= 5) { //when smaller than 5 days
      isDay = true;
      count = 1;
    } else if (difference < 30 * 5) { //when greater than 5 days, less than five lines for month
      isDay = true;
      if (difference / 20 < 1) {
        count = 1;
      } else {
        count = difference / 20;
      }
    } else if (difference < 365 / 12 * 30) { //when greater than 5 lines for month, one count
      isMonth = true;
      count = 1;
    } else if (difference < 365 * 5) { //when less than 5 years
      isMonth = true;
      count = difference / 30 / 20; // makes it 20 lines
    } else if (difference < 365 * 30) { //when less than 30 years
      isYear = true;
      count = 1;
    } else { //when greater than 30 years
      isYear = true;
      count = difference / 365 / 20; // makes it 20 lines
    }
    return new Timespan(isYear, isMonth, isDay, count);
  }

  //get default scale of a portfolio, maximum number of asterisks is 40
  private Scale getDefaultScale(Portfolio portfolio, List<LocalDate> dates, boolean isAbsolute) {
    List<Stock> stocks = portfolio.getStocks();
    Double minValue = null;
    Double maxValue = null;

    //scale field values
    int base = 0;
    int scale = 0;

    for (LocalDate date : dates) {
      //find the minimum value of the dates for relative scale
      if (minValue == null) {
        minValue = portfolio.calculateTotalValue(formatter.format(date));
      } else if (minValue > portfolio.calculateTotalValue(formatter.format(date))) {
        minValue = portfolio.calculateTotalValue(formatter.format(date));
      }


      //find the maximum value of the dates
      if (maxValue == null) {
        maxValue = portfolio.calculateTotalValue(formatter.format(date));
      } else if (maxValue < portfolio.calculateTotalValue(formatter.format(date))) {
        maxValue = portfolio.calculateTotalValue(formatter.format(date));
      }
    }

    //assign values for scale
    //when relative
    // 40 = (max - min) * scale
    // 40 / scale = (max - min)
    // scale = 40 / (max - min)
    if (minValue != null) {
      base = minValue.intValue();
      scale = (int) (40 / (maxValue - minValue));
    }
    //when absolute
    else {
      scale = 40 / maxValue.intValue();
    }
    return new Scale(base, scale);
  }

  //get default scale of a stock
  private Scale getDefaultScale(List<StockInfo> stocks, boolean isAbsolute) {
    Double minValue = Utils.minMaxStock(stocks, true).getClose();
    Double maxValue = Utils.minMaxStock(stocks, false).getClose();

    //scale field values
    int base = 0;
    int scale = 0;

    //assign values for scale
    //when relative
    if (isAbsolute) {
      scale = (int) (40 / maxValue.doubleValue());
    }
    //when absolute
    else {
      scale = (int) (40 / (maxValue.doubleValue() - minValue.doubleValue()));
      base = minValue.intValue();
    }
    return new Scale(base, scale);
  }

  //get asterisks for a portfolio, max asterisks: 40
  private List<String> getAsterisks(Portfolio portfolio, Scale scale, List<LocalDate> timeList,
                                     Timespan timespan) {
    List<Double> portfolioValues = new ArrayList<>();
    List<Integer> asterisksCount = new ArrayList<>();
    List<String> portfolioAsterisks = new ArrayList<>();

    //gets the portfolio value for each date in the list of dates
    for (LocalDate date : timeList) {
      double portfolioValue = 0.0;
      //gets the correct stock, or increments
      for (Stock stock : portfolio.getStocks()) {
        List<StockInfo> stockInfos = AlphaVantage.getStock(stock.getTicker());
        //gets value of the stock at given date, adds to portfolio value of that date
        for (StockInfo stockInfo : stockInfos) {
          if (stockInfo.getTimestamp().equals(formatter.format(date))) {
            //multiply closing value at date and the number of shares
            portfolioValue += stockInfo.getClose() * stock.getShares();
          }
        }
      }
      portfolioValues.add(portfolioValue);
    }

    //calculate number of asterisks given the scale, converts into string format
    int num = 0;
    //adds asterisks for each date
    for (Double value : portfolioValues) {
      //calculates number of asterisks
      //if absolute, base of scale would already be zero
      num = (int) ((value - scale.getBase()) / scale.getScale());
      asterisksCount.add(num);
    }

    for (Integer value : asterisksCount) {
      int count = value;
      String asterisks = "";
      while (count >= 0) {
        asterisks += "*";
        count -= 1;
      }
      portfolioAsterisks.add(asterisks);
    }

    return portfolioAsterisks;
  }

  //get asterisks for a stock
  private List<String> getAsterisks(List<StockInfo> stocks, Scale scale, List<LocalDate> timeList) {
    List<Integer> asterisks = new ArrayList<>();
    List<String> result = new ArrayList<>();

    for (LocalDate date : timeList) {
      int count = 0;
      String asterisksString = "*";
      //gets the correct stock closing value
      for (StockInfo stockInfo : stocks) {
        if (stockInfo.getTimestamp().equals(formatter.format(date))) {
          count = (int) ((stockInfo.getClose() - scale.getBase()) / scale.getScale());
        }
      }

      while (count >= 0) {
        asterisksString += "*";
        count -= 1;
      }

      result.add(asterisksString);
    }
    return result;
  }

  @Override
  public List<String> visualizePerformanceOverTime(String name, String startDate, String endDate,
                                                   boolean isPortfolio, boolean isAbsolute) {
    List<String> visualization = new ArrayList<>();
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);

    //data being visualized can either be from a stock or a portfolio
    List<StockInfo> stockInfo = null;
    Portfolio portfolio = null;

    //visualization constants
    List<LocalDate> timeList = null;
    List<String> asterisks = null;
    Scale scale = null;
    Timespan timespan = getTimespan(startDate, endDate);

    if (isPortfolio) {
      portfolio = findPortfolio(name);
      //using first
      timeList = timespan.getDateIncrements(startDate, endDate, portfolio.getStocks()
              .get(0).getTicker());
      scale = getDefaultScale(portfolio, timeList, isAbsolute);
      asterisks = getAsterisks(portfolio, scale, timeList, timespan);
    } else { //when it's a stock
      //gets the correct stock info from the API
      stockInfo = AlphaVantage.getStock(name);
      timeList = timespan.getDateIncrements(startDate, endDate, name);
      scale = getDefaultScale(stockInfo, isAbsolute);
      asterisks = getAsterisks(stockInfo, scale, timeList);
    }

    //time list and asterisk list must match
    if (timeList.size() != asterisks.size()) {
      throw new IllegalArgumentException("Time list mismatch.");
    }
    //convert list of LocalDates to String format
    List<String> timeListString = timespan.convertIncrements(timeList);

    //adds each line to the lines
    String type = "";
    if (isPortfolio) {
      type = "portfolio";
    } else {
      type = "stock";
    }
    visualization.add("Performance of " + type + " '" + name + "' from " + startDate + " to "
            + endDate + ": ");
    //skips a line
    visualization.add("");
    for (int i = 0; i < asterisks.size(); i++) {
      visualization.add(timeListString.get(i) + ": " + asterisks.get(i));
    }
    //skips a line
    visualization.add("");
    if (!isAbsolute) {
      visualization.add("Base value: " + scale.getBase());
    }
    visualization.add("Scale: * = " + scale.getScale());


    return visualization;
  }

  @Override
  public void savePortfolio(String portfolioName, String fileName) {
    Portfolio portfolio = findPortfolio(portfolioName);
    this.portfolios.remove(portfolio);
    boolean result = XMLParser.toXML(portfolio, fileName);
    loadablePortfolios.put(portfolioName, fileName + ".xml");
  }

  @Override
  public void loadPortfolio(String fileName) {
    Portfolio portfolio = XMLParser.fromXML(fileName);
    Portfolio existingPortfolio = findPortfolio(portfolio.getName());
    if (existingPortfolio == null) {
      this.portfolios.add(XMLParser.fromXML(fileName));
    } else if (existingPortfolio.equals(portfolio)) {
      this.portfolios.remove(existingPortfolio);
      this.portfolios.add(portfolio);
    } else {
      throw new IllegalArgumentException("Portfolio already exists.");
    }
  }
}
