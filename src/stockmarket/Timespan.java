package stockmarket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import data.AlphaVantage;

/**
 * This class Timespan represents the incrementation of time in between each
 * line of portfolio/stock visualization. This class contains boolean values for year,
 * month, and day that represents how distanced the visualization is incrementing
 * (incrementing by day, month, or year). The 'count' field represents that amount of
 * whichever scale is being incremented at.
 */
public class Timespan {
  private final boolean isYear;
  private final boolean isMonth;
  private final boolean isDay;
  private final int count;

  /**
   * This manually constructs a Timespan object with all the fields. This constructor will
   * throw exception if count is less than one, or if the number of boolean values true does
   * not equal one.
   * @param isYear determines if it's incrementing by year
   * @param isMonth determines if it's incrementing by month
   * @param isDay determines if it's incrementing by day
   * @param count the number of times to be incremented
   */
  public Timespan(boolean isYear, boolean isMonth, boolean isDay, int count) {
    //throws exceptions for improper parameters
    checkTimespan(isYear, isMonth, isDay);
    if (count < 0) {
      throw new IllegalArgumentException("Invalid visualization data.");
    }
    this.isYear = isYear;
    this.isMonth = isMonth;
    this.isDay = isDay;
    this.count = count;
  }

  /**
   * Gets the isYear boolean.
   * @return if timespan is in years
   */
  public boolean isYear() {
    return isYear;
  }

  /**
   * Gets the isMonth boolean.
   * @return if timespan is in months
   */
  public boolean isMonth() {
    return isMonth;
  }

  /**
   * Gets the isDay boolean.
   * @return if timespan is in months
   */
  public boolean isDay() {
    return isDay;
  }

  /**
   * Given a start and end date, and the ticker value of a stock, this method produces a list of
   * valid dates at the proper incrementation level. If timespan is in month or year, the date
   * produced is in the last day of the month or year. If last date is invalid, the date would be
   * incremented forward until a valid date is found. if timespan is in days, invalid days will
   * result in finding the next future valid date.
   * @param startDate The date that the visualization begins on
   * @param endDate the date that the visualization ends on
   * @param ticker The ticker value of a given stock
   * @return a list of valid LocalDates at the proper incrementation
   */
  public List<LocalDate> getDateIncrements(String startDate, String endDate, String ticker) {
    List<LocalDate> dateIncrements = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate current = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);

    while (!current.isEqual(end)) {
      dateIncrements.add(current);
      //gets the next incremented date, if not valid, then finds the
      current = LocalDate.parse(AlphaVantage.getStockInfo(ticker,
              incrementedDate(current, end), this).getTimestamp(), formatter);

    }
    dateIncrements.add(end);

    return dateIncrements;
  }

  /**
   * Converts a list of LocalDates into the proper string formatting. String formatting
   * depends on chart's resolution: year, month, or day.
   * @param dateIncrements a list of LocalDates
   * @return a list of converted strings
   */
  public List<String> convertIncrements(List<LocalDate> dateIncrements) {
    List<String> dateStrings = new ArrayList<>();
    Function<LocalDate, String> converter = null;
    if (isYear) {
      converter = x -> String.valueOf(x.getYear());
    } else if (isMonth) {
      converter = x -> x.getMonth().toString().charAt(0)
              + x.getMonth().toString().substring(1, 3).toLowerCase()
              + " " + x.getYear();
    } else if (isDay) {
      converter = x -> x.getMonth().toString().charAt(0)
              + x.getMonth().toString().substring(1, 3).toLowerCase()
              + " " + x.getDayOfMonth() + ", " + x.getYear();
    }

    if (converter == null) {
      throw new IllegalArgumentException("Invalid timespan");
    }

    for (LocalDate date : dateIncrements) {
      dateStrings.add(converter.apply(date));
    }
    return dateStrings;
  }

  //gets the next date increment, if month: last day of month, if year: last day of year
  private LocalDate incrementedDate(LocalDate currentDate, LocalDate endDate) {
    LocalDate newDate = null;
    //increments the date
    if (isYear) {
      newDate = currentDate.plusYears(count);
      if (!newDate.isEqual(newDate.with(TemporalAdjusters.lastDayOfYear()))) {
        newDate = newDate.with(TemporalAdjusters.lastDayOfYear());
      }
    } else if (isMonth) {
      newDate = currentDate.plusMonths(count);
      if (!newDate.isEqual(newDate.with(TemporalAdjusters.lastDayOfMonth()))) {
        newDate = newDate.with(TemporalAdjusters.lastDayOfMonth());
      }
    } else if (isDay) {
      newDate = currentDate.plusDays(count);
    }

    //throws exceptions if invalid timespan
    if (newDate == null) {
      throw new IllegalArgumentException("Invalid date format");
    }

    //ensures newDate doesn't pass final date
    if (newDate.isAfter(endDate) ) {
      newDate = endDate;
    }
    return newDate;
  }

  //check if timespan arguments are valid
  private void checkTimespan(boolean isYear, boolean isMonth, boolean isDay) {
    int count = 0;
    if (isDay) {
      count += 1;
    }
    if (isMonth) {
      count += 1;
    }
    if (isYear) {
      count += 1;
    }
    if (count != 1) {
      throw new IllegalArgumentException("Invalid visualization data.");
    }
  }
}

