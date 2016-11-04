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

        Map<Pair, DataT1Join> joinResult = readSecondData(t2Stream, data1);

        Map<Double, List<DataT1Join>> collect = joinResult.values().stream().collect(Collectors.groupingBy(DataT1Join::getX));

        List<ResultData> resultDatas = new ArrayList<>();

        collect.forEach((k, v) -> {
            double sumY1 = v.stream().mapToDouble(DataT1Join::getJointValue).sum();
            double sumY2 = v.stream().mapToDouble(DataT1Join::getJointValueT2).sum();
            ResultData data = new ResultData();
            data.setX(k);
            data.setSumY1(sumY1);
            data.setSumY2(sumY2);
            resultDatas.add(data);
        });

        return resultDatas.stream().sorted(Comparator.comparing(ResultData::getSumY2).reversed()).collect(Collectors.toList());
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
    private Map<Pair, DataT1Join> readSecondData(InputStream stream, Map<Pair, DataT1Join> data1) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        Map<Pair, DataT1Join> joinResult = new HashMap<>();
        while (line != null) {
            DataT2 data = parseJson(DataT2.class, line);
            List<DataT1Join> existedList = new ArrayList<>();
            for (Map.Entry<Pair, DataT1Join> entry : data1.entrySet()) {
                if (Double.compare((double) entry.getKey().first, data.getZ()) == 0) {
                    existedList.add(entry.getValue());
                    joinResult.put(entry.getKey(), entry.getValue());
                }
            }
            existedList.forEach(exist -> {
                exist.addJointValue(exist.getSumT1Y());
                exist.addJointValueT2(data.getY());

            });
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
