/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logic;

import bean.AvgTimeRange;
import java.util.ArrayList;

/**
 *
 * @author U6013046
 */
public class DataSolution {
    
    
    public float[] format(ArrayList<AvgTimeRange> l,String[] schedules,String account){
        System.out.println("DataSolution.java 20:"+account);
        if (account.equals("9002480")){
        System.out.println("DataSolution.java 22");
        float[] result = new float[8];
            for (AvgTimeRange a : l){
            if (a.getSchedule().toLowerCase().endsWith("1w")&&(checktype(a.getSchedule(),schedules))){
                
                result[0] = Float.parseFloat(a.getTakingtime());
            }
            else if (a.getSchedule().toLowerCase().endsWith("1m")&&(checktype(a.getSchedule(),schedules))){
                result[1] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("6m")&&(checktype(a.getSchedule(),schedules))){
                result[2] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("1y")&&(checktype(a.getSchedule(),schedules))){
                result[3] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("5y")&&(checktype(a.getSchedule(),schedules))){
                result[4] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("10y")&&(checktype(a.getSchedule(),schedules))){
                result[5] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("20y")&&(checktype(a.getSchedule(),schedules))){
                result[6] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().endsWith("1980")&&(checktype(a.getSchedule(),schedules))){
                result[7] = Float.parseFloat(a.getTakingtime());}
            }
        return result;
        }else
        {
            float[] result = new float[10];
            for (AvgTimeRange a : l){
            if (a.getSchedule().toLowerCase().contains("50")&&(!a.getSchedule().toLowerCase().contains("50k"))&&(!a.getSchedule().toLowerCase().contains("500"))&&(checktype(a.getSchedule(),schedules))){
                
                result[0] = Float.parseFloat(a.getTakingtime());
            }
            else if (a.getSchedule().toLowerCase().contains("100")&&(checktype(a.getSchedule(),schedules))){
                result[1] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("500")&&(checktype(a.getSchedule(),schedules))){
                result[2] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("1k")&&(checktype(a.getSchedule(),schedules))){
                result[3] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("2k")&&(checktype(a.getSchedule(),schedules))){
                result[4] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("5k")&&(!a.getSchedule().toLowerCase().contains("25k"))&&(!a.getSchedule().toLowerCase().contains("75k"))&&(checktype(a.getSchedule(),schedules))){
                result[5] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("10k")&&(checktype(a.getSchedule(),schedules))){
                result[6] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("25k")&&(checktype(a.getSchedule(),schedules))){
                result[7] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("50k")&&(checktype(a.getSchedule(),schedules))){
                result[8] = Float.parseFloat(a.getTakingtime());}
            else if (a.getSchedule().toLowerCase().contains("75k")&&(checktype(a.getSchedule(),schedules))){
                result[9] = Float.parseFloat(a.getTakingtime());}
            
            }
            return result;
        }
    }
    
    public boolean checktype(String content, String[] type){
        boolean result = false;
        for(String filter : type){
            if(content.equals(filter)){
                result = true;
                break;
            }
        }
        return result;
    }
    
}
