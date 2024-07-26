import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import stockmarket.Model;
import stockmarket.Portfolio;
import textui.StockMarketController;
import textui.View;

import static org.junit.Assert.assertEquals;

/**
 * Tests that the stock controller interacts with view correctly by displaying the correct
 * welcome, menu, and farewell messages.
 */
public class ControllerMockViewTest {
  private MockStockModel model;
  private Readable rd;
  private MockStockMarketView view;
  private StockMarketController cont;

  @Before
  public void setUp() {
    model = new MockStockModel();
    rd = new StringReader("");
    view = new MockStockMarketView();
    cont = new StockMarketController(model, rd, view);
  }

  @Test
  public void welcomeMessage() {
    view.welcomeMessage();
    rd = new StringReader("q");
    cont = new StockMarketController(model, rd, view);
    cont.goController();

    String expectedOutput = "Mock welcome message" + System.lineSeparator() + "Mock menu"
            + System.lineSeparator() + "Welcome to the stock market simulation!"
            + System.lineSeparator() + "The following are the supported user instructions:"
            + System.lineSeparator() + "Type the first command and the following information " +
            "based on your needs. " + System.lineSeparator() +
            "You can only perform portfolio operations on dates in chronological order. " +
            "The operations " + System.lineSeparator() +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions " + System.lineSeparator() +
            "portfolio-rebalance) cannot be performed on a date earlier than the " +
            "most recent date entered overall." + System.lineSeparator() +
            "" + System.lineSeparator() +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. " +
            "Takes in ticker, start date, and end date)" + System.lineSeparator() +
            "view-moving-average (Calculate x-day moving average starting from date. " +
            "Takes in ticker, date, and x-value.)" + System.lineSeparator() +
            "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)" + System.lineSeparator() +
            "create-portfolio (Creates a new portfolio. Takes in portfolio name.)"
            + System.lineSeparator() +
            "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and shares.)"
            + System.lineSeparator() +
            "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)" + System.lineSeparator() +
            "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)" + System.lineSeparator() +
            "portfolio-composition (Retrieves number of shares per stock in portfolio. " +
            "Takes in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-distribution (Retrieves values of each stock in portfolio. Takes " +
            "in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)" + System.lineSeparator() +
            "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)" + System.lineSeparator() +
            "persist-portfolio (Saves or loads portfolio.)" + System.lineSeparator() +
            "menu (Print supported instruction list.)" + System.lineSeparator() +
            "q or quit (Quits the program.)" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Type instruction: Thank you, goodbye!" + System.lineSeparator();

    assertEquals(expectedOutput, view.getLog());
  }

  @Test
  public void printMenu() {
    view.printMenu();
    rd = new StringReader("q");
    cont = new StockMarketController(model, rd, view);
    cont.goController();

    String expectedOutput = "Mock menu"
            + System.lineSeparator() + "Welcome to the stock market simulation!"
            + System.lineSeparator() + "The following are the supported user instructions:"
            + System.lineSeparator() + "Type the first command and the following information " +
            "based on your needs. " + System.lineSeparator() +
            "You can only perform portfolio operations on dates in chronological order. " +
            "The operations " + System.lineSeparator() +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions " + System.lineSeparator() +
            "portfolio-rebalance) cannot be performed on a date earlier than the " +
            "most recent date entered overall." + System.lineSeparator() +
            "" + System.lineSeparator() +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. " +
            "Takes in ticker, start date, and end date)" + System.lineSeparator() +
            "view-moving-average (Calculate x-day moving average starting from date. " +
            "Takes in ticker, date, and x-value.)" + System.lineSeparator() +
            "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)" + System.lineSeparator() +
            "create-portfolio (Creates a new portfolio. Takes in portfolio name.)"
            + System.lineSeparator() +
            "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and shares.)"
            + System.lineSeparator() +
            "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)" + System.lineSeparator() +
            "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)" + System.lineSeparator() +
            "portfolio-composition (Retrieves number of shares per stock in portfolio. " +
            "Takes in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-distribution (Retrieves values of each stock in portfolio. Takes " +
            "in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)" + System.lineSeparator() +
            "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)" + System.lineSeparator() +
            "persist-portfolio (Saves or loads portfolio.)" + System.lineSeparator() +
            "menu (Print supported instruction list.)" + System.lineSeparator() +
            "q or quit (Quits the program.)" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Type instruction: Thank you, goodbye!" + System.lineSeparator();

    assertEquals(expectedOutput, view.getLog());
  }

  @Test
  public void farewellMessage() {
    view.farewellMessage();
    rd = new StringReader("q");
    cont = new StockMarketController(model, rd, view);
    cont.goController();

    String expectedOutput = "Mock farewell message" + System.lineSeparator()
            + "Welcome to the stock market simulation!"
            + System.lineSeparator() + "The following are the supported user instructions:"
            + System.lineSeparator() + "Type the first command and the following information " +
            "based on your needs. " + System.lineSeparator() +
            "You can only perform portfolio operations on dates in chronological order. " +
            "The operations " + System.lineSeparator() +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions " + System.lineSeparator() +
            "portfolio-rebalance) cannot be performed on a date earlier than the " +
            "most recent date entered overall." + System.lineSeparator() +
            "" + System.lineSeparator() +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. " +
            "Takes in ticker, start date, and end date)" + System.lineSeparator() +
            "view-moving-average (Calculate x-day moving average starting from date. " +
            "Takes in ticker, date, and x-value.)" + System.lineSeparator() +
            "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)" + System.lineSeparator() +
            "create-portfolio (Creates a new portfolio. Takes in portfolio name.)"
            + System.lineSeparator() +
            "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and shares.)"
            + System.lineSeparator() +
            "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)" + System.lineSeparator() +
            "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)" + System.lineSeparator() +
            "portfolio-composition (Retrieves number of shares per stock in portfolio. " +
            "Takes in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-distribution (Retrieves values of each stock in portfolio. Takes " +
            "in portfolio name and date.)" + System.lineSeparator() +
            "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)" + System.lineSeparator() +
            "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)" + System.lineSeparator() +
            "persist-portfolio (Saves or loads portfolio.)" + System.lineSeparator() +
            "menu (Print supported instruction list.)" + System.lineSeparator() +
            "q or quit (Quits the program.)" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Type instruction: Thank you, goodbye!" + System.lineSeparator();

    assertEquals(expectedOutput, view.getLog());
  }

  /**
   * Represents a mock StockMarketView with stub return values in order to
   * test that the controller obtains the correct messages from the view.
   */
  public static class MockStockMarketView implements View, Appendable {
    private StringBuilder log;

    /**
     * Constructs a mock StockMarketView with log to check output.
     */
    public MockStockMarketView() {
      this.log = new StringBuilder();
    }

    @Override
    public void writeMessage(String message) {
      log.append(message);
    }

    @Override
    public void welcomeMessage() {
      log.append("Mock welcome message" + System.lineSeparator());
      printMenu();
    }

    @Override
    public void printMenu() {
      log.append("Mock menu" + System.lineSeparator());
    }

    @Override
    public void farewellMessage() {
      log.append("Mock farewell message" + System.lineSeparator());
    }

    /**
     * Gets log of mock StockMarketView as a string.
     */
    public String getLog() {
      return log.toString();
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      log.append(csq);
      return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      log.append(csq, start, end);
      return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
      log.append(c);
      return this;
    }
  }

  /**
   * Represents a mock StockModel with methods to input return values in order to
   * test that the controller sends the correct inputs to the model.
   */
  public static class MockStockModel implements Model {
    private double mockResult;
    private List<String> mockResultList;
    public String portfolioName;
    public String ticker;
    public double shares;
    public String date;
    public boolean updateStock;
    public boolean isAdding;
    public double portfolioValue;
    public Map<String, Double> portfolioComposition;
    public String fileName;

    //checks if method is called
    public boolean createPortfolioCalled;
    public boolean updateStockInPortfolioCalled;
    public boolean calculatePortfolioValueCalled;
    public boolean getPortfolioValueCalled;
    public boolean getPortfolioComposition;
    public boolean savePortfolioCalled;
    public boolean loadPortfolioCalled;

    /**
     * Sets given mockResult to this mockResult in constructor.
     *
     * @param mockResult double to set mockResult to
     */
    public void setMockResult(double mockResult) {
      this.mockResult = mockResult;
    }

    /**
     * Sets given list of mockResults to this mockResultList in constructor.
     *
     * @param mockResultList list of strings to set mockResultList to
     */
    public void setMockResultList(List<String> mockResultList) {
      this.mockResultList = mockResultList;
    }

    @Override
    public double calculateGainLoss(String ticker, String startDate, String endDate) {
      return mockResult;
    }

    @Override
    public double calculateMovingAverage(String ticker, String date, int x) {
      return mockResult;
    }

    @Override
    public List<String> calculateXDayCrossover(String ticker, String startDate, String endDate,
                                               int x) {
      return mockResultList;
    }

    @Override
    public void createPortfolio(String portfolioName) {
      this.portfolioName = portfolioName;
      this.createPortfolioCalled = true;
    }

    @Override
    public Portfolio findPortfolio(String portfolioName) {
      return null;
    }

    @Override
    public void updateStockInPortfolio(String portfolioName, String ticker, double shares,
                                       String date, boolean isAdding) {
      this.portfolioName = portfolioName;
      this.ticker = ticker;
      this.shares = shares;
      this.date = date;
      this.updateStock = true;
      this.isAdding = isAdding;
      this.updateStockInPortfolioCalled = true;
    }

    @Override
    public double calculatePortfolioValue(String portfolioName, String date) {
      this.portfolioName = portfolioName;
      this.date = date;
      this.calculatePortfolioValueCalled = true;
      this.portfolioValue = mockResult;
      return mockResult;
    }

    @Override
    public Map<String, Double> getPortfolioDistribution(String portfolioName, String date) {
      return Map.of();
    }

    @Override
    public Map<String, Double> getPortfolioComposition(String portfolioName, String date) {
      this.portfolioName = portfolioName;
      this.date = date;
      this.getPortfolioComposition = true;
      return Map.of();
    }

    @Override
    public void rebalancePortfolio(String portfolioName, List<Integer> percentages, String date) {
      //used to test rebalancePortfolio method
    }

    @Override
    public List<String> visualizePerformanceOverTime(String name, String startDate, String endDate,
                                                     boolean isPortfolio, boolean isAbsolute) {
      return List.of();
    }

    @Override
    public void savePortfolio(String portfolioName, String fileName) {
      this.portfolioName = portfolioName;
      this.fileName = fileName;
      this.savePortfolioCalled = true;
    }

    @Override
    public void loadPortfolio(String fileName) {
      this.fileName = fileName;
      this.loadPortfolioCalled = true;
    }
  }

}
