package com.southbanksoftware;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Please pass two data files");
            System.exit(1);
        }
        SqlQuery query = new SqlQuery();
        try {
            String json = query.performSqlQuery(new FileInputStream(args[0]), new FileInputStream(args[1]));
            query.writeToFile(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
