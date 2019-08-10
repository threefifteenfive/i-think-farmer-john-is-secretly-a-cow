//Revised version
import java.util.*;
import java.io.*;

class Segment{
    private int wAfter;
    private int length;

    public Segment(int l, int w){
        length = l;
        wAfter = w;
    }

    public int getLength(){
        return length;
    }

    public int getWAfter(){
        return wAfter;
    }

}

public class beads {

    public static void main(String[] args){

        try{
            beads b = new beads();
            System.out.println(b.work("beads.in"));
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename)throws Exception{
        int maxResult = 0;

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numBeads = Integer.parseInt(st.nextToken());
        String ogBeadLine = in.readLine();
        String beadLine = ogBeadLine + ogBeadLine;

        ArrayList<Segment> segments = new ArrayList<>();
        int index = 0;
        while(beadLine.charAt(index) == 'w'){
            beadLine = beadLine.substring(1);
        }
        while(index < beadLine.length()) {
            if(index < beadLine.length() && beadLine.charAt(index) == 'b'){
                int segSum = 0;
                int wLen = 0;
                while(index < beadLine.length() && beadLine.charAt(index) != 'r'){
                    if(index < beadLine.length() && beadLine.charAt(index) == 'w'){
                        wLen++;
                    }else{
                        wLen = 0;
                    }
                    index++;
                    segSum++;
                }
                segments.add(new Segment(segSum, wLen));
            }else if(index < beadLine.length() && beadLine.charAt(index) == 'r'){
                int segSum = 0;
                int wLen = 0;
                while(index < beadLine.length() && beadLine.charAt(index) != 'b'){
                    if(index < beadLine.length() && beadLine.charAt(index) == 'w'){
                        wLen++;
                    }else{
                        wLen = 0;
                    }
                    index++;
                    segSum++;
                }
                segments.add(new Segment(segSum, wLen));
            }
        }

        for(int i = 0; i < segments.size()-1; i++){
            int sumOne =  segments.get(i).getLength();
            if(i > 0){
                sumOne += segments.get(i-1).getWAfter();
            }
            int sumTwo = segments.get(i+1).getLength();
            maxResult = Math.max(maxResult, sumOne + sumTwo);
        }

        if(maxResult == 0){
            maxResult = ogBeadLine.length();
        }

        return maxResult;
    }

}
