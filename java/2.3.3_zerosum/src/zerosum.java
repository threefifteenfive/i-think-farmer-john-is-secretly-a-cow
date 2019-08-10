/*
ID: andrew.83
LANG: JAVA
TASK: zerosum
*/

import java.io.*;
import java.util.*;

/**
 * Zero Sum (2.3.3)
 *
 * First successful test run
 *    Test 1: TEST OK [0.093 secs, 24056 KB]
 *    Test 2: TEST OK [0.098 secs, 23604 KB]
 *    Test 3: TEST OK [0.096 secs, 23444 KB]
 *    Test 4: TEST OK [0.096 secs, 23920 KB]
 *    Test 5: TEST OK [0.105 secs, 23860 KB]
 *    Test 6: TEST OK [0.110 secs, 23988 KB]
 *    Test 7: TEST OK [0.133 secs, 25520 KB]
 */
public class zerosum {

    final char[] OPERATORS = {'+', '-', ' '};
    final int RADIX = 10;

    public static void main(String[] args){

        try{
            zerosum zs = new zerosum();
            //System.out.println(zs.work("zerosum.in"));
            PrintWriter out = new PrintWriter("zerosum.out");
            out.print(zs.work("zerosum.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception {
        String allZeroSums = "";

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int N = Integer.parseInt(st.nextToken());

        LinkedList<char[]> queue = new LinkedList<>();
        ArrayList<char[]> holder = new ArrayList<>();
        int length = N+N-1;
        char[][] allCombos = new char[(int)Math.pow(3,N-1)][length];
        int acIndex = 0;
        char[] start = new char[length];
        queue.add(start);
        for(int i = 1; i <= N; i++){
            holder.clear();
            while(!queue.isEmpty()){
                char[] temp = queue.removeFirst();
                int index = i+i-2;
                temp[index] = Character.forDigit(i, RADIX);
                if(index+1 < length) {
                    for (int operator = 0; operator < 3; operator++) {
                        char[] tempCopy = Arrays.copyOf(temp, length);
                        tempCopy[index + 1] = OPERATORS[operator];
                        holder.add(tempCopy);
                    }
                }else{
                    allCombos[acIndex] = temp;
                    acIndex++;
                }
            }
            queue.addAll(holder);
        }

        //char[] test = {'1',' ','2','+','3','-','4','-','5','-','6'};
        //char[] test = {'1','-','2',' ','3','+','4','+','5','+','6','+','7'};
        //int test3 = process(test,1, 11);

        ArrayList<String> goodCombos = new ArrayList<>();
        for(char[] combo : allCombos){
            int sum = process(combo, 1, length);
            if(sum == 0){
                goodCombos.add(getString(combo));
            }
        }

        Collections.sort(goodCombos);
        for(String gCombo : goodCombos){
            allZeroSums += gCombo + "\n";
        }

        return allZeroSums;
    }

    public String getString(char[] combo){
        StringBuilder sb = new StringBuilder();
        for(char ch : combo){
            String s = Character.toString(ch);
            sb.append(s);
        }
        return sb.toString();
    }

    public int process(char[] combo, int sum, int length){
        int index = 1;
        if(combo[index] == ' '){
            int tempSum = sum;
            while(index < length && combo[index] == ' '){
                tempSum *= 10;
                tempSum += Character.digit(combo[index+1], RADIX);
                index += 2;
            }
            sum = tempSum;
        }
        for(int i = index; i < length; i+=2){
            char operator = combo[i];
            switch(operator){
                case '+':
                    sum += Character.digit(combo[i+1], RADIX);
                    break;
                case '-':
                    sum -= Character.digit(combo[i+1], RADIX);
                    break;
                case ' ':
                    int tempSum = Character.digit(combo[i-1], RADIX);
                    boolean plus = true;
                    if(combo[i-2] == '+') {
                        sum -= Character.digit(combo[i - 1], RADIX);
                    }else{
                        sum += Character.digit(combo[i - 1], RADIX);
                        plus = false;
                    }
                    while(i < length && combo[i] == ' '){
                        tempSum *= 10;
                        tempSum += Character.digit(combo[i+1], RADIX);
                        i += 2;
                    }
                    i -= 2;
                    if(!plus){
                        tempSum *= -1;
                    }
                    sum += tempSum;
                    break;
            }
        }
        return sum;
    }

}
