package js.tools.console.res;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.dom.w3c.DocumentBuilderImpl;
import js.tools.commons.util.Strings;
import js.tools.console.Utils;

@SuppressWarnings("unused")
public class ProjectMeta
{
  private static final String PROJECT_ROOT = "project";

  public static ProjectMeta load(InputStream stream) throws FileNotFoundException, IllegalArgumentException, IllegalAccessException
  {
    ProjectMeta projectMeta = new ProjectMeta();
    DocumentBuilder builder = new DocumentBuilderImpl();
    Document doc = builder.loadXML(stream);
    for(Element el : doc.getRoot().getChildren()) {
      String fieldName = Strings.toMemberName(el.getTag());
      Field field = Utils.getField(ProjectMeta.class, fieldName);
      if(field != null) field.set(projectMeta, el.getText());
    }
    return projectMeta;
  }

  public static void save(ProjectMeta projectMeta, OutputStream stream) throws IllegalArgumentException, IllegalAccessException, IOException
  {
    DocumentBuilder builder = new DocumentBuilderImpl();
    Document doc = builder.createXML(PROJECT_ROOT);
    for(Field field : ProjectMeta.class.getDeclaredFields()) {
      field.setAccessible(true);
      String tagName = Strings.toDashCase(field.getName());
      Element el = doc.createElement(tagName);
      el.setText((String)field.get(projectMeta));
      doc.getRoot().addChild(el);
    }
    doc.serialize(new OutputStreamWriter(stream, "UTF-8"));
  }

  private String projectName;
  private String displayName;
  private String runtimePath;
  private String warName;
  private String packageRoot;

  public void setProjectName(String projectName)
  {
    this.projectName = projectName;
    this.displayName = Utils.toTitleCase(projectName);
  }

  public void setPackageRoot(String packageRoot)
  {
    this.packageRoot = packageRoot;
  }

  public String getPackageRoot()
  {
    return this.packageRoot;
  }

  public void setWarName(String warName)
  {
    this.warName = warName;
  }

  public void setRuntimePath(String runtimePath)
  {
    this.runtimePath = runtimePath;
  }
}
