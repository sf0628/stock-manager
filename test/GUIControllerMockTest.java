import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import gui.GUIController;
import gui.GUIFeatures;
import gui.GUIView;
import stockmarket.Model;
import stockmarket.Portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests that the GUI stock controller sends the correct messages and inputs to model.
 */
public class GUIControllerMockTest {
  Model model;
  GUIView view;
  GUIController controller;

  @Before
  public void setUp() {
    model = new MockStockModel();
    view = new MockViewGUI();
    controller = new GUIController(model);
  }

  @Test
  public void testSetView() {
    if (view instanceof MockViewGUI) {
      MockViewGUI mockView = (MockViewGUI) view;
      assertNull(mockView.getGuiFeatures());
      controller.setView(view);
      assertEquals(controller, mockView.getGuiFeatures());
    } else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testCreatePortfolio() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      assertNull(mockModel.portfolioName);
      controller.createPortfolio("Test Create Portfolio");
      assertEquals("Test Create Portfolio", mockModel.portfolioName);
      assertTrue(mockModel.createPortfolioCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testBuyStock() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      assertFalse(mockModel.updateStockInPortfolioCalled);
      controller.buyStock("Test Buy Stock",
              "Ticker", 1, "2023-02-20");
      assertEquals("Test Buy Stock", mockModel.portfolioName);
      assertEquals("Ticker", mockModel.ticker);
      assertEquals(1.0, mockModel.shares, 0.01);
      assertEquals("2023-02-20", mockModel.date);
      assertTrue(mockModel.isAdding);
      assertTrue(mockModel.updateStockInPortfolioCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testSellStock() {
    controller.setView(view);
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      assertFalse(mockModel.updateStockInPortfolioCalled);
      controller.sellStock("Test Buy Stock",
              "Ticker", 1, "2023-02-20");
      assertEquals("Test Buy Stock", mockModel.portfolioName);
      assertEquals("Ticker", mockModel.ticker);
      assertEquals(1.0, mockModel.shares, 0.01);
      assertEquals("2023-02-20", mockModel.date);
      assertFalse(mockModel.isAdding);
      assertTrue(mockModel.updateStockInPortfolioCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testGetPortfolioValue() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      mockModel.setMockResult(100);
      assertFalse(mockModel.getPortfolioValueCalled);
      controller.getPortfolioValue("Testing", "2000-01-01");
      assertEquals("Testing", mockModel.portfolioName);
      assertEquals("2000-01-01", mockModel.date);
      assertEquals(100, mockModel.portfolioValue, 0.01);
      assertTrue(mockModel.calculatePortfolioValueCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testGetPortfolioComposition() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      assertFalse(mockModel.getPortfolioComposition);
      controller.getPortfolioComposition("Testing", "2000-01-01");
      assertEquals("Testing", mockModel.portfolioName);
      assertEquals("2000-01-01", mockModel.date);
      assertTrue(mockModel.getPortfolioComposition);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testSavePortfolio() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      assertFalse(mockModel.savePortfolioCalled);
      controller.savePortfolio("Testing", "File");
      assertEquals("Testing", mockModel.portfolioName);
      assertEquals("File", mockModel.fileName);
      assertTrue(mockModel.savePortfolioCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
    }
  }

  @Test
  public void testLoadPortfolio() {
    if (view instanceof MockViewGUI && model instanceof MockStockModel) {
      MockStockModel mockModel = (MockStockModel) model;
      controller.setView(view);
      assertFalse(mockModel.loadPortfolioCalled);
      controller.loadPortfolio("Testing");
      assertEquals("Testing", mockModel.fileName);
      assertTrue(mockModel.loadPortfolioCalled);
    }
    else {
      fail("model is not an instance of MockStockModel");
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

  /**
   * Represents the mock of a GUIView with methods that are setters.
   */
  public static class MockViewGUI implements GUIView {
    String message;
    String portfolioValue;
    String portfolioComposition;
    public GUIFeatures guiFeatures;

    /**
     * Gets the message.
     * @return the message
     */
    public String getMessage() {
      return message;
    }

    /**
     * Gets the portfolio composition.
     * @return the portfolio composition
     */
    public String getPortfolioComposition() {
      return portfolioComposition;
    }

    /**
     * Gets the GUI features.
     * @return GUIFeatures
     */
    public GUIFeatures getGuiFeatures() {
      return guiFeatures;
    }

    @Override
    public void showMessage(String message) {
      this.message = message;
    }

    @Override
    public void displayPortfolioValue(String value) {
      this.portfolioValue = value;
    }

    @Override
    public void displayPortfolioComposition(String composition) {
      this.portfolioComposition = composition;
    }

    @Override
    public void addFeatures(GUIFeatures features) {
      this.guiFeatures = features;
    }

    @Override
    public void setVisible(boolean visible) {
      //stub implementation
    }
  }
}
