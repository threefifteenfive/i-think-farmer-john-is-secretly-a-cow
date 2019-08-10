/*
ID: andrew.83
LANG: JAVA
TASK: palsquare
*/

import java.io.*;
import java.util.*;


public class palsquare {

    private HashMap<String, String> SpecialDigits;

    public palsquare() {
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
            palsquare p = new palsquare();
            PrintWriter out = new PrintWriter("palsquare.out");
            out.print(p.work("palsquare.in"));
            out.close();
            //System.out.println(p.work("palsquare.in"));
        }catch(Exception e) {
            System.out.println(e.toString());
        }

    }

    public String work(String filename) throws Exception{
        String rt = "";

        //String test = conBase(1, 2);

        BufferedReader in = new BufferedReader(new FileReader(filename));
        int base = Integer.parseInt(in.readLine());
        for(int i = 1; i < 300; i++) {
            int squared = (int) Math.pow(i,2);
            String con = conBase(squared,base);
            System.out.println(conBase(i, base) + " " + con + "\n");
            if(isPal(con)) {
                rt += conBase(i, base) + " " + con + "\n";
            }
        }
        //convert i^2 to String before checking isPal()
        //convert i to String & i^2 to String before adding to rt;

        return rt;
    }

    /**
     * given a string
     * return true if it is a palindrome
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
     * given a number and a base, return the number converted to the given base
     */
    public String conBase(int num, int base) {
        String rt = "";
        //stores digits
        ArrayList<String> listDig = new ArrayList<String>();
        while(num != 0) {
            //exponent
            int exp = 0;
            int baseX;
            //calculating greatest possible exponent for the base for current number
            while(num % Math.pow(base, exp) != num) {
                exp++;
            }
            exp--;
            //adding digits
            if(listDig.isEmpty()) {
                for(int i = 0; i <= exp; i++) {
                    listDig.add("0");
                }
            }
            //multiplier of the digit
            baseX = (int)Math.pow(base, exp);
            //calculating the digit
            int digit = num/baseX;
            String d = Integer.toString(digit);
            //case of base larger than 10
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
