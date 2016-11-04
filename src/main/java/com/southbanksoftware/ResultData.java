package com.southbanksoftware;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class ResultData {

    private double x;

    private double sumY1;

    private double sumY2;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getSumY1() {
        return sumY1;
    }

    public void setSumY1(double sumY1) {
        this.sumY1 = sumY1;
    }

    public double getSumY2() {
        return sumY2;
    }

    public void setSumY2(double sumY2) {

        this.sumY2 = sumY2;
    }

    @Override
    public String toString() {
        return "x="+x+", sumY1="+sumY1+", sumY2="+sumY2;
    }
}
