import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import data.AlphaVantage;
import stockmarket.StockInfo;
import stockmarket.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains all the utilities of the program. This includes an inRange method
 * and a minMaxDate method.
 */
public class UtilsTest {
  List<StockInfo> googleStock;
  List<StockInfo> gameStopStock;
  DateTimeFormatter formatter;

  /**
   * Sets up the data in AlphaVantage for testing, as well as the formatting.
   */
  @Before
  public void setUp() {
    googleStock = AlphaVantage.getStock("GOOG");
    gameStopStock = AlphaVantage.getStock("GME");
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  }

  /**
   * Given a list of stock information, this method tests that the
   * minMaxDate method finds the minimum and maximum dates.
   */
  @Test
  public void testMinMaxDate() {
    LocalDate min = Utils.minMaxDate(googleStock, true);
    assertEquals("2013-08-19", min.format(formatter));
    LocalDate max = Utils.minMaxDate(gameStopStock, false);
    assertEquals("2024-06-03", max.format(formatter));
  }

  /**
   * Tests that the minMaxStock method gets the minimum and maximum closing value
   * for a stock.
   */
  @Test
  public void testMinMaxStock() {
    StockInfo min = Utils.minMaxStock(googleStock, true);
    assertEquals("2013-08-30", min.getTimestamp());
    assertEquals(21.31, min.getOpen(), 0.01);
    assertEquals(21.37, min.getHigh(), 0.01);
    assertEquals(21.06, min.getLow(), 0.01);
    assertEquals(21.09, min.getClose(), 0.01);
    assertEquals(74743109L, min.getVolume().longValue());
    StockInfo max = Utils.minMaxStock(googleStock, false);
    assertEquals("2024-05-21", max.getTimestamp());
    assertEquals(178.40, max.getOpen(), 0.01);
    assertEquals(179.82, max.getHigh(), 0.01);
    assertEquals(177.31, max.getLow(), 0.01);
    assertEquals(179.54, max.getClose(), 0.01);
    assertEquals(14706000L, max.getVolume().longValue());
  }

  /**
   * Tests that the inRange method accurately determines if a beginning, end, and
   * x value is in range of the stock info list.
   */
  @Test
  public void testInRange() {
    //valid date range
    assertTrue(Utils.inRange("2024-05-21",
            "2024-05-24", 3, googleStock));
    //date in the future
    assertFalse(Utils.inRange("2024-05-21",
            "2024-06-24", 3, googleStock));
  }

  /**
   * Tests that a when a start date comes before an end date, the dates are valid.
   */
  @Test
  public void checkDateStartEnd() {
    String startDate = "2024-05-21";
    Utils.checkDateStartEnd(startDate, "2024-05-24");
    assertEquals(startDate, "2024-05-21");
  }

  /**
   * Tests that when start and end date are the same, the dates are still valid.
   */
  @Test
  public void checkDateStartEndSame() {
    String startDate = "2024-05-21";
    Utils.checkDateStartEnd(startDate, "2024-05-21");
    assertEquals(startDate, "2024-05-21");
  }

  /**
   * Tests that when start date comes after end date, exception is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void checkDateStartEndInvalid() {
    Utils.checkDateStartEnd("2024-05-24", "2024-05-21");
  }

  /**
   * Tests that check date validates that stock information exists for that date.
   */
  @Test
  public void checkDate() {
    StockInfo min = Utils.checkDate("2013-08-30", "GOOG");
    assertEquals("2013-08-30", min.getTimestamp());
    assertEquals(21.31, min.getOpen(), 0.01);
    assertEquals(21.37, min.getHigh(), 0.01);
    assertEquals(21.06, min.getLow(), 0.01);
    assertEquals(21.09, min.getClose(), 0.01);
  }

  /**
   * Tests that exception is thrown when date doesn't exist in the list of stock information
   * for a specified stock.
   */
  @Test (expected = IllegalArgumentException.class)
  public void checkDateInvalid() {
    StockInfo min = Utils.checkDate("2025-08-30", "GOOG");
  }

  /**
   * Tests that validTicker validates all-caps letters between length of 1 and 4.
   */
  @Test
  public void testValidTicker() {
    String google = "GOOG";
    Utils.validTicker(google);
    Utils.validTicker("GOOG");
    Utils.validTicker("A");
    Utils.validTicker("ACDS");
    Utils.validTicker("G");
    assertEquals("GOOG", google);
  }

  /**
   * Tests that a ticker with numbers will cause exception to be thrown.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidTickerNumber() {
    Utils.validTicker("123");
  }

  /**
   * Tests that numbers and symbols will cause exception to be thrown.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidTickerNumberSymbol() {
    Utils.validTicker("!2");
  }

  /**
   * Tests that an empty string throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidTickerEmpty() {
    Utils.validTicker("");
  }

  /**
   * Tests that four digit integers are valid year values.
   */
  @Test
  public void testValidYear() {
    int year = 2020;
    Utils.validYear(2020);
    Utils.validYear(2010);
    Utils.validYear(7777);
    Utils.validYear(9999);
    assertEquals(year, 2020);
  }

  /**
   * Tests that integer greater than four digit throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidYearLarge() {
    Utils.validYear(33333);
  }

  /**
   * Tests that integer less than four digit throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidYearSmall() {
    Utils.validYear(11);
  }

  /**
   * Tests that an empty (0) value throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidYearEmpty() {
    Utils.validYear(0000);
  }

  /**
   * Tests that numbers between 1 and 12 are valid month values.
   */
  @Test
  public void testValidMonth() {
    int month = 12;
    for (int i = 1; i <= 12; i++) {
      Utils.validMonth(i);
    }
    assertEquals(month, 12);
  }

  /**
   * Tests that value larger than 12 throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidMonthLarge() {
    for (int i = 13; i < 30; i++) {
      Utils.validMonth(i);
    }
  }

  /**
   * Tests that month value smaller than 1 throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidMonthSmall() {
    Utils.validMonth(-1);
  }

  /**
   * Tests that zero value month throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidMonthEmpty() {
    Utils.validMonth(0);
  }

  /**
   * Tests that for numbers between 1 and 31, values are valid.
   */
  @Test
  public void testValidDay() {
    int day = 7;
    for (int i = 1; i <= 31; i++) {
      Utils.validDay(i);
    }
    assertEquals(day, 7);
  }

  /**
   * Tests that number greater than that range throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidDayLarge() {
    Utils.validDay(1221);
  }

  /**
   * Tests that negative day value throws exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidDaySmall() {
    Utils.validDay(-1);
  }

  /**
   * Tests that empty (0) day values throws exceptions.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testValidDayEmpty() {
    Utils.validDay(0);
  }

}
