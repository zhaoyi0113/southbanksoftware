package com.southbanksoftware;

/**
 * Created by zhaoyi0113 on 01/11/2016.
 */
public class DataT2Join {


    private double sumT2Y;

    private double y;

    public DataT2Join(double y, double sumT2Y) {
        this.sumT2Y = sumT2Y;
        this.y = y;
    }

    public void addToY(double y){
        this.sumT2Y += y;
    }


    public double getSumT2Y() {
        return sumT2Y;
    }

    public void setSumT2Y(double sumT2Y) {
        this.sumT2Y = sumT2Y;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
