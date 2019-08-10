/*
ID: andrew.83
LANG: JAVA
TASK: namenum
*/


import java.io.*;
import java.util.*;

public class namenum {

    public namenum() {
        string_dictionary.put("2",  "ABC");
        string_dictionary.put("3",  "DEF");
        string_dictionary.put("4",  "GHI");
        string_dictionary.put("5",  "JKL");
        string_dictionary.put("6",  "MNO");
        string_dictionary.put("7",  "PRS");
        string_dictionary.put("8",  "TUV");
        string_dictionary.put("9",  "WXY");
    }

    private String[] two = {"A", "B", "C"};
    private String[] three = {"D", "E", "F"};
    private String[] four = {"G", "H", "I"};
    private String[] five = {"J", "K", "L"};
    private String[] six = {"M", "N", "O"};
    private String[] seven = {"P", "R", "S"};
    private String[] eight = {"T", "U", "V"};
    private String[] nine = {"W", "X", "Y"};

    private HashMap<String, String> string_dictionary = new HashMap<String, String>();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            namenum n1 = new namenum();
            PrintWriter out = new PrintWriter("namenum.out");
            String answer = n1.work("namenum.in", "dict.txt");
            if(answer.substring(answer.length()-1).equals("\n")) {
                answer = answer.substring(0, answer.length()-1);
            }
            out.println(answer);
            out.close();
        }catch(Exception e) {
            System.out.println(e.toString());
        }

    }

    public String work(String filename, String dict) throws Exception {
        String rt = "";

        BufferedReader in = new BufferedReader(new FileReader(filename));
        String id = in.readLine();
        while(id.substring(id.length()-1).equals(" ")) {
            id = id.substring(0, id.length()-1);
        }
        int len = id.length();
        String[] list = new String[id.length()];
        for(int i=0; i<id.length(); i++) {
            list[i] = string_dictionary.get(id.substring(i, i+1));
        }

        HashSet<String> firstFilter = new HashSet<String>();
        for(int i=0; i<3; i++) {
            firstFilter.add(list[0].substring(i,  i+1));
        }

        //Creating dictionary
        HashSet<String> dictionary = new HashSet<String>();
        BufferedReader indict = new BufferedReader(new FileReader(dict));
        String line = indict.readLine();
        while(line != null) {
            if(line.length() == len && firstFilter.contains(line.substring(0, 1))){
                dictionary.add(line);
            }
            line = indict.readLine();
        }

        // get all dictionary first letter out into this set
        HashSet<String> firstLetters = new HashSet<String>();
        for(String s : dictionary) {
            firstLetters.add(s.substring(0, 1));
        }

        // now check list[0] if there is any char not in firstLetters
        String temp_str = "";
        for(int i=0; i<list[0].length(); i++) {
            String my_temp = list[0].substring(i,  i+1);
            if(firstLetters.contains(my_temp)) {
                temp_str += my_temp;
            }
        }
        list[0] = temp_str;
        int total = (int) Math.pow(3, id.length()-1) * list[0].length();

        HashSet<String> comb = new HashSet<String>();
        int list0_len = list[0].length();
        //all combinations
        for (int i = 0; i < total; i++) {
            String temp = "";
            int ind = 0;
            for (int a = 0; a < len; a++) {
                int itemp = (int) Math.pow(3, len - a - 1);
                if(a==0)
                    ind = (i/itemp)%list0_len;
                else
                    ind = (i / itemp) % 3;
                temp += list[a].substring(ind,  ind+1);
            }
            if(dictionary.contains(temp)) {
                if(!comb.contains(temp)) {
                    rt += temp + "\n";
                }
                comb.add(temp);
            }

        }



        if(rt.equals("")) {
            return "NONE";
        }
        return rt;
    }


    public String[] pos(String ch) {
        if(ch.equals("2")) {
            return two;
        }
        if(ch.equals("3")) {
            return three;
        }
        if(ch.equals("4")) {
            return four;
        }
        if(ch.equals("5")) {
            return five;
        }
        if(ch.equals("6")) {
            return six;
        }
        if(ch.equals("7")) {
            return seven;
        }
        if(ch.equals("8")) {
            return eight;
        }
        if(ch.equals("9")) {
            return nine;
        }
        String[] err = new String[0];
        return err;
    }

}
