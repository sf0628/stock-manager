import org.junit.Before;
import org.junit.Test;

import stockmarket.Portfolio;
import stockmarket.XMLParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests that the XML Parser class properly converts to
 * and from Portfolio object to XML file.
 */
public class XMLParserTest {
  Portfolio portfolio;

  /**
   * Sets up portfolio data to be parsed and converted.
   */
  @Before
  public void setUp() {
    portfolio = new Portfolio("Testing");
    portfolio.updateStock("AMZN", 3, "2013-06-04", true);
    portfolio.updateStock("GOOG", 3, "2013-08-19", true);
  }

  /**
   * Tests that parsing portfolio can effectively convert to and from XML.
   */
  @Test
  public void testParsePortfolio() {
    XMLParser.toXML(portfolio, "Testing");
    Portfolio parsedPortfolio = XMLParser.fromXML("Testing");
    assertNotNull(parsedPortfolio);
    assertEquals(portfolio.getName(), parsedPortfolio.getName());
    assertEquals(portfolio.getStocks().size(), parsedPortfolio.getStocks().size());
    for (int i = 0; i < portfolio.getStocks().size(); i++) {
      assertEquals(portfolio.getStocks().get(i), parsedPortfolio.getStocks().get(i));
    }
  }

  /**
   * Test that parsing empty portfolio can effectively convert to and from XML.
   */
  @Test
  public void testEmptyPortfolio() {
    Portfolio emptyPortfolio = new Portfolio("Empty");
    XMLParser.toXML(emptyPortfolio, "Empty");
    Portfolio parsedPortfolio = XMLParser.fromXML("Empty");
    assertNotNull(parsedPortfolio);
    assertEquals(emptyPortfolio.getName(), parsedPortfolio.getName());
    assertEquals(0, parsedPortfolio.getStocks().size());
    assertEquals(emptyPortfolio.getStocks().size(), parsedPortfolio.getStocks().size());
  }
}
