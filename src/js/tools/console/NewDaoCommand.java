package js.tools.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import js.tools.commons.util.Strings;
import js.tools.console.res.ClassMeta;
import js.tools.console.res.ProjectMeta;

public class NewDaoCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isProject(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not a project root: " + this.currentDirectory);
    }

    File projectMetaXML = new File(this.currentDirectory, "project.xml");
    ProjectMeta projectMeta = ProjectMeta.load(new FileInputStream(projectMetaXML));

    String daoName = args[0];
    String rootPackage = projectMeta.getPackageRoot();

    String interfaceClassName = rootPackage + ".dao." + daoName + "Dao";
    String implementationClassName = rootPackage + ".dao." + daoName + "DaoImpl";
    String testClassName = rootPackage + ".test." + daoName + "DaoUnitTests";

    // Meta meta = new Meta();
    // meta.put("language", "java");
    // meta.put("authorName", "Iulian Rotaru");
    // meta.put("version", "1.0");
    // meta.put("packageRoot", rootPackage);
    //
    // meta.put("templateName", "res/dao-interface.tmpl");

    ClassMeta meta = new ClassMeta();
    meta.setLanguage("java");
    meta.setAuthorName("Iulian Rotaru");
    meta.setVersion("1.0");
    meta.setPackageRoot(rootPackage);

    meta.setTemplateName("res/dao-interface.tmpl");
    meta.setClassName(interfaceClassName);
    exec(meta);

    meta.setTemplateName("res/dao-implementation.tmpl");
    meta.setClassName(implementationClassName);
    meta.setSuperClassName(interfaceClassName);
    exec(meta);

    meta.setTemplateName("res/dao-test.tmpl");
    meta.setClassName(testClassName);
    meta.setClassUnderTest(interfaceClassName);
    exec(meta);
  }

  private void exec(ClassMeta meta) throws IOException, Exception
  {
    String packagePath = meta.getPackageName().replace('.', System.getProperty("file.separator").charAt(0));

    File source = new File(this.currentDirectory, meta.getLanguage());
    source = new File(source, packagePath);
    if(!source.exists()) source.mkdirs();
    source = new File(source, Strings.concat(meta.getClassName(), '.', meta.getLanguage()));

    Template template = new Template(getClass().getResourceAsStream(meta.getTemplateName()));
    template.setModel(meta);
    template.serialize(new FileWriter(source));
  }
}
