package js.tools.console.res;

@SuppressWarnings("unused")
public class ClassMeta
{
  private String templateName;
  private String language;

  private String authorName;
  private String version;
  private String packageRoot;
  private String qualifiedClassName;
  private String packageName;
  private String className;
  private String superClassName;
  private String classUnderTestName;
  private String classUnderTestQualifiedName;

  public void setTemplateName(String templateName)
  {
    this.templateName = templateName;
  }

  public String getTemplateName()
  {
    return this.templateName;
  }

  public void setLanguage(String sourceCodeBase)
  {
    this.language = sourceCodeBase;
  }

  public String getLanguage()
  {
    return this.language;
  }

  public void setAuthorName(String authorName)
  {
    this.authorName = authorName;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public void setPackageRoot(String packageRoot)
  {
    this.packageRoot = packageRoot;
  }

  public void setClassName(String qualifiedClassName)
  {
    this.qualifiedClassName = qualifiedClassName;
    int lastDotIndex = qualifiedClassName.lastIndexOf('.');
    this.packageName = qualifiedClassName.substring(0, lastDotIndex);
    this.className = qualifiedClassName.substring(lastDotIndex + 1);
  }

  public String getClassName()
  {
    return this.className;
  }

  public String getPackageName()
  {
    return this.packageName;
  }

  public void setSuperClassName(String qualifiedClassName)
  {
    int lastDotIndex = qualifiedClassName.lastIndexOf('.');
    this.superClassName = qualifiedClassName.substring(lastDotIndex + 1);
  }

  public void setClassUnderTest(String qualifiedClassName)
  {
    this.classUnderTestQualifiedName = qualifiedClassName;
    int lastDotIndex = qualifiedClassName.lastIndexOf('.');
    this.classUnderTestName = qualifiedClassName.substring(lastDotIndex + 1);
  }
}
