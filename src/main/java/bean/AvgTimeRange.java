/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

/**
 *
 * @author U6013046
 */
public class AvgTimeRange {
    private int ExtraciontID;
    private String Schedule;
    private String Takingtime;

    public void setExtraciontID(int ExtraciontID) {
        this.ExtraciontID = ExtraciontID;
    }

    public void setSchedule(String Schedule) {
        this.Schedule = Schedule;
    }

    public void setTakingtime(String Takingtime) {
        this.Takingtime = Takingtime;
    }

    public int getExtraciontID() {
        return ExtraciontID;
    }

    public String getSchedule() {
        return Schedule;
    }

    public String getTakingtime() {
        return Takingtime;
    }
}
