package stockmarket;

import java.util.List;
import java.util.Map;

/**
 * Represents the model in the stock market simulation application.
 * Defines the methods for core stock market operations.
 */
public interface Model {
  /**
   * Calculates the gain or loss of a stock over a given time period.
   *
   * @param ticker    the stock ticker symbol
   * @param startDate the start date in the format "YYYY-MM-DD"
   * @param endDate   the end date in the format "YYYY-MM-DD"
   * @return the gain or loss over the period
   */
  public double calculateGainLoss(String ticker, String startDate, String endDate);

  /**
   * Calculates the x-day moving average of a stock for a given date and x.
   *
   * @param ticker the stock ticker symbol
   * @param date   the date in the format "YYYY-MM-DD"
   * @param x      the number of days for the moving average
   * @return the x-day moving average
   */
  public double calculateMovingAverage(String ticker, String date, int x);

  /**
   * Calculates the x-day crossovers for a given stock and x over a given date range.
   *
   * @param ticker    the stock ticker symbol
   * @param startDate the start date in the format "YYYY-MM-DD"
   * @param endDate   the end date in the format "YYYY-MM-DD"
   * @param x         the number of days for the crossover
   * @return a list of days that are x-day crossovers over date range
   */
  public List<String> calculateXDayCrossover(String ticker, String startDate, String endDate,
                                             int x);

  /**
   * Creates a new stock portfolio.
   *
   * @param portfolioName the name of the portfolio
   */
  public void createPortfolio(String portfolioName);

  /**
   * Finds a portfolio by its name.
   *
   * @param portfolioName the name of the portfolio to find
   * @return the portfolio with the given name
   * @throws IllegalArgumentException if the portfolio with the given name is not found
   */
  public Portfolio findPortfolio(String portfolioName);

  /**
   * Adds/sells a stock to/from a given portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio to add/sell the stock to/from
   * @param ticker        the stock ticker symbol to add/sell
   * @param shares        the number of shares of the stock to add/sell
   * @param date          the date the stock is added/sold
   * @param isAdding      whether the stock is being bought or sold
   *
   * @throws IllegalArgumentException when stock market isn't open on that date for that stock
   */
  public void updateStockInPortfolio(String portfolioName, String ticker, double shares,
                                     String date, boolean isAdding);

  /**
   * Calculates the value of a given portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date in the format "YYYY-MM-DD"
   * @return the value of the portfolio on the date
   * @throws IllegalArgumentException if date is in future or invalid
   */
  public double calculatePortfolioValue(String portfolioName, String date);

  /**
   * Gets value distribution of a given portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio to get the distribution of
   * @param date          the date to get the distribution of values on
   * @return the distribution of values in the format of a map object with stock ticker
   *         and the stock's individual value
   * @throws IllegalArgumentException if date is in future or invalid
   */
  public Map<String, Double> getPortfolioDistribution(String portfolioName, String date);

  /**
   * Gets share composition of a given portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio to get the composition of
   * @param date          the date to get the composition of values on
   * @return the composition of shares in the format of a map object with stock ticker
   *         and the stock's number of shares
   * @throws IllegalArgumentException if date is in future or invalid
   */
  public Map<String, Double> getPortfolioComposition(String portfolioName, String date);

  /**
   * Rebalances the shares of stocks in the portfolio on a specific date
   * so distribution of value of the portfolio matches the intended weights.
   *
   * @param portfolioName the name of the portfolio to get the composition of
   * @param percentages the intended distribution percentages
   * @param date the date of re-balance
   * @throws IllegalArgumentException if percentage list is empty or doesn't match portfolio size
   *         or if date is in future or invalid
   */
  public void rebalancePortfolio(String portfolioName, List<Integer> percentages, String date);

  /**
   * Visualizes either a stock or a portfolio of stocks given the start date, and end date. The
   * scale at which the stock or portfolio's value is represented is either absolute (starting
   * from 0) or relative (starting from base value).
   * @param name the name of the portfolio or the stock ticker
   * @param startDate the start date of the portfolio/stock's performance
   * @param endDate the end date of the portfolio/stock's performance
   * @param isPortfolio true if representing a portfolio, false if representing a stock in portfolio
   * @param isAbsolute true (absolute) if scale begins at one, false (relative) if scale begins at
   *                   a base value
   * @return a list of strings that represents each row of the visualization
   */
  public List<String> visualizePerformanceOverTime(String name, String startDate, String endDate,
                                                   boolean isPortfolio, boolean isAbsolute);

  /**
   * Saves a snapshot of a portfolio to an XML file. When a portfolio is saved, the portfolio will
   * be removed from the program, and stored as an XML file locally until later loaded again.
   * @param portfolioName the name of the portfolio
   * @param fileName the file name of the newly created XML file
   */
  public void savePortfolio(String portfolioName, String fileName);

  /**
   * Loads a previously saved, or user created XML file following the structure of a portfolio. Once
   * the portfolio is loaded, it is added to program.
   * @param fileName the file name of an XML file representing a portfolio
   */
  public void loadPortfolio(String fileName);
}