package js.tools.console;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import js.tools.commons.util.Strings;
import js.tools.console.res.ClassMeta;

public class NewClassCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isProject(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not a project root: " + this.currentDirectory);
    }

    String qualifiedClassFileName = args[0];
    int extensionIndex = qualifiedClassFileName.lastIndexOf('.');
    String qualifiedClassName = qualifiedClassFileName.substring(0, extensionIndex);
    String extension = qualifiedClassFileName.substring(extensionIndex + 1);
    int classNameIndex = qualifiedClassName.lastIndexOf('.', extensionIndex - 1);
    String classFileName = qualifiedClassName.substring(classNameIndex + 1);
    String packagePath = qualifiedClassName.substring(0, classNameIndex).replace('.', System.getProperty("file.separator").charAt(0));

    String templateName = null;
    File source = null;

    ClassMeta meta = new ClassMeta();
    meta.setAuthorName("Iulian Rotaru");
    meta.setVersion("1.0");
    meta.setClassName(qualifiedClassName);

    if(extension.equals("java")) {
      templateName = "res/java-class.tmpl";
      source = new File(this.currentDirectory, "java");
    }

    else if(extension.equals("js")) {
      templateName = "res/js-class.tmpl";
      source = new File(this.currentDirectory, "js");
    }

    else {
      throw new IllegalArgumentException("Invalid class file type: " + extension);
    }

    source = new File(source, packagePath);
    if(!source.exists()) source.mkdirs();
    source = new File(source, Strings.concat(classFileName, '.', extension));

    exec(templateName, meta, source);
  }

  void exec(String templateName, ClassMeta classMeta, File sourceCodeFile) throws IOException, Exception
  {
    Template template = new Template(getClass().getResourceAsStream(templateName));
    template.setModel(classMeta);
    template.serialize(new FileWriter(sourceCodeFile));
  }
}
