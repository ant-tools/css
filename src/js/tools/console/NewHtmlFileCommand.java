package js.tools.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import js.tools.commons.util.Files;
import js.tools.console.res.PageMeta;

public class NewHtmlFileCommand extends AbstractCommand
{
  @Override
  public void exec(String... args) throws Exception
  {
    if(!Utils.isProject(this.currentDirectory)) {
      throw new IllegalStateException("Incorect working directory. Is not a project root: " + this.currentDirectory);
    }

    String pageName = args[0];

    PageMeta pageMeta = new PageMeta();
    pageMeta.setProjectName(Utils.getProjectName(this.currentDirectory));
    pageMeta.setPageName(pageName);

    Template template = new Template(getClass().getResourceAsStream("res/html-file.tmpl"));
    template.setModel(pageMeta);
    template.serialize(new FileWriter(new File(this.currentDirectory, "res/layout/" + pageName + ".html")));

    Files.copy(Utils.getTemplateStream("res/css-file.tmpl"), new FileOutputStream(new File(this.currentDirectory, "res/layout/css/" + pageName + ".css")));
  }
}
