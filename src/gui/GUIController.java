package gui;

import java.io.File;
import java.util.Map;

import stockmarket.Model;

/**
 * The JFrameView class extends JFrame and implements the GUIView interface.
 * This class is responsible for creating and displaying the graphical user interface
 * for a portfolio management application. It includes various panels and components
 * to perform actions such as creating a portfolio, buying and selling stocks,
 * getting portfolio values and compositions, and saving or loading portfolios.
 */
public class GUIController implements GUIFeatures {
  private Model model;
  private GUIView view;

  /**
   * Constructs a GUIController with the specified model, this acts as an intermediary
   * between the model and the view, handling user actions and updating the view accordingly.
   * @param model the model to be used by the controller
   */
  public GUIController(Model model) {
    this.model = model;
  }

  @Override
  public void setView(GUIView view) {
    this.view = view;
    view.addFeatures(this);
  }

  @Override
  public void createPortfolio(String portfolioName) {
    try {
      model.createPortfolio(portfolioName);
      view.showMessage("Portfolio '" + portfolioName + "' created successfully.");
    }
    catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void buyStock(String portfolioName, String ticker, double shares, String date) {
    try {
      model.updateStockInPortfolio(portfolioName, ticker, shares, date, true);
      view.showMessage(shares + " shares of " + ticker + " added to portfolio '"
              + portfolioName + "' on " + date + ".");
    }
    catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void sellStock(String portfolioName, String ticker, double shares, String date) {
    try {
      model.updateStockInPortfolio(portfolioName, ticker, shares, date, false);
      view.showMessage(shares + " shares of " + ticker + " sold from portfolio '"
              + portfolioName + "' on " + date + ".");
    }
    catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void getPortfolioValue(String portfolioName, String date) {
    try {
      double value = model.calculatePortfolioValue(portfolioName, date);
      view.displayPortfolioValue("Value of portfolio '" + portfolioName + "' on " + date + ": $"
              + value);
    }
    catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void getPortfolioComposition(String portfolioName, String date) {
    try {
      Map<String, Double> composition = model.getPortfolioComposition(portfolioName, date);
      String output = "Portfolio composition for " + portfolioName + " on " + date + ":"
              + System.lineSeparator();
      for (Map.Entry<String, Double> entry : composition.entrySet()) {
        output += "Stock " + entry.getKey() + " has " + entry.getValue()
                + " shares. " + System.lineSeparator();
      }
      view.displayPortfolioComposition(output);
    }
    catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void savePortfolio(String portfolioName, String filePath) {
    String fileName = new File(filePath).getName();

    if (fileName.endsWith(".xml")) {
      fileName = fileName.substring(0, fileName.length() - 4);
    }

    try {
      model.savePortfolio(portfolioName, fileName);
      view.showMessage("Portfolio '" + portfolioName + "' saved successfully to file '"
              + fileName + "'.");
    } catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void loadPortfolio(String filePath) {
    String fileName = new File(filePath).getName();

    if (fileName.endsWith(".xml")) {
      fileName = fileName.substring(0, fileName.length() - 4);
    }

    try {
      model.loadPortfolio(fileName);
      view.showMessage("File '" + fileName + "' loaded successfully.");
    } catch (Exception e) {
      view.showMessage(e.getMessage());
    }
  }
}
