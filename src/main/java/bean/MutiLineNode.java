/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import java.util.Date;

/**
 *
 * @author U6013046
 */
public class MutiLineNode {
    
    private Date date;
    private float basetime;
    private float targettime;
    private int basefields;
    private int targetfields;
    private int baseilcount;
    private int targetilcount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getBasetime() {
        return basetime;
    }

    public void setBasetime(float basetime) {
        this.basetime = basetime;
    }

    public float getTargettime() {
        return targettime;
    }

    public void setTargettime(float targettime) {
        this.targettime = targettime;
    }

    public int getBasefields() {
        return basefields;
    }

    public void setBasefields(int basefields) {
        this.basefields = basefields;
    }

    public int getTargetfields() {
        return targetfields;
    }

    public void setTargetfields(int targetfields) {
        this.targetfields = targetfields;
    }

    public int getBaseilcount() {
        return baseilcount;
    }

    public void setBaseilcount(int baseilcount) {
        this.baseilcount = baseilcount;
    }

    public int getTargetilcount() {
        return targetilcount;
    }

    public void setTargetilcount(int targetilcount) {
        this.targetilcount = targetilcount;
    }
    
    
    
}
