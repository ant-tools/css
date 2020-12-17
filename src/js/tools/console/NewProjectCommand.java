package js.tools.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import js.tools.commons.util.Files;
import js.tools.console.res.ProjectMeta;

public class NewProjectCommand extends AbstractCommand
{
  private static final String PACKAGE_BASE = "ro.phoenix.";

  private String projectName;
  private File projectDirectory;
  private ProjectMeta projectMeta;

  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isWorkspace(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not an workspace root: " + this.currentDirectory);
    }

    this.projectName = args[0];
    Console.print("Create project %s into workspace %s.", this.projectName, this.currentDirectory);

    String packageRoot = getPackageRoot(args);
    this.projectDirectory = new File(this.currentDirectory, this.projectName);
    if(!this.projectDirectory.exists()) this.projectDirectory.mkdir();

    File projectMetaXML = new File(this.projectDirectory, "project.xml");
    this.projectMeta = new ProjectMeta();
    this.projectMeta.setProjectName(this.projectName);
    this.projectMeta.setPackageRoot(packageRoot);
    this.projectMeta.setWarName(this.projectName + ".war");
    this.projectMeta.setRuntimePath("D:\\jvm\\tomcat-5.5");
    ProjectMeta.save(this.projectMeta, new FileOutputStream(projectMetaXML));

    createDirectoriesLayout();
    copyTemplate("res/eclipse-project.tmpl", ".project");
    copyFile("res/eclipse-classpath.tmpl", ".classpath");
    copyTemplate("res/ant-builder.tmpl", "build.xml");
    copyTemplate("res/log4j-properties.tmpl", "conf/log4j.properties");
    copyTemplate("res/web-xml.tmpl", "conf/web.xml");
    copyTemplate("res/app-config.tmpl", "conf/app.xml");
    copyTemplate("res/app-descriptor.tmpl", "conf/app-descriptor.xml");
    copyFile("res/reset-css.tmpl", "res/layout/css/reset.css");
    copyFile("res/mysql-eer.mwb", "db/" + this.projectName + ".mwb");
    copyFile("res/model.vsd", "doc/model.vsd");
    copyFile("res/model.doc", "doc/model.doc");

    execCommand("new", "html", "index");
    execCommand("new", "package", "java", packageRoot);
    execCommand("new", "package", "java", packageRoot + ".dao");
    execCommand("new", "package", "java", packageRoot + ".model");
    execCommand("new", "package", "java", packageRoot + ".service");
    execCommand("new", "package", "java", packageRoot + ".test");
    execCommand("new", "package", "js", packageRoot);
    execCommand("new", "dao", "");
  }

  private void execCommand(String commandName, String subcommandName, String... args) throws Exception
  {
    Command command = CommandFactory.create(commandName, subcommandName);
    command.setCurrentDirectory(this.projectDirectory);
    command.exec(args);
  }

  private String getPackageRoot(String... args)
  {
    if(args.length > 1) return args[1];
    return PACKAGE_BASE + this.projectName.toLowerCase().replaceAll("[\\s-]", "");
  }

  private static final String[] directoriesLayout = new String[]
  {
      "bin", //
      "build", //
      "conf", //
      "db", //
      "doc", //
      "java", //
      "js", //
      "libs", //
      "res",//
      "res/layout",//
      "res/layout/css",//
      "res/layout/img",//
      "res/menu", //
      "res/values"
  };

  private void createDirectoriesLayout()
  {
    for(String directoryPath : directoriesLayout) {
      File directory = new File(this.projectDirectory, directoryPath);
      directory.mkdirs();
    }
  }

  private void copyTemplate(String templateResource, String targetPath) throws Exception
  {
    Template template = new Template(Utils.getTemplateStream(templateResource));
    template.setModel(this.projectMeta);
    template.serialize(new FileWriter(new File(this.projectDirectory, targetPath)));
  }

  private void copyFile(String templateResource, String targetPath) throws FileNotFoundException, IOException
  {
    Files.copy(Utils.getTemplateStream(templateResource), new FileOutputStream(new File(this.projectDirectory, targetPath)));
  }
}
