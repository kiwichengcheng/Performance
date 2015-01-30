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
public class TakingTime {
    
    private String taketime;
    private int omax = 0;
    private int imax = 0;
    private int fmax = 0;
    private int omin = 0;
    private int imin = 0;
    private int fmin = 0;

    public int getOmin() {
        return omin;
    }

    public void setOmin(int omin) {
        this.omin = omin;
    }

    public int getImin() {
        return imin;
    }

    public void setImin(int imin) {
        this.imin = imin;
    }

    public int getFmin() {
        return fmin;
    }

    public void setFmin(int fmin) {
        this.fmin = fmin;
    }
    
    
    
    public String getTaketime() {
        return taketime;
    }

    public void setTaketime(String taketime) {
        this.taketime = taketime;
    }

    public int getOmax() {
        return omax;
    }

    public void setOmax(int omax) {
        this.omax = omax;
    }

    public int getImax() {
        return imax;
    }

    public void setImax(int imax) {
        this.imax = imax;
    }

    public int getFmax() {
        return fmax;
    }

    public void setFmax(int fmax) {
        this.fmax = fmax;
    }
    
    
    
}
