package textui;

import java.io.IOException;

/**
 * Handles input and output interactions with the user in a text based interface.
 */
public class StockMarketView implements View {
  private final Appendable appendable;

  /**
   * Constructs a StockMarketView with the given Appendable object.
   *
   * @param appendable the Appendable to use for output
   * @throws IllegalArgumentException if Appendable is null
   */
  public StockMarketView(Appendable appendable) throws IllegalArgumentException {
    if (appendable == null) {
      throw new IllegalArgumentException("Appendable cannot be null");
    }
    this.appendable = appendable;
  }

  @Override
  public void writeMessage(String message) {
    try {
      appendable.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to write message", e);
    }
  }

  @Override
  public void welcomeMessage() {
    writeMessage("Welcome to the stock market simulation!" + System.lineSeparator());
    printMenu();
  }

  @Override
  public void printMenu() {
    writeMessage("The following are the supported user instructions:"
            + System.lineSeparator()
            + "Type the first command and the following information based on your needs. "
            + System.lineSeparator()
            + "You can only perform portfolio operations on dates in chronological order. The " +
            "operations "
            + System.lineSeparator()
            + "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions "
            + System.lineSeparator()
            + "portfolio-rebalance) cannot be performed on a date earlier than the most recent " +
            "date entered overall."
            + System.lineSeparator()
            + System.lineSeparator()
            + "view-gain-loss (Calculates gain or loss of the stock over the time period. Takes " +
            "in ticker, start date, and end date)"
            + System.lineSeparator()
            + "view-moving-average (Calculate x-day moving average starting from date. Takes in " +
            "ticker, date, and x-value.)"
            + System.lineSeparator()
            + "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)"
            + System.lineSeparator()
            + "create-portfolio (Creates a new portfolio. Takes in portfolio name.)"
            + System.lineSeparator()
            + "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and shares.)"
            + System.lineSeparator()
            + "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)"
            + System.lineSeparator()
            + "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)"
            + System.lineSeparator()
            + "portfolio-composition (Retrieves number of shares per stock in portfolio. Takes " +
            "in portfolio name and date.)"
            + System.lineSeparator()
            + "portfolio-distribution (Retrieves values of each stock in portfolio. Takes in " +
            "portfolio name and date.)"
            + System.lineSeparator()
            + "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)"
            + System.lineSeparator()
            + "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)"
            + System.lineSeparator()
            + "persist-portfolio (Saves or loads portfolio.)"
            + System.lineSeparator()
            + "menu (Print supported instruction list.)"
            + System.lineSeparator()
            + "q or quit (Quits the program.)"
            + System.lineSeparator()
            + System.lineSeparator());

  }

  @Override
  public void farewellMessage() {
    writeMessage("Thank you, goodbye!" + System.lineSeparator());
  }
}
