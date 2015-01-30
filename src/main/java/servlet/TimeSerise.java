/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DBConnect.DBConnect;
import bean.TakingTime;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author U6013046
 */
@WebServlet(name = "TestSerise", urlPatterns = {"/TestSerise"})
public class TimeSerise extends HttpServlet {

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
        String account = request.getParameter("account");
        String schedule = request.getParameter("querysch");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String type = request.getParameter("type");
        //String jsonpcallback = request.getParameter("jsonpcallback");
        response.setContentType("text/html;charset=UTF-8");
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "content-type");
        String env = request.getParameter("env");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Connection conn = DBConnect.getConnection();
            try {

                if (type.equals("env")) {
                    PreparedStatement pstmt = conn.prepareStatement("SELECT concat(date_format(ver.Date,'%Y,%c,%e'),',',ftp.TakingTime ,',',ftp.InputListNumber ,',',ftp.ReportTemplateFeildsNumber) as takingtime "
                            + "FROM ex_account acc, ex_account_type act, ex_env_type ent, ex_extraction_result exr,"
                            + "ex_ftp_notes ftp, ex_version ver, ex_extraction ext "
                            + "WHERE acc.AccountTypeID = act.ID "
                            + "AND   ext.AccountID = acc.ID "
                            + "AND   exr.ExtractionID = ext.ID "
                            + "AND   exr.VersionID = ver.ID "
                            + "AND ent.ID = ver.ENV_Type_ID "
                            + "AND   ftp.ResultID = exr.ID "
                            + "AND  acc.Account = '" + account + "' "
                            + "AND ent.Name = '" + env + "' "
                            + "and ext.Name = '" + schedule + "' "
                            + "and ftp.TakingTime >0 "
                            + "and ver.Date between '" + request.getParameter("fromdate") + "' and '" + request.getParameter("todate") + "'"
                            + "order by ver.Date");

                    ResultSet rs = pstmt.executeQuery();
                    ArrayList<TakingTime> takingtimes = new ArrayList<TakingTime>();
                    int maxindex = 0;
                    int minindex = 0;
                    double maxtemp = 0.0d;
                    double mintemp = 0.0d;
                    int counter = 0;

                    int perimaxindex = 0;
                    int periminindex = 0;
                    double perimaxtemp = 0.0d;
                    double perimintemp = 0.0d;

                    int perfmaxindex = 0;
                    int perfminindex = 0;
                    double perfmaxtemp = 0.0d;
                    double perfmintemp = 0.0d;

                    while (rs.next()) {

                        TakingTime takingtime = new TakingTime();
                        double takingtimevalue = Double.parseDouble(rs.getString(1).split(",")[3]);
                        double itakingtimevalue = Double.parseDouble(rs.getString(1).split(",")[3]) / Double.parseDouble(rs.getString(1).split(",")[4]);
                        double ftakingtimevalue = Double.parseDouble(rs.getString(1).split(",")[3]) / Double.parseDouble(rs.getString(1).split(",")[5]);
                        if (takingtimevalue >= maxtemp) {
                            maxtemp = takingtimevalue;
                            maxindex = counter;
                        }
                        if (itakingtimevalue >= perimaxtemp) {
                            perimaxtemp = itakingtimevalue;
                            perimaxindex = counter;
                        }
                        if (ftakingtimevalue >= perfmaxtemp) {
                            perfmaxtemp = ftakingtimevalue;
                            perfmaxindex = counter;
                        }
                        if (counter == 0) {
                            mintemp = takingtimevalue;
                            perimintemp = itakingtimevalue;
                            perfmintemp = ftakingtimevalue;
                            minindex = counter;
                            periminindex = counter;
                            perfminindex = counter;
                        } else {
                            if (takingtimevalue <= mintemp) {
                                mintemp = takingtimevalue;
                                minindex = counter;
                            }
                            if (itakingtimevalue <= perimintemp) {
                                perimintemp = itakingtimevalue;
                                periminindex = counter;
                            }
                            if (ftakingtimevalue <= perfmintemp) {
                                perfmintemp = ftakingtimevalue;
                                perfminindex = counter;
                            }
                        }
                        takingtime.setTaketime(rs.getString(1));
                        takingtimes.add(takingtime);

                        counter++;
                    }
                    if (takingtimes.size() > 0) {
                        TakingTime max = takingtimes.get(maxindex);
                        max.setOmax(1);
                        takingtimes.set(maxindex, max);
                        TakingTime min = takingtimes.get(minindex);
                        min.setOmin(1);
                        takingtimes.set(minindex, min);
                        TakingTime imax = takingtimes.get(perimaxindex);
                        imax.setImax(1);
                        takingtimes.set(perimaxindex, imax);
                        TakingTime fmax = takingtimes.get(perfmaxindex);
                        fmax.setFmax(1);
                        takingtimes.set(perfmaxindex, fmax);
                        TakingTime imin = takingtimes.get(periminindex);
                        imin.setImin(1);
                        takingtimes.set(periminindex, imin);
                        TakingTime fmin = takingtimes.get(perfminindex);
                        fmin.setFmin(1);
                        takingtimes.set(perfminindex, fmin);
                    }
                    JSONArray jsonarray = JSONArray.fromObject(takingtimes);
                //JSONObject jsonobject = new JSONObject();
                    //jsonobject.element("data", jsonarray);
                    out.println(jsonarray);
                }

                if (type.equals("avg")) {
                    PreparedStatement pstmt = conn.prepareStatement("SELECT avg(ftp.TakingTime) as takingtime "
                            + "FROM ex_account acc, ex_account_type act, ex_env_type ent, ex_extraction_result exr,"
                            + "ex_ftp_notes ftp, ex_version ver, ex_extraction ext "
                            + "WHERE acc.AccountTypeID = act.ID "
                            + "AND   ext.AccountID = acc.ID "
                            + "AND   exr.ExtractionID = ext.ID "
                            + "AND   exr.VersionID = ver.ID "
                            + "AND ent.ID = ver.ENV_Type_ID "
                            + "AND   ftp.ResultID = exr.ID "
                            + "AND  acc.Account = '" + account + "' "
                            + "AND ent.Name = '" + env + "' "
                            + "and ext.Name = '" + schedule + "' "
                            + "and ftp.TakingTime >0 "
                            + "and ver.Date between '" + request.getParameter("avgfromdate") + "' and '" + request.getParameter("avgtodate") + "'"
                            + "order by ver.Date");

                    ResultSet rs = pstmt.executeQuery();
                    ArrayList<TakingTime> takingtimes = new ArrayList<TakingTime>();
                    Float result = 0.0f;
                    while (rs.next()) {
                        result = rs.getFloat(1);
                    }
                    TakingTime takingtimefrom = new TakingTime();
                    TakingTime takingtimeto = new TakingTime();
                    String[] fromdate = request.getParameter("fromdate").split("-");
                    String[] todate = request.getParameter("todate").split("-");
                    takingtimefrom.setTaketime(fromdate[0] + "," + fromdate[1] + "," + fromdate[2] + "," + result);
                    takingtimeto.setTaketime(todate[0] + "," + todate[1] + "," + todate[2] + "," + result);
                    takingtimes.add(takingtimefrom);
                    takingtimes.add(takingtimeto);
                    JSONArray jsonarray = JSONArray.fromObject(takingtimes);
                //JSONObject jsonobject = new JSONObject();
                    //jsonobject.element("data", jsonarray);
                    out.println(jsonarray);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimeSerise.class.getName()).log(Level.SEVERE, null, ex);
            }
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
