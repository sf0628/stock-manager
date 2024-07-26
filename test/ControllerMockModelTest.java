import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import gui.GUIFeatures;
import gui.GUIView;
import stockmarket.Model;
import stockmarket.Portfolio;
import textui.StockMarketController;

import static org.junit.Assert.assertEquals;

/**
 * Tests that the stock controller sends the correct messages and inputs to model.
 */
public class ControllerMockModelTest {
  private MockStockModel model;
  private Readable rd;
  private Appendable ap;
  private StockMarketController cont;
  String expectedOutput;


  @Before
  public void setUp() {
    model = new MockStockModel();
    rd = new StringReader("");
    ap = new StringBuilder();
    cont = new StockMarketController(model, rd, ap);
    expectedOutput = "Welcome to the stock market simulation!"
            + System.lineSeparator()
            + "The following are the supported user instructions:"
            + System.lineSeparator()
            + "view-gain-loss ticker start-date end-date " +
            "(calculate gain or loss of the stock over the time period)"
            + System.lineSeparator()
            + "view-moving-average ticker date x  " +
            "(calculate x-day moving average starting from date)"
            + System.lineSeparator()
            + "view-crossovers ticker start-date end-date x " +
            "(determine x-day crossovers for the stock over the time period)"
            + System.lineSeparator()
            + "create-portfolio portfolio-name (create a new portfolio)"
            + System.lineSeparator()
            + "add-stock portfolio-name ticker shares (add stock to portfolio)"
            + System.lineSeparator()
            + "portfolio-value portfolio-name date (calculate portfolio value)"
            + System.lineSeparator()
            + "menu (print supported instruction list)"
            + System.lineSeparator();
  }

  //testing the controller transmits messages to Appendable
  @Test
  public void welcomeMessage() {
    MockStockModel stockModel = new MockStockModel();
    rd = new StringReader("q");
    cont = new StockMarketController(stockModel, rd, ap);
    cont.goController();

    String[] lines = ap.toString().split(System.lineSeparator());
    StringBuilder welcomeMessage = new StringBuilder();

    for (int i = 0; i < 9; i++) {
      welcomeMessage.append(lines[i]).append(System.lineSeparator());
    }

    expectedOutput = "Welcome to the stock market simulation!" + System.lineSeparator() +
            "The following are the supported user instructions:" + System.lineSeparator() +
            "Type the first command and the following information based on your needs. "
            + System.lineSeparator() +
            "You can only perform portfolio operations on dates in chronological order. " +
            "The operations " + System.lineSeparator() +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions " + System.lineSeparator() + "portfolio-rebalance) " +
            "cannot be performed on a date earlier than the most recent date entered overall."
            + System.lineSeparator() + ""  + System.lineSeparator() +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. Takes in "
            + "ticker, start date, and end date)" + System.lineSeparator() +
            "view-moving-average (Calculate x-day moving average starting from date. Takes in " +
            "ticker, date, and x-value.)"  + System.lineSeparator();

    assertEquals(expectedOutput, welcomeMessage.toString());
  }

  @Test
  public void farewellMessage() {
    MockStockModel stockModel = new MockStockModel();
    rd = new StringReader("q");
    cont = new StockMarketController(stockModel, rd, ap);
    cont.goController();

    String[] lines = ap.toString().split(System.lineSeparator());
    String actualFarewellMessage = lines[lines.length - 1];
    String expectedFarewellMessage = "Type instruction: Thank you, goodbye!";

    assertEquals(expectedFarewellMessage, actualFarewellMessage);
  }

  @Test
  public void menu() {
    rd = new StringReader("menu\nq");
    cont = new StockMarketController(model, rd, ap);
    cont.goController();

    expectedOutput = "Welcome to the stock market simulation!\n" +
            "The following are the supported user instructions:\n" +
            "Type the first command and the following information based on your needs. \n" +
            "You can only perform portfolio operations on dates in chronological order. The " +
            "operations \n" +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions \n" +
            "portfolio-rebalance) cannot be performed on a date earlier than the most recent " +
            "date entered overall.\n" +
            "\n" +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. Takes " +
            "in ticker, start date, and end date)\n" +
            "view-moving-average (Calculate x-day moving average starting from date. Takes " +
            "in ticker, date, and x-value.)\n" +
            "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)\n" +
            "create-portfolio (Creates a new portfolio. Takes in portfolio name.)\n" +
            "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and shares.)" +
            "\n" +
            "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)\n" +
            "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)\n" +
            "portfolio-composition (Retrieves number of shares per stock in portfolio. Takes " +
            "in portfolio name and date.)\n" +
            "portfolio-distribution (Retrieves values of each stock in portfolio. Takes " +
            "in portfolio name and date.)\n" +
            "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)\n" +
            "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)\n" +
            "persist-portfolio (Saves or loads portfolio.)\n" +
            "menu (Print supported instruction list.)\n" +
            "q or quit (Quits the program.)\n" +
            "\n" +
            "Type instruction: The following are the supported user instructions:\n" +
            "Type the first command and the following information based on your needs. \n" +
            "You can only perform portfolio operations on dates in chronological order. The " +
            "operations \n" +
            "(buy-stock, sell-stock, portfolio-value, portfolio-composition, " +
            "portfolio-distributions \n" +
            "portfolio-rebalance) cannot be performed on a date earlier than the most recent " +
            "date entered overall.\n" +
            "\n" +
            "view-gain-loss (Calculates gain or loss of the stock over the time period. Takes " +
            "in ticker, start date, and end date)\n" +
            "view-moving-average (Calculate x-day moving average starting from date. Takes " +
            "in ticker, date, and x-value.)\n" +
            "view-crossovers (Determine x-day crossovers for the stock over the time period. " +
            "Takes in ticker, start date, end date, and x-value.)\n" +
            "create-portfolio (Creates a new portfolio. Takes in portfolio name.)\n" +
            "buy-stock (Adds stock to a portfolio. Takes in portfolio name, ticker, and " +
            "shares.)\n" +
            "sell-stock (Removes stock from a portfolio. Takes in portfolio name, ticker, " +
            "and shares.)\n" +
            "portfolio-value (Calculate portfolio value of a given date. Takes in portfolio " +
            "name and date.)\n" +
            "portfolio-composition (Retrieves number of shares per stock in portfolio. Takes " +
            "in portfolio name and date.)\n" +
            "portfolio-distribution (Retrieves values of each stock in portfolio. Takes in " +
            "portfolio name and date.)\n" +
            "portfolio-rebalance (Re-balances stock distribution by percentage. Takes in " +
            "portfolio name, percentages, and date.)\n" +
            "performance-visualization (Visualizes the portfolio/stock value over a period " +
            "of time.)\n" +
            "persist-portfolio (Saves or loads portfolio.)\n" +
            "menu (Print supported instruction list.)\n" +
            "q or quit (Quits the program.)\n" +
            "\n" +
            "Type instruction or type 'menu' to see options: Thank you, goodbye!\n";

    assertEquals(expectedOutput, ap.toString());
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