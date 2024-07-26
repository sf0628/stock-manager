import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import stockmarket.StockModel;

import static org.junit.Assert.assertEquals;

/**
 * Tests that the Stock Model properly visualizes the data of a certain portfolio
 * or stock from a given start time to end time. Each row of the bar chart must be
 * correctly incremented, and the scale should adjust to a relative or absolute scale.
 */
public class PerformanceVisualizationTest {
  StockModel model;

  /**
   * Sets up a model with a portfolio added.
   */
  @Before
  public void setUp() {
    model = new StockModel();
    model.createPortfolio("Testing");
    model.updateStockInPortfolio("Testing", "AMZN",
            3, "2013-06-04", true);
    model.updateStockInPortfolio("Testing", "GOOG",
            3, "2013-08-19", true);
  }

  /**
   * Tests that visualizing the portfolio at monthly increments work.
   */
  @Test
  public void testVisualizePerformanceOverTimeMonthy() {
    List<String> visualization = model.visualizePerformanceOverTime("Testing", "2013-08-19",
            "2014-08-19", true, true);
    List<String> expected = new ArrayList<>();
    expected.add("Performance of portfolio 'Testing' from 2013-08-19 to 2014-08-19: ");
    expected.add("");
    expected.add("Aug 2013: *");
    expected.add("Sep 2013: ******");
    expected.add("Oct 2013: *************************");
    expected.add("Nov 2013: ********************************");
    expected.add("Dec 2013: *************************************");
    expected.add("Jan 2014: ************************************");
    expected.add("Feb 2014: ***************************************");
    expected.add("Mar 2014: ***************************");
    expected.add("Apr 2014: ******************");
    expected.add("May 2014: ************************");
    expected.add("Jun 2014: ****************************");
    expected.add("Jul 2014: **************************");
    expected.add("Aug 2014: ********************************");
    expected.add("");
    expected.add("Scale: * = 1");


    for (int i = 0; i < visualization.size(); i++) {
      System.out.println(visualization.get(i));
      assertEquals(expected.get(i), visualization.get(i));
    }
  }

  /**
   * Tests that visualizing the portfolio at daily increments work.
   */
  @Test
  public void testVisualizePerformanceOverTimeDaily() {
    List<String> visualization = model.visualizePerformanceOverTime("Testing", "2013-08-19",
            "2013-08-30", true, true);
    List<String> expected = new ArrayList<>();
    expected.add("Performance of portfolio 'Testing' from 2013-08-19 to 2013-08-30: ");
    expected.add("");
    expected.add("Aug 19, 2013: *");
    expected.add("Aug 20, 2013: *");
    expected.add("Aug 21, 2013: *");
    expected.add("Aug 22, 2013: *");
    expected.add("Aug 23, 2013: *");
    expected.add("Aug 26, 2013: *");
    expected.add("Aug 27, 2013: *");
    expected.add("Aug 28, 2013: *");
    expected.add("Aug 29, 2013: *");
    expected.add("Aug 30, 2013: *");
    expected.add("");
    expected.add("Scale: * = 12");

    for (int i = 0; i < visualization.size(); i++) {
      assertEquals(expected.get(i), visualization.get(i));
    }

  }
}
