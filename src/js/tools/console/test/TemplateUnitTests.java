package js.tools.console.test;

import java.io.OutputStreamWriter;
import java.io.Writer;

import js.tools.console.Template;
import js.tools.console.res.ClassMeta;
import junit.framework.TestCase;

public class TemplateUnitTests extends TestCase
{
  public void testSerialize() throws Exception
  {
    Writer writer = new OutputStreamWriter(System.out, "UTF-8");

    ClassMeta model = new ClassMeta();
    model.setAuthorName("Iulian Rotaru");
    model.setVersion("1.0");
    model.setClassName("comp.prj.Class");

    Template template = new Template(getClass().getResourceAsStream("JsClass.tmpl"));
    template.setModel(model);
    template.serialize(writer);
  }
}
