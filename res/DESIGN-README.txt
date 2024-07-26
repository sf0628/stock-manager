Recent design changes:
    In gui package (new):
    - Added GUIView and GUIFeatures interfaces to support the new view and controller for the GUI
        - GUIView: represents the new view interface, contains methods to appropriately display,
        add, and set visible features
            - Decided to create new GUIView interface for gui due to Interface segregation
            principle, so no client must depend on methods it does not use
        - GUIFeatures: new controller interface, represents requests from the user, has methods
        that act as the “features” of the GUI (ex. creating portfolio, getting portfolio value,
        setting view, etc.)
            - Decided to create new specialized GUIFeatures controller for new gui view and model
            pairing because text and gui interfaces are different (they don’t exist in tandem and,
            therefore, don’t have to stay consistent)
            - GUIFeatures interface hides Swing specific event listeners, exposes
            application-specific events
    - Added StockMarketJFrame view and StockMarketGUIController classes for GUI functionality
        - View has different tabs for menu and each function of the program
        - Designed fields in view for better user functionality and to be less error-prone:
            - Portfolio names that have been created show as options when managing portfolios
            (using JComboBoxes)
            - Shares and dates have values to increment by (JSpinners)
            - File save/load takes in files (JFileChooser)
        - JFrameView implements GUIView and extends JFrame to create and display the GUI
    - Separated these classes and interfaces that support GUI functionality into 'gui' package

    In data package:
    - Added an interface for the AlphaVantage class to increase reusablity for different API's in
    the future

    In stockmarket package:
    - In StockMarketProgram, main method changed to work with JSwing and offer both text and
    graphical user interfaces through jar files (assigning args[0] as “ -text” launches text ui,
    otherwise, gui)
