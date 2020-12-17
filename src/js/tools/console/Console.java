package js.tools.console;

public class Console
{
  public static void print(String format, Object... args)
  {
    System.out.println(String.format(format, args));
  }

  public static void error(Object error, Object... args)
  {
    if(error instanceof String) {
      System.out.println(String.format((String)error, args));
    }
    else {
      System.out.println(error.toString());
    }
  }
}
