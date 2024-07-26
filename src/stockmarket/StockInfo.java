package stockmarket;

/**
 * Represents stock information at a timestamp.
 */
public class StockInfo {
  private final String timestamp; //yyyy-MM-dd format
  private final double open;
  private final double high;
  private final double low;
  private final double close;
  private final Long volume;

  /**
   * Constructs a StockInfo object with the given timestamp, open, high,
   * low, close prices, and volume information.
   *
   * @param timestamp the timestamp of the stock information in yyyy-MM-dd format
   * @param open the opening price of the stock
   * @param high the highest price of the stock
   * @param low the lowest price of the stock
   * @param close the closing price of the stock
   * @param volume the trading volume of the stock
   */
  public StockInfo(String timestamp, double open, double high,
                    double low, double close, Long volume) {
    this.timestamp = timestamp;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  //getters and setters
  /**
   * Gets the timestamp of the stock information.
   *
   * @return the timestamp in yyyy-MM-dd format
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Gets the opening price of the stock.
   *
   * @return the opening price
   */
  public double getOpen() {
    return open;
  }

  /**
   * Gets the highest price of the stock.
   *
   * @return the highest price
   */
  public double getHigh() {
    return high;
  }

  /**
   * Gets the lowest price of the stock.
   *
   * @return the lowest price
   */
  public double getLow() {
    return low;
  }

  /**
   * Gets the closing price of the stock.
   *
   * @return the closing price
   */
  public double getClose() {
    return close;
  }

  /**
   * Gets the trading volume of the stock.
   *
   * @return the trading volume
   */
  public Long getVolume() {
    return volume;
  }

  /**
   * Overrides the toString method that would return the fields in
   * listed formatting.
   * @return string of the fields
   */
  @Override
  public String toString() {
    return timestamp + "," + open + "," + high + "," + low + "," + close
            + volume;
  }
}
