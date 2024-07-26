The features that work and are complete are the following:

1. Portfolios: Our stock market simulation allows for users to have different portfolios of stocks.
Each portfolio that is added must be given a name and contains no stocks to begin with.
In StockModel, the following methods are created:
    - getPortfolios(): Returns the list of portfolios that the user has.
    - createPortfolio(String portfolioName): generates a new empty stock portfolio with the
    given name
    - findPortfolio(String portfolioName): find the correct Portfolio object in a list of
    portfolios given the portfolio name
    - updateStockInPortfolio(String portfolioName, String ticker, double shares, String date,
    boolean isAdding): adds/deletes a specified number of shares of a stocks to a portfolio for
    buying/selling on a specific date
    - calculatePortfolioValue(String portfolioName, String date): calculates the total value of a
    portfolio on a specified date
    - getPortfolioDistribution(String portfolioName, String date): gets values of each stock in
    portfolio
    - getPortfolioComposition(String portfolioName, String date): gets shares of each stock in
    portfolio
    - rebalancePortfolio(String portfolioName, List<Integer> percentages, String date): rebalances
    stock distribution by weights
    - savePortfolio(String portfolioName, String fileName): saves the portfolio with the matching
    portfolio name to an XML file that matches the file name
    - loadPortfolio(String fileName): loads a portfolio from XML file

2. Stock Trend Statistics: In addition to portfolio management, our program also allows users to
gain insight on various trends in the stock market: whether a stock gains or loses value over a
period of time, x-day moving average, and x-day crossovers.
    - calculateGainLoss(String ticker, String startDate, String endDate): calculates gain or loss
    in value of a certain stock during a specified time frame.
    - calculateMovingAverage(String ticker, String date, int x): calculates the x-day moving
    average, also known as the average stock value of the last x days starting from the given date
    - calculateXDayCrossover(String ticker, String startDate, String endDate, int x): produces a
    list of dates that fall between the given start date and end date such that the closing price
    is greater than the x-day moving average for that day.
    - visualizePerformanceOverTime(String name, String startDate, String endDate, boolean
    isPortfolio, boolean isAbsolute): visualizes either a stock or a portfolio of stocks given
    the start date, and end date

3. Stock: For every stock that is added to a portfolio, the ticker, number of shares, and date
added is stored with a class called Stock
    - getTicker(): This method is used to get the ticker, a unique combination of symbols that
    represents a stock.
    - getShares(): This method gets the number of shares added for that particular stock
    - getDateAdded(): This method gets the date the stock was added
    - equals(): This method overrides the original equals() method by enforcing that stocks are
    considered equal if they have the same ticker symbol, number of shares, and date added
    - hashCode(): This method overrides the original hashCode() method allowing stocks that are
    equal to also have equal hash codes.

4. Stock Data: Each stock has a list of data points for the period of days that the market is open.
This data is represented in the StockInfo class. This class includes the timestamp in YYYY-MM-DD
format, the opening and closing price, the highest and lowest value of the day, and the volume
of shares.
    - getTimestamp(): gets the represented time stamp.
    - getOpen(): gets the opening price.
    - getHigh(): gets the highest price of the day.
    - getLow(): gets the lowest price of the day.
    - getClose(): gets the closing price of the day.
    - getVolume(): gets the volume of shares of the stock that day.
    - toString(): overrides the original toString() method to return the fields in a list

5. Stock Market Controller: In our stock simulation, the stock market controller interacts with
the model and view to control the flow of the stock market simulation application.
    - goController(): initializes a scanner object that reads inputs and handles the different cases to
    process different user instructions.
    - handleViewGainLoss(Scanner scanner): reads in and sets the input values, calls the
    calculateGainLoss method and gets the view to output the results.
    - handleViewMovingAverage(Scanner scanner): reads in and sets the input values, calls the
    calculateMovingAverage method and gets the view to output the results.
    - handleViewCrossovers(Scanner scanner): reads in and sets the input values, calls the
    calculateXDayCrossover method and gets the view to output the results.
    - handleCreatePortfolio(Scanner scanner): reads in and sets the input values, calls the
    createPortfolio method and gets the view to output the results.
    - handleBuyStock(Scanner scanner): reads in and sets the input values, calls the
    updateStockInPortfolio method and gets the view to output the results.
    - handleSellStock(Scanner scanner): reads in and sets the input values, calls the
    updateStockInPortfolio method and gets the view to output the results.
    - handleViewPortfolioValue(Scanner scanner): reads in and sets the input values, calls the
    calculatePortfolioValue method and gets the view to output the results.
    - handlePortfolioComposition(Scanner scanner): reads in and sets the input values, calls the
    getPortfolioComposition method and gets the view to output the results.
    - handlePortfolioDistribution(Scanner scanner): reads in and sets the input values, calls the
    getPortfolioDistribution method and gets the view to output the results.
    - handlePortfolioRebalance(Scanner scanner): reads in and sets the input values, calls the
    rebalancePortfolio method and gets the view to output the results.
    - handlePerformanceVisualization(Scanner scanner): reads in and sets the input values, calls
    the visualizePerformanceOverTime method and gets the view to output the results.
    - handlePersistPortfolio(Scanner scanner): reads in and sets the input values, calls the
    parsing method and gets the view to output the results.
    - getStockPercentages(Scanner scanner, List<Stock> stocks): reads in and sets the input values
    for getting percentages, throws errors if invalid percentages
    - handleTicker(Scanner scanner): reads in and sets the input values for getting ticker, throws
    errors if invalid
    - handleDate(Scanner scanner, String name, boolean isStart, boolean isNeither, boolean
    isPortfolio): reads in and sets the input values for getting date, throws errors if invalid
    - handleShares(Scanner scanner): reads in and sets the input values for getting shares,
    throws errors if invalid

6. Stock Market View: In our stock simulation, the stock market view controls what messages and
information get outputted to the text-based user interface.
    - writeMessage(String message): writes the given message to the output by appending it to an
    appendable.
    - welcomeMessage(): displays the welcome message and the menu.
    - printMenu(): displays a menu of the supported instructions.
    - farewellMessage(): displays the final message before the program quits.

7. Parsing XML data files: We can persist a portfolio so that it can be saved to and retrieved
from files.
    - toXML(Portfolio portfolio, String fileName): converts a Portfolio object to an XML file,
    given the portfolio and the file name
    - fromXML(String fileName): converts an XML file with the given name, if found, into a
    Portfolio object.

8. Utilities: In our program we have a few methods in a Utils class that serve as various checks
that are used to prevent error and throw exceptions in the other parts of the program.
    - minMaxDate(List<StockInfo> stock, boolean isMin): traverses through a list of historical
    daily stock price information and finds the minimum/maximum date depending on the isMin value.
    - inRange(String startDate, String endDate, int x, List<StockInfo> stockInfo): primarily used
    to calculate x-day crossover, takes in two dates, an x-value, and a list of stocks to
    determine if that from each date with x-day prior to that day is in range of all the date
    values given.
    - checkDateStartEnd(String startDate, String endDate): ensures that the start date is before
    the end date, throws exception if not.
    - checkDate(String date, String ticker): verifies that a given date exists in the stock data
    for a specified ticker.
    - validTicker(String ticker): validates if a ticker symbol is in the correct format
    (only capitalized letters, up to four letters long).
    - validYear(int year): checks if the given year is in a valid format (four digits).
    - validMonth(int month): ensures the month is between 1 and 12.
    - validDay(int day): ensures the day is between 1 and 31.

9. Data Collection: Our AlphaVantage class queries and stores all the stock price data. More
specifically, the class holds a public static field, stockPriceHistory, that maps a ticker value
to a list of stock information of that list.
    - getStock(String ticker): If stockPriceHistory already stores a copy of the stock information,
    then the method will return the list. Otherwise, addStock(String ticker) is called upon to get
    the ticker.
    - addStock(String ticker): attempts to find the file path to a locally stored csv file of
    popular stocks. If found, the stock information is added to stockPriceHistory and returned as
    a result. If the file is not found, then the getAPIStockData method is used to query the API.
    - getAPIStockData(String ticker): retrieves historical daily stock price data for a given
    symbol from the Alpha Vantage API and parses this data into a list of StockInfo objects.

10. User Interface: In our program, we have two ways to interact with the stock program. You can
either interact with a text-based interface, or a graphical interface.
    - main(String[] args: In the main method, assigning args[0] as “ -text” will launch the
    text-based interface. Otherwise, the GUI is set up and made visible by linking controller
    with the view
    - Specifically, the GUI functionality allows a user to do the following:
        - Create a new portfolio: specify name
        - Buy stocks: specify portfolio name, stock ticker, number of shares, date
        - Sell stocks: specify portfolio name, stock ticker, number of shares, date
        - Get portfolio value: specify portfolio name, specify date
        - Get portfolio composition: specify portfolio name, specify date
        - Save portfolios from files: specify portfolio name, file name
        - Load portfolios from files: specify file name


