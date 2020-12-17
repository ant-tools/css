package js.tools.css;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CssTestSuite extends TestCase
{
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(PropertyValueUnitTests.class);
    suite.addTestSuite(RulesetUnitTests.class);
    suite.addTestSuite(ParserUnitTests.class);
    return suite;
  }
}
