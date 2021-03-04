package com.revature.rarasey.project1;

public class Pad {
    public static String leftPad(int size, String input, char fill){
        if (input.length() >= size){
            return input;
        }
        int padding = size - input.length();
        StringBuilder padded = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            padded.append(fill);
        }
        padded.append(input);
        return padded.toString();
    }
    public static String rightPad(int size, String input, char fill){
        if (input.length() >= size){
            return input;
        }
        int padding = size - input.length();
        StringBuilder padded = new StringBuilder();
        padded.append(input);
        for (int i = 0; i < padding; i++) {
            padded.append(fill);
        }
        return padded.toString();

    }

    public static String balancedTable(int colWidth,String left, String right, char fill, String middle){
        int leftWidth = colWidth;
        int rightWidth = colWidth;
        if (left.length() > colWidth){
            rightWidth -= left.length() - colWidth;
        }
        if (right.length() > colWidth){
            leftWidth -= right.length() - colWidth;
        }
        return rightPad(leftWidth, left, fill) + middle + leftPad(rightWidth, right, fill);
    }

    public static void newScreen(){
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
