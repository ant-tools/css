package js.tools.css;

import java.io.FileReader;
import java.util.List;

import js.tools.commons.util.Classes;
import js.tools.css.Builder;
import js.tools.css.CssException;
import js.tools.css.Parser;
import js.tools.css.Ruleset;
import js.tools.css.StyleSheet;
import junit.framework.TestCase;

public class ParserUnitTests extends TestCase
{
  private Parser parser;

  @Override
  protected void setUp() throws Exception
  {
    this.parser = Builder.getParser();
  }

  public void testNull() throws Exception
  {
    this.parser.parse((String)null);
  }

  public void testEmpty() throws Exception
  {
    this.parser.parse("   ");
    this.parser.parse(" \n  ");
    this.parser.parse(" \n \t ");
    this.parser.parse(" \n \t \r ");
  }

  public void testComments() throws Exception
  {
    this.parser.parse("  /* this should be ok */  ");
    this.parser.parse(" /** JavaDoc comment **/  ");
    this.parser.parse(" /** comment with \n new line **/  ");
    this.parser.parse(" /* double **/ /** comment */ ");
  }

  public void testBasicSingle() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("div { width: 100px; }");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(1, rules.size());

    Ruleset rule = (Ruleset)rules.get(0);
    assertEquals("div", rule.getSelectors().get(0).toString());
    assertEquals("width", rule.getPropertyValues().get(0).getProperty());
    assertEquals("100px", rule.getPropertyValues().get(0).getValue());
  }

  public void testCommentInsideRule() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("table /* and a comment here */ td { width: 100px; /* should be ignored */ text-decoration: underlined; }");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(1, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("table  td", rule.getSelectors().get(0).toString());
    assertEquals("width", rule.getPropertyValues().get(0).getProperty());
    assertEquals("100px", rule.getPropertyValues().get(0).getValue());
    assertEquals("text-decoration", rule.getPropertyValues().get(1).getProperty());
    assertEquals("underlined", rule.getPropertyValues().get(1).getValue());
  }

  public void testBase64() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("image { background-image:url(data:image/gif;base64,ABC123/ABC123=ABC123);}");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(1, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("image", rule.getSelectors().get(0).toString());
    assertEquals("background-image", rule.getPropertyValues().get(0).getProperty());
    assertEquals("url(data:image/gif;base64,ABC123/ABC123=ABC123)", rule.getPropertyValues().get(0).getValue());
  }

  public void testEmptPropertyValues() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("its-all-empty { /*empty*/ } empty { }");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(0, rules.size());
  }

  public void testDoubleComments() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("its-all-empty { /* /* double comment */ } empty { height: 200px; /* /* double comment */width: 100px; }");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(1, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("height", rule.getPropertyValues().get(0).getProperty());
    assertEquals("200px", rule.getPropertyValues().get(0).getValue());
    assertEquals("width", rule.getPropertyValues().get(1).getProperty());
    assertEquals("100px", rule.getPropertyValues().get(1).getValue());
  }

  public void testBasicMultipleValues() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("div { width: 100px; -mozilla-opacity: 345; } /* a comment */ beta{height:200px;display:blocked;}table td{}");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(2, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("div", rule.getSelectors().get(0).toString());
    assertEquals("width", rule.getPropertyValues().get(0).getProperty());
    assertEquals("100px", rule.getPropertyValues().get(0).getValue());
    //assertEquals("-mozilla-opacity", rule.getPropertyValues().get(1).getProperty());
    //assertEquals("345", rule.getPropertyValues().get(1).getValue());

    rule = rules.get(1);
    assertEquals("beta", rule.getSelectors().get(0).toString());
    assertEquals("height", rule.getPropertyValues().get(0).getProperty());
    assertEquals("200px", rule.getPropertyValues().get(0).getValue());
    assertEquals("display", rule.getPropertyValues().get(1).getProperty());
    assertEquals("blocked", rule.getPropertyValues().get(1).getValue());
  }

  public void testDuplicatePropertiesSameOrder() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("product-row { background: #ABC123; border: 1px black solid; border: none;background:   url(http://www.domain.com/image.jpg);}");
    List<Ruleset> rules = styleSheet.getRulesets();

    Ruleset rule = rules.get(0);
    assertEquals("product-row", rule.getSelectors().get(0).toString());
    assertEquals("background", rule.getPropertyValues().get(0).getProperty());
    assertEquals("#ABC123", rule.getPropertyValues().get(0).getValue());
    assertEquals("border", rule.getPropertyValues().get(1).getProperty());
    assertEquals("1px black solid", rule.getPropertyValues().get(1).getValue());
    assertEquals("border", rule.getPropertyValues().get(2).getProperty());
    assertEquals("none", rule.getPropertyValues().get(2).getValue());
    assertEquals("background", rule.getPropertyValues().get(3).getProperty());
    assertEquals("url(http://www.domain.com/image.jpg)", rule.getPropertyValues().get(3).getValue());
  }

  public void testMultipleSelectors() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse("alpha, beta { width: 100px; text-decoration: underlined; } gamma delta { } epsilon, /* some comment */ zeta { height: 34px; } ");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(2, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("alpha", rule.getSelectors().get(0).toString());
    assertEquals("beta", rule.getSelectors().get(1).toString());
    assertEquals("width", rule.getPropertyValues().get(0).getProperty());
    assertEquals("100px", rule.getPropertyValues().get(0).getValue());
    assertEquals("text-decoration", rule.getPropertyValues().get(1).getProperty());
    assertEquals("underlined", rule.getPropertyValues().get(1).getValue());

    rule = rules.get(1);
    assertEquals("epsilon", rule.getSelectors().get(0).toString());
    assertEquals("zeta", rule.getSelectors().get(1).toString());
    assertEquals("height", rule.getPropertyValues().get(0).getProperty());
    assertEquals("34px", rule.getPropertyValues().get(0).getValue());
  }

  public void testSelectorWithPseudoElement()
  {
    StyleSheet styleSheet = this.parser.parse("h3:after { content: ':'; }");
    List<Ruleset> rules = styleSheet.getRulesets();
    assertEquals(1, rules.size());

    Ruleset rule = rules.get(0);
    assertEquals("h3:after", rule.getSelectors().get(0).toString());
    assertEquals("content", rule.getPropertyValues().get(0).getProperty());
    assertEquals("':'", rule.getPropertyValues().get(0).getValue());
  }

  public void testFileString() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse(Classes.getResourceAsString("js/tools/css/test/style.css"));
    styleSheet.dump();
  }

  public void testFileReader() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse(Classes.getResourceAsReader("js/tools/css/test/style.css"));
    styleSheet.dump();
  }

  public void testKeyFramesFileReader() throws Exception
  {
    StyleSheet styleSheet = this.parser.parse(new FileReader("fixture/keyframes.css"));
    styleSheet.dump();
  }

  public void testCommaFirstAsSelector() throws Exception
  {
    try {
      this.parser.parse("alpha { width: 100px; } , beta { height: 200px; } ");
      fail();
    }
    catch(CssException unused) {}
  }

  public void testValueShouldEndWithSemiColon() throws Exception
  {
    try {
      this.parser.parse("alpha { width: 100px }");
      fail();
    }
    catch(CssException unused) {}
  }

  public void testMissingColon() throws Exception
  {
    try {
      this.parser.parse("alpha { color red; }");
      fail();
    }
    catch(CssException unused) {}
  }

  public void testMissingSemiColon() throws Exception
  {
    try {
      this.parser.parse("alpha { border: 1px solid green background-color:white; left: 0px; }");
    }
    catch(CssException unused) {
      fail();
    }
  }
}
