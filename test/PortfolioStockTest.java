import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockmarket.Portfolio;
import stockmarket.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests that methods in the portfolio class work as expected. Stocks can be added and
 * portfolio value can be calculated with appropriate exceptions thrown given an
 * invalid date. An invalid date is when the stock market is not open.
 * Tests that methods in Stock class run properly. Overriding equals and hashcode
 * result in proper equality assignment.
 */
public class PortfolioStockTest {
  Stock aal;
  Stock amzn;
  Stock ba;
  Stock ba1;
  Stock goog;
  Portfolio portfolio;
  Stock stock1;
  Stock stock2;

  /**
   * Sets up data for stocks and a portfolio.
   * Sets up two different objects of the same fields.
   */
  @Before
  public void setUp() {
    aal = new Stock("AAL", 3, "2023-05-01");
    amzn = new Stock("AMZN", 1, "2023-05-02");
    ba = new Stock("BA", 9, "2024-05-01");
    ba1 = new Stock("BA", 10, "2024-05-02");
    goog = new Stock("GOOG", 2, "2024-05-03");

    portfolio = new Portfolio("Test");

    stock1 = new Stock("AAPL", 10, "2024-05-01");
    stock2 = new Stock("AAPL", 10, "2024-05-01");
  }

  /**
   * Tests that buying stocks to a portfolio that is new or already exists in the portfolio
   * properly updates the stock and shares of that stock.
   */
  @Test
  public void buyStock() {
    List<Stock> list1 = new ArrayList<>();
    assertEquals(list1, portfolio.getStocks());

    list1.add(aal);
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    assertEquals(list1, portfolio.getStocks());

    list1.add(amzn);
    portfolio.updateStock("AMZN", 1, "2023-05-02", true);
    assertEquals(list1, portfolio.getStocks());

    list1.add(ba);
    portfolio.updateStock("BA", 9, "2024-05-01", true);
    assertEquals(list1, portfolio.getStocks());

    // Test that adding shares to a stock that has been already added works properly
    list1.remove(ba);
    list1.add(ba1);
    portfolio.updateStock("BA", 1, "2024-05-02", true);
    assertEquals(list1, portfolio.getStocks());

    list1.add(goog);
    portfolio.updateStock("GOOG", 2, "2024-05-03", true);
    assertEquals(list1, portfolio.getStocks());
  }

  /**
   * Tests that adding stocks with negative shares throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void addStockWithNegativeShares() {
    portfolio.updateStock("AAL", -1, "2023-05-01", true);
  }

  /**
   * Tests that selling stocks works correctly, including removing a stock when all shares are sold.
   */
  @Test
  public void sellStock() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AMZN", 1, "2023-05-02", true);

    //sell some AAL shares
    portfolio.updateStock("AAL", 1, "2023-05-03", false);
    assertEquals(2, portfolio.getStocks().get(0).getShares(), 0.01);

    //sell remaining AAL shares
    portfolio.updateStock("AAL", 2, "2023-05-04", false);
    assertEquals(1, portfolio.getStocks().get(0).getShares(), 0.01);
    assertEquals(1, portfolio.getStocks().size());

    List<Stock> list1 = new ArrayList<>();
    list1.add(amzn);
    assertEquals(list1, portfolio.getStocks());

    portfolio.updateStock("AMZN", 0.5, "2023-05-05", false);
    assertEquals(0.5, portfolio.getStocks().get(0).getShares(), 0.01);
    assertEquals(1, portfolio.getStocks().size());

    portfolio.updateStock("AMZN", 0.5, "2023-05-05", false);
    assertEquals(0, portfolio.getStocks().size());

    list1.remove(amzn);
    assertEquals(list1, portfolio.getStocks());
  }

  /**
   * Tests that selling more shares than owned throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void sellMoreSharesThanOwned() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AAL", 4, "2023-05-03", false);
  }

  /**
   * Tests that selling a stock not in the portfolio throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void sellStockNotInPortfolio() {
    portfolio.updateStock("AAL", 1, "2023-05-03", false);
  }

  /**
   * Tests that given a proper date, the total value of the portfolio is accurately
   * calculated.
   */
  @Test
  public void calculateValue() {
    assertEquals(0.0, portfolio.calculateTotalValue("2024-05-01"), 0.01);
    portfolio.updateStock("GOOG", 3, "2024-05-01",true);
    assertEquals(portfolio.getStocks().get(0).getShares(), 3.0, 0.01);
    assertEquals(505.38, portfolio.calculateTotalValue("2024-05-02"), 0.01);
    portfolio.updateStock("GOOG", 1, "2024-05-02",true);
    assertEquals(675.96, portfolio.calculateTotalValue("2024-05-03"), 0.01);
    portfolio.updateStock("F", 1, "2024-05-03", true);
    assertEquals(717.48, portfolio.calculateTotalValue("2024-05-24"), 0.01);
  }

  /**
   * Tests that when an invalid date (weekend, holiday), that the method throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void calculateValueInvalidDate() {
    assertEquals(0.0, portfolio.calculateTotalValue("2024-05-27"), 0.01);
    portfolio.updateStock("GOOG", 3, "2024-05-01", true);
    assertEquals(534.06, portfolio.calculateTotalValue("2024-05-27"), 0.01);
  }

  /**
   * Tests getDistribution with a valid date.
   */
  @Test
  public void getDistributionValidDate() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AMZN", 1, "2023-05-02", true);

    Map<String, Double> expectedDistribution = new HashMap<>();
    expectedDistribution.put("AAL", 41.31);
    expectedDistribution.put("AMZN", 103.629997);

    assertEquals(expectedDistribution, portfolio.getDistribution("2023-05-02"));
  }

  /**
   * Tests getDistribution with invalid dates (future, non-chronological).
   */
  @Test(expected = IllegalArgumentException.class)
  public void getDistributionFutureDate() {
    portfolio.updateStock("GOOG", 2, "2024-05-03", true);
    portfolio.getDistribution("2025-01-01");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getDistributionNonChronologicalDate() {
    portfolio.updateStock("GOOG", 2, "2024-05-03", true);
    portfolio.getDistribution("2023-05-03");
  }

  /**
   * Tests getDistribution with an empty portfolio.
   */
  @Test
  public void getDistributionEmptyPortfolio() {
    Map<String, Double> expectedDistribution = new HashMap<>();
    assertEquals(expectedDistribution, portfolio.getDistribution("2024-05-01"));
  }

  /**
   * Tests getDistribution with multiple stocks and dates.
   */
  @Test
  public void getDistributionMultipleStocks() {
    portfolio.updateStock("GOOG", 2, "2024-05-01", true);
    portfolio.updateStock("BA", 9, "2024-05-02", true);
    portfolio.updateStock("AAL", 4, "2024-05-02", true);
    portfolio.updateStock("AAL", 2, "2024-05-02", false);

    Map<String, Double> expectedDistribution = new HashMap<>();
    expectedDistribution.put("GOOG", 337.98001);
    expectedDistribution.put("AAL", 27.7);
    expectedDistribution.put("BA", 1618.1099370000002);

    assertEquals(expectedDistribution, portfolio.getDistribution("2024-05-03"));
  }

  /**
   * Tests getComposition with valid inputs.
   */
  @Test
  public void testGetCompositionValidInputs() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AMZN", 1, "2023-05-02", true);
    portfolio.updateStock("BA", 9, "2024-05-01", true);

    Map<String, Double> expectedComposition = new HashMap<>();
    expectedComposition.put("AAL", 3.0);
    expectedComposition.put("AMZN", 1.0);
    expectedComposition.put("BA", 9.0);

    assertEquals(expectedComposition, portfolio.getComposition("2024-05-01"));
  }

  /**
   * Tests getComposition with invalid future date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetCompositionFutureDate() {
    portfolio.getComposition("2050-01-01");
  }

  /**
   * Tests getComposition with invalid dates (future, non-chronological).
   */
  @Test(expected = IllegalArgumentException.class)
  public void getCompositionFutureDate() {
    portfolio.updateStock("GOOG", 2, "2024-05-03", true);
    portfolio.getComposition("2025-01-01");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCompositionNonChronologicalDate() {
    portfolio.updateStock("GOOG", 2, "2024-05-03", true);
    portfolio.getComposition("2023-05-03");
  }

  /**
   * Tests getComposition with empty portfolio.
   */
  @Test
  public void testGetCompositionEmptyPortfolio() {
    Map<String, Double> expectedComposition = new HashMap<>();

    assertEquals(expectedComposition, portfolio.getComposition("2023-05-01"));
  }

  /**
   * Tests getComposition with multiple stocks.
   */
  @Test
  public void getCompositionMultipleStocks() {
    portfolio.updateStock("GOOG", 2, "2024-05-01", true);
    portfolio.updateStock("BA", 9, "2024-05-02", true);
    portfolio.updateStock("AAL", 4, "2024-05-02", true);
    portfolio.updateStock("AAL", 2, "2024-05-02", false);

    Map<String, Double> expectedComposition = new HashMap<>();
    expectedComposition.put("GOOG", 337.98001);
    expectedComposition.put("AAL", 27.7);
    expectedComposition.put("BA", 1618.1099370000002);

    assertEquals(expectedComposition, portfolio.getDistribution("2024-05-03"));
  }

  /**
   * Tests rebalance with valid inputs.
   */
  @Test
  public void rebalanceValidInputs() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AMZN", 1, "2023-05-02", true);
    portfolio.updateStock("BA", 9, "2024-05-01", true);

    List<Integer> percentages = new ArrayList<>();
    percentages.add(30);
    percentages.add(20);
    percentages.add(50);

    portfolio.rebalance(percentages, "2024-05-01");

    assertEquals(38.94, portfolio.getStocks().get(0).getShares(), 0.01);
    assertEquals(1.96, portfolio.getStocks().get(1).getShares(), 0.01);
    assertEquals(5.14, portfolio.getStocks().get(2).getShares(), 0.01);

    portfolio.rebalance(percentages, "2024-05-03");

    assertEquals(39.64, portfolio.getStocks().get(0).getShares(), 0.01);
    assertEquals(1.96, portfolio.getStocks().get(1).getShares(), 0.01);
    assertEquals(5.09, portfolio.getStocks().get(2).getShares(), 0.01);
  }

  /**
   * Tests rebalance with empty percentages list.
   */
  @Test(expected = IllegalArgumentException.class)
  public void rebalanceEmptyPercentages() {
    List<Integer> percentages = new ArrayList<>();
    portfolio.rebalance(percentages, "2023-05-01");
  }

  /**
   * Tests rebalance with percentages list size that doesn't match portfolio size.
   */
  @Test(expected = IllegalArgumentException.class)
  public void rebalancePercentagesSizeMismatch() {
    List<Integer> percentages = new ArrayList<>();
    percentages.add(30);
    percentages.add(20);
    percentages.add(50);
    percentages.add(10);

    portfolio.rebalance(percentages, "2023-05-01");
  }

  /**
   * Tests rebalance with invalid future date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void rebalanceFutureDate() {
    List<Integer> percentages = new ArrayList<>();
    percentages.add(30);
    percentages.add(20);
    percentages.add(50);

    portfolio.rebalance(percentages, "2050-01-01");
  }

  /**
   * Tests that performing operations with an invalid date (not chronological) throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOperationInvalidDate1() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.updateStock("AAL", 1, "2023-04-30", true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOperationInvalidDate2() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.calculateTotalValue("2023-04-30");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOperationInvalidDate3() {
    portfolio.updateStock("AAL", 3, "2023-05-01", true);
    portfolio.getDistribution("2023-04-30");
  }

  /**
   * Tests that different instances of the same portfolio are considered equal.
   * In addition, their hash codes are also equal.
   */
  @Test
  public void testPortfolioEqualsHashCode() {
    Portfolio portfolio2 = new Portfolio("Test");
    assertTrue(portfolio2.equals(portfolio));
    assertTrue(portfolio2.hashCode() == portfolio.hashCode());
    assertTrue(portfolio.equals(portfolio));
    assertTrue(portfolio2.equals(portfolio2));
    portfolio2.updateStock("GOOG", 3, "2024-05-01", true);
    assertFalse(portfolio2.equals(portfolio));
    assertTrue(portfolio2.hashCode() != portfolio.hashCode());
    portfolio.updateStock("GOOG", 3, "2024-05-01", true);
    assertTrue(portfolio2.equals(portfolio));
    assertTrue(portfolio2.hashCode() == portfolio.hashCode());
  }

  /**
   * Tests construction with valid tickers and shares.
   */
  @Test
  public void testValidStockConstruction() {
    Stock validStock = new Stock("GOOG", 20, "2024-05-01");
    assertEquals("GOOG", validStock.getTicker());
    assertEquals(20, validStock.getShares(), 0.001);
    assertEquals("2024-05-01", validStock.getDateAdded());
  }

  /**
   * Tests construction with zero shares.
   */
  @Test
  public void testZeroShares() {
    Stock zeroSharesStock = new Stock("MSFT", 0, "2024-05-01");
    assertEquals(0, zeroSharesStock.getShares(), 0.001);
  }

  /**
   * Tests construction with many of shares.
   */
  @Test
  public void testLargeShares() {
    Stock largeSharesStock = new Stock("AMZN", 100000000, "2024-05-01");
    assertEquals(100000000, largeSharesStock.getShares(), 0.001);
  }

  /**
   * Tests construction with fractional shares.
   */
  @Test
  public void testFractionalShares() {
    Stock largeSharesStock = new Stock("AMZN", 1020.89230, "2024-05-01");
    assertEquals(1020.89230, largeSharesStock.getShares(), 0.001);
  }

  /**
   * Tests that the two separate instances are considered equal and
   * have the same hashcode.
   */
  @Test
  public void testStockEquals() {
    assertTrue(stock1.equals(stock2));
    assertEquals(stock1.hashCode(), stock2.hashCode());
  }

  /**
   * Tests that null ticker throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullTicker() {
    new Stock(null, 10, "2024-05-01");
  }

  /**
   * Tests that null dateAdded throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullDateAdded() {
    new Stock("AAPL", 10, null);
  }

  /**
   * Tests that invalid ticker will throw exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidTicker1() {
    new Stock("apple", 10, "2024-05-01");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidTicker2() {
    new Stock("GOOGL", 10, "2024-05-01");
  }

  /**
   * Tests that negative shares will throw exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidShares() {
    new Stock("AAPL", -10, "2024-05-01");
  }

  /**
   * Tests that invalid dates will throw exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidDate1() {
    new Stock("AAPL", 1, "2025-05-01");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidDate2() {
    new Stock("AAPL", 1, "2025-05-11");
  }
}