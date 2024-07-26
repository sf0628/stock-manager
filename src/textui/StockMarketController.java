package textui;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stockmarket.Model;
import stockmarket.Stock;
import stockmarket.Utils;

/**
 * Controls the flow of the stock market simulation application.
 */
public class StockMarketController implements Controller {
  private final View view;
  private final Readable readable;
  private final Model model;

  /**
   * Constructs a StockMarketController with the given model, readable, and appendable.
   *
   * @param model      the model to use
   * @param readable   the readable to use for input
   * @param appendable the appendable to use for output
   */
  public StockMarketController(Model model, Readable readable, Appendable appendable) {
    if (model == null || readable == null || appendable == null) {
      throw new IllegalArgumentException("Model, readable, or appendable cannot be null");
    }
    this.model = model;
    this.readable = readable;
    this.view = new StockMarketView(appendable);
  }

  @Override
  public void goController() {
    Scanner scanner = new Scanner(readable);
    boolean quit = false;

    view.welcomeMessage();

    while (!quit) {
      view.writeMessage("Type instruction: ");
      while (scanner.hasNext()) {
        String userInstruction = scanner.next();

        switch (userInstruction) {
          case "view-gain-loss":
            handleViewGainLoss(scanner);
            break;
          case "view-moving-average":
            handleViewMovingAverage(scanner);
            break;
          case "view-crossovers":
            handleViewCrossovers(scanner);
            break;
          case "create-portfolio":
            handleCreatePortfolio(scanner);
            break;
          case "buy-stock":
            handleBuyStock(scanner);
            break;
          case "sell-stock":
            handleSellStock(scanner);
            break;
          case "portfolio-value":
            handleViewPortfolioValue(scanner);
            break;
          case "portfolio-composition":
            handlePortfolioComposition(scanner);
            break;
          case "portfolio-distribution":
            handlePortfolioDistribution(scanner);
            break;
          case "portfolio-rebalance":
            handlePortfolioRebalance(scanner);
            break;
          case "performance-visualization":
            handlePerformanceVisualization(scanner);
            break;
          case "persist-portfolio":
            handlePersistPortfolio(scanner);
            break;
          case "menu":
            view.printMenu();
            break;
          case "q":
          case "quit":
            quit = true;
            break;
          default:
            view.writeMessage("Undefined instruction: " + userInstruction + System.lineSeparator());
        }

        if (!quit) {
          view.writeMessage("Type instruction or type 'menu' to see options: ");
        }
      }
    }

    view.farewellMessage();
  }

  /**
   * Outputs message to see gain/loss for inputted ticker, date range, and portfolio name
   * with "view-gain-loss" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleViewGainLoss(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      String ticker = handleTicker(scanner);
      String startDate = handleDate(scanner, ticker, true, false, false);
      String endDate = handleDate(scanner, ticker,false, false, false);
      try {
        double gainLoss = model.calculateGainLoss(ticker, startDate, endDate);
        view.writeMessage("Gain/Loss for " + ticker + " from " + startDate + " to " + endDate
                + ": " + gainLoss + System.lineSeparator());
        handleComplete = true;
      }
      catch (Exception e) {
        view.writeMessage("Error calculating gain loss: " + e.getMessage()
                + System.lineSeparator());
      }
    }
  }

  /**
   * Outputs message to see moving average for inputted ticker, date, x, and portfolio name
   * with "view-moving-average" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleViewMovingAverage(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      String ticker = handleTicker(scanner);
      String date = handleDate(scanner, ticker, false, true, false);
      view.writeMessage("Enter x value: ");
      int x = scanner.nextInt();
      try {
        double movingAverage = model.calculateMovingAverage(ticker, date, x);
        view.writeMessage(x + "-day moving average for " + ticker + " on " + date + ": "
                + movingAverage + System.lineSeparator());
        handleComplete = true;
      }
      catch (Exception e) {
        view.writeMessage("Error calculating moving average: " + e.getMessage()
                + System.lineSeparator());
      }
    }
  }

  /**
   * Outputs message to see x-day crossovers for inputted ticker, date range, x, and portfolio name
   * with "view-crossovers" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleViewCrossovers(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      String ticker = handleTicker(scanner);
      String startDate = handleDate(scanner, ticker, true, false, false);
      String endDate = handleDate(scanner, ticker,false, false, false);
      view.writeMessage("Enter x value: ");
      int x = scanner.nextInt();
      try {
        List<String> crossovers = model.calculateXDayCrossover(ticker, startDate, endDate, x);
        view.writeMessage(x + "-day crossovers for " + ticker + " from " + startDate + " to "
                + endDate + ": " + System.lineSeparator());
        for (String crossover : crossovers) {
          view.writeMessage(crossover + System.lineSeparator());
        }
        handleComplete = true;
      }
      catch (Exception e) {
        view.writeMessage("Error calculating x crossovers: " + e.getMessage()
                + System.lineSeparator());
      }
    }
  }

  /**
   * Creates portfolio with inputted name and outputs message to verify creation
   * with "create-portfolio" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleCreatePortfolio(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        model.createPortfolio(portfolioName);
        view.writeMessage("Portfolio '" + portfolioName + "' created successfully."
                + System.lineSeparator());
        handleComplete = true;
      } catch (Exception e) {
        view.writeMessage(e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Adds inputted number of shares of the inputted stock to inputted portfolio and outputs message
   * to verify with "add-stock" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleBuyStock(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        String ticker = handleTicker(scanner);
        double shares = handleShares(scanner);
        String date = handleDate(scanner, ticker, false, true, false);
        model.updateStockInPortfolio(portfolioName, ticker, shares, date, true);
        view.writeMessage(shares + " shares of " + ticker + " added to portfolio '" + portfolioName
                + "' on " + date + "." + System.lineSeparator());
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Deletes inputted number of shares of the inputted stock to inputted portfolio and outputs
   * message to verify with "sell-stock" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleSellStock(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        String ticker = handleTicker(scanner);
        double shares = handleShares(scanner);
        String date = handleDate(scanner, ticker, false, true, false);
        model.updateStockInPortfolio(portfolioName, ticker, shares, date,false);
        view.writeMessage(shares + " shares of " + ticker + " sold from portfolio '"
                + portfolioName
                + "' on " + date + "." + System.lineSeparator());
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Outputs message to see portfolio value for inputted date
   * with "portfolio-value" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handleViewPortfolioValue(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        String date = handleDate(scanner, portfolioName, false, true,
                true);

        double portfolioValue = model.calculatePortfolioValue(portfolioName, date);
        view.writeMessage("Value of portfolio '" + portfolioName + "' on " + date + ": $"
                + portfolioValue + System.lineSeparator());
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Outputs message to see portfolio composition for inputted date
   * with "portfolio-composition" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handlePortfolioComposition(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        String date = handleDate(scanner, portfolioName, false, true,
                true);

        Map<String, Double> portfolioComposition = model.getPortfolioComposition(portfolioName,
                date);

        view.writeMessage("Portfolio Composition for " + portfolioName + " on " + date + ":"
                + System.lineSeparator());
        for (Map.Entry<String, Double> entry : portfolioComposition.entrySet()) {
          view.writeMessage("Stock " + entry.getKey() + " has " + entry.getValue()
                  + " shares. " + System.lineSeparator());
        }
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Outputs message to see portfolio distribution for inputted date
   * with "portfolio-distribution" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handlePortfolioDistribution(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        String date = handleDate(scanner, portfolioName, false, true,
                true);

        Map<String, Double> portfolioDistribution =
                model.getPortfolioDistribution(portfolioName, date);
        view.writeMessage("Portfolio Distribution for " + portfolioName + " on " + date + ":"
                + System.lineSeparator());
        for (Map.Entry<String, Double> entry : portfolioDistribution.entrySet()) {
          view.writeMessage("For stock " + entry.getKey() + ", the value is $"
                  + entry.getValue() + "." + System.lineSeparator());
        }
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  /**
   * Outputs message to show successful portfolio rebalancing for inputted date
   * with "portfolio-rebalance" instruction from the user.
   *
   * @param scanner the scanner to read user input
   */
  private void handlePortfolioRebalance(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Enter portfolio name: ");
        String portfolioName = scanner.next();
        List<Stock> stocks = model.findPortfolio(portfolioName).getStocks();
        List<Integer> percentages = getStockPercentages(scanner, stocks);
        String date = handleDate(scanner, portfolioName, false, true,
                true);
        model.rebalancePortfolio(portfolioName, percentages, date);
        view.writeMessage("Portfolio '" + portfolioName + "' has been rebalanced on " + date + "."
                + System.lineSeparator());
        handleComplete = true;
      } catch (InputMismatchException e) {
        view.writeMessage("Error: Invalid input format. Please re-enter the instruction."
                + System.lineSeparator());
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  private void handlePerformanceVisualization(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        String name;
        String startDate;
        String endDate;
        boolean isPortfolio;
        boolean isAbsolute;
        view.writeMessage("Select number: (1) Portfolio, or (2) Stock: ");
        int number = scanner.nextInt();

        if (number == 1) {
          isPortfolio = true;
          view.writeMessage("Enter portfolio name : ");
          name = scanner.next();
          startDate = handleDate(scanner, name, true, false, true);
          endDate = handleDate(scanner, name, false, false, true);
        } else if (number == 2) {
          isPortfolio = false;
          name = handleTicker(scanner);
          startDate = handleDate(scanner, name, true, false, false);
          endDate = handleDate(scanner, name, false, false, false);
        } else {
          throw new IllegalArgumentException("Invalid selection. Choose 1 or 2.");
        }
        view.writeMessage("Select absolute (1) or relative (2) scale: ");
        int scale = scanner.nextInt();
        if (scale == 1) {
          isAbsolute = true;
        } else if (scale == 2) {
          isAbsolute = false;
        } else {
          throw new IllegalArgumentException("Invalid selection. Choose 1 or 2.");
        }
        view.writeMessage("Loading visualization now... ");
        List<String> visual = model.visualizePerformanceOverTime(
                name, startDate, endDate, isPortfolio, isAbsolute);
        for (String s : visual) {
          view.writeMessage(s + System.lineSeparator());
        }
        handleComplete = true;
      } catch (Exception e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        scanner.nextLine();
      }
    }
  }

  private void handlePersistPortfolio(Scanner scanner) {
    boolean handleComplete = false;
    while (!handleComplete) {
      try {
        view.writeMessage("Select Number: Save (1), or Load (2) portfolio: ");
        int number = scanner.nextInt();
        if (number == 1) {
          view.writeMessage("Enter portfolio name : ");
          String portfolioName = scanner.next();
          view.writeMessage("Enter file name (excluding .xml) : ");
          String fileName = scanner.next();
          model.savePortfolio(portfolioName, fileName);
        } else if (number == 2) {
          view.writeMessage("Enter file name (excluding .xml) : ");
          String fileName = scanner.next();
          model.loadPortfolio(fileName);
        } else {
          throw new IllegalArgumentException("Invalid selection. Choose 1 or 2.");
        }
        handleComplete = true;
      }
      catch (Exception e) {
        view.writeMessage(e.getMessage() + System.lineSeparator());
      }
    }
  }

  private List<Integer> getStockPercentages(Scanner scanner, List<Stock> stocks) {
    List<Integer> percentages = new ArrayList<>();
    int stockCount = stocks.size();
    int currentCount = 0;
    int total = 0;
    while (currentCount < stockCount) {
      view.writeMessage("Enter stock percentage for '"
              + stocks.get(currentCount).getTicker()
              + "': ");
      int nextPercentage;
      if (scanner.hasNextInt()) {
        nextPercentage = scanner.nextInt();
        if (nextPercentage > 100 || nextPercentage < 0) {
          throw new InputMismatchException("Percentage must be between 0 and 100");
        }
        total += nextPercentage;
        if (total > 100) {
          throw new IllegalArgumentException("Percentages cannot be greater than 100");
        }
        percentages.add(nextPercentage);
        currentCount++;
      }
    }
    return percentages;
  }

  private String handleTicker(Scanner scanner) {
    String ticker = "";
    boolean validInput = false;
    while (!validInput) {
      try {
        view.writeMessage("Enter ticker: ");
        ticker = scanner.next();
        Utils.validTicker(ticker);
        validInput = true;
      }
      catch (IllegalArgumentException e) {
        view.writeMessage(e.getMessage() + "Try again." + System.lineSeparator());
      }
    }
    return ticker;
  }

  private String handleDate(Scanner scanner, String name, boolean isStart,
                            boolean isNeither, boolean isPortfolio) {
    boolean validDate = false;
    String date = "";
    String startOrEnd = "";
    if (isNeither) {
      //remains empty string
    } else if (isStart) {
      startOrEnd = "start ";
    } else {
      startOrEnd = "end ";
    }
    while (!validDate) {
      boolean validYear = false;
      boolean validMonth = false;
      boolean validDay = false;
      try {
        while (!validYear) {
          try {
            view.writeMessage("Enter " + startOrEnd + "year: ");
            int year = scanner.nextInt();
            Utils.validYear(year);
            validYear = true;
            date += String.valueOf(year);
            date += "-";
          }
          catch (IllegalArgumentException e) {
            view.writeMessage(e.getMessage() + ". Try again." + System.lineSeparator());
          }
          catch (InputMismatchException e) {
            view.writeMessage("Month value must be integer. Try again." + System.lineSeparator());
          }
        }

        while (!validMonth) {
          try {
            view.writeMessage("Enter " + startOrEnd + "month: ");
            int month = scanner.nextInt();
            Utils.validMonth(month);
            validMonth = true;
            if (String.valueOf(month).length() == 1) {
              date += "0";
            }
            date += String.valueOf(month);
            date += "-";
          }
          catch (IllegalArgumentException e) {
            view.writeMessage(e.getMessage() + "Try again." + System.lineSeparator());
          }
          catch (InputMismatchException e) {
            view.writeMessage("Month value must be integer. Try again." + System.lineSeparator());
          }
        }

        while (!validDay) {
          try {
            view.writeMessage("Enter " + startOrEnd + "day: ");
            int day = scanner.nextInt();
            Utils.validDay(day);
            validDay = true;
            if (String.valueOf(day).length() == 1) {
              date += "0";
            }
            date += String.valueOf(day);
          }
          catch (IllegalArgumentException e) {
            view.writeMessage(e.getMessage() + "Try again." + System.lineSeparator());
          }
          catch (InputMismatchException e) {
            view.writeMessage("Month value must be integer. Try again." + System.lineSeparator());
          }
        }
        String ticker = null;
        if (isPortfolio) {
          ticker = "GOOG";
        } else {
          ticker = name;
        }
        //checks if date exist in ticker, throws error if not
        Utils.checkDate(date, ticker);
        validDate = true;
      }
      catch (Exception e) {
        view.writeMessage(e.getMessage() + ". Try again." + System.lineSeparator());
        date = "";
      }
    }
    return date;
  }

  private double handleShares(Scanner scanner) {
    double shares = 0;
    boolean validInput = false;
    while (!validInput) {
      try {
        view.writeMessage("Enter shares: ");
        shares = scanner.nextDouble();
        if (shares < 0) {
          throw new InputMismatchException("Invalid shares value." + System.lineSeparator());
        }
        validInput = true;
      }
      catch (IllegalArgumentException e) {
        view.writeMessage(e.getMessage() + " Try again." + System.lineSeparator());
      }
    }
    return shares;
  }
}

