/*
ID: andrew.83
LANG: JAVA
TASK: transform
*/

import java.io.*;

public class transform {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            transform t = new transform();
            PrintWriter out = new PrintWriter("transform.out");
            out.println(t.work("transform.in"));
            out.close();
        }catch(Exception e) {
            System.out.println(e.toString());
        }

    }

    public int work(String filename) throws Exception{
        BufferedReader in = new BufferedReader(new FileReader(filename));
        int dim = Integer.parseInt(in.readLine());
        String[][] org = new String[dim][dim];
        String[][] res = new String[dim][dim];
        String[][] mir = new String[dim][dim];

        for(int i = 0; i < dim; i++) {
            String temp = in.readLine();
            for(int j = 0; j < dim; j++) {
                org[i][j] = temp.substring(j, j+1);
                mir[i][j] = temp.substring(j, j+1);
            }
        }
        boolean six = true;
        for(int i = 0; i < dim; i++) {
            String temp = in.readLine();
            for(int j = 0; j < dim; j++) {
                String ch = temp.substring(j, j+1);
                res[i][j] = ch;
                if(!ch.equals(org[i][j])) {
                    six = false;
                }
            }
        }

        //System.out.println(twodprint(org));
        //System.out.println(twodprint(res));

        //90 degree rotation
        rotate(org);
        //System.out.println("1\n" + twodprint(org));
        if(check(org, res)) {
            return 1;
        }
        //180 degree rotation
        rotate(org);
        //System.out.println("2\n" + twodprint(org));
        if(check(org, res)) {
            return 2;
        }
        //270 degree rotation
        rotate(org);
        //System.out.println("3\n" + twodprint(org));
        if(check(org, res)) {
            return 3;
        }

        //mirror
        flip(mir);
        //System.out.println("4\n" + twodprint(mir));
        if(check(mir, res)) {
            return 4;
        }
        //90 degree rotation
        rotate(mir);
        //System.out.println("5, 1\n" + twodprint(mir));
        if(check(mir, res)) {
            return 5;
        }
        //180 degree rotation
        rotate(mir);
        //System.out.println("5, 2\n" + twodprint(mir));
        if(check(mir, res)) {
            return 5;
        }
        //270 degree rotation
        rotate(mir);
        //System.out.println("5, 3\n" + twodprint(mir));
        if(check(mir, res)) {
            return 5;
        }

        if(six == true) {
            return 6;
        }

        return 7;
    }

    public static void rotate(String[][] arr) {
        String[][] storage = new String[arr.length][arr[0].length];
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                storage[arr.length-1-j][i] = arr[arr.length-1-i][arr.length-1-j];
            }
        }
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                arr[i][j] = storage[i][j];
            }
        }
    }

    public static boolean check(String[][] org, String[][] res) {
        boolean same = true;
        for(int i = 0; i < org.length; i++) {
            for(int j = 0; j < org.length; j++) {
                if(!org[i][j].equals(res[i][j])) {
                    same = false;
                }
            }
        }
        return same;
    }

    public static void flip(String[][] arr) {
        String[][] rt = new String[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                rt[i][arr[0].length-1-j] = arr[i][j];
            }
        }
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                arr[i][j] = rt[i][j];
            }
        }
    }

    public static String twodprint(String[][] arr) {
        String rt = "";
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                rt += arr[i][j];
            }
            rt += "\n";
        }
        return rt;
    }

}
