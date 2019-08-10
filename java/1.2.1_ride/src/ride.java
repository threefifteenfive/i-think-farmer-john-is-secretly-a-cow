/*
ID: andrew.83
LANG: JAVA
TASK: ride
*/

import java.io.*;

public class ride {

    public static void main(String[] args){
        
    	try{
    		ride r = new ride();
            PrintWriter out = new PrintWriter("ride.out");
            String rt = r.work("ride.in");
            out.println(rt);
            out.close();
        }catch(Exception e){
        	System.out.println(e.toString());
        }
    	        
    }
    
    public String work(String filename) throws Exception {
    	BufferedReader in = new BufferedReader(new FileReader(filename));
           
        String group;
        String comet;
        
        group = in.readLine();
        comet = in.readLine();
        
        int prodGroup = 1;
        int prodComet = 1;
        for (int i = 0; i < group.length(); i++) {
        	prodGroup *= (group.substring(i, i+1).compareTo("A") + 1);
        }
        
        for (int i = 0; i < comet.length(); i++) {
        	prodComet *= (comet.substring(i, i+1).compareTo("A") + 1);
        }
        
        if (prodGroup % 47 == prodComet %47) {
        	return "GO";
        } else {
        	return "STAY";
        }
       
    }
    
}
