package stockmarket;

import java.io.InputStreamReader;

import gui.GUIController;
import gui.GUIFeatures;
import gui.GUIView;
import gui.JFrameView;
import textui.Controller;
import textui.StockMarketController;

/**
 * The StockMarketProgram class initializes and starts the stock market simulation application.
 */
public class StockMarketProgram {
  /**
   * Main method to initialize and run the stock market simulation application using a model
   * object, readable, appendable, and controller for inputs, outputs, and functionality with a
   * graphical user interface.
   */
  public static void main(String[] args) {
    Model model = new StockModel();

    // for text ui
    Readable readable = new InputStreamReader(System.in);
    Appendable appendable = System.out;
    Controller textController = new StockMarketController(model, readable, appendable);

    // for gui
    GUIFeatures controller = new GUIController(model);
    GUIView view = new JFrameView("Stock Market Simulation");

    if (args.length > 0 && args[0].equals("-text")) {
      textController.goController();
    } else {
      controller.setView(view);
      view.setVisible(true);
    }
  }
}

