package js.tools.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;

public class Template
{
  private BufferedReader breader;
  private Object model;
  private State state;
  private StringBuilder tokenBuilder;

  public Template(InputStream stream)
  {
    try {
      this.breader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }
    catch(UnsupportedEncodingException e) {
      throw new IllegalStateException("JVM with missing support for UTF-8.");
    }
    this.tokenBuilder = new StringBuilder();
  }

  public void setModel(Object model)
  {
    this.model = model;
  }

  public void serialize(Writer writer) throws Exception
  {
    this.state = State.TEXT;
    BufferedWriter bwriter = new BufferedWriter(writer);
    for(;;) {
      int i = this.breader.read();
      if(i == -1) break;
      char c = (char)i;

      switch(this.state) {
      case TEXT:
        if(c == '$') {
          this.state = State.FLAG;
          continue;
        }
        break;

      case FLAG:
        if(c == '{') {
          this.state = State.TOKEN;
          continue;
        }
        this.state = State.TEXT;
        bwriter.write('$');
        if(c != '$') {
          bwriter.write(c);
        }
        continue;

      case TOKEN:
        if(c == '}') {
          this.state = State.TEXT;
          String token = this.tokenBuilder.toString();
          String value = getValue(token);
          if(value != null) {
            bwriter.append(value);
          }
          else {
            bwriter.append("${" + token + "}");
          }
          this.tokenBuilder.setLength(0);
        }
        else {
          this.tokenBuilder.append(c);
        }
        continue;

      default:
        throw new IllegalStateException(this.state.name());
      }

      bwriter.write(c);
    }

    bwriter.close();
  }

  private String getValue(String token)
  {
    try {
      Class<?> clazz = this.model.getClass();
      Field field = clazz.getDeclaredField(token);
      field.setAccessible(true);
      Object value = field.get(this.model);
      if(value instanceof String) return (String)value;
      return value.toString();
    }
    catch(SecurityException e) {}
    catch(NoSuchFieldException e) {}
    catch(IllegalArgumentException e) {}
    catch(IllegalAccessException e) {}
    return null;
  }

  private static enum State
  {
    NONE, TEXT, FLAG, TOKEN
  }
}
