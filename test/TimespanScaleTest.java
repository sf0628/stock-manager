import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stockmarket.Scale;
import stockmarket.Timespan;

import static org.junit.Assert.assertEquals;

/**
 * This class tests methods in timespan that get the proper date increments a
 * and properly converts to String format according the given timespan. Additionally
 * ensure that increments are accurate for daily, monthly, and yearly increments.
 * Tests that the getters in scale work properly.
 */
public class TimespanScaleTest {
  public Timespan byDay;
  public Timespan byMonth;
  public Timespan byYear;
  Scale scale0;
  Scale scale1;

  /**
   * Sets up three different time spans that increment at varying amounts
   * (days, months, years).
   * Sets up two different scale with different base and scale values.
   */
  @Before
  public void setUp() {
    byDay = new Timespan(false, false, true, 2);
    byMonth = new Timespan(false, true, false, 1);
    byYear = new Timespan(true, false, false, 3);
    scale0 = new Scale(50, 100);
    scale1 = new Scale(90, 250);
  }

  /**
   * Tests that the isYear method determines if a timespan is incrementing by year.
   */
  @Test
  public void testIsYear() {
    assertEquals(true, byYear.isYear());
    assertEquals(false, byMonth.isYear());
    assertEquals(false, byDay.isYear());
  }

  /**
   * Tests that the isMonth method determines if a timespan is incrementing by month.
   */
  @Test
  public void testIsMonth() {
    assertEquals(false, byYear.isMonth());
    assertEquals(true, byMonth.isMonth());
    assertEquals(false, byDay.isMonth());
  }

  /**
   * Tests that the isDay method determines if a timespan is incrementing by day.
   */
  @Test
  public void testIsDay() {
    assertEquals(false, byYear.isDay());
    assertEquals(false, byMonth.isDay());
    assertEquals(true, byDay.isDay());
  }

  /**
   * Tests that for a timespan that increments by day, the date increments are accurate
   * and the converted output follows the correct formatting.
   */
  @Test
  public void getDateIncrementsDay() {
    List<LocalDate> dates = byDay.getDateIncrements("2013-08-19", "2013-08-30",
            "GOOG");
    List<String> expected = new ArrayList<>(Arrays.asList("2013-08-19", "2013-08-21", "2013-08-23",
            "2013-08-26", "2013-08-28", "2013-08-30"));
    for (int i = 0; i < dates.size(); i++) {
      assertEquals(expected.get(i), dates.get(i).toString());
    }

    List<String> converted = byDay.convertIncrements(dates);
    List<String> expectedConverted = new ArrayList<>(Arrays.asList("Aug 19, 2013",
            "Aug 21, 2013", "Aug 23, 2013", "Aug 26, 2013", "Aug 28, 2013", "Aug 30, 2013"));
    for (int i = 0; i < converted.size(); i++) {
      assertEquals(expectedConverted.get(i), converted.get(i));
    }
  }

  /**
   * Tests that for a timespan that increments by month, the date increments are accurate
   * and the converted output follows the correct formatting.
   */
  @Test
  public void testGetDateIncrementsMonth() {
    List<LocalDate> dates = byMonth.getDateIncrements("2013-08-19", "2014-02-20",
            "GOOG");
    List<String> expected = new ArrayList<>(Arrays.asList("2013-08-19", "2013-09-30", "2013-10-31",
            "2013-11-29", "2013-12-31", "2014-01-31", "2014-02-20"));

    for (int i = 0; i < dates.size(); i++) {
      assertEquals(expected.get(i), dates.get(i).toString());
    }

    List<String> converted = byMonth.convertIncrements(dates);
    List<String> expectedConverted = new ArrayList<>(Arrays.asList("Aug 2013", "Sep 2013",
            "Oct 2013", "Nov 2013", "Dec 2013", "Jan 2014", "Feb 2014"));
    for (int i = 0; i < converted.size(); i++) {
      assertEquals(expectedConverted.get(i), converted.get(i));
    }
  }

  /**
   * Tests that for a timespan that increments by year, the date increments are accurate
   * and the converted output follows the correct formatting.
   */
  @Test
  public void testGetDateIncrementsYear() {
    List<LocalDate> dates = byYear.getDateIncrements("2013-08-20", "2023-10-30",
            "GOOG");
    List<String> expected = new ArrayList<>(Arrays.asList("2013-08-20", "2016-12-30", "2019-12-31",
            "2022-12-30", "2023-10-30"));

    for (int i = 0; i < dates.size(); i++) {
      assertEquals(expected.get(i), dates.get(i).toString());
    }

    List<String> converted = byYear.convertIncrements(dates);
    List<String> expectedConverted = new ArrayList<>(Arrays.asList("2013", "2016", "2019",
            "2022", "2023"));
    for (int i = 0; i < converted.size(); i++) {
      assertEquals(expectedConverted.get(i), converted.get(i));
    }
  }

  /**
   * Test that getBase gets the base of each Scale.
   */
  @Test
  public void testGetBase() {
    assertEquals(50, scale0.getBase());
    assertEquals(90, scale1.getBase());
  }

  /**
   * Test that getScale gets the scale of each Scale.
   */
  @Test
  public void testGetScale() {
    assertEquals(100, scale0.getScale());
    assertEquals(250, scale1.getScale());
  }
}
