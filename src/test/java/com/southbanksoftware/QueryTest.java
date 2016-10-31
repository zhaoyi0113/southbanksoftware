package com.southbanksoftware;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhaoyi0113 on 31/10/2016.
 */
public class QueryTest {

    private static final double ACTUAL = 0.01;

    @Test
    public void test_query_data() throws IOException {
        SqlQuery query = new SqlQuery();
        ResultData resultData = query.queryData(new FileInputStream("src/test/resources/t1.json"),
                new FileInputStream("src/test/resources/t2.json"));
        Assert.assertEquals(5, resultData.getX(), ACTUAL);
        Assert.assertEquals(2636871, resultData.getSumY1(), ACTUAL);
        Assert.assertEquals(3872169.89999956, resultData.getSumY2(), ACTUAL);

    }

    @Test
    public void test_query_data1() throws IOException {
        SqlQuery query = new SqlQuery();
        ResultData resultData = query.queryData(new FileInputStream("src/test/resources/com/southbanksoftware/test1_data1.json"),
                new FileInputStream("src/test/resources/com/southbanksoftware/test1_data2.json"));
        Assert.assertEquals(1, resultData.getX(), ACTUAL);
        Assert.assertEquals(6, resultData.getSumY1(), ACTUAL);
        Assert.assertEquals(4, resultData.getSumY2(), ACTUAL);
    }

    @Test
    public void test_query_data2() throws IOException {
        SqlQuery query = new SqlQuery();
        ResultData resultData = query.queryData(new FileInputStream("src/test/resources/com/southbanksoftware/test2_data1.json"),
                new FileInputStream("src/test/resources/com/southbanksoftware/test2_data2.json"));
        Assert.assertEquals(3, resultData.getX(), ACTUAL);
        Assert.assertEquals(6276.564774221999, resultData.getSumY1(), ACTUAL);
        Assert.assertEquals(1552.4575759757201, resultData.getSumY2(), ACTUAL);
    }

    @Test
    public void test_parse_data2() throws IOException {
        SqlQuery query = new SqlQuery();
        Map<Double, DataT1Join> data = query.readFirstData(new FileInputStream("src/test/resources/com/southbanksoftware/test2_data1.json"));
        Assert.assertEquals(2, data.size());
        Assert.assertEquals(1.93884, data.get(1.0).getSumT1Y(), ACTUAL);
        Assert.assertEquals(3135.374127111, data.get(2.0).getSumT1Y(), ACTUAL);
    }
}
