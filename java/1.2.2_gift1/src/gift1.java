/*
ID: andrew.83
LANG: JAVA
TASK: gift1
*/

import java.util.StringTokenizer;
import java.util.HashMap;
import java.io.*;
import java.util.ArrayList;

class Gifter {
    private String myName;
    private int myBalance = 0;
        
    public Gifter(String name){
        myName = name;
    }
    
    public String getName(){
        return myName;
    }
    public void add(int val) {
        myBalance += val;
    }
    
    public String toString(){
        return myName + " " + myBalance;
    }
        
}

public class gift1 {
    
    public static void main(String args[]){
        
       gift1 s2 = new gift1();
       try {
           ArrayList<Gifter> dic = s2.work("gift1.in");
           PrintWriter out = new PrintWriter("gift1.out");
           for (Gifter e : dic){
                out.println(e.toString());
           }
           out.close();
       }
       catch(Exception e) {
           System.out.println(e.toString());
           // write 
       }
       
      // PrintWriter out = new PrintWriter("gift1.out");
    }
    
    public ArrayList<Gifter> work(String filename) throws Exception {
        
        BufferedReader in = new BufferedReader(new FileReader(filename));
        //read number of people
        int num = Integer.parseInt(in.readLine());
        //create dictionary
        ArrayList<Gifter> rt = new ArrayList<Gifter>();
        HashMap<String, Gifter> people = new HashMap<String, Gifter>();
        for (int i = 0; i<num; i++){
            String tempName = in.readLine();
            Gifter tempGifter = new Gifter(tempName);
            people.put(tempName, tempGifter);
            rt.add(tempGifter);
        }
        String line = in.readLine();
        while (line != null){
            String tempName = line;
            String numbers = in.readLine();
            StringTokenizer st = new StringTokenizer(numbers);
            int amount = Integer.parseInt(st.nextToken());
            int numPeople = Integer.parseInt(st.nextToken());
            int received = 0;
            int amt = amount;
            if (numPeople != 0){
               received = amount/numPeople;
               amt = -amount + amount%numPeople;
            }
            people.get(tempName).add(amt);
            for (int l = 0; l<numPeople; l++){
                String rec = in.readLine();
                people.get(rec).add(received);
            }
            line = in.readLine();
        }
        return rt;
    }

}

