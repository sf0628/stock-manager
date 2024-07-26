package stockmarket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class provides methods to convert a Portfolio object to an XML file and vice versa.
 */
public class XMLParser {
  /**
   * Converts a Portfolio object to an XML file, given the portfolio and the file name.
   * @param portfolio a portfolio of stocks that is to be converted
   * @param fileName the name of the file being created
   * @return true if successful, false if unsuccessful
   */
  public static boolean toXML(Portfolio portfolio, String fileName) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();
      Element root = doc.createElement("portfolio");
      doc.appendChild(root);

      // Portfolio name
      Element nameElement = doc.createElement("name");
      nameElement.appendChild(doc.createTextNode(portfolio.getName()));
      root.appendChild(nameElement);

      // Latest date
      Element latestDateElement = doc.createElement("latestDate");
      latestDateElement.appendChild(doc.createTextNode(portfolio.getLatestDate()));
      root.appendChild(latestDateElement);

      // Stocks
      Element stocksElement = doc.createElement("stocks");
      root.appendChild(stocksElement);

      for (Stock stock : portfolio.getStocks()) {
        Element stockElement = doc.createElement("stock");

        Element ticker = doc.createElement("ticker");
        ticker.appendChild(doc.createTextNode(stock.getTicker()));
        stockElement.appendChild(ticker);

        Element shares = doc.createElement("shares");
        shares.appendChild(doc.createTextNode(String.valueOf(stock.getShares())));
        stockElement.appendChild(shares);

        Element dateAdded = doc.createElement("dateAdded");
        dateAdded.appendChild(doc.createTextNode(stock.getDateAdded()));
        stockElement.appendChild(dateAdded);

        stocksElement.appendChild(stockElement);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File("src/portfolio/" + fileName
              + ".xml"));
      transformer.transform(source, result);
      System.out.println("XML file created successfully!");
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Converts XML file into a new Portfolio object.
   * @param fileName a file under the data directory that ends with .xml,
   *                a valid file name should not contain ".xml" in its name,
   *                 only the letters before the '.' symbol.
   * @return a Portfolio object
   */
  public static Portfolio fromXML(String fileName) {
    String portfolioName = null;
    List<Stock> stocks = new ArrayList<>();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new File("src/portfolio/" + fileName + ".xml"));
      document.getDocumentElement().normalize();

      NodeList nameList = document.getElementsByTagName("name");
      if (nameList.getLength() > 0) {
        portfolioName = nameList.item(0).getTextContent();
      }

      NodeList stockNodes = document.getElementsByTagName("stock");
      for (int i = 0; i < stockNodes.getLength(); i++) {
        Node stockNode = stockNodes.item(i);
        if (stockNode.getNodeType() == Node.ELEMENT_NODE) {
          Element stockElement = (Element) stockNode;

          String ticker = stockElement.getElementsByTagName("ticker").item(0)
                  .getTextContent();
          double shares = Double.parseDouble(stockElement.getElementsByTagName("shares")
                  .item(0).getTextContent());
          String dateAdded = stockElement.getElementsByTagName("dateAdded").item(0)
                  .getTextContent();

          Stock stock = new Stock(ticker, shares, dateAdded);
          stocks.add(stock);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new Portfolio.PortfolioBuilder().name(portfolioName).addAllStock(stocks).build();
  }
}
