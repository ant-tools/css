package js.tools.console;

import java.io.File;

public interface Command
{
  void setCurrentDirectory(File currentDirectory);

  void exec(String... args) throws Exception;
}
