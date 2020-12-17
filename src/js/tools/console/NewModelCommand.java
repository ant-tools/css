package js.tools.console;

public class NewModelCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isProject(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not a project root: " + this.currentDirectory);
    }

    // String modelName = args[0];
  }
}
