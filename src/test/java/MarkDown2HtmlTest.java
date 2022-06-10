import com.home.badpencil.utils.FileUtil;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkDown2HtmlTest {
    @Test
    public void testSearch2() throws Exception {
        MutableDataSet options = new MutableDataSet();
        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        String mdFileContent = new String(Files.readAllBytes(Paths.get("/Users/shanjunwei/Documents/服务面试/面经收集/笔记无项目/SSM.md")));

        // You can re-use parser and renderer instances
        Node document = parser.parse(mdFileContent);
        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        System.out.println(html);
        FileUtil.write("/Users/shanjunwei/Documents/服务面试/面经收集/笔记无项目/SSM.html",html);
    }
}
