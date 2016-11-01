package com.southbanksoftware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;


/**
 * This class is used to perform the sql statement in Java:
 * SELECT t1.x,SUM(t1.y),SUM(t2.y)
 * FROM t1 INNER JOIN t2 ON (t1.z=t2.z)
 * WHERE t1.z >0
 * ORDER BY 3 DESC
 * <p>
 * <p>
 * Created by zhaoyi0113 on 31/10/2016.
 */
public class SqlQuery {


    private static final String OUTPUT_FILE = "target/result.json";

    /**
     * perform the sql query on the given data files and parse the result to be json
     *
     * @param t1Stream the first data file
     * @param t2Stream the second data file
     * @return json data file of the result
     * @throws IOException
     */
    public String performSqlQuery(InputStream t1Stream, InputStream t2Stream) throws IOException {
        ResultData resultData = queryData(t1Stream, t2Stream);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(resultData);
    }

    /**
     * perform the sql query on the given data files
     *
     * @param t1Stream the first data file
     * @param t2Stream the second data file
     * @return the query result data
     * @throws IOException
     */
    public ResultData queryData(InputStream t1Stream, InputStream t2Stream) throws IOException {
        Map<Double, DataT1Join> data1 = readFirstData(t1Stream);
        Map<Double, DataT2Join> data2 = readSecondData(t2Stream, data1);

        double sumY1 = data1.values().stream().mapToDouble(v -> v.getJointValue()).sum();
        ResultData resultData = new ResultData();
        resultData.setX(data1.entrySet().iterator().next().getValue().getX());
        resultData.setSumY1(sumY1);

        double sumY2 = data2.values().stream().mapToDouble(v -> v.getSumT2Y()).sum();
        resultData.setSumY2(sumY2);
        return resultData;
    }

    /**
     * read the first json data file
     *
     * @param stream the first data file input stream
     * @return a Hahsmap instance where key is the value of column Z
     * @throws IOException
     */
    public Map<Double, DataT1Join> readFirstData(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Map<Double, DataT1Join> dataMap = new LinkedHashMap<>();
        String line = reader.readLine();
        while (line != null) {
            DataT1 data = parseJson(DataT1.class, line);
            if (Double.compare(data.getZ(), 0) < 0) {
                //skip the data where z < 0
                line = reader.readLine();
                continue;
            }
            sumYValueOnTheFirstTable(dataMap, data);
            line = reader.readLine();
        }
        return dataMap;
    }

    /**
     * parse the first table and sum the y value on the same z value
     *
     * @param dataMap the result map
     * @param data    the current row data of the first table
     */
    private void sumYValueOnTheFirstTable(Map<Double, DataT1Join> dataMap, DataT1 data) {
        if (dataMap.containsKey(data.getZ())) {
            //if the z value already exists, sum the y
            DataT1Join existed = dataMap.get(data.getZ());
            existed.addNumber(1);
            existed.addToSumY(data.getY());
        } else {
            DataT1Join join = new DataT1Join(data);
            dataMap.put(data.getZ(), join);
        }
    }

    /**
     * read and parse the second data file
     *
     * @param stream the second data file input stream
     * @param data1  the data map from the first data file
     * @return the joint value of the second data file
     * @throws IOException
     */
    private Map<Double, DataT2Join> readSecondData(InputStream stream, Map<Double, DataT1Join> data1) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Map<Double, DataT2Join> dataMap = new LinkedHashMap<>();
        String line = reader.readLine();
        while (line != null) {
            DataT2 data = parseJson(DataT2.class, line);

            if (data1.containsKey(data.getZ())) {
                //only process the data when it's z value exist on the data1 hash map. the time complexity is O(1)
                performJoinOnParsingSecondTable(data1, dataMap, data);
            }

            line = reader.readLine();
        }
        return dataMap;
    }

    /**
     * perform join operation on parsing the second table
     *
     * @param data1   the data set from the first table
     * @param dataMap the joined value on the second table
     * @param data    the data represent the current row
     */
    private void performJoinOnParsingSecondTable(Map<Double, DataT1Join> data1, Map<Double, DataT2Join> dataMap, DataT2 data) {
        DataT1Join existedD1 = data1.get(data.getZ());
        existedD1.addJointValue(existedD1.getSumT1Y());
        if (dataMap.containsKey(data.getZ())) {
            DataT2Join existed = dataMap.get(data.getZ());
            existed.addToY(existedD1.getNumber() * data.getY());
        } else {
            DataT2Join t2Join = new DataT2Join(data.getY(), data.getY() * existedD1.getNumber());
            dataMap.put(data.getZ(), t2Join);
        }
    }

    /**
     * parse json data to the spcified data type
     */
    private <T> T parseJson(Class<T> dataType, String line) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(line, dataType);
    }

    public void writeToFile(String json) throws IOException {
        FileWriter writer = new FileWriter(OUTPUT_FILE);
        writer.write(json);
        writer.flush();
        writer.close();
    }

}
