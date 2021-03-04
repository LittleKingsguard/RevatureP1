package com.revature.rarasey.project1;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SafeScan {
    public static int getPosInt(Scanner scan) {
        int input = -1;
        boolean display = true;
        while (input < 0){
            try {
                System.out.print("Enter positive whole number: ");
                input = scan.nextInt();
                display = input < 0;
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a positive whole number.");
                scan.next();
            }
            finally {
                if (display){
                    System.out.println("Please enter a positive whole number.");
                }
            }
        }
        return input;
    }

    public static float getPosFloat(Scanner scan) {
        float input = -1f;
        boolean display = true;
        while (input < 0){
            try {
                System.out.print("Enter positive number: ");
                input = scan.nextFloat();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                scan.next();
            }
        }
        return input;
    }

    public static int getPosInt(Scanner scan, int max) {
        int input = -1;
        boolean display = true;
        while (input < 0 || input > max){
            try {
                System.out.print("Enter positive whole number: ");
                input = scan.nextInt();
                display = input < 0 || input > max;
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a whole number.");
                scan.next();
            }
            finally {
                if (display) {
                    System.out.println("Out of range, enter number between 1 and " + max);
                }
            }
        }
        return input;
    }

    public static int getPosInt(Scanner scan, int max, int banned) {
        int input = -1;
        boolean display = true;
        while (input < 0 || input > max || input == banned){
            try {
                System.out.print("Enter positive whole number: ");
                input = scan.nextInt();
                display = input < 0 || input > max || input == banned;
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a whole number.");
                scan.next();
            }
            finally {
                if (display) {
                    System.out.println("Out of range, enter number between 1 and " + max + " other than " + banned);
                }
            }
        }
        return input;
    }



    public static int getPosIntBan(Scanner scan, int banned) {
        int input = -1;
        boolean display = true;
        while (input < 0 || input == banned){
            try {
                System.out.print("Enter positive whole number: ");
                input = scan.nextInt();
                display = input < 0 || input == banned;
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a whole number.");
                scan.next();
            }
            finally {
                if (display) {
                    System.out.println("Not allowed, enter positive number other than " + banned);
                }
            }
        }
        return input;
    }

    public static char getChar(Scanner scan, char[] accept) {
        boolean loop = true;
        char input = ' ';
        while (loop){
            try {
                System.out.print("Enter character: ");
                input = scan.next().charAt(0);
                for (char c: accept) {
                    if (input == c) {
                        loop = false;
                        break;
                    }
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a character in " + Arrays.toString(accept) + ".");
                scan.next();
            }
            finally {
                if (loop) {
                    System.out.println("Invalid input, please enter a character in " + Arrays.toString(accept) + ".");
                }
            }
        }
        return input;
    }

    public static void scrollPause(Scanner scan){
        System.out.println("Enter any to continue");
        scan.next();
    }
}
