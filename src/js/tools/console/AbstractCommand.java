package js.tools.console;

import java.io.File;

public abstract class AbstractCommand implements Command
{
  protected File currentDirectory;

  @Override
  public void setCurrentDirectory(File currentDirectory)
  {
    this.currentDirectory = currentDirectory;
  }

  @Override
  public String toString()
  {
    return getClass().getCanonicalName();
  }
}
