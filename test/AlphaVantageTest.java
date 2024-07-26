import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import data.AlphaVantage;
import stockmarket.StockInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the methods using the AlphaVantage API. This includes getting the
 * stock information from csv file and the API.
 */
public class AlphaVantageTest {
  /**
   * This method includes all the tests as described above.
   */
  @Test
  public void getStock() {
    assertFalse(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("GOOG"));
    assertFalse(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("BA"));
    //gets to from local csv
    AlphaVantage.getStock("GOOG");
    assertTrue(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("GOOG"));
    assertFalse(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("BA"));
    AlphaVantage.getStock("BA");
    assertTrue(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("GOOG"));
    assertTrue(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("BA"));
    //tests that only two stock information lists have been added
  }

  /**
   * Tests that getStock works to call the private method getStockFromAPI
   * when ticker is valid but cannot be found in the either the class or
   * the csv file. API updates data continuously, so tests change.
   */
  @Test
  public void getStockFromAPI() {
    //gets stock from api
    AlphaVantage.getStock("DJT");
    //validate that key is there
    assertTrue(AlphaVantage.STOCK_PRICE_HISTORY.containsKey("DJT"));
    //validates that one of the stock data
    assertEquals("2024-06-06", AlphaVantage.getStock("DJT").get(0).getTimestamp());
    assertEquals(46.1, AlphaVantage.getStock("DJT").get(0).getOpen(), 0.01);
    assertEquals(47.2, AlphaVantage.getStock("DJT").get(0).getHigh(), 0.01);
    assertEquals(45.48, AlphaVantage.getStock("DJT").get(0).getLow(), 0.01);
    assertEquals(46.02, AlphaVantage.getStock("DJT").get(0).getClose(), 0.01);
    assertEquals(1460373L , AlphaVantage.getStock("DJT").get(0).getVolume(), 0.01);
    //validates another one of the stock data
    int length = AlphaVantage.getStock("DJT").size() - 1;
    assertEquals("2021-09-30", AlphaVantage.getStock("DJT").get(length).getTimestamp());
    assertEquals(16.0, AlphaVantage.getStock("DJT").get(length).getOpen(), 0.01);
    assertEquals(17.33, AlphaVantage.getStock("DJT").get(length).getHigh(), 0.01);
    assertEquals(9.9, AlphaVantage.getStock("DJT").get(length).getLow(), 0.01);
    assertEquals(9.95, AlphaVantage.getStock("DJT").get(length).getClose(), 0.01);
    assertEquals(296126L , AlphaVantage.getStock("DJT").get(length).getVolume(), 0.01);
    //checks that the length of stock info list is accurate
    assertEquals(675, AlphaVantage.getStock("DJT").size());
  }

  /**
   * Tests that lower case ticker throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testLowerCaseTicker() {
    AlphaVantage.getStock("goog");
  }

  /**
   * Tests that number ticker throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNumberTicker() {
    AlphaVantage.getStock("123");
  }

  /**
   * Tests empty ticker throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testEmpty() {
    AlphaVantage.getStock("");
  }

  /**
   * Tests null ticker throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNullTicker() {
    AlphaVantage.getStock(null);
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
