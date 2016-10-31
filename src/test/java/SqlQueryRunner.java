import com.southbanksoftware.SqlQuery;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class SqlQueryRunner {

    private static final String DIR = "src/test/resources/";

    @Test
    public void performSqlQuery() throws IOException {
        SqlQuery query = new SqlQuery();

        String json = query.performSqlQuery(new FileInputStream(DIR + "t1.json"), new FileInputStream(DIR + "t2.json"));
        query.writeToFile(json);

    }
}
