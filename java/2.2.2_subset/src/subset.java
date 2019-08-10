/*
ID: andrew.83
LANG: JAVA
TASK: subset
*/

import java.util.*;
import java.io.*;

public class subset {

    public static void main(String[] args){

        try{
            subset s = new subset();
            System.out.println(s.work("subset.in"));
            PrintWriter out = new PrintWriter("subset.out");
            out.println(s.work("subset.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public long work(String filename)throws Exception {
        int numSets = 0;

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());

        long[] holder = new long[1080];
        holder[0] = 1;
        long[] holderTemp = Arrays.copyOf(holder, holder.length);

        int N = Integer.parseInt(st.nextToken());
        int total = N * (N + 1) / 2;

        if(total % 2 != 0) {
            return 0;
        }
        else{

            total /= 2;
            int temp = 0;

            for(int i = 1; i <= N; i++){

                for(int j = 0; j <= temp; j++){

                    holder[j + i] += holderTemp[j];

                }

                holderTemp = Arrays.copyOf(holder, holder.length);
                temp = i * (i + 1) / 2;

            }

            return (holder[total]/2);

        }

        /*
        int highNum = Integer.parseInt(st.nextToken());
        int totalSum = ((highNum + 1) * (highNum / 2)) + (highNum + 1) / 2;
        if ((totalSum & 1) == 1) {
            return 0;
        }

        long start = 0;
        int startHalf = totalSum/2;
        int n = highNum;
        while(startHalf > 0){
            if(startHalf < n){
                start = start | (1 << startHalf);
                startHalf = 0;
            }else{
                start = start | (1 << n);
                startHalf -= n;
                n--;
            }
        }

        HashSet<Long> usedCombos = new HashSet<>();
        HashSet<Long> opposites = new HashSet<>();
        LinkedList<Long> queue = new LinkedList<>();
        queue.add(start);
        while(!queue.isEmpty()){
            long tempCombo = queue.removeFirst();
            if(!opposites.contains(tempCombo)){
                usedCombos.add(tempCombo);
            }
            queue.addAll(decompose(tempCombo, highNum, opposites));
            numSets++;
        }

        numSets = usedCombos.size();
        return numSets;

         */

    }

    public ArrayList<Long> decompose(long combo, int maxNum, HashSet<Long> opposites){
        ArrayList<Long> newCombo = new ArrayList<>();
        int largest = maxNum;
        while(largest > 0){
            if((((combo >>> largest) & 1) != 0)) {
                for(int nMinus = largest-1; nMinus > largest/2; nMinus--){
                    if((((combo >>> nMinus) & 1) == 0) && (((combo >>> largest-nMinus) & 1) == 0)){
                        long tempCombo = combo;
                        tempCombo &= ~(1<<largest);
                        tempCombo |= (1<<nMinus);
                        tempCombo |= (1<<(largest-nMinus));
                        newCombo.add(tempCombo);
                        long opposite = (long)(Math.pow(2,maxNum+1)-2-tempCombo);
                        opposites.add(opposite);
                    }
                }
            }
            largest--;
        }

        return newCombo;
    }

    public String translate(long combo, int maxNum){
        boolean outOfOrder = false;
        int prev = maxNum;
        String rt = "";
        int largest = maxNum;
        int sum = 0;
        while(largest > 0){
            if((((combo >>> largest) & 1) != 0)){
                rt += largest + " ";
                sum += largest;
            }
            largest--;
        }
        rt += sum;
        return rt;
    }

}
