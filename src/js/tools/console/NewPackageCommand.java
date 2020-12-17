package js.tools.console;

import java.io.File;

public class NewPackageCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isProject(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not a project root: " + this.currentDirectory);
    }

    String sourceBaseName = args[0];
    String packageName = args[1];
    Console.print("Create package %s on %s.", packageName, sourceBaseName);

    File sourceBase = new File(this.currentDirectory, sourceBaseName);
    if(!sourceBase.exists()) {
      throw new IllegalStateException("Invalid project layout. Missing source base: " + sourceBase);
    }
    File packagePath = new File(sourceBase, packageName.replace(".", System.getProperty("file.separator")));
    packagePath.mkdirs();
  }
}
