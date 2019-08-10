/*
ID: andrew.83
LANG: JAVA
TASK: lamps
*/

import java.io.*;
import java.util.*;

/**
 *
 */
public class lamps {

    public static void main(String[] args) {

        try{
            lamps l = new lamps();
            System.out.print(l.work("lamps.in"));
            PrintWriter out = new PrintWriter("lamps.out");
            out.print(l.work("lamps.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String allCombos = "";

        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int N = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(in.readLine());
        int C = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(in.readLine());
        ArrayList<Integer> mustBeOn = new ArrayList<>();
        int temp = Integer.parseInt(st.nextToken());
        while(temp != -1){
            mustBeOn.add(temp);
            temp = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(in.readLine());
        ArrayList<Integer> mustBeOff = new ArrayList<>();
        temp = Integer.parseInt(st.nextToken());
        while(temp != -1){
            mustBeOff.add(temp);
            temp = Integer.parseInt(st.nextToken());
        }

        boolean[] startCombo = new boolean[N];
        Arrays.fill(startCombo, true);
        HashSet<String> possibleCombo = new HashSet<>();
        int comboLength = Math.min(4, C);
        if(comboLength == 0){
            possibleCombo.add(getComboString(startCombo));
        }
        LinkedList<boolean[]> queue = new LinkedList<>();
        queue.add(startCombo);
        for(int cL = 1; cL <= comboLength; cL++){
            ArrayList<boolean[]> nextComboList = new ArrayList<>();
            while(!queue.isEmpty()){
                boolean[] tempCombo = queue.removeFirst();
                for(int button = 1; button <= 4; button++){
                    boolean[] nextCombo = Arrays.copyOf(tempCombo,N);
                    switch(button){
                        case 1:
                            for(int i = 0; i < N; i++){
                                nextCombo[i] = !nextCombo[i];
                            }
                            break;
                        case 2:
                            for(int i = 0; i < N; i += 2){
                                nextCombo[i] = !nextCombo[i];
                            }
                            break;
                        case 3:
                            for(int i = 1; i < N; i += 2){
                                nextCombo[i] = !nextCombo[i];
                            }
                            break;
                        case 4:
                            for(int i =0; i < N; i += 3){
                                nextCombo[i] = !nextCombo[i];
                            }
                            break;
                    }
                    nextComboList.add(Arrays.copyOf(nextCombo,N));
                    possibleCombo.add(getComboString(nextCombo));
                }
            }
            queue.addAll(nextComboList);
        }

        ArrayList<String> sortList = new ArrayList<>();
        for(String possCombo : possibleCombo){
            boolean goodCombo = true;
            for(Integer onIndex : mustBeOn){
                if(possCombo.charAt(onIndex-1) != '1'){
                    goodCombo = false;
                }
            }
            for(Integer offIndex : mustBeOff){
                if(possCombo.charAt(offIndex-1) != '0'){
                    goodCombo = false;
                }
            }
            if(goodCombo){
                sortList.add(possCombo);
            }
        }

        Collections.sort(sortList);
        for(String goodCombo : sortList){
            allCombos += goodCombo.toString() + "\n";
        }

        if(allCombos.length() == 0){
            allCombos = "IMPOSSIBLE\n";
        }

        return allCombos;
    }

    public String getComboString(boolean[] combo){
        String rtCombo = "";
        for(boolean light : combo){
            if(light){
                rtCombo += 1;
            }else{
                rtCombo += 0;
            }
        }
        return rtCombo;
    }

}
