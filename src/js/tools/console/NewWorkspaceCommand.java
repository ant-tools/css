package js.tools.console;

import java.io.File;

public class NewWorkspaceCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    String workspaceName = args[0];
    Console.print("Create Workspace %s", workspaceName);

    File dir = new File(workspaceName);
    if(dir.exists()) {
      Console.error("Directory %s already exist.", workspaceName);
    }
    Console.print(dir.getAbsolutePath());
    dir.mkdir();
  }
}
