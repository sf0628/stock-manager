import org.junit.Before;
import org.junit.Test;

import textui.StockMarketView;
import textui.View;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the StockMarketView. This includes printing and appending
 * messages.
 */
public class StockMarketViewTest {
  //regular view
  Appendable appendable;
  View view;
  //view that throws
  Appendable mockAppendableThrow;
  View mockViewThrow;

  /**
   * Sets up the appendable and view for testing purposes.
   */
  @Before
  public void setUp() {
    appendable = new StringBuilder();
    mockAppendableThrow = new MockAppendable(true);
    view = new StockMarketView(appendable);
    mockViewThrow = new StockMarketView(mockAppendableThrow);
  }

  /**
   * Tests that when the appendable is null, then an exception is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    View error = new StockMarketView(null);
  }

  /**
   * Tests that given a mock appendable meant to throw an IO Exception,
   * that the writeMessage method throws an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testWriteMessageError() {
    mockViewThrow.writeMessage("Testing");
  }

  /**
   * Tests that writeMessage adds to the string builder appendable.
   */
  @Test
  public void testWriteMessage() {
    view.writeMessage("Testing");
    assertEquals("Testing", appendable.toString());
    view.writeMessage(" Completed!");
    assertEquals("Testing Completed!", appendable.toString());
  }

  /**
   * Tests that welcomeMessage method produces the welcome message and adds it to the
   * string builder.
   */
  @Test
  public void testWelcomeMessage() {
    view.welcomeMessage();
    assertEquals("Welcome to the stock market simulation!"
            + System.lineSeparator() + "The following are the supported user instructions:" +
            System.lineSeparator() + "view-gain-loss ticker start-date end-date " +
            "(calculate gain or loss of the stock over the time period)" + System.lineSeparator()
            + "view-moving-average ticker date x  " + "(calculate x-day moving average starting " +
            "from date)"
            + System.lineSeparator() + "view-crossovers ticker start-date end-date x " +
            "(determine x-day crossovers for the stock over the time period)"
            + System.lineSeparator()
            + "create-portfolio portfolio-name (create a new portfolio)" + System.lineSeparator()
            + "add-stock portfolio-name ticker shares (add stock to portfolio)"
            + System.lineSeparator() +
            "portfolio-value portfolio-name date (calculate portfolio value)"
            + System.lineSeparator()
            + "menu (print supported instruction list)" + System.lineSeparator()
            + "q or quit (quit the program)"
            + System.lineSeparator(), appendable.toString());
  }

  /**
   * Tests that the printMenu method produces the proper menu and adds it to the
   * string builder.
   */
  @Test
  public void testPrintMenu() {
    view.printMenu();
    assertEquals("The following are the supported user instructions:" +
            System.lineSeparator() + "view-gain-loss ticker start-date end-date " +
            "(calculate gain or loss of the stock over the time period)" + System.lineSeparator()
            + "view-moving-average ticker date x  " + "(calculate x-day moving average " +
            "starting from date)"
            + System.lineSeparator() + "view-crossovers ticker start-date end-date x " +
            "(determine x-day crossovers for the stock over the time period)"
            + System.lineSeparator()
            + "create-portfolio portfolio-name (create a new portfolio)" + System.lineSeparator()
            + "add-stock portfolio-name ticker shares (add stock to portfolio)"
            + System.lineSeparator() +
            "portfolio-value portfolio-name date (calculate portfolio value)"
            + System.lineSeparator()
            + "menu (print supported instruction list)" + System.lineSeparator()
            + "q or quit (quit the program)"
            + System.lineSeparator(), appendable.toString());
  }

  /**
   * Tests that the farewellMessage method produces the proper message.
   */
  @Test
  public void testFarewellMessage() {
    view.farewellMessage();
    assertEquals("Thank you, goodbye!" + System.lineSeparator(), appendable.toString());
  }
}
