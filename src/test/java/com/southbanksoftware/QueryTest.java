package com.southbanksoftware;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhaoyi0113 on 31/10/2016.
 */
public class QueryTest {

    private static final double ACTUAL = 0.00001;

    @Test
    public void test_query_data() throws IOException {
        SqlQuery query = new SqlQuery();
        List<ResultData> resultData = query.queryData(new FileInputStream("src/test/resources/t1.json"),
                new FileInputStream("src/test/resources/t2.json"));
        Assert.assertEquals(9, resultData.size());
        Assert.assertEquals(9, resultData.get(0).getX(), ACTUAL);
        Assert.assertEquals(298071, resultData.get(0).getSumY1(), ACTUAL);
        Assert.assertEquals(566334.899999997, resultData.get(0).getSumY2(), ACTUAL);

    }

    @Test
    public void test_query_data1() throws IOException {
        SqlQuery query = new SqlQuery();
        List<ResultData> resultData = query.queryData(new FileInputStream("src/test/resources/com/southbanksoftware/test1_data1.json"),
                new FileInputStream("src/test/resources/com/southbanksoftware/test1_data2.json"));
        Assert.assertEquals(2, resultData.size());
        Assert.assertEquals(1, resultData.get(0).getX(), ACTUAL);
        Assert.assertEquals(2, resultData.get(0).getSumY1(), ACTUAL);
        Assert.assertEquals(2, resultData.get(0).getSumY2(), ACTUAL);
        Assert.assertEquals(2, resultData.get(1).getX(), ACTUAL);
        Assert.assertEquals(4, resultData.get(1).getSumY1(), ACTUAL);
        Assert.assertEquals(2, resultData.get(1).getSumY2(), ACTUAL);
    }

    @Test
    public void test_query_data2() throws IOException {
        SqlQuery query = new SqlQuery();
        List<ResultData> resultData = query.queryData(new FileInputStream("src/test/resources/com/southbanksoftware/test2_data1.json"),
                new FileInputStream("src/test/resources/com/southbanksoftware/test2_data2.json"));
        Assert.assertEquals(3, resultData.size());
        Assert.assertEquals(2, resultData.get(0).getX(), ACTUAL);
        Assert.assertEquals(6.373, resultData.get(0).getSumY1(), ACTUAL);
        Assert.assertEquals(776.22878798786, resultData.get(0).getSumY2(), ACTUAL);
    }


    @Test
    public void test_query_data3() throws IOException{
        SqlQuery query = new SqlQuery();
        List<ResultData> resultData = query.queryData(new FileInputStream("src/test/resources/com/southbanksoftware/test3_data1.json"),
                new FileInputStream("src/test/resources/com/southbanksoftware/test3_data2.json"));
        Assert.assertTrue(resultData.isEmpty());
    }
}
