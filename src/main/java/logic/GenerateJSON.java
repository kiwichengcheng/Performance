/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import DBConnect.DBConnect;
import bean.AvgTimeRange;
import bean.SingleLineNode;
import bean.TakingTime;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import servlet.Test;
import servlet.TimeSerise;

/**
 *
 * @author U6013046
 */
public class GenerateJSON {

    private Connection conn = DBConnect.getConnection();

    public float[] BarJson(String type, String account, String[] schedules, String fromdate, String todate, String env) {

        float[] result = null;
        if (type.equals("1")) {
            try {
                CallableStatement cs = null;
                cs = conn.prepareCall("{call QueryTimeRangeAvg(?,?,?,?)}");
                cs.setString(1, account);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date udatestart = sdf.parse(fromdate);
                java.sql.Date sdatestart = new java.sql.Date(udatestart.getTime());
                cs.setDate(2, sdatestart);
                java.util.Date udateend = sdf.parse(todate);
                java.sql.Date sdateend = new java.sql.Date(udateend.getTime());
                cs.setDate(3, sdateend);
                cs.setString(4, env);
                ResultSet rs = cs.executeQuery();
                ArrayList<AvgTimeRange> avgTimeRangelist = new ArrayList<AvgTimeRange>();

                while (rs.next()) {
                    AvgTimeRange avgTimeRange = new AvgTimeRange();
                    avgTimeRange.setExtraciontID(rs.getInt(1));
                    avgTimeRange.setSchedule(rs.getString(2));
                    avgTimeRange.setTakingtime(rs.getString(3));
                    avgTimeRangelist.add(avgTimeRange);
                }

                DataSolution ds = new DataSolution();
                result = ds.format(avgTimeRangelist, schedules, account);
            } catch (SQLException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("2")) {
            try {
                CallableStatement cs2 = null;
                cs2 = conn.prepareCall("{call QueryExtractionTakingTime2(?,?,?)}");
                cs2.setString(1, account);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date udatestart = sdf.parse(fromdate);
                java.sql.Date sdatestart = new java.sql.Date(udatestart.getTime());
                cs2.setDate(2, sdatestart);
                cs2.setString(3, env);

                ResultSet rs2 = cs2.executeQuery();
                ArrayList<AvgTimeRange> avgTimeRangelist2 = new ArrayList<AvgTimeRange>();

                while (rs2.next()) {
                    AvgTimeRange avgTimeRange2 = new AvgTimeRange();
                    avgTimeRange2.setExtraciontID(rs2.getInt(1));
                    avgTimeRange2.setSchedule(rs2.getString(2));
                    avgTimeRange2.setTakingtime(rs2.getString(3));
                    avgTimeRangelist2.add(avgTimeRange2);
                }

                DataSolution ds2 = new DataSolution();
                result = ds2.format(avgTimeRangelist2, schedules, account);
            } catch (SQLException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<SingleLineNode> LineJson(String account, String schedule, String type, String env, String fromdate, String todate, String avgfromdate, String avgtodate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                ArrayList<SingleLineNode> takingtimes = new ArrayList<SingleLineNode>();
        try {

            if (type.equals("env")) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT date_format(ver.Date,'%Y-%c-%e'),ftp.TakingTime ,ftp.InputListNumber ,ftp.ReportTemplateFeildsNumber  "
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
                        + "and ver.Date between '" + fromdate + "' and '" + todate + "'"
                        + "order by ver.Date");

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {

                    SingleLineNode takingtime = new SingleLineNode();
                    takingtime.setDate(sdf.parse(rs.getString(1)));
                    takingtime.setTime(rs.getFloat(2));
                    takingtime.setIlcount(rs.getInt(3));
                    takingtime.setFields(rs.getInt(4));
                    takingtimes.add(takingtime);
                }
                
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
                        + "and ver.Date between '" + avgfromdate + "' and '" + avgtodate + "'"
                        + "order by ver.Date");

                ResultSet rs = pstmt.executeQuery();
                Float result = 0.0f;
                while (rs.next()) {
                    result = rs.getFloat(1);
                }
                SingleLineNode singlelinenode = new SingleLineNode();
                singlelinenode.setTime(result);
                takingtimes.add(singlelinenode);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimeSerise.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GenerateJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (takingtimes.size() == 0){
            SingleLineNode zero = new SingleLineNode();
            try {
                zero.setDate(sdf.parse(fromdate));
            } catch (ParseException ex) {
                Logger.getLogger(GenerateJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
            zero.setTime(0);
            zero.setFields(0);
            zero.setIlcount(0);
            takingtimes.add(zero);
            
        }
        
        return takingtimes;
    }

}
