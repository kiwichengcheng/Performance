/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DBConnect.DBConnect;
import bean.AvgTimeRange;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import logic.DataSolution;
import logic.GenerateJSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author U6013046
 */
@WebServlet(name = "Exportfile", urlPatterns = {"/Exportfile"})
public class Exportfile extends HttpServlet {

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
        try {
            System.out.println("I'm in");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "content-type");
            response.setHeader("Content-disposition", "attachment; filename=\"result.xls\"");
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Performance", 0);
            String[] accounts = request.getParameterValues("formRT");
            String[] ts = {"1W", "1M", "6M", "1Y", "5Y", "10Y", "20Y", "1980"};
            String[] normal = {"50", "100", "500", "1K", "2K", "5K", "10K", "25K", "50K", "75K"};
            WritableFont font = new WritableFont(WritableFont.createFont("TimesNewRoman"), 12, WritableFont.BOLD);
            WritableCellFormat Cellformat = new WritableCellFormat(font);
            Cellformat.setBackground(Colour.YELLOW);
            String schedules = request.getParameter("formSch");
            String basetype = request.getParameter("formbasetype");
            String targettype = request.getParameter("formtargettype");
            String baseenv = request.getParameter("formbaseenv");
            String targetenv = request.getParameter("formtargetenv");
            String basefromdate = request.getParameter("formbasefromdate");
            String basetodate = request.getParameter("formbasetodate");
            String targetfromdate = request.getParameter("formtargetfromdate");
            String targettodate = request.getParameter("formtargettodate");
            System.out.println(accounts[0]);
            System.out.println(schedules);
            System.out.println("basetype:"+basetype+" basefromdate:"+basefromdate);
                    
            JSONArray jsonarray = JSONArray.fromObject(schedules);
            for (int i = 0; i < jsonarray.size(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                System.out.println(jsonobject.get("account"));

                JSONArray baseschedulearray = jsonobject.getJSONArray("baseschedules");
                JSONArray targetschedulearray = jsonobject.getJSONArray("targetschedules");
                String[] baseschedulearraystring = new String[baseschedulearray.size()];
                for (int j = 0; j < baseschedulearray.size(); j++) {
                    baseschedulearraystring[j] = baseschedulearray.getString(j);

                }
                String[] targetschedulearraystring = new String[targetschedulearray.size()];
                for (int j = 0; j < targetschedulearray.size(); j++) {
                    targetschedulearraystring[j] = targetschedulearray.getString(j);

                }
                Label title = new Label(0, i * 30, (String) jsonobject.get("account"), Cellformat);
                s.addCell(title);
                GenerateJSON generatejson = new GenerateJSON();
                float[] baseresult = generatejson.BarJson(basetype, (String) jsonobject.getString("account"), baseschedulearraystring, basefromdate, basetodate, baseenv);
                float[] targetresult = generatejson.BarJson(targettype, (String) jsonobject.getString("account"), targetschedulearraystring, targetfromdate, targettodate, targetenv);

                Label baselabel = new Label(0, i * 30 + 1, (baseenv + " " + (basetype.equals("1") ? ("avg from " + basefromdate + " to " + basetodate) : ("single date " + basefromdate))), Cellformat);
                Label targetlabel = new Label(0, i * 30 + 2, (targetenv + " " + (targettype.equals("1") ? ("avg from " + targetfromdate + " to " + targettodate) : ("single date " + targetfromdate))), Cellformat);
                s.addCell(baselabel);
                s.addCell(targetlabel);
                if (((String) jsonobject.get("account")).equals("9002480")) {
                    for (int l = 0; l < 8; l++) {
                        Label label = new Label(l + 1, i * 30, ts[l], Cellformat);
                        s.addCell(label);
                        Number basenumber = new jxl.write.Number(l + 1, i * 30 + 1, baseresult[l]);
                        Number targetnumber = new jxl.write.Number(l + 1, i * 30 + 2, targetresult[l]);
                        s.addCell(basenumber);
                        s.addCell(targetnumber);
                    }
                } else {
                    for (int l = 0; l < 10; l++) {
                        Label label = new Label(l + 1, i * 30, normal[l], Cellformat);
                        Number basenumber = new jxl.write.Number(l + 1, i * 30 + 1, baseresult[l]);
                        Number targetnumber = new jxl.write.Number(l + 1, i * 30 + 2, targetresult[l]);
                        s.addCell(basenumber);
                        s.addCell(targetnumber);
                        s.addCell(label);
                    }
                }
            }

            w.write();
            w.close();

        } catch (Exception e) {
            // TODO: handle exception
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
