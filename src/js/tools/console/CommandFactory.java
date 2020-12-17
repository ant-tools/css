package js.tools.console;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory
{
  private static final Map<String, Class<? extends Command>> commands = new HashMap<String, Class<? extends Command>>();
  static {
    commands.put("newworkspace", NewWorkspaceCommand.class);
    commands.put("newproject", NewProjectCommand.class);
    commands.put("newpackage", NewPackageCommand.class);
    commands.put("newclass", NewClassCommand.class);
    commands.put("newmodel", NewModelCommand.class);
    commands.put("newdao", NewDaoCommand.class);
    commands.put("newservice", NewServiceCommand.class);
    commands.put("newhtml", NewHtmlFileCommand.class);
    commands.put("openeclipse", OpenEclipseCommand.class);
  }

  public static Command create(String commandName, String subcommandName)
  {
    String key = (commandName + subcommandName).toLowerCase();
    Class<? extends Command> commandClass = commands.get(key);
    if(commandClass == null) return null;
    try {
      return commandClass.newInstance();
    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}
