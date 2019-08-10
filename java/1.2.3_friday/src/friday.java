/*
ID: andrew.83
LANG: JAVA
TASK: friday
*/

import java.io.*;

/**
 * returns the number of times for each weekday that the day occurs on a 13th of a month within a given time frame
 * starts from January 1 (Monday), 1900
 * given a number, represents number of years from 1900
 * Ex:
 * input: 20
 * output: 36 33 34 33 35 35 34
 *         |Saturday.........|Friday
 */
public class friday {
    
    public static void main(String[] args){
        friday s3 = new friday();
        
        try{
            int[] week = s3.work("");
            PrintWriter out = new PrintWriter("friday.out");
            for (int i = 0; i < week.length-1; i++){
            	out.print(week[i]);
            	out.print(" ");
            }
            out.print(week[week.length-1] + "\n");
            out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public int[] work(String filename) throws Exception{
        
        BufferedReader in = new BufferedReader(new FileReader("friday.in"));
        //rt is array of the week days
        //Saturday represented by 0, Sunday represented by 1
        int[] rt = new int[7];
        
        int years = Integer.parseInt(in.readLine());

        //all of this is a single block of logic
        //try compartmentalizing it to set different data structures for different month types
        //operates on standard rules of the conventional Gregorian calendar
        /**
         * January 1, 1900 was on a Monday.
         * Thirty days has September, April, June, and November, all the rest have 31 except for February which has 28 except in leap years when it has 29.
         * Every year evenly divisible by 4 is a leap year (1992 = 4*498 so 1992 will be a leap year, but the year 1990 is not a leap year)
         * The rule above does not hold for century years. Century years divisible by 400 are leap years, all other are not. Thus, the century years 1700, 1800, 1900 and 2100 are not leap years, but 2000 is a leap year.
         */
        int weekday = 2;
        for (int y = 0; y < years; y++){
            for(int months = 1; months <= 12; months++){
                int days = 31;
                if(months == 4 || months == 6 || months == 9 || months == 11)
                    days = 30;
                if(months == 2){
                    int currentyear = y+1900;
                    if (currentyear%100 == 0){
                        if (currentyear%400 == 0){
                            days = 29;
                        } else{
                            days = 28;
                        }
                    } else{
                        if (currentyear%4 == 0){
                            days = 29;
                        } else{
                            days = 28;
                        }
                    }
                }
                for (int d = 1; d <= days; d++){
                    weekday = daytracker(weekday);
                    if (d == 13){
                        rt[weekday]++;
                    }
                    weekday++;
                }
                System.out.println(days);
            }
            
        }
        return rt;
    }

    /**
     * this method is entirely unnecessary, just use %7
     * @param input
     * @return
     */
    public static int daytracker(int input){
        int rt = input;
        if (input % 7 == 0){
            rt = 0;
        }
        return rt;
    }
    
}
