# Coding Exercise Solution

This is the coding exercise solution for Southbank Software.

The Code is written in Java8 and managed by maven. In order to run this program,
you need to install JDK8 and Maven3. Then run below command to execute the program:

* mvn clean install

After running above command, you can see the result file got generated in target/result.json file.
The input files for this program are src/test/resources/t1.json and src/test/resources/t2.json.

The class Main is the entry class for this program. It is specified in 'exec-maven-plugin' and
bind to maven install phase.

A fat-jar file will be generated target/SqlToJava-1.0-SNAPSHOT-jar-with-dependencies.jar which
include all the dependencies and Main-Entry class. Please run below command to run this program
against other data files:

* java -jar target/SqlToJava-1.0-SNAPSHOT-jar-with-dependencies.jar FILE1 FILE2

The class SqlQuery is the implementation class to do the sql query logic in Java.

# Project Dependencies

This project is using below dependencies:

* com.google.code.gson A library to parse json and Java object

* junit     test depenency to run test cases

# Design Description

This program will read the first input file line by line, then parse each line to DataT1 object.
And save it on a Hash Map. The key of the hash map is the value of z in the data file. The reason to
use hash map is that the time complexity of querying data from the map by key is O(1).
After finish reading the first data, the program will start reading the second one. It will parse each
row to a DataT2 instance, then check if it's z value exist on the hash map generated before. If not, add to
the join table. So the total time complexity of this program is O(n+m).

