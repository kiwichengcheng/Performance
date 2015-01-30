/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import DBConnect.DBConnect;
import bean.JSONBean;
import bean.Schedule;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

/**
 *
 * @author U6013046
 */
@WebServlet(name = "GetSchedule", urlPatterns = {"/GetSchedule"})
public class GetSchedule extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "content-type");
        try (PrintWriter out = response.getWriter()) {
        Connection conn = DBConnect.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT e.Name FROM ex_extraction e, ex_account acc where e.AccountID = acc.ID and acc.Account=?");
        String accountname=request.getParameter("account");
        String jsonpcallback = request.getParameter("jsonpcallback");
        pstmt.setString(1, accountname);
        ResultSet rs = pstmt.executeQuery();
        
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        while(rs.next()){
            
            Schedule schedule = new Schedule();
            schedule.setAccount(accountname);
            schedule.setScheduleName(rs.getString(1));
            String[] value = new String[2];
            value[0] = accountname;
            value[1] = rs.getString(1);
            schedule.setValue(value);
            schedules.add(schedule);
            
        }
        /*JSONBean bean = new JSONBean();
        bean.setList(schedules);*/
        JSONArray jsonobject = JSONArray.fromObject(schedules);
        out.print(jsonpcallback+"("+jsonobject+")");
        } catch (SQLException ex) {
            Logger.getLogger(GetSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
