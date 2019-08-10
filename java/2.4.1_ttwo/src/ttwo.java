/*
ID: andrew.83
LANG: JAVA
TASK: ttwo
*/

import java.io.*;
import java.util.*;

/**
 * BINGO BANGO BONGO
 */

/**
 * Tamworth Two (2.4.1)
 *
 * given a 10x10 grid and the initial location of a cow and a farmer as well as immovable obstacles
 *  the cow and the farmer have the same movement patterns and initial direction
 * return how long (if at all) it will take for the cow and the farmer to occupy the same space
 *
 * the cow and the farm move simultaneously and in the same pattern:
 *  a move takes 1 minute, how many moves until they occupy the same space
 *  at most, can move one coordinate point vertically or horizontally
 *  if encounter an obstacle and unable to proceed, rotate 90 clockwise (rotation counts as a turn, no movement)
 *
 * Ex:
 * *...*.....
 * ......*...
 * ...*...*..
 * ..........
 * ...*.F....
 * *.....*...
 * ...*......
 * ..C......*
 * ...*.*....
 * .*.*......
 * after 49 turns (minutes), F & C occupy the same space (row 6, col 2)
 *
 * First Successful Run
 *    Test 1: TEST OK [0.112 secs, 24380 KB]
 *    Test 2: TEST OK [0.091 secs, 23700 KB]
 *    Test 3: TEST OK [0.121 secs, 24776 KB]
 *    Test 4: TEST OK [0.126 secs, 24648 KB]
 *    Test 5: TEST OK [0.131 secs, 24644 KB]
 *    Test 6: TEST OK [0.114 secs, 24404 KB]
 *    Test 7: TEST OK [0.103 secs, 23704 KB]
 *    Test 8: TEST OK [0.112 secs, 24516 KB]
 *    Test 9: TEST OK [0.105 secs, 23940 KB]
 */

/**
 * stores three variables (coordinate row, col) and direction
 *  rotation is important for tracking
 */
class coordinate{

    int row;
    int col;
    int dir;

    public coordinate(int r, int c, int d){
        row = r;
        col = c;
        dir = d;
    }

    public coordinate(coordinate copy){
        row = copy.getRow();
        col = copy.getCol();
        dir = copy.getDirection();
    }

    //accessor methods
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int getDirection(){
        return dir;
    }

    //mutator method
    public void setDir(int d){
        dir = d;
    }

    /**
     * movement method
     * direction can be 0,1,2,3
     * 0 = 0 rotations (of 90 degrees), default north (vertical 12 o'clock)
     * 1 = 1 rotations, east (horizontal right 3 o'clock)
     * 2 = 2 rotations, south (vertical 6 o'clock)
     * 3 = 3 rotations, west (horizontal left 9 o'clock)
     * @param direction
     * @param grid
     * @param farmerOrCow meant to update grid for visual tracking/debugging (NOT NECESSARY)
     */
    public void move(int direction, int[][] grid, int farmerOrCow){
        dir = direction;
        switch(direction){
            case 0:
                row--;
                break;
            case 1:
                col++;
                break;
            case 2:
                row++;
                break;
            case 3:
                col--;
                break;
        }
        grid[row][col] = farmerOrCow;
    }

    /**
     * comparison of two coordinate points
     * NOTE: ONLY COMPARES LOCATION, NOT DIRECTION. COW AND FARMER HAVE TO SHARE SAME LOCATION, DON'T CARE ABOUT DIRECTION
     * @param other
     * @return
     */
    public boolean same(coordinate other){
        if((row == other.getRow())&&(col == other.getCol())){
            return true;
        }else{
            return false;
        }
    }

    //toString() method for identification
    public String toString(){
       return("row " + row + " col " + col + " direction " + dir);
    }

}

public class ttwo {

    //return variable
    static int numMinutes = 0;
    //tracks whether the cow or the farmer have fallen into a deadloop
    static boolean cowLoop = false;
    static boolean farmerLoop = false;

    public static void main(String[] args){

        try{
            ttwo t2 = new ttwo();
            //System.out.println(t2.work("ttwo.in"));
            PrintWriter out = new PrintWriter("ttwo.out");
            out.println(t2.work("ttwo.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public int work(String filename) throws Exception{

        BufferedReader in = new BufferedReader(new FileReader(filename));
        /**
         * grid stores the coordinate plane
         *  if grid[row][col] = 0, nothing there
         *  if grid[row][col] = -1, obstacle, cannot pass
         *  if grid[row][col] = 1, cows here
         *  if grid[row][col] = 2, farmer john here
         * coordinate points are initialized for farmer and cow starting positions (begin facing north)
         */
        int[][] grid = new int[10][10];

        /**
         * 3D boolean arrays
         * first 2 layers parallel the coordinate grid
         *  each coordinate point has a boolean[] of size 4 (0,1,2,3)
         *  when the coordinate point is visited, the direction is marked and recorded
         *  if cow moves into the same coordinate point twice and has same direction both times, it's a loop
         *  if farmer moves into the same coordinate point twice and has different direction, it may not be a loop
         */
        boolean[][][] farmerObstacles = new boolean[10][10][4];
        boolean[][][] cowObstacles = new boolean[10][10][4];

        coordinate farmerLocation = new coordinate(0,0, 0);
        coordinate cowLocation = new coordinate(0,0, 0);
        for(int row = 0; row < 10; row++){
            String tempLine = in.readLine();
            for(int col = 0; col < 10; col++){
                char tempChar = tempLine.charAt(col);
                switch(tempChar){
                    case '.':
                        grid[row][col] = 0;
                        break;
                    case '*':
                        grid[row][col] = -1;
                        break;
                    case 'C':
                        grid[row][col] = 1;
                        cowLocation = new coordinate(row,col, 0);
                        break;
                    case 'F':
                        grid[row][col] = 2;
                        farmerLocation = new coordinate(row, col, 0);
                        break;
                }
            }
        }

        /**
         * direction is important
         * 0 = north (up)
         * 1 = east (right)
         * 2 = south (down)
         * 3 = west (left)
         */
        int farmerDirection = 0;
        int cowDirection = 0;

        /**
         * if a loop exists, fLoop/cLoop store respective coordinates in a list
         * fLoopString/cLoopString used to determine the end of a loop
         *  since toString() returns direction, ends loop when the same string is found
         *  allows for back and forth, traversing same coordinate with different directions
         *  Ex:
         *      0,0 -> 0,1 -> 0,2 -> 0,3 -> 0,3 -> 0,3 -> 0,2 -> 0,1 -> 0,0 -> 0,0 coordinate points
         *       2      2      2      2      3      0      0      0      0      1  direction
         *      need to cross over points multiple times to form complete loop, direction changes used to differentiate
         */
        ArrayList<coordinate> fLoop = new ArrayList<>();
        HashSet<String> fLoopString = new HashSet<>();
        boolean fLoopDone = false;
        ArrayList<coordinate> cLoop = new ArrayList<>();
        HashSet<String> cLoopString = new HashSet<>();
        boolean cLoopDone = false;

        //run loop until the farmer loop and the cow loop are created
        //if simple case, no loops or only one loop, intersection is possible and loop breaks
        while(!fLoopDone || !cLoopDone){
            //simple case
            if(farmerLocation.same(cowLocation)){
                break;
            }
            //get the next direction of the cow using getNextDirection()
            int nextCow = getNextDirection(1, cowDirection, cowLocation, grid, cowObstacles);
            //if the direction is the same as before, the cow moves
            if(nextCow == cowDirection) {
                cowLocation.move(nextCow, grid, 1);
            }
            //if direction is different, the cow remains stationary but rotates
            else{
                cowLocation.setDir(nextCow);
                cowDirection = nextCow;
            }
                //DEBUG
                //System.out.println(cowLocation.toString() + " " + nextCow);
            //repeat cow steps for farmer
            int nextFarmer = getNextDirection(2, farmerDirection, farmerLocation, grid, farmerObstacles);
            if(nextFarmer == farmerDirection){
                farmerLocation.move(nextFarmer, grid, 2);
            }else{
                farmerLocation.setDir(nextFarmer);
                farmerDirection = nextFarmer;
            }

            /**
             * getNextDirection detects when a loop is confirmed
             *  will update static variables farmerLoop and/or cowLoop to true if loop is confirmed
             * if loop exists, add the coordinate to respective loop list
             *  repeat until HashSet fLoopString/cLoopString uses toString() and finds a repeat
             *  toString() records direction, same() only compares position
             */
            if(farmerLoop){
                if(!fLoopString.contains(farmerLocation.toString())){
                    fLoop.add(new coordinate(farmerLocation));
                    fLoopString.add(farmerLocation.toString());
                }else{
                    fLoopDone = true;
                }
            }
            if(cowLoop){
                if(!cLoopString.contains(cowLocation.toString())){
                    cLoop.add(new coordinate(cowLocation));
                    cLoopString.add(cowLocation.toString());
                }else{
                    cLoopDone = true;
                }
            }
            //increment numMinutes
            numMinutes++;
                //DEBUG
                //System.out.println(farmerLocation.toString() + " " + nextFarmer);
                //System.out.println("cowLoop " + cowLoop + " farmerLoop " + farmerLoop);
                //System.out.println(numMinutes);
        }

        //if there is one or less loops, then there is an intersection that stopped the while loop so return numMinutes
        if(!fLoopDone || !cLoopDone){
            return numMinutes;
        }

        //if both the cow and the farmer fall into a loop, calculate how long until they intersect
        //timeUntilLoop() method used to calculate this
        int addOn = timeUntilLoop(cLoop, fLoop, cowLocation, farmerLocation);
        //if the cow/farmer don't ever meet, return 0
        if(addOn == -1){
            return 0;
        }

        return numMinutes;
    }

    /**
     * given two lists containing all the coordinates of two deadloops, the two starting positions
     * return the amount of time it will take for the cow and the farmer to intersect
     *  if the two will never meet, return -1
     * @param cLoop
     * @param fLoop
     * @param curCow
     * @param curFarmer
     * @return
     */
    public int timeUntilLoop(ArrayList<coordinate> cLoop, ArrayList<coordinate> fLoop, coordinate curCow, coordinate curFarmer){
        //allIntersections stores all possible intersection point
        ArrayList<coordinate> allIntersections = new ArrayList<>();
        //allIntersectionTimes is parallel list, storing the time until cow and farmer both reach the intersection point
        ArrayList<Integer> allIntersectionTimes = new ArrayList<>();
        //cowIndex/farmIndex is parallel array for intersection point
        //how long from beginning of loop to the intersection (where intersection point is located in loop list)
        ArrayList<Integer> cowIndex = new ArrayList<>();
        ArrayList<Integer> farmIndex = new ArrayList<>();
        //cowStart/farmStart is the index of the initial location of farmer and cow in their respective loops
        int cowStart = -1;
        int farmStart = -1;

        int index = 0;
        /**
         * iterate through cow loop, find cowStart using index variable
         * if the coordinate also exists in farmer loop, add the coordinate to allIntersections
         * add index to cowIndex, recording the locaiton of the intersection in the cowLoop
         */
        for(coordinate cPoint : cLoop){
            if(cowStart == -1 && cPoint.same(curCow)){
                cowStart = index;
            }
            for(coordinate fPoint : fLoop) {
                if (cPoint.same(fPoint)) {
                    allIntersections.add(cPoint);
                    cowIndex.add(index);
                }
            }
            index++;
        }
        //rinse repeat for farmer loop
        index = 0;
        for(coordinate fPoint : fLoop){
            if(farmStart == -1 && fPoint.same(curFarmer)){
                farmStart = index;
            }
            for(coordinate inter : allIntersections) {
                if (fPoint.same(inter)) {
                    farmIndex.add(index);
                }
            }
            index++;
        }

        //if there are no intersections, no possible way to meet, return -1
        if(allIntersections.size() == 0){
            return -1;
        }
        //if there are intersections, check to see if possible to get to it
        else{
            //iterate through all intersection possibilities
            for(index = 0; index < allIntersections.size(); index++){
                /**
                 * cowPlus/farmPlus is time until cow/farmer reach the intersection point
                 * calculate by index of intersection point - index of starting point
                 *  if starting point comes after the intersection point, will be negative
                 *  calculate by adding negative number to size of loop to wrap around
                 */
                int cowPlus = cowIndex.get(index)-cowStart;
                if(cowPlus < 0){
                    cowPlus = cLoop.size()+cowPlus;
                }
                int farmPlus = farmIndex.get(index)-farmStart;
                if(farmPlus < 0){
                    farmPlus = fLoop.size()+farmPlus;
                }
                /**
                 * if the distance between the cow's location and the intersection is the same as the farmer's
                 * return one or the other, they're the same
                 * Ex:
                 * cowLoop : point 0,1,2,3,4,5 (start at point 1, point 4 is intersect with farmLoop)
                 * farmLoop : point 0,1,2,3,4,5,6,7,8 (start at point 8, point 2 is point 4 of cowLoop)
                 * in 3 minutes, cow and farmer will reach intersection point
                 */
                if(farmPlus == cowPlus){
                    return cowPlus; //are same
                }
                //if the time until cow and farmer reach intersection point is different and loops are the same size
                //never intersect
                if(fLoop.size() == cLoop.size()){
                    return -1;
                }else{
                    /**
                     * if loops are of different sizes
                     *  cowTimes stores all times that cow will hit intersection point
                     *  farmTimes stores all times that farmer will hit intersection point
                     * if there is a time that exists in both lists, add it to allIntersectionTimes
                     */
                    ArrayList<Integer> cowTimes = new ArrayList<>();
                    HashSet<Integer> farmTimes = new HashSet<>();
                    for(int c = 0; c < fLoop.size(); c++){
                        cowPlus += cLoop.size();
                        cowTimes.add(cowPlus);
                    }
                    for(int f = 0; f < cLoop.size(); f++){
                        farmPlus += fLoop.size();
                        farmTimes.add(farmPlus);
                    }
                    for(Integer time : cowTimes){
                        if(farmTimes.contains(time)){
                            allIntersectionTimes.add(time);
                        }
                    }
                }
            }
        }
        //if there are no intersection times, return -1
        if(allIntersectionTimes.isEmpty()){
            return -1;
        }
        //sort the list, return the lowest value
        else {
            Collections.sort(allIntersectionTimes);
            return allIntersectionTimes.get(0);
        }
    }

    /**
     * given a direction, an identifier, a coordinate, the respective grid, and a parallel array of obstacles
     * return the next direction to proceed from the given coordinate
     * if there is no obstacle
     *  continue forward, return initial direction
     * if there is an obstacle
     *  update direction (direction = (direction+1)%4)
     *  check if a dead loop has been confirmed
     *  update boolean loop as necessary
     *  return updated direction
     * @param farmerOrCow
     * @param direction
     * @param location
     * @param grid
     * @param obstacles
     * @return
     */
    public int getNextDirection(int farmerOrCow, int direction, coordinate location, int[][] grid, boolean[][][] obstacles){
        int row = location.getRow();
        int col = location.getCol();
        grid[row][col] = 0;
        /**
         * checking to see if the coordinate is on the edge
         * if so, mark the edge coordinate to have been visited
         *  if a loop is confirmed, update loop variables
         */
        if((row == 0 && direction == 0) || (row == 9 && direction == 2)) {
            if (!obstacles[row][col][direction]) {
                obstacles[row][col][direction] = true;
            } else {
                if (farmerOrCow == 1) {
                    cowLoop = true;
                } else {
                    farmerLoop = true;
                }
            }
            direction = (direction + 1) % 4;
        }else if((col == 0 && direction == 3) || (col == 9 && direction == 1)){
            if(!obstacles[row][col][direction]) {
                obstacles[row][col][direction] = true;
            }else{
                if (farmerOrCow == 1) {
                    cowLoop = true;
                } else {
                    farmerLoop = true;
                }
            }
            direction = (direction+1)%4;
        }
        //if the coordinate is located anywhere not on the edge
        else {
            switch (direction) {
                /**
                 * given direction as 0,1,2,3
                 * check the coordinate that will be moved into
                 *  if the next spot is occupied by an obstacle, return a different direction
                 *      check if the spot has been marked by the same direction, if so, update loop variable
                 *  if not occupied, return same direction
                 */
                case 0:
                    if (grid[row - 1][col] == -1) {
                        direction = (direction + 1) % 4;
                        if (!obstacles[row][col][direction]) {
                            obstacles[row][col][direction] = true;
                        } else {
                            if (farmerOrCow == 1) {
                                cowLoop = true;
                            } else {
                                farmerLoop = true;
                            }
                        }
                    }
                    break;
                case 1:
                    if (grid[row][col + 1] == -1) {
                        direction = (direction + 1) % 4;
                        if (!obstacles[row][col][direction]) {
                            obstacles[row][col][direction] = true;
                        } else {
                            if (farmerOrCow == 1) {
                                cowLoop = true;
                            } else {
                                farmerLoop = true;
                            }
                        }
                    }
                    break;
                case 2:
                    if (grid[row + 1][col] == -1) {
                        direction = (direction + 1) % 4;
                        if (!obstacles[row][col][direction]) {
                            obstacles[row][col][direction] = true;
                        } else {
                            if (farmerOrCow == 1) {
                                cowLoop = true;
                            } else {
                                farmerLoop = true;
                            }
                        }
                    }
                    break;
                case 3:
                    if (grid[row][col - 1] == -1) {
                        direction = (direction + 1) % 4;
                        if (!obstacles[row][col][direction]) {
                            obstacles[row][col][direction] = true;
                        } else {
                            if (farmerOrCow == 1) {
                                cowLoop = true;
                            } else {
                                farmerLoop = true;
                            }
                        }
                    }
                    break;
            }
        }
        return direction;
    }

}
