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

    /**
     * the joint value of y in the second table
     */
    private double jointValueT2;

    private double z;

    public DataT1Join(DataT1 data) {
        this.number = 1;
        sumT1Y = data.getY();
        x = data.getX();
        z = data.getZ();
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

    public void addJointValueT2(double value){
        this.jointValueT2+= (value * getNumber());
    }
    public double getJointValueT2() {
        return jointValueT2;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }
}
