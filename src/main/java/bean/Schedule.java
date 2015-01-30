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
public class Schedule {

    public String getAccount() {
        return account;
    }

    public String getScheduleName() {
        return ScheduleName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setScheduleName(String ScheduleName) {
        this.ScheduleName = ScheduleName;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
    
    private String account;
    private String ScheduleName;
    private String[] value;
}
