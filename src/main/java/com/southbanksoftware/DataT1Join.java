package com.southbanksoftware;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class DataT1Join {

    /**
     * the x value of the data of the row with the z value first appeared
     */
    private double x;
    /**
     * the count number of the z value appeared on the table
     */
    private int number;
    /**
     * the sum value of y with the same value of z
     */
    private double sumT1Y;
    /**
     * the joint value of y
     */
    private double jointValue;

    public DataT1Join(DataT1 data) {
        this.number = 1;
        sumT1Y = data.getY();
        x = data.getX();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addNumber(int n){
        this.number += n;
    }

    public double getSumT1Y() {
        return sumT1Y;
    }

    public void setSumT1Y(double sumT1Y) {
        this.sumT1Y = sumT1Y;
    }

    public void addToSumY(double y){
        this.sumT1Y += y;
    }

    public double getJointValue() {
        return jointValue;
    }

    public void setJointValue(double jointValue) {
        this.jointValue = jointValue;
    }

    public void addJointValue(double value){
        this.jointValue+=value;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
