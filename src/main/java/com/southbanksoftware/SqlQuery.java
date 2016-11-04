package com.southbanksoftware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


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
        List<ResultData> resultData = queryData(t1Stream, t2Stream);
        StringBuffer buffer = new StringBuffer();
        resultData.forEach(result -> {
            Gson gson = new GsonBuilder().create();
            buffer.append(gson.toJson(result)).append("\n");
        });
        return buffer.toString();
    }

    /**
     * perform the sql query on the given data files
     *
     * @param t1Stream the first data file
     * @param t2Stream the second data file
     * @return the query result data
     * @throws IOException
     */
    public List<ResultData> queryData(InputStream t1Stream, InputStream t2Stream) throws IOException {
        Map<Pair, DataT1Join> data1 = readFirstData(t1Stream);

        Map<Double, List<DataT1Join>> aggregateZ = getAggregateZValue(data1);

        Map<Double, List<DataT1Join>> joinResult = readSecondData(t2Stream, aggregateZ);

        Map<Double, DataT1Join> aggregateX = aggregateXValue(joinResult);
        List<ResultData> resultDatas = new ArrayList<>();

        aggregateX.forEach((k, v) -> {
            ResultData data = new ResultData();
            data.setX(k);
            data.setSumY1(v.getJointValue());
            data.setSumY2(v.getJointValueT2());
            resultDatas.add(data);
        });

        return resultDatas.stream().sorted(Comparator.comparing(ResultData::getSumY2).reversed()).collect(Collectors.toList());
    }

    private Map<Double, DataT1Join> aggregateXValue(Map<Double, List<DataT1Join>> joinResult) {
        Map<Double, DataT1Join> aggregateX = new HashMap<>();
        joinResult.forEach((k, v) -> {
            v.forEach(data -> {
                if (aggregateX.containsKey(data.getX())) {
                    DataT1Join dataT1Join = aggregateX.get(data.getX());
                    dataT1Join.addJointValue(data.getJointValue());
                    dataT1Join.addJointValueT2(data.getJointValueT2());
                } else {
                    aggregateX.put(data.getX(), data);
                }
            });
        });
        return aggregateX;
    }

    private Map<Double, List<DataT1Join>> getAggregateZValue(Map<Pair, DataT1Join> data1) {
        Map<Double, List<DataT1Join>> aggregateZ = new HashMap<>();
        data1.forEach((k, v) -> {
            if (aggregateZ.containsKey(k.getFirst())) {
                List<DataT1Join> dataT1Joins = aggregateZ.get(k.getFirst());
                dataT1Joins.add(v);
            } else {
                List<DataT1Join> list = new ArrayList<>();
                list.add(v);
                aggregateZ.put((Double) k.getFirst(), list);
            }
        });
        return aggregateZ;
    }

    /**
     * read the first json data file
     *
     * @param stream the first data file input stream
     * @return a Hahsmap instance where key is the value of column Z
     * @throws IOException
     */
    public Map<Pair, DataT1Join> readFirstData(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Map<Pair, DataT1Join> dataMap = new LinkedHashMap<>();
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
    private void sumYValueOnTheFirstTable(Map<Pair, DataT1Join> dataMap, DataT1 data) {
        Pair pair = new Pair(data.getZ(), data.getX());
        if (dataMap.containsKey(pair)) {
            //if the z value already exists, sum the y
            DataT1Join existed = dataMap.get(pair);
            existed.addNumber(1);
            existed.addToSumY(data.getY());
        } else {
            DataT1Join join = new DataT1Join(data);
            dataMap.put(pair, join);
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
    private Map<Double, List<DataT1Join>> readSecondData(InputStream stream, Map<Double, List<DataT1Join>> data1) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        Map<Double, List<DataT1Join>> joinResult = new HashMap<>();
        while (line != null) {
            DataT2 data = parseJson(DataT2.class, line);
            List<DataT1Join> existedList = data1.get(data.getZ());
            if (existedList != null) {
                existedList.forEach(exist -> {
                    exist.addJointValue(exist.getSumT1Y());
                    exist.addJointValueT2(data.getY());

                });
                joinResult.put(data.getZ(), existedList);
            }
            line = reader.readLine();
        }
        return joinResult;
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
