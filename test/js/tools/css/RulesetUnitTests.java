package js.tools.css;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js.tools.commons.util.Classes;
import js.tools.css.Ruleset;
import junit.framework.TestCase;

public class RulesetUnitTests extends TestCase
{
  public void testBasic() throws Exception
  {
    String selector = "div";
    Ruleset ruleset = Classes.newInstance("js.tools.css.impl.RulesetImpl", selector);
    assertEquals(selector, ruleset.getSelectors().get(0));
  }

  public void testFontFacePattern() throws ClassNotFoundException, Exception
  {
    String fontFaceRule = "font-face {\r\n" + //
        "\tfont-family: 'Note this';\r\n" + //
        "\tsrc: url('http://sixqs.com/Note_this.ttf');\r\n" + //
        "}";

    Pattern pattern = Classes.getFieldValue(Class.forName("js.tools.css.impl.FontFaceRuleImpl"), "PATTERN");
    Matcher matcher = pattern.matcher(fontFaceRule);
    assertTrue(matcher.find());
    assertEquals("Note this", matcher.group(1));
    assertEquals("http://sixqs.com/Note_this.ttf", matcher.group(2));
  }
}
