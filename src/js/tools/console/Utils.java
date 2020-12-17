package js.tools.console;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

public final class Utils
{
  private static final String METADATA_FILE = ".metadata";
  private static final String PROJECT_FILE = ".project";

  /**
   * Test if given directory is an workspace root. A directory is a workspace root if it has metadata directory.
   * 
   * @param directory directory to test.
   * @return true if given directory is an workspace.
   */
  public static boolean isWorkspace(File directory)
  {
    File workspaceMeta = new File(directory, METADATA_FILE);
    return workspaceMeta.exists() && workspaceMeta.isDirectory();
  }

  /**
   * Test if given directory is a project root. A directory is a project root if it has project descriptor file.
   * 
   * @param directory directory to test.
   * @return true if given directory is a project.
   */
  public static boolean isProject(File directory)
  {
    File projectDescriptor = new File(directory, PROJECT_FILE);
    return projectDescriptor.exists();
  }

  public static String getProjectName(File directory)
  {
    assert isProject(directory);
    return directory.getName();
  }

  public static InputStream getTemplateStream(String templateResource)
  {
    return Utils.class.getResourceAsStream(templateResource);
  }

  public static String toTitleCase(String cssCase)
  {
    String[] parts = cssCase.toLowerCase().split("-");
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < parts.length; i++) {
      if(i > 0) sb.append(' ');
      sb.append(Character.toUpperCase(parts[i].charAt(0)) + parts[i].substring(1));
    }
    return sb.toString();
  }

  public static Field getField(Class<?> type, String fieldName)
  {
    try {
      Field field = type.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    }
    catch(NoSuchFieldException e) {
      return null;
    }
    catch(SecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
