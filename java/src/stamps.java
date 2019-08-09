/*
ID: andrew.83
LANG: JAVA
TASK: stamps
*/

import java.io.*;
import java.util.*;

public class stamps {

    public static void main(String[] args){

        try{
            stamps s = new stamps();
            //System.out.println(s.work("stamps.in"));
            PrintWriter out = new PrintWriter("stamps.out");
            out.println(s.work("stamps.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename)throws Exception{
        int numSequential = 0;

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int maxCoins = Integer.parseInt(st.nextToken());
        int numCoins = Integer.parseInt(st.nextToken());
        ArrayList<Integer> coinList = new ArrayList<>();
        st = new StringTokenizer(in.readLine());
        boolean onePossible = false;
        for(int i = 1; i <= numCoins; i++){
            if(i%16 == 0){
                st = new StringTokenizer(in.readLine());
            }
            int coin = Integer.parseInt(st.nextToken());
            if(coin == 1){
                onePossible = true;
            }
            coinList.add(coin);
        }

        if(!onePossible){
            return numSequential;
        }else{
            Collections.sort(coinList);
            int maxVal = maxCoins * coinList.get(coinList.size()-1);
            int[][] sums = new int[numCoins][maxVal+1];
            int[] coinNum = new int[maxVal+1];
            Arrays.fill(coinNum, 201);
            coinNum[0] = 0;
            for(int one = 1; one <= maxCoins; one++){
                sums[0][one] = one;
                coinNum[one] = one;
            }
            for(int coin : coinList){
                int curMax = coin * maxCoins;
                if(coin != 1) {
                    for (int i = coin; i <= curMax; i++) {
                        int mult = i/coin;
                        int nCoins = Math.min(mult+coinNum[i-(coin*mult)], coinNum[i]);
                        coinNum[i] = nCoins;
                    }
                }
            }
            int val = 1;
            while(val <= maxVal && coinNum[val] <= maxCoins){
                val++;
            }
            numSequential = val-1;
        }

        return numSequential;
    }

}
