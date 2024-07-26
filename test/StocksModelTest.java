import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import data.AlphaVantage;
import stockmarket.Portfolio;
import stockmarket.Stock;
import stockmarket.StockModel;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the StockModel class methods. This includes all the methods uses to calculate
 * information and add/change to a portfolio.
 */
public class StocksModelTest {
  StockModel stockModel;

  /**
   * Sets up the stock model and makes sure that the Google stock information
   * is stored in a static field.
   */
  @Before
  public void setUp() {
    stockModel = new StockModel();
    AlphaVantage.getStock("GOOG");
  }

  /**
   * Tests that the calculateGainLoss method accurately calculates the change in closing
   * price given the ticker value, start date, and endDate.
   */
  @Test
  public void calculateGainLoss() {
    //calculate gain
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2024-05-28", "2024-05-29"), 0.01);
    //calculate loss
    assertEquals(0.42, stockModel.calculateGainLoss("GOOG",
            "2024-05-08", "2024-05-09"), 0.01);
  }

  /**
   * Tests that calculateGainLoss method throws exception when start date
   * comes after end date.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateGainLossEndBeforeStart() {
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2024-05-29", "2024-05-28"), 0.01);
  }

  /**
   * Tests that calculateGainLoss method throws exception when there is an invalid
   * start date (weekend date).
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidStartDate() {
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2024-05-27", "2024-05-28"), 0.01);
  }

  /**
   * Tests that calculateGainLoss method throws exception when there is an invalid
   * end date (weekend date).
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidEndDate() {
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2024-05-24", "2024-05-27"), 0.01);
  }

  /**
   * Tests that calculateGainLoss method throws exception when there is an invalid
   * end date (future date).
   */
  @Test (expected = IllegalArgumentException.class)
  public void testFutureEndDate() {
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2024-05-24", "2025-05-27"), 0.01);
  }

  /**
   * Tests that calculateGainLoss method throws exception when there is an invalid
   * start date (too much in the past).
   */
  @Test (expected = IllegalArgumentException.class)
  public void testOutdatedStartDate() {
    assertEquals(-0.62, stockModel.calculateGainLoss("GOOG",
            "2000-05-24", "2024-05-27"), 0.01);
  }

  /**
   * Tests that the calculateGainLoss method throws exception when an invalid
   * ticker has been inputted.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateGainLossWithInvalidTicker() {
    assertEquals(-0.62, stockModel.calculateGainLoss("ABDDSS",
            "2024-05-24", "2024-05-27"), 0.01);
  }

  /**
   * Tests various cases of CalculateMovingAverage method with various ticker value,
   * dates, and x values.
   */
  @Test
  public void calculateMovingAverage() {
    //non-consecutive, skips weekend
    assertEquals(176.47, stockModel.calculateMovingAverage(
            "GOOG", "2024-05-28", 3), 0.01);
    //also skips weekend
    assertEquals(177.17, stockModel.calculateMovingAverage(
            "GOOG", "2024-05-28", 2), 0.01);
    assertEquals(21.39, stockModel.calculateMovingAverage(
            "GME", "2024-05-28", 2), 0.01);
    //consecutive weekday
    assertEquals(20.74, stockModel.calculateMovingAverage(
            "GME", "2024-05-24", 5), 0.01);

  }

  /**
   * Tests that CalculateMovingAverage method throws exception when the date is in the future.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateMovingAverageFutureDate() {
    assertEquals(176.47, stockModel.calculateMovingAverage(
            "GOOG", "2025-05-28", 3), 0.01);
  }

  /**
   * Tests that CalculateMovingAverage method throws exception when the x value
   * causes the date to go too far back.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateMovingAverageInvalidX() {
    assertEquals(176.47, stockModel.calculateMovingAverage(
            "GOOG", "2013-08-19", 3), 0.01);
  }

  /**
   * Tests that CalculateMovingAverage method throws exception given an invalid ticker value.
   */
  @Test (expected = Exception.class)
  public void calculateMovingAverageInvalidTicker() {
    assertEquals(176.47, stockModel.calculateMovingAverage(
            "ASSFDSD", "2024-05-28", 3), 0.01);
  }

  /**
   * Tests various cases of calculating x-day crossovers for various ranges of dates,
   * x values, and ticker values. This includes consecutive and non-consecutive days.
   */
  @Test
  public void calculateXDayCrossover() {
    List<String> result = new ArrayList<>();
    result.add("2024-05-28");
    //consecutive days
    assertEquals(result,
            stockModel.calculateXDayCrossover("GOOG",
                    "2024-05-28", "2024-05-29", 2));
    result.clear();
    result.add("2024-04-24");
    result.add("2024-04-26");
    //non-consecutive days
    assertEquals(result,
            stockModel.calculateXDayCrossover("GOOG",
                    "2024-04-24", "2024-04-29", 2));
    result.clear();
    result.add("2024-04-24");
    result.add("2024-04-26");
    result.add("2024-04-29");
    assertEquals(result,
            stockModel.calculateXDayCrossover("GOOG",
                    "2024-04-24", "2024-04-29", 5));
    result.clear();
    result.add("2024-04-26");
    result.add("2024-04-29");
    assertEquals(result,
            stockModel.calculateXDayCrossover("AMZN",
                    "2024-04-24", "2024-04-29", 5));

  }

  /**
   * Tests that calculateXDayCrossover method throws exceptions for
   * invalid ticker values.
   */
  @Test (expected = Exception.class)
  public void calculateXDayCrossoverInvalidTicker() {
    assertEquals(new ArrayList<>(),
            stockModel.calculateXDayCrossover("DJSKFKSK",
                    "2024-04-24", "2024-04-29", 5));
  }

  /**
   * Tests that calculateXDayCrossover method throws exception for
   * future dates.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateXDayCrossoverFutureDates() {
    assertEquals(new ArrayList<>(),
            stockModel.calculateXDayCrossover("GOOG",
                    "2025-04-24", "2025-04-29", 5));
  }

  /**
   * Tests that dates that are too old causes calculateXDayCrossover method
   * to throw exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateXDayCrossoverTooOldDates() {
    assertEquals(new ArrayList<>(),
            stockModel.calculateXDayCrossover("GOOG",
                    "2000-04-24", "2000-04-29", 5));
  }

  /**
   * Tests that invalid x days that cause the final date to be out of range
   * causes calculateXDayCrossover method to throw exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculateXDayCrossoverValidDatesInvalidX() {
    assertEquals(new ArrayList<>(),
            stockModel.calculateXDayCrossover("GOOG",
                    "2013-08-19", "2013-08-20", 10));
  }

  /**
   * Tests that calculateXDayCrossover method throws exception start date comes
   * after end date.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calcualteXDayCrossoverStartDateInvalid() {
    assertEquals(new ArrayList<>(),
            stockModel.calculateXDayCrossover("GOOG",
                    "2013-08-24", "2013-08-23", 1));
  }

  /**
   * Tests that createPortfolio method properly updates the list of portfolios
   * in StockModel. New portfolio's added should have corresponding titles and
   * be empty for the rest of the fields.
   */
  @Test
  public void createPortfolio() {
    List<Portfolio> portfolios = new ArrayList<>();
    //test empty portfolio
    assertEquals(portfolios, stockModel.getPortfolios());
    //tests adding one portfolio
    stockModel.createPortfolio("First Portfolio");
    portfolios.add(new Portfolio("First Portfolio"));
    assertEquals(portfolios, stockModel.getPortfolios());
    //tests adding another portfolio
    stockModel.createPortfolio("Second Portfolio");
    portfolios.add(new Portfolio("Second Portfolio"));
    assertEquals(portfolios, stockModel.getPortfolios());
  }

  /**
   * Tests that findPorfolio finds the correct portfolio with matching names in a stock model
   * with a list of portfolios.
   */
  @Test
  public void testFindPortfolio() {
    Portfolio portfolio1 = new Portfolio("Find this 1");
    Portfolio portfolio2 = new Portfolio("Find this 2");
    stockModel.createPortfolio("Find this 1");
    stockModel.createPortfolio("Find this 2");
    assertEquals(portfolio1, stockModel.findPortfolio("Find this 1"));
    assertEquals(portfolio2, stockModel.findPortfolio("Find this 2"));
  }

  /**
   * Tests that exception will be thrown when trying to find a portfolio that doesn't exist.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testFindPortfolioInvalid() {
    stockModel.findPortfolio("Find this 1");
  }

  /**
   * Tests that exception is thrown when trying to create a portfolio that already exists.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testPortfolioAlreadyExists() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.createPortfolio("First Portfolio");
  }

  /**
   * Tests that incrementally adding stocks to a portfolio properly updates the
   * list of stocks within a portfolio. Additionally, each time the stock is added,
   * tests that calculatePortfolioValue correctly calculates the value of the result.
   */
  @Test
  public void addStockToPortfolioAndCalculatePortfolioValue() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.createPortfolio("Second Portfolio");
    List<Portfolio> portfolios =
            new ArrayList<>(Arrays.asList(new Portfolio("First Portfolio"),
                    new Portfolio("Second Portfolio")));
    //tests that the portfolios are properly added
    assertEquals(portfolios, stockModel.getPortfolios());
    //tests that each portfolio is empty
    assertEquals(new ArrayList<Stock>(),
            stockModel.getPortfolios().get(0).getStocks());
    assertEquals(new ArrayList<Stock>(),
            stockModel.getPortfolios().get(1).getStocks());
    //test that the portfolio has a value of zero
    assertEquals(0.0, stockModel.calculatePortfolioValue("First Portfolio",
            "2024-05-13"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-13"), 0.01);
    //add stock to a portfolio
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    //tests that the first array has a list of one stock
    assertEquals(new ArrayList<Stock>(Arrays.asList(new Stock("GOOG", 8,
                    "2024-05-13"))),
            stockModel.getPortfolios().get(0).getStocks());
    //test that the portfolio value is accurate for various days
    assertEquals(1367.20, stockModel.calculatePortfolioValue(
            "First Portfolio","2024-05-13"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-13"), 0.01);
    assertEquals(1375.44, stockModel.calculatePortfolioValue(
            "First Portfolio","2024-05-14"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-14"), 0.01);
    //tests the stock that has been added has appropriate shares and tickers
    assertEquals(8, stockModel.getPortfolios().get(0).getStocks().get(0).getShares(),
            0.01);
    assertEquals("GOOG", stockModel.getPortfolios().get(0).getStocks().get(0).getTicker());
    //tests that the second portfolio has not been changed
    assertEquals(new ArrayList<Stock>(),
            stockModel.getPortfolios().get(1).getStocks());
    //add another share of stock to a portfolio
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 1,
            "2024-05-15", true);
    //tests that adding another share of the same stock increases the number of shares appropriately
    assertEquals(9, stockModel.getPortfolios().get(0).getStocks().get(0).getShares(),
            0.01);
    assertEquals("GOOG", stockModel.getPortfolios().get(0).getStocks().get(0).getTicker());
    //test that the portfolio value is accurate for various days
    assertEquals(1564.92, stockModel.calculatePortfolioValue(
            "First Portfolio", "2024-05-15"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-15"), 0.01);
    assertEquals(1578.87, stockModel.calculatePortfolioValue(
            "First Portfolio", "2024-05-16"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-16"), 0.01);
    //add another different stock
    stockModel.updateStockInPortfolio("First Portfolio", "NKE", 1,
            "2024-05-17", true);
    //test that adding a second different stock adds to the list of portfolios
    assertEquals(1, stockModel.getPortfolios().get(0).getStocks().get(1).getShares(),
            0.01);
    assertEquals("NKE", stockModel.getPortfolios().get(0).getStocks().get(1).getTicker());
    //test that the portfolio value is accurate for various days
    assertEquals(1697.91, stockModel.calculatePortfolioValue(
            "First Portfolio", "2024-05-20"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-20"), 0.01);
    assertEquals(1708.68, stockModel.calculatePortfolioValue(
            "First Portfolio", "2024-05-21"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-21"), 0.01);
  }

  /**
   * Tests that adding to a portfolio that doesn't exist throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void addStockToPortfolioPortfolioDoesNotExist() {
    stockModel.updateStockInPortfolio("Fake Portfolio", "GOOG", 1,
            "2023-01-04", true);
  }

  /**
   * Tests that buying fractional shares from a portfolio throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void buyFractionalShares() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "AAL", 3.4,
            "2023-05-01", true);
  }

  /**
   * Tests that calculating portfolio value of a portfolio that doesn't exists
   * throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculatePortfolioValuePortfolioDoesNotExist() {
    stockModel.calculatePortfolioValue("Fake Portfolio", "2024-05-28");
  }

  /**
   * Tests that calculating the value of a portfolio with an invalid date throws
   * exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void calculatePortfolioValueInvalidDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.calculatePortfolioValue("First Portfolio", "2025-05-28");
  }

  /**
   * Tests that incrementally selling stocks from a portfolio properly updates the
   * list of stocks within a portfolio. Additionally, each time a stock is sold,
   * tests that calculatePortfolioValue correctly calculates the value of the result.
   */
  @Test
  public void sellStockFromPortfolioAndCalculatePortfolioValue() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.createPortfolio("Second Portfolio");
    List<Portfolio> portfolios =
            new ArrayList<>(Arrays.asList(new Portfolio("First Portfolio"),
                    new Portfolio("Second Portfolio")));
    //tests that the portfolios are properly added
    assertEquals(portfolios, stockModel.getPortfolios());
    //tests that each portfolio is empty
    assertEquals(new ArrayList<Stock>(),
            stockModel.getPortfolios().get(0).getStocks());
    assertEquals(new ArrayList<Stock>(),
            stockModel.getPortfolios().get(1).getStocks());
    //add stocks to a portfolio
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "AAPL", 5,
            "2024-05-13", true);
    //test that the portfolio has the correct stocks and values
    assertEquals(new ArrayList<Stock>(Arrays.asList(new Stock("GOOG", 8,
                    "2024-05-13"), new Stock("AAPL", 5,
                    "2024-05-13"))),
            stockModel.getPortfolios().get(0).getStocks());
    assertEquals(2298.60, stockModel.calculatePortfolioValue(
            "First Portfolio","2024-05-13"), 0.01);
    //sell some shares of a stock
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 3,
            "2024-05-14", false);
    //tests that the number of shares of GOOG has decreased
    assertEquals(5, stockModel.getPortfolios().get(0).getStocks().get(0).getShares(),
            0.01);
    //test that the portfolio value is accurate for various days
    assertEquals(1796.80, stockModel.calculatePortfolioValue(
            "First Portfolio", "2024-05-14"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-14"), 0.01);
    //sell all shares of a stock
    stockModel.updateStockInPortfolio("First Portfolio", "AAPL", 5,
            "2024-05-15", false);
    //tests that the AAPL stock has been removed from the portfolio
    assertEquals(1, stockModel.getPortfolios().get(0).getStocks().size());
    //test that the portfolio value is accurate for various days
    assertEquals(869.40, stockModel.calculatePortfolioValue("First Portfolio",
            "2024-05-15"), 0.01);
    assertEquals(0.0, stockModel.calculatePortfolioValue("Second Portfolio",
            "2024-05-15"), 0.01);
  }

  /**
   * Tests that selling more stock from a portfolio than owned throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void sellMoreSharesThanOwned() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "AAL", 3,
            "2023-05-01", true);
    stockModel.updateStockInPortfolio("First Portfolio", "AAL", 4,
            "2023-05-03", false);
  }

  /**
   * Tests that selling from a portfolio that doesn't exist throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void sellStockFromPortfolioPortfolioDoesNotExist() {
    stockModel.updateStockInPortfolio("Fake Portfolio", "GOOG", 1,
            "2023-01-04", false);
  }

  /**
   * Tests that selling from a portfolio at an invalid date throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void sellStockFromPortfolioPortfolioInvalidDate() {
    stockModel.updateStockInPortfolio("Fake Portfolio", "GOOG", 1,
            "2023-01-04", true);
    stockModel.updateStockInPortfolio("Fake Portfolio", "GOOG", 1,
            "2023-01-03", false);
  }

  /**
   * Tests getting the distribution of a portfolio's value on a specific date.
   */
  @Test
  public void getPortfolioDistribution() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);

    Map<String, Double> distribution = stockModel.getPortfolioDistribution(
            "First Portfolio", "2024-05-13");

    assertEquals(1, distribution.size());
    assertEquals(1367.20, distribution.get("GOOG"), 0.01);
  }

  /**
   * Tests getting the composition of a portfolio's shares on a specific date.
   */
  @Test
  public void getPortfolioComposition() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);

    Map<String, Double> composition = stockModel.getPortfolioComposition(
            "First Portfolio", "2024-05-13");

    assertEquals(1, composition.size());
    assertEquals(8, composition.get("GOOG"), 0.01);
  }

  /**
   * Tests getting the composition of a portfolio with multiple stocks, rebalancing,
   * and selling fractional shares.
   */
  @Test
  public void testCompositionRebalancingSelling() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "AAPL", 4,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "MSFT", 6,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "MSFT", 2.5,
            "2024-05-13", false);

    Map<String, Double> composition = stockModel.getPortfolioComposition(
            "First Portfolio", "2024-05-13");

    assertEquals(3, composition.size());
    assertEquals(8, composition.get("GOOG"), 0.01);
    assertEquals(4, composition.get("AAPL"), 0.01);
    assertEquals(3.5, composition.get("MSFT"), 0.01);

    List<Integer> percentages = Arrays.asList(30, 20, 50);
    stockModel.rebalancePortfolio("First Portfolio", percentages, "2024-05-13");

    Map<String, Double> rebalancedComposition = stockModel.getPortfolioComposition(
            "First Portfolio", "2024-05-13");

    assertEquals(6.25, rebalancedComposition.get("GOOG"), 0.01);
    assertEquals(3.82, rebalancedComposition.get("AAPL"), 0.01);
    assertEquals(4.30, rebalancedComposition.get("MSFT"), 0.01);

    stockModel.updateStockInPortfolio("First Portfolio", "MSFT", 6,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "MSFT", 2.5,
            "2024-05-13", false);

    Map<String, Double> updatedComposition = stockModel.getPortfolioComposition(
            "First Portfolio", "2024-05-13");

    assertEquals(6.25, updatedComposition.get("GOOG"), 0.01);
    assertEquals(3.82, updatedComposition.get("AAPL"), 0.01);
    assertEquals(7.80, updatedComposition.get("MSFT"), 0.01);
  }


  /**
   * Tests rebalancing the shares of stocks in a portfolio.
   */
  @Test
  public void rebalancePortfolio1() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "AAL", 2,
            "2024-05-13", true);

    List<Integer> percentages = Arrays.asList(50, 50);
    stockModel.rebalancePortfolio("First Portfolio", percentages, "2024-05-13");

    Map<String, Double> composition = stockModel.getPortfolioComposition(
            "First Portfolio", "2024-05-13");

    assertEquals(2, composition.size());
    assertEquals(4.09, composition.get("GOOG"), 0.01);
    assertEquals(46.79, composition.get("AAL"), 0.01);

    Map<String, Double> distribution = stockModel.getPortfolioDistribution(
            "First Portfolio", "2024-05-13");

    assertEquals(2, distribution.size());
    assertEquals(698.53, distribution.get("GOOG"), 0.01); // 50% of total value
    assertEquals(698.53, distribution.get("AAL"), 0.01); // 50% of total value
  }

  @Test
  public void rebalancePortfolio2() {
    Map<String, Double> initialPrices = Map.of("NFLX", 10.0, "GOOG", 25.0,
            "MSFT", 10.0, "AAPL", 50.0);

    Map<String, Integer> initialWeights = Map.of("NFLX", 25, "GOOG", 10,
            "MSFT", 25, "AAPL", 5);

    stockModel.createPortfolio("Test Portfolio");
    for (Map.Entry<String, Integer> entry : initialWeights.entrySet()) {
      stockModel.updateStockInPortfolio("Test Portfolio", entry.getKey(),
              entry.getValue(), "2024-01-03", true);
    }

    //current prices are "NFLX", 15.0, "GOOG", 30.0, "MSFT", 10.0, "AAPL", 30.0

    stockModel.rebalancePortfolio("Test Portfolio", Arrays.asList(25, 25, 25, 25),
            "2024-01-03");

    Map<String, Double> expectedValues = Map.of("NFLX", 5836.58754,
            "GOOG", 5836.58754, "MSFT", 5836.587540000001,
            "AAPL", 5836.58754);

    Map<String, Double> actualValues = stockModel.getPortfolioDistribution(
            "Test Portfolio", "2024-01-03");

    assertEquals(expectedValues, actualValues);
  }

  /**
   * Tests rebalancing with empty percentage list that doesn't add to 100 throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void rebalanceNot100Percentages() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.updateStockInPortfolio("First Portfolio", "AAL", 8,
            "2024-05-13", true);

    List<Integer> percentages = Arrays.asList(20, 30);
    stockModel.rebalancePortfolio("First Portfolio", percentages, "2024-05-13");
  }

  /**
   * Tests rebalancing the shares of stocks in a portfolio with empty percentages list
   * throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void rebalanceEmptyPercentages() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);

    List<Integer> percentages = Arrays.asList();
    stockModel.rebalancePortfolio("First Portfolio", percentages, "2024-05-13");
  }

  /**
   * Tests rebalancing the shares of stocks in a portfolio with percentages list size different
   * from portfolio size throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void rebalanceDifferentSizePercentages() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);

    List<Integer> percentages = Arrays.asList(50, 50);
    stockModel.rebalancePortfolio("First Portfolio", percentages, "2024-05-13");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid future date is provided
   * to getPortfolioDistribution.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioDistributionInvalidFutureDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.getPortfolioDistribution("First Portfolio", "2025-01-02");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid future date is provided
   * to getPortfolioComposition.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionInvalidFutureDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.getPortfolioComposition("First Portfolio", "2025-01-02");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid future date is provided
   * to rebalancePortfolio.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRebalancePortfolioInvalidFutureDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.rebalancePortfolio("First Portfolio", Arrays.asList(),
            "2025-01-02");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid chronological date
   * is provided to getPortfolioDistribution.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioDistributionInvalidChronologicalDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.getPortfolioDistribution("First Portfolio", "2024-05-12");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid chronological date
   * is provided to getPortfolioComposition.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionInvalidChronologicalDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.getPortfolioComposition("First Portfolio", "2024-05-12");
  }

  /**
   * Tests that IllegalArgumentException is thrown when an invalid chronological date
   * is provided to rebalancePortfolio.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRebalancePortfolioInvalidChronologicalDate() {
    stockModel.createPortfolio("First Portfolio");
    stockModel.updateStockInPortfolio("First Portfolio", "GOOG", 8,
            "2024-05-13", true);
    stockModel.rebalancePortfolio("First Portfolio", Arrays.asList(50, 50),
            "2024-05-12");
  }

  /**
   * Tests that getting composition of a portfolio that doesn't exist throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void getCompositionFromPortfolioPortfolioDoesNotExist() {
    stockModel.getPortfolioComposition("Fake Portfolio", "2025-01-02");
  }

  /**
   * Tests that getting distribution of a portfolio that doesn't exist throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void getDistributionFromPortfolioPortfolioDoesNotExist() {
    stockModel.getPortfolioComposition("Fake Portfolio", "2025-01-02");
  }

  /**
   * Tests that re-balancing a portfolio that doesn't exist throws an exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void rebalancePortfolioPortfolioDoesNotExist() {
    stockModel.rebalancePortfolio("Fake Portfolio", Arrays.asList(50, 50),
            "2024-05-12");
  }
}
