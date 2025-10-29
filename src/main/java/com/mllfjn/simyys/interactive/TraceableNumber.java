package com.mllfjn.simyys.interactive;


import java.text.DecimalFormat;

public class TraceableNumber {
    private static final DecimalFormat df = new DecimalFormat("0.##");
    private double number;
    private final StringBuilder trace = new StringBuilder();

    public TraceableNumber() {
        trace.append("计算过程：");
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

    private void append(String op, double num, String desc) {
        // op .2f (desc)
        trace.append(op).append(df.format(num)).append("(").append(desc).append(")");
    }

    public void addTrace(String s) {
        trace.append(s);
    }

    public String getNumberString() {
        return df.format(number);
    }

    public double getNumber() {
        return number;
    }

    public String getTrace() {
        return trace.toString();
    }
}
