import data.AlphaVantage;

import org.junit.Before;
import org.junit.Test;
import stockmarket.Portfolio;
import stockmarket.StockInfo;
import stockmarket.StockModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests that the alpha vantage stock price history interacts with model correctly
 * by testing methods and outputs with mock data.
 */
public class ModelMockAlphaVantageTest {

  private StockModel stockModel;

  @Before
  public void setUp() {
    List<Portfolio> portfolios = new ArrayList<>();
    this.stockModel = new StockModel(portfolios);
    AlphaVantage.STOCK_PRICE_HISTORY.put("GOOG", MockAlphaVantage.MOCK_STOCKS_LIST);
  }

  @Test
  public void calculateGainLoss() {
    double gainLoss = stockModel.calculateGainLoss("GOOG", "2023-01-04", "2023-01-05");
    assertEquals(5.0, gainLoss, 0.01);
  }

  @Test
  public void calculateMovingAverage() {
    double movingAverage = stockModel.calculateMovingAverage("GOOG", "2023-01-05",
            1);
    assertEquals(110.0, movingAverage, 0.01);
  }

  @Test
  public void calculateXDayCrossover() {
    List<String> crossoverDates = stockModel.calculateXDayCrossover("GOOG",
            "2023-01-05", "2023-01-06", 1);
    assertEquals(0, crossoverDates.size());
  }

  @Test
  public void createPortfolio() {
    stockModel.createPortfolio("Portfolio");
    assertEquals(1, stockModel.getPortfolios().size());
    assertEquals("Portfolio", stockModel.getPortfolios().get(0).getName());
  }

  @Test
  public void updateStockInPortfolio() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    assertEquals(1, stockModel.getPortfolios().get(0).getStocks().size());
    assertEquals("GOOG", stockModel.getPortfolios().get(0).getStocks().get(0).getTicker());
    assertEquals(10, stockModel.getPortfolios().get(0).getStocks().get(0).getShares(),
            0.01);
  }

  @Test
  public void calculatePortfolioValue() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    double portfolioValue = stockModel.calculatePortfolioValue("Portfolio",
            "2023-01-04");
    assertEquals(1050.0, portfolioValue, 0.01);
  }

  @Test
  public void getPortfolioDistribution() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    Map<String, Double> distribution = stockModel.getPortfolioDistribution("Portfolio",
            "2023-01-04");
    assertEquals(1, distribution.size());
    assertEquals(1050.0, distribution.get("GOOG"), 0.01);
  }

  @Test
  public void getPortfolioComposition() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    Map<String, Double> composition = stockModel.getPortfolioComposition("Portfolio",
            "2023-01-04");
    assertEquals(1, composition.size());
    assertEquals(10, composition.get("GOOG"), 0.01);
  }

  @Test
  public void rebalancePortfolio() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    List<Integer> percentages = Collections.singletonList(100);
    stockModel.rebalancePortfolio("Portfolio", percentages, "2023-01-04");
    Map<String, Double> composition = stockModel.getPortfolioComposition("Portfolio",
            "2023-01-04");
    assertEquals(10, composition.get("GOOG"), 0.01);
  }

  @Test
  public void savePortfolio() {
    stockModel.createPortfolio("Portfolio");
    stockModel.updateStockInPortfolio("Portfolio", "GOOG", 10,
            "2023-01-04", true);
    stockModel.savePortfolio("Portfolio", "Empty.xml");
    assertEquals(1, stockModel.getPortfolios().size());
  }

  @Test
  public void loadPortfolio() {
    stockModel.loadPortfolio("Empty.xml");
    assertEquals(1, stockModel.getPortfolios().size());
    assertEquals("Portfolio", stockModel.getPortfolios().get(0).getName());
  }

  /**
   * Represents a mock AlphaVantage that has a static list of stock information
   * for a given ticker symbol to test interactions with model.
   */
  public static class MockAlphaVantage extends AlphaVantage {
    public static final List<StockInfo> MOCK_STOCKS_LIST = new ArrayList<>();

    static {
      MOCK_STOCKS_LIST.add(new StockInfo("2023-01-04", 100.0, 110.0, 90.0,
              105.0, 1000000L));
      MOCK_STOCKS_LIST.add(new StockInfo("2023-01-05", 105.0, 115.0, 95.0,
              110.0, 1200000L));
      MOCK_STOCKS_LIST.add(new StockInfo("2023-01-06", 110.0, 120.0, 100.0,
              115.0, 1500000L));
    }

    /**
     * Gets mock stock information for the given ticker symbol.
     *
     * @param ticker the stock ticker symbol
     * @return the mock list of stock information
     */
    public static List<StockInfo> getStock(String ticker) {
      return MOCK_STOCKS_LIST;
    }
  }
}
