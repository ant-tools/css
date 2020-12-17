package js.tools.css;

import js.tools.commons.util.Classes;
import js.tools.css.Builder;
import js.tools.css.ImportRule;
import js.tools.css.Parser;
import js.tools.css.ParserListener;
import js.tools.css.PropertyValue;
import js.tools.css.StyleSheet;
import junit.framework.TestCase;

public class ParserListenerUnitTests extends TestCase
{
  public void testOnRule() throws Exception
  {
    Parser parser = Builder.getParser();
    parser.setListener(new ParserListener()
    {
      @Override
      public ImportRule onImportRule(ImportRule importRule)
      {
        System.out.println(importRule.getHref());
        return importRule;
      }

      @Override
      public PropertyValue onPropertyValue(PropertyValue propertyValue)
      {
        if(propertyValue.getProperty().equals("background")) {
          propertyValue.setValue("XXXXXXXXXXXXXXXXXXXXXXXX");
        }
        return propertyValue;
      }
    });

    StyleSheet styleSheet = parser.parse(Classes.getResourceAsReader("js/test/dom/css/style.css"));
    styleSheet.dump();
  }
}
