package test;

import java.util.Scanner;

public class TestDB extends Common {

    public static void main(String[] args) {
        Common common = new Common();
        common.updateData("test@gmail.com", "Wright", "Eric");
        common.readData();


    }
}
