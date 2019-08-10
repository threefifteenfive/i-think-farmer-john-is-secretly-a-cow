/*
ID: andrew.83
LANG: JAVA
TASK: castle
*/

import java.util.*;
import java.io.*;
import java.time.*;

/**
 * The Castle (2.1.1)
 * given a series of numbers arranged in a 2D array such that each number represents a unit in a rectangular building
 * the number representing a unit details the location of any walls on the unit
 *  1: wall to the west
 *  2: wall to the north
 *  4: wall to the east
 *  8: wall to the south
 * Ex:
 * 7 4 (# of columns, # of rows)
 * 11 6 11 6 3 10 6
 * 7 9 6 13 5 15 5
 * 1 10 12 7 13 7 5
 * 13 11 10 8 10 12 13
 *
 * OVERALL SCHEMATIC
 *      1   2   3   4   5   6   7
 *    #############################
 *  1 #   |   #   |   #   |   |   #
 *    #####---#####---#---#####---#
 *  2 #   #   |   #   #   #   #   #
 *    #---#####---#####---#####---#
 *  3 #   |   |   #   #   #   #   #
 *    #---#########---#####---#---#
 *  4 # ->#   |   |   |   |   #   #
 *    #############################
 *
 * #  = Wall     -,|  = No wall
 * -> = Points to the wall to remove to
 *      make the largest possible new room
 *
 * INTERPRETATION OF DATA GIVEN
 *      1    2    3    4    5    6    7
 *    ####|####|####|####|####|####|#####
 *  1 #   |   #|#   |   #|#   |    |    #
 *    ####|   #|####|   #|#   |####|    #
 *   -----|----|----|----|----|----|-----
 *    ####|#   |####|#  #|#  #|####|#   #
 *  2 #  #|#   |   #|#  #|#  #|#  #|#   #
 *    #  #|####|   #|####|#  #|####|#   #
 *   -----|----|----|----|----|----|-----
 *    #   |####|   #|####|#  #|####|#   #
 *  3 #   |    |   #|#  #|#  #|#  #|#   #
 *    #   |####|####|#  #|####|#  #|#   #
 *   -----|----|----|----|----|----|-----
 *    #  #|####|####|    |####|   #|#   #
 *  4 #  #|#   |    |    |    |   #|#   #
 *    ####|####|####|####|####|####|#####
 *
 * return the following:
 *  the number of rooms in the building (a room is completely isolated from the other by walls and entirely enclosed)
 *  the largest room size (the max number of units in a single room)
 *  the largest combined room size (by breaking 1 wall, what is the largest room size that can be created)
 *  the location of the wall to break in order to create the largest room
 *
 *  Original Version using only Unit class and parallel arrays
 *    Test 1: TEST OK [0.098 secs, 23716 KB]
 *    Test 2: TEST OK [0.156 secs, 23844 KB]
 *    Test 3: TEST OK [0.096 secs, 23628 KB]
 *    Test 4: TEST OK [0.107 secs, 24072 KB]
 *    Test 5: TEST OK [0.103 secs, 23784 KB]
 *    Test 6: TEST OK [0.135 secs, 25048 KB]
 *    Test 7: TEST OK [0.203 secs, 26060 KB]
 *    Test 8: TEST OK [0.189 secs, 26776 KB]
 *
 * Used for practice with comparator class and sorting
 * because comparing and prioritizing based on many variables, such as size, proximity to left, proximity to right
 *  inline comparator is too complicated and not very readable
 *  create a custom comparator and a class to hold necessary data for connections
 *  Revised Version with comparator and connection class
 *    Test 1: TEST OK [0.098 secs, 23760 KB]
 *    Test 2: TEST OK [0.208 secs, 24000 KB]
 *    Test 3: TEST OK [0.100 secs, 23736 KB]
 *    Test 4: TEST OK [0.114 secs, 23948 KB]
 *    Test 5: TEST OK [0.112 secs, 23716 KB]
 *    Test 6: TEST OK [0.142 secs, 24876 KB]
 *    Test 7: TEST OK [0.238 secs, 26572 KB]
 *    Test 8: TEST OK [0.201 secs, 26440 KB]
 */

/**
 * Unit functions as a coordinate square in the grid and counts as a single space in a room
 * ints row & col represent the location on the grid
 * int room is the ID of the room in the castle (the index of the ArrayList<Unit> (room) in the ArrayList<ArrayList<Unit>> (castle))
 */
class Unit{

    private int row;
    private int col;
    private int room;
    private int roomSize;

    public Unit(int r, int c){
        row = r;
        col = c;
    }

    public void setRoom(int roomID){
        room = roomID;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getRoom(){
        return room;
    }

    public void setRoomSize(int size){
        roomSize = size;
    }

    public int getRoomSize(){
        return roomSize;
    }

}

/**
 * creates a connection between two units
 * tracks direction, and which units are connected
 * easy toString() for output
 */
class Connection{

    private Unit from;
    private Unit to;
    private int direction;

    public Connection(int d, Unit f, Unit t){
        from = f;
        to = t;
        direction = d;
    }

    public int getCombinedSize(){
        return from.getRoomSize() + to.getRoomSize();
    }

    public String toString(){
        String tag = " N";
        if(direction == 4){
            tag = " E";
        }
        return getCombinedSize() + "\n" + (from.getRow()+1) + " " + (from.getCol()+1) + tag;
    }

    public Unit getFrom(){
        return from;
    }

    public int getDirection(){
        return direction;
    }

}

/**
 * used to sort an arrayList of Connection objects
 * prioritize size of room, then the proximity to the left, then the proximity to the south
 * Ex:
 *  4 1 N vs. 5 4 E (both are size 17)
 *  1 < 4 (closer to the left)
 *  4 1 N is sorted higher than 5 4 E
 */
class SortConnection implements Comparator<Connection>{

    public int compare(Connection a, Connection b){
        //priority is first on size
        int aSize = a.getCombinedSize();
        int bSize = b.getCombinedSize();
        if(aSize != bSize){
            return aSize  - bSize;
        }

        //priority is then west then south
        //more west, then more south
        Unit aFrom = a.getFrom();
        Unit bFrom = b.getFrom();
        int aCol = aFrom.getCol();
        int bCol = bFrom.getCol();
        if(aCol != bCol){
            return -(aCol - bCol);
        }
        int aRow = aFrom.getRow();
        int bRow = bFrom.getRow();
        if(aRow != bRow){
            return aRow - bRow;
        }

        //then check direction, prioritize North before East
        int aDirection = a.getDirection();
        int bDirection = b.getDirection();
        if(aDirection == 2) {
            return 1;
        }else if(bDirection == 2){
            return -1;
        }
        //impossible but necessary catch case
        return 0;
    }

}

public class castle {

    public static void main(String[] args){

        try{
            castle c = new castle();
            System.out.println(c.work("castle.in"));
            PrintWriter out = new PrintWriter("castle.out");
            out.println(c.work("castle.in"));
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }

    }

    public String work(String filename)throws Exception{
        String result = "";

        int numRooms = 0;
        int maxRoom = 0;
        int maxCombine = 0;
        String wallRemove = "";

        /**
         * reading input
         * storing each token of each line into a structured 2D array
         * roomStorage has vertical number of rows and horizontal number of columns
         * roomStorage stores the values give detailing the location of any walls on the edges of the unit
         */
        BufferedReader in = new BufferedReader(new FileReader(filename));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int horizontal = Integer.parseInt(st.nextToken());
        int vertical = Integer.parseInt(st.nextToken());
        int[][] roomStorage = new int[vertical][horizontal];
        for(int row = 0; row < vertical; row++){
            st = new StringTokenizer(in.readLine());
            for(int col = 0; col < horizontal; col++){
                roomStorage[row][col] = Integer.parseInt(st.nextToken());
            }
        }

        /**
         * castle is an arraylist of rooms (rooms are arraylists of units)
         * usedRooms is parallel 2D array of roomStorage with same dimensions
         *  usedRooms contains the index of the room in castle that the unit belongs to
         *  Ex:
         *      usedRooms[2][1] = 1
         *      the Unit in third row at second column belongs to the arrayList with index 1 in list castle
         *  usedRooms is set to -1 when a unit hasn't been assigne to a room yet
         */
        ArrayList<ArrayList<Unit>> castle = new ArrayList<>();
        int[][] usedRooms = new int[vertical][horizontal];
        for(int row = 0; row < vertical; row++){
            for(int col = 0; col < horizontal; col++){
                usedRooms[row][col] = -1;
            }
        }

        /**
         * creating the rooms inside the castle (approach uses BFS methodology)
         *  create a queue of rooms to explore
         * allUsed() takes in array usedRooms, finds the Unit that hasn't been assigned that is closest to the left and top
         * roomID is used to identify the rooms and tie them to an arraylist within castle
         * while allUsed() returns unassigned Units, use getAdjacent() to find accessible units to add to the room
         */
        LinkedList<Unit> roomExplore = new LinkedList<>();
        Unit start = allUsed(usedRooms);
        int roomID = 0;
        while(start != null){
            roomExplore.add(start);
            //creating a room
            ArrayList<Unit> room = new ArrayList<>();
            //adds Units that haven't been explored to the room
            //adds accessible Units using getAdjacent()
            while(roomExplore.size() != 0){
                Unit tempUnit = roomExplore.remove(0);
                if(usedRooms[tempUnit.getRow()][tempUnit.getCol()] == -1){
                    room.add(tempUnit);
                    tempUnit.setRoom(roomID);
                    usedRooms[tempUnit.getRow()][tempUnit.getCol()] = roomID;
                    //if no wall blocks access to the Units above, below, right, or left, add the Units to the queue
                    roomExplore.addAll(getAdjacent(tempUnit, usedRooms, roomStorage));
                }
            }
            //starting a new room
            castle.add(room);
            roomID++;
            start = allUsed(usedRooms);
        }

        /**
         * creating a parallel 2D array of Units in order to find the wall to break and combine to make largest room
         */
        Unit[][] unitGrid = new Unit[vertical][horizontal];
        for(ArrayList<Unit> room : castle){
            for(Unit u : room){
                u.setRoomSize(room.size());
                unitGrid[u.getRow()][u.getCol()] = u;
            }
        }

        /**
         * revised version (much simpler) of finding which wall to break to form the largest combined room
         * runs through all of the units and finds possible connections to other rooms (looks up or right only)
         * Connection objects stores the direction (N or E only), the originating node (bottom or left) and destination
         * also stores size and has a toString() for easy return
         * sort the arrayList of Connection objects, choose the last one to return
         */
        /*ArrayList<Connection> connections = new ArrayList<>();
        for(int row = unitGrid.length-1; row >= 0; row--){
            for(int col = 0; col < unitGrid[row].length; col++){
                int tempUnit = usedRooms[row][col];
                if(row > 0){
                    if(tempUnit != usedRooms[row-1][col]){
                        connections.add(new Connection(2, unitGrid[row][col], unitGrid[row-1][col]));
                    }
                }
                if(col < unitGrid[row].length-1){
                    if(tempUnit != usedRooms[row][col+1]){
                        connections.add(new Connection(4, unitGrid[row][col], unitGrid[row][col+1]));
                    }
                }
            }
        }

        Collections.sort(connections, new SortConnection());
        Connection connectionResult = connections.get(connections.size()-1);

         */

        /**
         * original way using parallel array usedRooms and their IDs
         * prioritize size, then closeness to the left, then closeness to the south
         * for size: simply check maxCombine
         * for closeness to the left (case of same size combinations): start from the left and work right
         * for closeness to the bottom (case of same size combinations): start from the bottom
         * if possible, check if the Unit above has a different roomID (using usedRooms, which stores ID)
         * test to see if combination of thisUnit and aboveUnit rooms is max
         *  if it is max, create the return string of the room location and direction
         * if possible, check if the Unit on the right has a different roomID
         * test to see if combination of thisUnit and rightUNit rooms is max
         *  if it is max, create the return string of the room location and direction
         * because of starting location at bottom left and moving right, will prioritize according to problem requirements
         */
        for(int col = 0; col < usedRooms[0].length; col++){
            for(int row = usedRooms.length-1; row >= 0; row--){
                int tempUnit = usedRooms[row][col];
                if(row > 0){
                    int northRoom = usedRooms[row-1][col];
                    if(tempUnit != northRoom){
                        int combine = castle.get(tempUnit).size() + castle.get(northRoom).size();
                        if(combine > maxCombine){
                            maxCombine = combine;
                            Unit unitAccess  = unitGrid[row][col];
                            wallRemove = ((unitAccess.getRow()+1) + " " + (unitAccess.getCol()+1) + " N");
                        }
                    }
                }
                if(col < usedRooms[0].length-1) {
                    int eastRoom = usedRooms[row][col + 1];
                    if (tempUnit != eastRoom) {
                        int combine = castle.get(tempUnit).size() + castle.get(eastRoom).size();
                        if (combine > maxCombine) {
                            maxCombine = combine;
                            Unit unitAccess = unitGrid[row][col];
                            wallRemove = (unitAccess.getRow() + 1) + " " + (unitAccess.getCol() + 1) + " E";
                        }
                    }
                }
            }
        }


        //finding the number of rooms
        numRooms = castle.size();
        //finding the largest room
        for(ArrayList<Unit> room : castle){
            maxRoom = Math.max(maxRoom, room.size());
        }

        //original result creation
        result += numRooms + "\n" + maxRoom + "\n" + maxCombine + "\n" + wallRemove;

        //revised version with comparator creation
        //result += numRooms + "\n" + maxRoom + "\n" + connectionResult.toString();
        return result;
    }

    /**
     * determines whether or not a new room can be created
     * getAdjacent() only helps with creating rooms and is limited by walls
     * this provides a new starting point to create the next room (if it's possible)
     * @param usedRooms
     * @return
     */
    public Unit allUsed(int[][] usedRooms){
        for(int row = 0; row < usedRooms.length; row++){
            for(int col = 0; col < usedRooms[row].length; col++){
                if(usedRooms[row][col] == -1){
                    return new Unit(row, col);
                }
            }
        }
        return null;
    }

    /**
     * determines the location of any walls the room has
     * uses bit mask for maximum efficiency
     * max walls = 1(west)+2(north)+4(east)+8(south) = 15
     * 15 = 1111 (binary)
     * to check if 1 is used, check last bit (0001)
     * to check if 2 is used, check second to last bit (0010)
     * @param temp
     * @param usedRooms
     * @param roomStorage
     * @return
     */
    public ArrayList<Unit> getAdjacent(Unit temp, int[][] usedRooms, int[][] roomStorage){
        ArrayList<Unit> result = new ArrayList<>();
        int row = temp.getRow();
        int col = temp.getCol();
        int roomSum = roomStorage[row][col];
        /**
         * use bit mask operation to make things easy
         */
        if((roomSum & 0x1) < 1){
            //West
            result.add(new Unit(row,col-1));
        }
        if((roomSum & 0x2) < 1){
            //North
            result.add(new Unit(row-1,col));
        }
        if((roomSum & 0x4) < 1){
            //East
            result.add(new Unit(row,col+1));
        }
        if((roomSum & 0x8) < 1){
            //South
            result.add(new Unit(row+1,col));
        }

        //removes Units that have already been assigned to a room (no backtracking for infinite loop)
        for(int i = result.size()-1; i >= 0; i--){
            if(usedRooms[result.get(i).getRow()][result.get(i).getCol()] != -1){
                result.remove(i);
            }
        }

        return result;
    }

}
