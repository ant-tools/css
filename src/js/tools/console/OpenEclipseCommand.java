package js.tools.console;

import java.io.File;
import java.io.IOException;

public class OpenEclipseCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(Utils.isProject(this.currentDirectory)) {
      this.currentDirectory = new File(this.currentDirectory, "..");
    }

    Console.print("Open eclipse on %s workspace.", this.currentDirectory);

    String[] cmdarray = new String[]
    {
        "D:\\jvm\\eclipse-web\\eclipse.exe", " -nosplash", "-data", this.currentDirectory.getAbsolutePath()
    };
    try {
      Runtime.getRuntime().exec(cmdarray, null, this.currentDirectory);
    }
    catch(IOException e) {
      Console.error(e);
    }
  }
}
