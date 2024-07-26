package textui;

/**
 * Represents the view in the stock market simulation application.
 * A text based interface that displays outputs for user interaction.
 */
public interface View {
  /**
   * Writes a given message to the output.
   *
   * @param message the message to write
   */
  void writeMessage(String message);

  /**
   * Displays the welcome message and the menu.
   */
  void welcomeMessage();

  /**
   * Displays the menu of supported instructions.
   */
  void printMenu();

  /**
   * Displays the farewell message to the user.
   */
  void farewellMessage();
}
