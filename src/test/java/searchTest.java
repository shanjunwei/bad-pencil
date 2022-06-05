import com.alibaba.fastjson.JSONObject;
import com.home.badpencil.constans.Constants;
import com.home.badpencil.lucence.index.IndexDao;
import com.home.badpencil.lucence.search.IndexSearch;
import com.home.badpencil.pojo.api.doc.ResDocs;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.MMapDirectory;
import org.junit.Test;
import java.nio.file.Paths;
import java.util.Iterator;

public class searchTest {

    @Test
    public void testSearch() throws Exception {
//        IndexDao indexDao = new IndexDao();
//        indexDao.createIndex("/Users/shanjunwei/Data/BadPencil/");
        IndexSearch indexSearch = new IndexSearch();
        ResDocs topDocs = indexSearch.search("密集");
        System.out.println(JSONObject.toJSONString(topDocs));
    }

    /**
     * 该测试类可以帮助查看索引的结构
     */
    @Test
    public void testSearch2() throws Exception {
        IndexReader indexReader  = DirectoryReader.open(MMapDirectory.open(Paths.get(Constants.LUCENE_INDEX_PATH)));
        int max = indexReader.maxDoc();
        System.out.println(max);
        // 显示记录总数
        for (int n = 0; n < max; n++) {
            Document document = indexReader.document(n);
            // 显示每条记录的title字段内容
            Iterator<IndexableField>  fields =   document.iterator();
            while (fields.hasNext()){
                IndexableField indexableField =   fields.next();
                System.out.printf("%s=%s\n" ,indexableField.name(),indexableField.stringValue());
            }
        }
        indexReader.close();
    }


}
