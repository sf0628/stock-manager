package gui;

/**
 * The GUIView interface represents all the elements of the graphical user interface (GUI) of our
 * stocks program. It includes methods to display messages, portfolio values, and portfolio
 * compositions, as well as to add features and control the visibility of the GUI.
 */
public interface GUIView {
  /**
   * Displays a message to the user in a pop-up manner.
   * @param message the message to be displayed
   */
  void showMessage(String message);

  /**
   * Displays the value of the portfolio.
   * @param value the value of the portfolio to be displayed
   */
  void displayPortfolioValue(String value);

  /**
   * Displays the composition of the portfolio.
   * @param composition the composition of the portfolio to be displayed
   */
  void displayPortfolioComposition(String composition);

  /**
   * Adds features to the GUI. This method allows the GUI to respond to user interactions
   * by linking the GUI with the controller's features through action listeners.
   * @param features the features to be added to the GUI
   */
  void addFeatures(GUIFeatures features);

  /**
   * Sets the visibility of the GUI.
   * @param visible true to make the GUI visible, false to hide it
   */
  void setVisible(boolean visible);
}
