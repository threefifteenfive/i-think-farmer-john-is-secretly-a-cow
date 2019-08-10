/*
ID: andrew.83
LANG: JAVA
TASK: cowtour
*/

import java.io.*;
import java.util.*;
import java.time.*;

/**
 * Cow Tours (Usaco 2.4.3)
 *
 * First Successful Runs
 *    Test 1: TEST OK [0.112 secs, 24296 KB]
 *    Test 2: TEST OK [0.117 secs, 24580 KB]
 *    Test 3: TEST OK [0.107 secs, 24468 KB]
 *    Test 4: TEST OK [0.131 secs, 24676 KB]
 *    Test 5: TEST OK [0.728 secs, 29212 KB]
 *    Test 6: TEST OK [0.366 secs, 28780 KB]
 *    Test 7: TEST OK [0.422 secs, 29608 KB]
 *    Test 8: TEST OK [0.516 secs, 29420 KB]
 *    Test 9: TEST OK [0.408 secs, 28992 KB]
 */

class cowtour1 {
    private static int[][] adjacent;
    private static int[][] location;
    private static int[] visited;
    private static int N;

    public static void main(String[] args) throws Exception {
        work();
    }

    public static void work() throws Exception {
        int numTimesHypotUsed = 0;
        // set startTime to measure how long the program takes
        long startTime = System.currentTimeMillis();

        // create input BufferedReader from file
        BufferedReader br = new BufferedReader(new FileReader("cowtour.in"));

        N = Integer.parseInt(br.readLine());


        location = new int[N][2];
        for (int i = 0; i < N; i++) {
            StringTokenizer line = new StringTokenizer(br.readLine());
            location[i][0] = Integer.parseInt(line.nextToken());
            location[i][1] = Integer.parseInt(line.nextToken());
        }

        adjacent = new int[N][N];

        for (int i = 0; i < N; i++) {
            char[] lineArray = br.readLine().toCharArray();
            for (int j = 0; j < N; j++) {
                adjacent[i][j] = lineArray[j]-'0';
            }
        }

        br.close();

        visited = new int[N];
        Arrays.fill(visited, -1);

        for (int i = 0; i < N; i++) {
            solve(i, i);
        }

        double[][] adjacencyMatrix = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i==j) {
                    adjacencyMatrix[i][j]=0;
                } else if (adjacent[i][j]==1) {
                    adjacencyMatrix[i][j]=Math.hypot(location[j][0]-location[i][0], location[j][1]-location[i][1]);
                    numTimesHypotUsed++;
                } else {
                    adjacencyMatrix[i][j]=Double.MAX_VALUE;
                }
            }
        }

        adjacencyMatrix=floyd(adjacencyMatrix);

        double min = Double.MAX_VALUE;

        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                if (visited[i]==visited[j]) {
                    continue;
                }
                double distance = Math.hypot(location[j][0]-location[i][0], location[j][1]-location[i][1]);
                numTimesHypotUsed++;
                double max = 0;
                inner: for (int k = 0; k < N; k++) {
                    if (visited[k]!=visited[i]&&visited[k]!=visited[j]) {
                        continue;
                    }
                    for (int m = k+1; m < N; m++) {
                        if (visited[m]!=visited[i]&&visited[m]!=visited[j] || adjacencyMatrix[k][m] < max) {
                            continue;
                        }
                        if (visited[i]==visited[k]) {
                            max = Math.max(max, Math.min(adjacencyMatrix[k][m], distance+adjacencyMatrix[k][i]+adjacencyMatrix[j][m]));
                        } else {
                            max = Math.max(max, Math.min(adjacencyMatrix[k][m], distance+adjacencyMatrix[k][j]+adjacencyMatrix[i][m]));
                        }
                        if (max > min) {
                            break inner;
                        }
                    }
                }
                min = Math.min(min, max);
            }
        }

        String result = String.format("%.6f", min);
        while (result.length()-result.indexOf('.')<6) {
            result+="0";
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cowtour.out")));
        out.println(result);
        out.close();
        // print final time taken
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(numTimesHypotUsed);
    }
    private static void solve (int current, int number) {
        if (visited[current]!=-1) {
            return; //already visited
        }
        visited[current]=number;
        for (int i = 0; i < N; i++) {
            if (adjacent[current][i]==1) {
                solve(i, number); // try every connected one
            }
        }
    }
    public static double[][] floyd (double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    matrix[j][k]=Math.min(matrix[j][k], matrix[j][i]+matrix[i][k]);
                }
            }
        }
        return matrix;
    }
}

public class cowtour {

    final static int MAX = Integer.MAX_VALUE;

    public static void main(String[] args){

        try{
            {
                Instant wtf1 = Instant.now();
                cowtour ct = new cowtour();
                System.out.println(ct.work("cowtour.in"));
                PrintWriter out = new PrintWriter("cowtour.out");
                String answer = ct.work("cowtour.in");
                out.println(answer);
                out.close();
                Instant wtf2 = Instant.now();
                Duration wtf = Duration.between(wtf1, wtf2);
                System.out.println("wtf " + wtf.toMillis());
            }
            {
                cowtour1.work();
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename) throws Exception{
        String diameter = "";
        int numTimesCalculateDistance = 0;

        Instant t1s = Instant.now();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numPasture = Integer.parseInt(st.nextToken());
        int[][] pastureLocations = new int[numPasture][2];
        for(int i = 0; i < numPasture; i++){
            st = new StringTokenizer(in.readLine());
            pastureLocations[i][0] = Integer.parseInt(st.nextToken());
            pastureLocations[i][1] = Integer.parseInt(st.nextToken());
        }
        /**
         * initialize the adjacency matrix to contain distances to all other points
         *  if direct connection, figure out the weight (distance to other point)
         *  if self, set to 0 (min distance possible)
         *  if unconnected directly, initialized to MAX
         * set up for running Floyd-Warshall algo to find min distance from each point to all connected points
         *  finds all distances to other points in ONE field
         *  makes it easy to find diameters, only need to run FW algo once
         */
        double[][] adjacencyMatrix = new double[numPasture][numPasture];
        for(int row = 0; row < numPasture; row++){
            String tempLine = in.readLine();
            for(int col = 0; col < numPasture; col++){
                if(row != col && tempLine.charAt(col)-'0' == 1){
                    adjacencyMatrix[row][col] = calculateDistance(row, col, pastureLocations);
                    numTimesCalculateDistance++;
                }else if(row == col){
                    adjacencyMatrix[row][col] = 0;
                }else{
                    adjacencyMatrix[row][col] = MAX;
                }
            }
        }
        floydWarshall(adjacencyMatrix);

        /**
         * dividing pastures into separate fields
         *  fieldList contains all fields (list of pastures)
         *  fieldID ties pasture ID to its field ID (pasture ID's are from 0 to #pastures)
         */
        ArrayList<ArrayList<Integer>> fieldList = new ArrayList<>();
        int[] fieldID = new int[numPasture];
        Arrays.fill(fieldID, -1);
        int numVisited = 0;
        int ID = 0;
        while(numVisited != numPasture){
            int pasture = -1;
            for(int i = 0; i < numPasture; i++){
                if(fieldID[i] == -1){
                    pasture = i;
                    break;
                }
            }
            if(pasture < 0){
                break;
            }
            LinkedList<Integer> queue = new LinkedList<>();
            queue.add(pasture);
            ArrayList<Integer> tempList = new ArrayList<>();
            while(pasture < numPasture && !queue.isEmpty()) {
                int p = queue.removeFirst();
                if(fieldID[p] == -1){
                    tempList.add(p);
                    fieldID[p] = ID;
                    numVisited++;
                    for (int connect = 0; connect < numPasture; connect++) {
                        if(fieldID[connect] == -1 && adjacencyMatrix[p][connect] != MAX){
                            queue.add(connect);
                        }
                    }
                }
            }
            fieldList.add(tempList);
            ID++;
        }
        Instant t1e = Instant.now();
        Duration d1 = Duration.between(t1s, t1e);
        System.out.println("d1 " + d1.toMillis());

        //PART TWO

        Instant t2s = Instant.now();
        double shortestDiameter = MAX;

        //iterate through all rows (one pasture)
        for(int row = 0; row < numPasture; row++){
            //iterate through all columns (pasture to connect to)
            for(int col = row+1; col < numPasture; col++){
                //if the pastures are from the same field, don't care
                //
                if(fieldID[row] != fieldID[col]){
                    double connection = calculateDistance(row, col, pastureLocations);
                    double tempMax = 0;
                    inner: for (int k = 0; k < numPasture; k++) {
                        if (fieldID[k]!=fieldID[row]&&fieldID[k]!=fieldID[col]) {
                            continue;
                        }
                        for (int m = k+1; m < numPasture; m++) {
                            if (fieldID[m]!=fieldID[row]&&fieldID[m]!=fieldID[col] || adjacencyMatrix[k][m] < tempMax) {
                                continue;
                            }
                            if (fieldID[row]==fieldID[k]) {
                                tempMax = Math.max(tempMax, Math.min(adjacencyMatrix[k][m], connection+adjacencyMatrix[k][row]+adjacencyMatrix[col][m]));
                            } else {
                                tempMax = Math.max(tempMax, Math.min(adjacencyMatrix[k][m], connection+adjacencyMatrix[k][col]+adjacencyMatrix[row][m]));
                            }
                            if (tempMax > shortestDiameter) {
                                break inner;
                            }
                        }
                    }
                    shortestDiameter = Math.min(shortestDiameter, tempMax);
                }
            }
        }

        if(shortestDiameter == MAX){
            shortestDiameter = getDiameter(adjacencyMatrix, fieldList.get(0));
        }

        diameter = String.format("%.6f", shortestDiameter);
        Instant t2e = Instant.now();
        Duration d2 = Duration.between(t2s, t2e);
        System.out.println("d2 " + d2.toMillis());
        System.out.println("times calcDist used :" + numTimesCalculateDistance);
        return diameter;
    }

    public double calculateDistance(int row, int col, int[][] pastureLocations){
        return Math.hypot(pastureLocations[col][1] - pastureLocations[row][1], pastureLocations[col][0] - pastureLocations[row][0]);
        /*
        int y = Math.abs(pastureLocations[col][1] - pastureLocations[row][1]);
        int x = Math.abs(pastureLocations[col][0] - pastureLocations[row][0]);
        return Math.sqrt(x*x + y*y);

         */
    }

    public void floydWarshall(double[][] adjacencyMatrix){
        int numPoints = adjacencyMatrix.length;
        for(int k = 0; k < numPoints; k++){
            for(int i = 0; i < numPoints; i++){
                for(int j = 0; j < numPoints; j++){
                    if(i != j && adjacencyMatrix[i][k] + adjacencyMatrix[k][j] < adjacencyMatrix[i][j]){
                        adjacencyMatrix[i][j] = adjacencyMatrix[i][k] + adjacencyMatrix[k][j];
                    }
                }
            }
        }
    }

    public double getDiameter(double[][] adjacencyMatrix, ArrayList<Integer> allPoints){
        double largestDistance = 0;
        int numPoints = allPoints.size();
        double[][] distance = new double[numPoints][numPoints];
        for(int row = 0; row < numPoints; row++){
            int r = allPoints.get(row);
            for(int col = 0; col < numPoints; col++){
                int c = allPoints.get(col);
                if(adjacencyMatrix[r][c] == 0){
                    distance[row][col] = MAX;
                }else{
                    distance[row][col] = adjacencyMatrix[r][c];
                }
            }
        }
        for(int k = 0; k < numPoints; k++){
            for(int i = 0; i < numPoints; i++){
                for(int j = 0; j < numPoints; j++){
                    if(i != j && distance[i][k] + distance[k][j] < distance[i][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
        for(int row = 0; row < numPoints; row++){
            for(int col = 0; col < numPoints; col++){
                if(row != col && distance[row][col] != MAX) {
                    largestDistance = Math.max(largestDistance, distance[row][col]);
                }
            }
        }
        return largestDistance;
    }

    public String workOriginal(String filename) throws Exception{
        String diameter = "";
        int numTimesCalculateDistance = 0;

        Instant t1s = Instant.now();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int numPasture = Integer.parseInt(st.nextToken());
        int[][] pastureLocations = new int[numPasture][2];
        for(int i = 0; i < numPasture; i++){
            st = new StringTokenizer(in.readLine());
            pastureLocations[i][0] = Integer.parseInt(st.nextToken());
            pastureLocations[i][1] = Integer.parseInt(st.nextToken());
        }
        double[][] adjacencyMatrix = new double[numPasture][numPasture];
        for(int row = 0; row < numPasture; row++){
            String tempLine = in.readLine();
            for(int col = 0; col < numPasture; col++){
                if(row != col && tempLine.charAt(col)-'0' == 1){
                    adjacencyMatrix[row][col] = calculateDistance(row, col, pastureLocations);
                    numTimesCalculateDistance++;
                }
            }
        }

        ArrayList<ArrayList<Integer>> fieldList = new ArrayList<>();
        int[] fieldID = new int[numPasture];
        Arrays.fill(fieldID, -1);
        int numVisited = 0;
        int ID = 0;
        while(numVisited != numPasture){
            int pasture = -1;
            for(int i = 0; i < numPasture; i++){
                if(fieldID[i] == -1){
                    pasture = i;
                    break;
                }
            }
            if(pasture < 0){
                break;
            }
            LinkedList<Integer> queue = new LinkedList<>();
            queue.add(pasture);
            ArrayList<Integer> tempList = new ArrayList<>();
            while(pasture < numPasture && !queue.isEmpty()) {
                int p = queue.removeFirst();
                if(fieldID[p] == -1){
                    tempList.add(p);
                    fieldID[p] = ID;
                    numVisited++;
                    for (int connect = 0; connect < numPasture; connect++) {
                        if(fieldID[connect] == -1 && adjacencyMatrix[p][connect] > 0){
                            queue.add(connect);
                        }
                    }
                }
            }
            fieldList.add(tempList);
            ID++;
        }
        Instant t1e = Instant.now();
        Duration d1 = Duration.between(t1s, t1e);
        System.out.println("d1 " + d1.toMillis());

        Instant t2s = Instant.now();
        double shortestDiameter = MAX;
        long total_dr_cnt = 0;
        long total_dr_tmp = 0;
        for(int field = 0; field < ID; field++){
            ArrayList<Integer> curField = fieldList.get(field);
            for(int connect = field+1; connect<ID; connect++){
                ArrayList<Integer> connectField = fieldList.get(connect);
                ArrayList<Double> connection = new ArrayList<>();
                double minDist = MAX;
                for(int fIndex = 0; fIndex < curField.size(); fIndex++){
                    int fPasture = curField.get(fIndex);
                    for(int cIndex = 0; cIndex < connectField.size(); cIndex++){
                        double tempDist = calculateDistance(fPasture, connectField.get(cIndex), pastureLocations);
                        numTimesCalculateDistance++;
                        if(tempDist < minDist){
                            connection.add((new Integer(fPasture).doubleValue()));
                            connection.add(connectField.get(cIndex).doubleValue());
                            connection.add(tempDist);
                            minDist = tempDist;
                        }else if(tempDist == minDist){
                            connection.add((new Integer(fPasture).doubleValue()));
                            connection.add(connectField.get(cIndex).doubleValue());
                            connection.add(tempDist);
                        }
                    }
                }
                ArrayList<Integer> combineFields = new ArrayList<>();
                combineFields.addAll(curField);
                combineFields.addAll(connectField);
                double fPoint;
                double cPoint;
                for(int c = 0; c < connection.size(); c+=3) {
                    if(connection.get(c+2) == minDist) {
                        fPoint = connection.get(c);
                        cPoint = connection.get(c + 1);
                        adjacencyMatrix[(int) Math.round(fPoint)][(int) Math.round(cPoint)] = connection.get(c + 2);
                        Instant tmp1 = Instant.now();
                        double combineDiameter = getDiameter(adjacencyMatrix, combineFields);
                        Instant tmp2 = Instant.now();
                        Duration d_tmp = Duration.between(tmp1, tmp2);
                        total_dr_cnt += 1;
                        total_dr_tmp += d_tmp.toMillis();

                        shortestDiameter = Math.min(shortestDiameter, combineDiameter);
                        adjacencyMatrix[(int) Math.round(fPoint)][(int) Math.round(cPoint)] = 0;
                    }
                }
            }
        }

        System.out.println("total_dr_cnt=" + total_dr_cnt + ", total_ms=" + total_dr_tmp);

        if(shortestDiameter == MAX){
            shortestDiameter = getDiameter(adjacencyMatrix, fieldList.get(0));
        }

        diameter = String.format("%.6f", shortestDiameter);
        Instant t2e = Instant.now();
        Duration d2 = Duration.between(t2s, t2e);
        System.out.println("d2 " + d2.toMillis());
        System.out.println("times calcDist used :" + numTimesCalculateDistance);
        return diameter;
    }

}


