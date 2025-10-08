package com.mllfjn.simyys.interactive;


import java.text.DecimalFormat;

public class TraceableNumber {
    private static final DecimalFormat df = new DecimalFormat("0.##");
    private double number;
    private final StringBuilder trace = new StringBuilder();

    public TraceableNumber() {
        trace.append("数据来源：");
    }

    public void add(double num, String desc) {
        number += num;
        append("+", num, desc);
    }

    public void sub(double num, String desc) {
        number -= num;
        append("-", num, desc);
    }

    public void mul(double num, String desc) {
        number *= num;
        append("*", num, desc);
    }

//    public void div(double num, String desc) {
//        number /= num;
//        trace.append("/").append(num).append("(").append(desc).append(")");
//    }

    private void append(String op, double num, String desc) {
        trace.append(op).append(df.format(num)).append("(").append(desc).append(")");
    }

    public String getNumber() {
        return df.format(number);
    }

    public String getTrace() {
        return trace.toString();
    }
}
