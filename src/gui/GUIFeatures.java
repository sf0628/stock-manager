package gui;

/**
 * This interface represents all the features of in the graphical user interface (GUI) of our stock
 * program.  Our GUI includes various methods that allow users to interact with stocks and manage
 * their portfolio. This includes initiating the program with a user-friendly interface, create
 * portfolio functionality that adds a portfolio given a valid name, buy/sell stock method that
 * allows users to add and remove stocks from the portfolio. Additionally, users can view a
 * portfolio's composition (the number of shares per stock in a portfolio, they can view the
 * monetary value of a portfolio, and save/load portfolios locally.
 */
public interface GUIFeatures {
  /**
   * This method associates all the buttons with various action listeners. This action listeners
   * may include calling methods in the model to load/manipulate portfolios, or to navigate to a
   * different tabbed pane.
   * @param view represents all the GUI elements of an interface
   */
  void setView(GUIView view);

  /**
   * This method calls the createPortfolio method in the model, allowing users to add any amount
   * of portfolios to their program. In cases where the portfolio name is invalid (portfolio
   * already exists, name wasn't enter), a tab with error messages will show.
   * Otherwise, a tab indicating that the portfolio was successfully created will show.
   * @param portfolioName the name of the portfolio to be created
   */
  void createPortfolio(String portfolioName);

  /**
   * This method calls the updateStockInPortfolio method in the model to add a given amount of
   * shares of a specified stock to a specified portfolio. If stock was successfully added, a
   * message showing the shares of stocks added to a certain portfolio will show. Otherwise, a
   * message will explain the reason why the stock could not be bought (portfolio doesn't exist,
   * ticker invalid, etc.)
   * @param portfolioName the name of the portfolio stock is being bought
   * @param ticker the ticker of the stock
   * @param shares the number of shares of the stock
   * @param date the date at which the stock is being bought
   */
  void buyStock(String portfolioName, String ticker, double shares, String date);

  /**
   * This method calls the updateStockInPortfolio method in the model to remove a given amount of
   * shares of a specified stock to a specified portfolio.  If stock was successfully removed,
   * a message showing the shares of stocks sold to a certain portfolio will show. Otherwise, a
   * message will explain the reason why the stock could not be bought (portfolio doesn't exist,
   * ticker invalid, not enough shares to sell, etc.).
   * @param portfolioName the name of the portfolio stock is being sold
   * @param ticker the ticker of the stock
   * @param shares the number of shares of the stock
   * @param date the date at which the stock is being bought
   */
  void sellStock(String portfolioName, String ticker, double shares, String date);

  /**
   * This method calls the calculatePortfolioValue method in the model to calculate the total value
   * of a portfolio in a certain date. If the portfolio value was successfully calculated, then a
   * message revealing the value on the given date will be shown. Otherwise, a message will explain
   * the reason why the value could not be calculated (portfolio doesn't exist, date valid).
   * @param portfolioName the name of the portfolio being calculated
   * @param date the date when the portfolio is being calculated
   */
  void getPortfolioValue(String portfolioName, String date);

  /**
   * This method calls the getPortfolioComposition method in the model to retrieve the number of
   * shares per stock in a portfolio. If the composition was successfully retrieved, then a message
   * revealing the value on the given date will be shown. Otherwise, a message will explain the
   * reason why the value could not be calculated (portfolio doesn't exist, date valid).
   * @param portfolioName the name of the portfolio the composition is being retrieved from
   * @param date the date that the composition is being retrieved
   */
  void getPortfolioComposition(String portfolioName, String date);

  /**
   * This method calls the savePortfolio method in the model to save a portfolio to an XML file.
   * If the portfolio was successfully saved, then a message indicating success with show.
   * Otherwise, a message will explain the reason why portfolio could not be saved
   * (portfolio doesn't exist, file name already exists, etc.).
   * @param portfolioName the name of the portfolio being saved
   * @param fileName the name of the file being saved to
   */
  void savePortfolio(String portfolioName, String fileName);

  /**
   * This method calls the loadPortfolio method in the model to load a portfolio to from an XML
   * file given the name of the file. If the portfolio was successfully loaded, then a message
   * indicating successful loading will show. Otherwise, a message will explain the reason why
   * the portfolio could not be loaded (file doesn't exist with the given name).
   * @param fileName name of file being loaded
   */
  void loadPortfolio(String fileName);
}
