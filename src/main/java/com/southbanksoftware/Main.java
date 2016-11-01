package com.southbanksoftware;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class Main {


    private static void closeFileStream(FileInputStream inputStream){
        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Please pass two data files");
            System.exit(1);
        }
        SqlQuery query = new SqlQuery();
        FileInputStream t1Stream = null;
        FileInputStream t2Stream = null;
        try {
            t1Stream = new FileInputStream(args[0]);
            t2Stream = new FileInputStream(args[1]);
            String json = query.performSqlQuery(t1Stream, t2Stream);
            query.writeToFile(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFileStream(t1Stream);
            closeFileStream(t2Stream);
        }
    }
}
