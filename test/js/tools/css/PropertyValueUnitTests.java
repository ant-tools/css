package js.tools.css;

import js.tools.commons.util.Classes;
import js.tools.css.PropertyValue;
import junit.framework.TestCase;

public class PropertyValueUnitTests extends TestCase
{
  public void testGetters() throws Exception
  {
    PropertyValue propertyValue = Classes.newInstance("js.tools.css.impl.PropertyValueImpl", "width", "100px");
    assertEquals(propertyValue.getProperty(), "width");
    assertEquals(propertyValue.getValue(), "100px");
  }

  public void testEquals() throws Exception
  {
    PropertyValue selector1a = Classes.newInstance("js.tools.css.impl.PropertyValueImpl", "width", "100%");
    PropertyValue selector1b = Classes.newInstance("js.tools.css.impl.PropertyValueImpl", "width", "100%");
    PropertyValue selector2 = Classes.newInstance("js.tools.css.impl.PropertyValueImpl", "width", "200%");
    PropertyValue selectorNull = null;
    assertTrue(selector1a.equals(selector1b));
    assertFalse(selector1a.equals(selector2));
    assertFalse(selector1a.equals(selectorNull));
  }
}
