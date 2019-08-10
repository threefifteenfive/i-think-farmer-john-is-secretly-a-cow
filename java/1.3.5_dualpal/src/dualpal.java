/*
ID: andrew.83
LANG: JAVA
TASK: dualpal
*/

import java.io.*;
import java.util.*;

public class dualpal {

    private HashMap<String, String> SpecialDigits;

    public dualpal() {
        SpecialDigits = new HashMap<String, String>();
        SpecialDigits.put("10", "A");
        SpecialDigits.put("11", "B");
        SpecialDigits.put("12", "C");
        SpecialDigits.put("13", "D");
        SpecialDigits.put("14", "E");
        SpecialDigits.put("15", "F");
        SpecialDigits.put("16", "G");
        SpecialDigits.put("17", "H");
        SpecialDigits.put("18", "I");
        SpecialDigits.put("19", "J");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            dualpal d = new dualpal();
            ArrayList<Integer> list = d.work("dualpal.in");
            PrintWriter out = new PrintWriter("dualpal.out");
            for(Integer i : list) {
                out.println(i);
            }
            out.close();
        }catch(Exception e) {
            System.out.println(e.toString());
        }

    }

    public ArrayList<Integer> work(String filename) throws Exception{
        ArrayList<Integer> rt = new ArrayList<Integer>();

        BufferedReader in = new BufferedReader(new FileReader(filename));
        String input = in.readLine();
        StringTokenizer st = new StringTokenizer(input);
        int n = Integer.parseInt(st.nextToken());
        int start = Integer.parseInt(st.nextToken()) + 1;

        //check size of rt when it equals n we are done
        int inc = start;
        while(rt.size() < n) {
            //count checks how many times the converted form of inc is palindromic
            int count = 0;
            //convert inc to all bases 2 through 10
            for(int i = 2; i <= 10; i++) {
                //if the converted inc is palindromic, increment count
                if(isPal(conBase(inc, i))) {
                    count++;
                }
            }
            //if inc is palindromic in more than one base, add it to rt list
            if(count > 1) {
                rt.add((Integer) inc);
            }
            inc++;
        }
        return rt;
    }

    /**
     * palindrome checking method
     * given a string
     * return true if the string is palindromic
     * @param num
     * @return
     */
    public boolean isPal(String num) {
        boolean rt = true;
        int len = num.length();
        for(int i = 0; i < num.length(); i++) {
            if(num.charAt(i) != (num.charAt(len-1-i))) {
                rt = false;
            }
        }
        return rt;
    }

    /**
     * base conversion method
     * given a number and a base
     * return the number in the given base
     * @param num
     * @param base
     * @return
     */
    public String conBase(int num, int base) {
        String rt = "";
        ArrayList<String> listDig = new ArrayList<String>();
        while(num != 0) {
            int exp = 0;
            int baseX;
            while(num % Math.pow(base, exp) != num) {
                exp++;
            }
            exp--;
            if(listDig.isEmpty()) {
                for(int i = 0; i <= exp; i++) {
                    listDig.add("0");
                }
            }
            baseX = (int)Math.pow(base, exp);
            int digit = num/baseX;
            String d = Integer.toString(digit);
            if(d.length() > 1) {
                d = SpecialDigits.get(d);
            }
            if(num != 0) {
                num = num % (baseX * digit);
                listDig.set(listDig.size() - exp - 1, d);
            } else {
                break;
            }
        }
        for(String dig : listDig) {
            rt += dig;
        }
        return rt;
    }

}
