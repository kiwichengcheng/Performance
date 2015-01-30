/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import bean.MutiLineNode;
import bean.SingleLineNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import logic.GenerateJSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author U6013046
 */
@WebServlet(name = "ExportfileTimeseriessingle", urlPatterns = {"/ExportfileTimeseriessingle"})
public class ExportfileTimeseriessingle extends HttpServlet {

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
            response.setContentType("application/vnd.ms-excel");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "content-type");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            response.setHeader("Content-disposition", "attachment; filename=\"result.xls\"");
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Performance", 0);
            String schedules = request.getParameter("formSch");
            JSONArray jsonarray = JSONArray.fromObject(schedules);
            GenerateJSON generatejson = new GenerateJSON();
            String basetype = request.getParameter("formbasetype");
            String targettype = request.getParameter("formtargettype");
            String fromdate = request.getParameter("formbasefromdate");
            String todate = request.getParameter("formbasetodate");
            String avgfromdate = request.getParameter("formavgfromdate");
            String avgtodate = request.getParameter("formavgtodate");
            String perfield = request.getParameter("formfperfield");
            System.out.println("hahaha:" + perfield);

            System.out.println("hahaha:" + avgtodate);
            WritableFont font = new WritableFont(WritableFont.createFont("TimesNewRoman"), 12, WritableFont.BOLD);
            WritableCellFormat Cellformat = new WritableCellFormat(font);
            Cellformat.setBackground(Colour.YELLOW);
            WritableCellFormat maxformat = new WritableCellFormat();
            maxformat.setBackground(Colour.RED);
            WritableCellFormat minformat = new WritableCellFormat();
            minformat.setBackground(Colour.GREEN);
            for (int i = 0; i < jsonarray.size(); i++) {
                ArrayList<MutiLineNode> result = new ArrayList<MutiLineNode>();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                List<SingleLineNode> baseline = generatejson.LineJson(jsonobject.getString("baseaccount"), jsonobject.getString("baseschedule"), basetype, jsonobject.getString("baseenv"), fromdate, todate, avgfromdate, avgtodate);
                List<SingleLineNode> targetline = generatejson.LineJson(jsonobject.getString("targetaccount"), jsonobject.getString("targetschedule"), targettype, jsonobject.getString("targetenv"), fromdate, todate, avgfromdate, avgtodate);

                if (basetype.equals("avg")) {

                    for (SingleLineNode snode : targetline) {
                        MutiLineNode resultnode = new MutiLineNode();
                        resultnode.setDate(snode.getDate());
                        resultnode.setBasetime(baseline.get(0).getTime());
                        resultnode.setTargettime(snode.getTime());
                        result.add(resultnode);

                    }
                    Label title = new Label(0, i * 30, "normal chart", Cellformat);
                    Label basetitle = new Label(0, i * 30 + 1, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv") + " avg from " + avgfromdate + " to " + avgtodate, Cellformat);
                    Label targettitle = new Label(0, i * 30 + 2, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv"), Cellformat);
                    s.addCell(title);
                    s.addCell(basetitle);
                    s.addCell(targettitle);

                } else {

                    int basesize = baseline.size();
                    int targetsize = targetline.size();
                    int basetemp = 0;
                    int targettemp = 0;
                    int basecounter = 0;
                    int targetcounter = 0;
                    while (basecounter < basesize || targetcounter < targetsize) {
                        basetemp = basecounter == basesize ? (basecounter - 1) : basecounter;
                        targettemp = targetcounter == targetsize ? (targetcounter - 1) : targetcounter;

                        MutiLineNode resultnode = new MutiLineNode();
                        if (baseline.get(basetemp).getDate().before(targetline.get(targettemp).getDate()) && basecounter <= basesize) {

                            if (basecounter == basesize) {
                                resultnode.setTargetfields(targetline.get(targettemp).getFields());
                                resultnode.setTargetilcount(targetline.get(targettemp).getIlcount());
                                resultnode.setTargettime(targetline.get(targettemp).getTime());
                                resultnode.setDate(targetline.get(targettemp).getDate());
                                resultnode.setBasefields(0);
                                resultnode.setBaseilcount(0);
                                resultnode.setBasetime(0);
                                targetcounter++;
                            } else {
                                resultnode.setBasefields(baseline.get(basetemp).getFields());
                                resultnode.setBaseilcount(baseline.get(basetemp).getIlcount());
                                resultnode.setBasetime(baseline.get(basetemp).getTime());
                                resultnode.setDate(baseline.get(basetemp).getDate());
                                resultnode.setTargetfields(0);
                                resultnode.setTargetilcount(0);
                                resultnode.setTargettime(0);
                                basecounter++;
                            }
                        } else if (baseline.get(basetemp).getDate().after(targetline.get(targettemp).getDate()) && targetcounter <= targetsize) {

                            if (targetcounter == targetsize) {
                                resultnode.setBasefields(baseline.get(basetemp).getFields());
                                resultnode.setBaseilcount(baseline.get(basetemp).getIlcount());
                                resultnode.setBasetime(baseline.get(basetemp).getTime());
                                resultnode.setDate(baseline.get(basetemp).getDate());
                                resultnode.setTargetfields(0);
                                resultnode.setTargetilcount(0);
                                resultnode.setTargettime(0);
                                basecounter++;
                            } else {
                                resultnode.setTargetfields(targetline.get(targettemp).getFields());
                                resultnode.setTargetilcount(targetline.get(targettemp).getIlcount());
                                resultnode.setTargettime(targetline.get(targettemp).getTime());
                                resultnode.setDate(targetline.get(targettemp).getDate());
                                resultnode.setBasefields(0);
                                resultnode.setBaseilcount(0);
                                resultnode.setBasetime(0);
                                targetcounter++;
                            }
                        } else {
                            resultnode.setTargetfields(targetline.get(targettemp).getFields());
                            resultnode.setTargetilcount(targetline.get(targettemp).getIlcount());
                            resultnode.setTargettime(targetline.get(targettemp).getTime());
                            resultnode.setDate(targetline.get(targettemp).getDate());
                            resultnode.setBasefields(baseline.get(basetemp).getFields());
                            resultnode.setBaseilcount(baseline.get(basetemp).getIlcount());
                            resultnode.setBasetime(baseline.get(basetemp).getTime());
                            targetcounter++;
                            basecounter++;
                        }
                        result.add(resultnode);
                    }
                    Label title = null;
                    if (perfield.equals("o")) {
                        title = new Label(0, i * 30, "normal chart", Cellformat);
                    } else if (perfield.equals("i")) {
                        title = new Label(0, i * 30, "per Instrument", Cellformat);
                    } else if (perfield.equals("f")) {
                        title = new Label(0, i * 30, "per Field", Cellformat);
                    }

                    Label basetitle = new Label(0, i * 30 + 1, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv"), Cellformat);
                    Label targettitle = new Label(0, i * 30 + 2, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv"), Cellformat);
                    s.addCell(title);
                    s.addCell(basetitle);
                    s.addCell(targettitle);

                }
                if (perfield.equals("o")) {
                    Label baseiltitle = new Label(0, i * 30 + 3, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv") + " ILcount", Cellformat);
                    Label targetiltitle = new Label(0, i * 30 + 5, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv") + " ILCount", Cellformat);
                    Label baseftitle = new Label(0, i * 30 + 4, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv") + " Fields", Cellformat);
                    Label targetftitle = new Label(0, i * 30 + 6, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv") + " Fields", Cellformat);
                    s.addCell(baseftitle);
                    s.addCell(targetftitle);
                    s.addCell(baseiltitle);
                    s.addCell(targetiltitle);
                    for (int j = 0; j < result.size(); j++) {
                        Label col = new Label(j + 1, i * 30, sdf.format(result.get(j).getDate()), Cellformat);
                        jxl.write.Number basenumber = new jxl.write.Number(j + 1, i * 30 + 1, result.get(j).getBasetime());
                        jxl.write.Number targetnumber = new jxl.write.Number(j + 1, i * 30 + 2, result.get(j).getTargettime());

                        s.addCell(col);
                        s.addCell(basenumber);
                        s.addCell(targetnumber);
                        jxl.write.Number baseilcount = new jxl.write.Number(j + 1, i * 30 + 3, result.get(j).getBaseilcount());
                        jxl.write.Number targetilcount = new jxl.write.Number(j + 1, i * 30 + 5, result.get(j).getTargetilcount());
                        jxl.write.Number basefields = new jxl.write.Number(j + 1, i * 30 + 4, result.get(j).getBasefields());
                        jxl.write.Number targetfields = new jxl.write.Number(j + 1, i * 30 + 6, result.get(j).getTargetfields());

                        s.addCell(baseilcount);
                        s.addCell(targetilcount);
                        s.addCell(basefields);
                        s.addCell(targetfields);

                    }
                } else if (perfield.equals("i")) {
                    Label basevtitle = new Label(0, i * 30 + 3, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv") + " ILcount", Cellformat);
                    Label targetvtitle = new Label(0, i * 30 + 4, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv") + " ILCount", Cellformat);
                    s.addCell(basevtitle);
                    s.addCell(targetvtitle);
                    for (int j = 0; j < result.size(); j++) {
                        Label col = new Label(j + 1, i * 30, sdf.format(result.get(j).getDate()), Cellformat);
                        jxl.write.Number basenumber = new jxl.write.Number(j + 1, i * 30 + 1, result.get(j).getBaseilcount() == 0 ? 0 : result.get(j).getBasetime() / result.get(j).getBaseilcount());
                        jxl.write.Number targetnumber = new jxl.write.Number(j + 1, i * 30 + 2, result.get(j).getTargetilcount() == 0 ? 0 : result.get(j).getTargettime() / result.get(j).getTargetilcount());
                        jxl.write.Number baseilcount = new jxl.write.Number(j + 1, i * 30 + 3, result.get(j).getBaseilcount());
                        jxl.write.Number targetilcount = new jxl.write.Number(j + 1, i * 30 + 4, result.get(j).getTargetilcount());
                        s.addCell(col);
                        s.addCell(basenumber);
                        s.addCell(targetnumber);
                        s.addCell(baseilcount);
                        s.addCell(targetilcount);
                    }
                } else if (perfield.equals("f")) {

                    Label basevtitle = new Label(0, i * 30 + 3, jsonobject.getString("baseaccount") + " " + jsonobject.getString("baseschedule") + " " + jsonobject.getString("baseenv") + " Fields", Cellformat);
                    Label targetvtitle = new Label(0, i * 30 + 4, jsonobject.getString("targetaccount") + " " + jsonobject.getString("targetschedule") + " " + jsonobject.getString("targetenv") + " Fields", Cellformat);
                    s.addCell(basevtitle);
                    s.addCell(targetvtitle);
                    for (int j = 0; j < result.size(); j++) {
                        Label col = new Label(j + 1, i * 30, sdf.format(result.get(j).getDate()), Cellformat);
                        jxl.write.Number basenumber = new jxl.write.Number(j + 1, i * 30 + 1, result.get(j).getBasefields() == 0 ? 0 : result.get(j).getBasetime() / result.get(j).getBasefields());
                        jxl.write.Number targetnumber = new jxl.write.Number(j + 1, i * 30 + 2, result.get(j).getTargetfields() == 0 ? 0 : result.get(j).getTargettime() / result.get(j).getTargetfields());
                        jxl.write.Number basefields = new jxl.write.Number(j + 1, i * 30 + 3, result.get(j).getBasefields());
                        jxl.write.Number targetfields = new jxl.write.Number(j + 1, i * 30 + 4, result.get(j).getTargetfields());
                        s.addCell(col);
                        s.addCell(basenumber);
                        s.addCell(targetnumber);
                        s.addCell(basefields);
                        s.addCell(targetfields);
                    }
                }

                Cell[] baserow = s.getRow(i * 30 + 1);
                Cell[] targetrow = s.getRow(i * 30 + 2);

                double max = 0.0d;
                int maxindex = 0;
                double min = baseline.get(0).getTime();
                int minindex = 0;
                double sum = 0.0d;
                int counter = 0;
                for (int basecol = 1; basecol <= result.size(); basecol++) {
                    double value = Double.parseDouble(baserow[basecol].getContents());
                    sum += value;
                    if (value != 0) {
                        counter++;
                    }

                    if (value > max) {
                        max = value;
                        maxindex = basecol;
                    }

                    if (value < min && value != 0) {
                        min = value;
                        minindex = basecol;
                    }
                }

                Label avgtitle = new Label(result.size() + 2, i * 30, "avg", Cellformat);
                Label maxtitle = new Label(result.size() + 3, i * 30, "max", Cellformat);
                Label mintitle = new Label(result.size() + 4, i * 30, "min", Cellformat);

                s.addCell(avgtitle);
                s.addCell(maxtitle);
                s.addCell(mintitle);

                jxl.write.Number basemin = new jxl.write.Number(result.size() + 4, i * 30 + 1, min);
                jxl.write.Number baseavg = new jxl.write.Number(result.size() + 2, i * 30 + 1, sum / counter);
                jxl.write.Number basemax = new jxl.write.Number(result.size() + 3, i * 30 + 1, max);

                s.addCell(basemax);
                s.addCell(basemin);
                s.addCell(baseavg);
                if (!basetype.equals("avg")) {
                    jxl.write.Number basemaxvalue = new jxl.write.Number(maxindex, i * 30 + 1, max, maxformat);
                    jxl.write.Number baseminvalue = new jxl.write.Number(minindex, i * 30 + 1, min, minformat);
                    s.addCell(basemaxvalue);
                    s.addCell(baseminvalue);
                }

                max = 0.0d;
                maxindex = 0;
                min = targetline.get(0).getTime();
                minindex = 0;
                sum = 0.0d;
                counter = 0;
                for (int targetcol = 1; targetcol <= result.size(); targetcol++) {
                    double value = Double.parseDouble(targetrow[targetcol].getContents());
                    sum += value;
                    if (value != 0) {
                        counter++;
                    }

                    if (value > max) {
                        max = value;
                        maxindex = targetcol;
                    }

                    if (value < min && value != 0) {
                        min = value;
                        minindex = targetcol;
                    }
                }

                jxl.write.Number targetmin = new jxl.write.Number(result.size() + 4, i * 30 + 2, min);
                jxl.write.Number targetavg = new jxl.write.Number(result.size() + 2, i * 30 + 2, sum / counter);
                jxl.write.Number targetmax = new jxl.write.Number(result.size() + 3, i * 30 + 2, max);

                s.addCell(targetmin);
                s.addCell(targetavg);
                s.addCell(targetmax);
                
                jxl.write.Number targetmaxvalue = new jxl.write.Number(maxindex, i * 30 + 2, max, maxformat);
                jxl.write.Number targetminvalue = new jxl.write.Number(minindex, i * 30 + 2, min, minformat);
                s.addCell(targetmaxvalue);
                s.addCell(targetminvalue);

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
