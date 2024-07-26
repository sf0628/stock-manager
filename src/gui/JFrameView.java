package gui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import stockmarket.Model;

/**
 * The JFrameView class extends JFrame and implements the GUIView interface. This class is
 * responsible for creating and displaying the graphical user interface of our stock program.
 * It includes various panels and components to manage portfolios. This includes creating
 * portfolio, buying/selling stocks, getting portfolio values and compositions, and saving or
 * loading portfolios.
 */
public class JFrameView extends JFrame implements GUIView {
  private Model viewModel;
  private JTabbedPane tabbedPane;

  //portfolio fields/combo box for each method
  private JTextField portfolioNameFieldCreate;
  private JComboBox<String> portfolioNameFieldBuy;
  private JComboBox<String> portfolioNameFieldSell;
  private JComboBox<String> portfolioNameFieldValue;
  private JComboBox<String> portfolioNameFieldComposition;
  private JComboBox<String> portfolioNameFieldSave;

  //list to keep track of portfolio names
  private List<String> portfolioNames = new ArrayList<>();

  //stock fields for each method
  private JTextField stockTickerFieldBuy;
  private JTextField stockTickerFieldSell;

  //shares fields for each method
  private JTextField sharesFieldBuy;
  private JTextField sharesFieldSell;

  //date spinner for each method
  private JSpinner dateSpinnerBuy;
  private JSpinner dateSpinnerSell;
  private JSpinner dateSpinnerValue;
  private JSpinner dateSpinnerComposition;

  private JButton createPortfolioButton;
  private JButton buyStockButton;
  private JButton sellStockButton;
  private JButton getPortfolioValueButton;
  private JButton getPortfolioCompositionButton;
  private JButton savePortfolioButton;
  private JButton loadPortfolioButton;
  private JButton navigateButton;

  //file chooser for save portfolio
  private JButton selectFileSaveButton;
  private JFileChooser fileSaveChooser;
  private String selectedFileSavePath;
  private JLabel selectedFileSaveLabel;
  //file chooser for load portfolio
  private JButton selectFileLoadButton;
  private JFileChooser fileLoadChooser;
  private String selectedFileLoadPath;
  private JLabel selectedFileLoadLabel;

  /**
   * Constructs a JFrameView with the specified title.
   * @param title the title of the JFrame.
   */
  public JFrameView(String title) {
    createAndShowGUI(title);
  }

  private void createAndShowGUI(String title) {
    setTitle(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);

    tabbedPane = new JTabbedPane();

    tabbedPane.add("Menu", loadMenuPanel());
    tabbedPane.add("Create Portfolio", createPortfolioPanel());
    tabbedPane.add("Buy Stock", buyStockPanel());
    tabbedPane.add("Sell Stock", sellStockPanel());
    tabbedPane.add("Get Portfolio Value", getPortfolioValuePanel());
    tabbedPane.add("Get Portfolio Composition", getPortfolioCompositionPanel());
    tabbedPane.add("Save Portfolio", savePortfolioPanel());
    tabbedPane.add("Load Portfolio", loadPortfolioPanel());

    tabbedPane.setSelectedIndex(0);

    JScrollPane scrollPane = new JScrollPane(tabbedPane);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    getContentPane().add(scrollPane);

    add(tabbedPane);

    fileSaveChooser = new JFileChooser();
    fileSaveChooser.setFileFilter(new FileNameExtensionFilter("XML files",
            "xml"));
    fileLoadChooser = new JFileChooser();
    fileLoadChooser.setFileFilter(new FileNameExtensionFilter("XML files",
            "xml"));

    pack();
    setVisible(true);
  }

  private JPanel createPortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); //padding
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel nameLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldCreate = new JTextField(20);

    createPortfolioButton = new JButton("Create Portfolio");
    createPortfolioButton.setActionCommand("Create Portfolio");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(nameLabel, gbc);

    gbc.gridx = 1;
    panel.add(portfolioNameFieldCreate, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    panel.add(createPortfolioButton, gbc);

    return panel;
  }

  private JPanel buyStockPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldBuy = new JComboBox<>();
    JLabel tickerLabel = new JLabel("Stock Ticker:");
    stockTickerFieldBuy = new JTextField(20);
    JLabel sharesLabel = new JLabel("Number of Shares:");

    JSpinner sharesSpinner = new JSpinner(new SpinnerNumberModel(1, 1,
            Integer.MAX_VALUE, 1));
    sharesFieldBuy = ((JSpinner.DefaultEditor) sharesSpinner.getEditor()).getTextField();

    JLabel dateLabel = new JLabel("Date:");
    dateSpinnerBuy = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerBuy,
            "yyyy-MM-dd");
    dateSpinnerBuy.setEditor(dateEditor);
    buyStockButton = new JButton("Buy Stocks");
    buyStockButton.setActionCommand("Buy Stocks");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(portfolioLabel, gbc);

    gbc.gridx = 1;
    panel.add(portfolioNameFieldBuy, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(tickerLabel, gbc);

    gbc.gridx = 1;
    panel.add(stockTickerFieldBuy, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(sharesLabel, gbc);

    gbc.gridx = 1;
    panel.add(sharesSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(dateLabel, gbc);

    gbc.gridx = 1;
    panel.add(dateSpinnerBuy, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    panel.add(buyStockButton, gbc);

    panel.revalidate();
    panel.repaint();

    return panel;
  }


  private JPanel sellStockPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldSell = new JComboBox<>();
    JLabel tickerLabel = new JLabel("Stock Ticker:");
    stockTickerFieldSell = new JTextField(20);
    JLabel sharesLabel = new JLabel("Number of Shares:");

    JSpinner sharesSpinner = new JSpinner(new SpinnerNumberModel(1, 1,
            Integer.MAX_VALUE, .001));
    sharesFieldSell = ((JSpinner.DefaultEditor) sharesSpinner.getEditor()).getTextField();

    JLabel dateLabel = new JLabel("Date:");
    dateSpinnerSell = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerSell,
            "yyyy-MM-dd");
    dateSpinnerSell.setEditor(dateEditor);
    sellStockButton = new JButton("Sell Stocks");
    sellStockButton.setActionCommand("Sell Stocks");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(portfolioLabel, gbc);

    gbc.gridx = 1;
    panel.add(portfolioNameFieldSell, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(tickerLabel, gbc);

    gbc.gridx = 1;
    panel.add(stockTickerFieldSell, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(sharesLabel, gbc);

    gbc.gridx = 1;
    panel.add(sharesSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(dateLabel, gbc);

    gbc.gridx = 1;
    panel.add(dateSpinnerSell, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    panel.add(sellStockButton, gbc);

    panel.revalidate();
    panel.repaint();

    return panel;
  }

  private JPanel getPortfolioValuePanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldValue = new JComboBox<>();
    JLabel dateLabel = new JLabel("Date:");
    dateSpinnerValue = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerValue,
            "yyyy-MM-dd");
    dateSpinnerValue.setEditor(dateEditor);
    getPortfolioValueButton = new JButton("Get Value");
    getPortfolioValueButton.setActionCommand("Get Value");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(portfolioLabel, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(portfolioNameFieldValue, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(dateLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(dateLabel, gbc);

    gbc.gridx = 1;
    panel.add(dateSpinnerValue, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    panel.add(getPortfolioValueButton, gbc);

    return panel;
  }

  private JPanel getPortfolioCompositionPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldComposition = new JComboBox<>();
    JLabel dateLabel = new JLabel("Date:");
    dateSpinnerComposition = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerComposition,
            "yyyy-MM-dd");
    dateSpinnerComposition.setEditor(dateEditor);
    getPortfolioCompositionButton = new JButton("Get Composition");
    getPortfolioCompositionButton.setActionCommand("Get Composition");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(portfolioLabel, gbc);

    gbc.gridx = 1;
    panel.add(portfolioNameFieldComposition, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(dateLabel, gbc);

    gbc.gridx = 1;
    panel.add(dateSpinnerComposition, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    panel.add(getPortfolioCompositionButton, gbc);

    return panel;
  }

  private JPanel savePortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    portfolioNameFieldSave = new JComboBox<>();
    JLabel fileLabel = new JLabel("Select File:");
    selectedFileSaveLabel = new JLabel("No file selected");
    selectFileSaveButton = new JButton("Choose File");
    selectFileSaveButton.setActionCommand("Choose File");
    savePortfolioButton = new JButton("Save Portfolio");
    savePortfolioButton.setActionCommand("Save Portfolio");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(portfolioLabel, gbc);

    gbc.gridx = 1;
    panel.add(portfolioNameFieldSave, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(fileLabel, gbc);

    gbc.gridx = 1;
    panel.add(selectFileSaveButton, gbc);

    gbc.gridx = 2;
    panel.add(selectedFileSaveLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    panel.add(savePortfolioButton, gbc);

    return panel;
  }

  private JPanel loadPortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel fileLabel = new JLabel("Select File:");
    selectFileLoadButton = new JButton("Choose File");
    selectFileLoadButton.setActionCommand("Choose File");
    selectedFileLoadLabel = new JLabel("No file selected");
    loadPortfolioButton = new JButton("Load Portfolio");
    loadPortfolioButton.setActionCommand("Load Portfolio");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(fileLabel, gbc);

    gbc.gridx = 1;
    panel.add(selectFileLoadButton, gbc);

    gbc.gridx = 2;
    panel.add(selectedFileLoadLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    panel.add(loadPortfolioButton, gbc);

    return panel;
  }

  private JPanel loadMenuPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    JTextArea menu = new JTextArea();
    menu.setEditable(false);
    menu.setLineWrap(true);
    menu.setWrapStyleWord(true);
    menu.setText("Welcome to Our Stock Market Simulation: \n \n"
            + "This program allows you to create portfolios, \n"
            + "buy/sell stocks, view a portfolio's value/composition, \n"
            + "and save/load portfolios from/to files. \n \n"
            + "Please navigate to other pages to explore, or get started "
            + "by creating a portfolio!");
    JScrollPane scrollPane = new JScrollPane(menu);
    navigateButton = new JButton("Go to Create Portfolio");
    navigateButton.setActionCommand("Navigate");

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(scrollPane, gbc);

    gbc.gridy = 1;
    panel.add(navigateButton, gbc);

    return panel;
  }

  private void updatePortfolioNameFields(String portfolioName, boolean isRemoval) {
    if (isRemoval) {
      portfolioNameFieldBuy.removeItem(portfolioName);
      portfolioNameFieldSell.removeItem(portfolioName);
      portfolioNameFieldValue.removeItem(portfolioName);
      portfolioNameFieldComposition.removeItem(portfolioName);
      portfolioNameFieldSave.removeItem(portfolioName);
    } else {
      portfolioNameFieldBuy.addItem(portfolioName);
      portfolioNameFieldSell.addItem(portfolioName);
      portfolioNameFieldValue.addItem(portfolioName);
      portfolioNameFieldComposition.addItem(portfolioName);
      portfolioNameFieldSave.addItem(portfolioName);
    }
  }

  @Override
  public void addFeatures(GUIFeatures f) {
    createPortfolioButton.addActionListener(evt -> {
      String portfolioName = portfolioNameFieldCreate.getText().trim();
      f.createPortfolio(portfolioName);
      updatePortfolioNameFields(portfolioName, false);
    });
    buyStockButton.addActionListener(evt -> f.buyStock((String) portfolioNameFieldBuy
                    .getSelectedItem(), stockTickerFieldBuy.getText().toUpperCase(),
            Double.parseDouble(sharesFieldBuy.getText()), dateFormatter(dateSpinnerBuy)));
    sellStockButton.addActionListener(evt -> f.sellStock((String) portfolioNameFieldSell
                    .getSelectedItem(), stockTickerFieldSell.getText().toUpperCase(),
            Double.parseDouble(sharesFieldSell.getText()), dateFormatter(dateSpinnerSell)));
    getPortfolioValueButton.addActionListener(evt -> f.getPortfolioValue(
            (String) portfolioNameFieldValue.getSelectedItem(), dateFormatter(dateSpinnerValue)));
    getPortfolioCompositionButton.addActionListener(evt -> f.getPortfolioComposition(
            (String) portfolioNameFieldComposition.getSelectedItem(),
            dateFormatter(dateSpinnerComposition)));
    selectFileSaveButton.addActionListener(evt -> {
      int returnValue = fileSaveChooser.showOpenDialog(this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        selectedFileSavePath = fileSaveChooser.getSelectedFile().getAbsolutePath();
        selectedFileSaveLabel.setText(fileSaveChooser.getSelectedFile().getName());
      }
    });
    savePortfolioButton.addActionListener(evt -> {
      if (selectedFileSavePath != null) {
        f.savePortfolio((String) portfolioNameFieldSave.getSelectedItem(), selectedFileSavePath);
      } else {
        showMessage("Please select a file first.");
      }
    });
    selectFileLoadButton.addActionListener(evt -> {
      int returnValue = fileLoadChooser.showOpenDialog(this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        selectedFileLoadPath = fileLoadChooser.getSelectedFile().getAbsolutePath();
        selectedFileLoadLabel.setText(fileLoadChooser.getSelectedFile().getName());
      }
    });
    loadPortfolioButton.addActionListener(evt -> {
      if (selectedFileLoadPath != null) {
        f.loadPortfolio(selectedFileLoadPath);
      } else {
        showMessage("Please select a file first.");
      }
    });
    navigateButton.addActionListener(evt -> tabbedPane.setSelectedIndex(1));
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void displayPortfolioValue(String value) {
    JTextArea valueArea = new JTextArea(value);
    valueArea.setEditable(false);
    JOptionPane.showMessageDialog(this, new JScrollPane(valueArea),
            "Portfolio Value", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void displayPortfolioComposition(String composition) {
    JTextArea compositionArea = new JTextArea(composition);
    compositionArea.setEditable(false);
    JOptionPane.showMessageDialog(this, new JScrollPane(compositionArea),
            "Portfolio Composition", JOptionPane.INFORMATION_MESSAGE);
  }

  //converts a JSpinner to a yyyy-MM-dd string formatting
  private String dateFormatter(JSpinner dateSpinner) {
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner,
            "yyyy-MM-dd");
    dateSpinner.setEditor(dateEditor);
    return dateEditor.getFormat().format(dateSpinner.getValue());
  }

}

