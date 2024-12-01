package client.helper;

import java.util.ArrayList;

public class ShipGenerator {

    static ArrayList<String> shipsLocation = new ArrayList<String>();
    static int[] shipSizes = {5, 4, 3, 3, 2};

    private static boolean toss() {
        if ((int) (Math.random() * 10 + 1) > 5) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<String> generateShip() {
        ArrayList<String> shipsLocation = new ArrayList<String>();
        for (int i : shipSizes) {
            shipsLocation.add("/");
            String[] currentShip = new String[i];
            boolean flag = false;
            while (!flag) {
                flag = true;
                if (toss()) {
                    int l = (int) (Math.random() * 10);
                    int n = (int) (Math.random() * (10 - i));
                    currentShip[0] = n + "" + l;
                    for (int j = 1; j < i; j++) {
                        currentShip[j] = (n + j) + "" + l;
                    }
                } else {
                    int l = (int) (Math.random() * (10 - i));
                    int n = (int) (Math.random() * (10));
                    currentShip[0] = n + "" + l;
                    for (int j = 1; j < i; j++) {
                        currentShip[j] = n + "" +(l + j) ;
                    }
                }
                for (String s : currentShip) {
                    if (shipsLocation.contains(s)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    for (String s : currentShip) {
                        shipsLocation.add(s);
                    }
                }
            }
        }
        return shipsLocation;
    }
}
