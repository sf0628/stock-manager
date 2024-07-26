package stockmarket;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a stock that someone can own in a portfolio with a ticker symbol,
 * the number of shares, and the date it was added.
 */
@XmlRootElement(name = "Stock")
public class Stock {
  private final String ticker;
  private final double shares;
  private final String dateAdded;

  /**
   * Default constructor is required for JAXB.
   */
  public Stock() {
    this.ticker = "";
    this.shares = 0;
    this.dateAdded = "";
  }

  /**
   * Constructs a Stock with the given ticker symbol, number of shares, and date added.
   *
   * @param ticker the stock ticker symbol
   * @param shares the number of shares of the stock
   * @param dateAdded the date the stock was added
   *
   * @throws IllegalArgumentException if ticker is invalid, shares are negative, or date is invalid
   */
  public Stock(String ticker, double shares, String dateAdded) throws IllegalArgumentException {
    Utils.validTicker(ticker);
    if (shares < 0) {
      throw new IllegalArgumentException("Shares cannot be negative");
    }
    Utils.checkDate(dateAdded, ticker);

    this.ticker = ticker;
    this.shares = shares;
    this.dateAdded = dateAdded;
  }

  /**
   * Gets the stock ticker symbol.
   *
   * @return the stock ticker symbol
   */
  @XmlElement
  public String getTicker() {
    return ticker;
  }

  /**
   * Gets the number of shares of the stock.
   *
   * @return the number of shares
   */
  @XmlElement
  public double getShares() {
    return shares;
  }

  /**
   * Gets the date the stock was added.
   *
   * @return the date the stock was added
   */
  @XmlElement
  public String getDateAdded() {
    return dateAdded;
  }

  /**
   * Override the equals method when comparing two Stocks. Compares stock based
   * on if ticker and string equals.
   * @param o any object
   * @return whether a given object is equal to this stock
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Stock)) {
      return false;
    }
    Stock stock = (Stock) o;
    return Double.compare(stock.shares, shares) == 0 &&
            ticker.equals(stock.ticker) &&
            dateAdded.equals(stock.dateAdded);
  }

  /**
   * Overrides the hashcode methods such that when the stocks are considered
   * equal, their hash code is equal too.
   * @return an integer of the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(ticker, shares, dateAdded);
  }
}
