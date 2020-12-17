package js.tools.console;

import java.io.File;

public class Main
{
  public static void main(String[] commandLineArguments)
  {
    String currentDirectory = commandLineArguments[0];
    String commandName = commandLineArguments[1];
    String subcommandName = commandLineArguments[2];

    Command command = CommandFactory.create(commandName, subcommandName);
    if(command == null) {
      Console.print("Command '%s %s' not found.", commandName, subcommandName);
      return;
    }
    command.setCurrentDirectory(new File(currentDirectory));

    String[] commandArguments = new String[Math.max(0, commandLineArguments.length - 3)];
    for(int i = 0; i < commandArguments.length; ++i) {
      commandArguments[i] = commandLineArguments[i + 3];
    }

    try {
      command.exec(commandArguments);
    }
    catch(Throwable e) {
      Console.error("Fatal error executin command %s: %s", command, e);
      e.printStackTrace();
    }
  }
}
