import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import java.awt.Color;
import java.awt.Dimension;

public class PptServlet extends HttpServlet {

    private String getDbPath() {
        return getServletContext().getRealPath("/WEB-INF/dictionary.db");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(new Dimension(720, 540));

        // Титульный слайд
        XSLFSlide titleSlide = ppt.createSlide();
        XSLFTextBox titleBox = titleSlide.createTextBox();
        titleBox.setAnchor(new java.awt.Rectangle(50, 150, 620, 100));
        XSLFTextParagraph titlePara = titleBox.addNewTextParagraph();
        titlePara.setTextAlign(TextAlign.CENTER);
        XSLFTextRun titleRun = titlePara.addNewTextRun();
        titleRun.setText("Русско-английский словарь");
        titleRun.setFontSize(32.0);
        titleRun.setFontColor(new Color(0, 0, 128));
        titleRun.setBold(true);

        XSLFTextBox subtitleBox = titleSlide.createTextBox();
        subtitleBox.setAnchor(new java.awt.Rectangle(50, 270, 620, 60));
        XSLFTextParagraph subPara = subtitleBox.addNewTextParagraph();
        subPara.setTextAlign(TextAlign.CENTER);
        XSLFTextRun subRun = subPara.addNewTextRun();
        subRun.setText("Лабораторная работа №3 - Конча Д.В., гр. 320604");
        subRun.setFontSize(18.0);
        subRun.setFontColor(new Color(100, 100, 100));

        // Слайды со словами из БД
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + getDbPath());
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT rus, eng FROM dictionary ORDER BY id");

            int count = 0;
            XSLFSlide currentSlide = null;
            XSLFTextBox contentBox = null;

            while (rs.next()) {
                if (count % 10 == 0) {
                    currentSlide = ppt.createSlide();

                    XSLFTextBox headerBox = currentSlide.createTextBox();
                    headerBox.setAnchor(new java.awt.Rectangle(50, 20, 620, 50));
                    XSLFTextParagraph hPara = headerBox.addNewTextParagraph();
                    hPara.setTextAlign(TextAlign.CENTER);
                    XSLFTextRun hRun = hPara.addNewTextRun();
                    hRun.setText("Словарь");
                    hRun.setFontSize(24.0);
                    hRun.setFontColor(new Color(0, 0, 128));
                    hRun.setBold(true);

                    contentBox = currentSlide.createTextBox();
                    contentBox.setAnchor(new java.awt.Rectangle(50, 80, 620, 420));
                }

                XSLFTextParagraph para = contentBox.addNewTextParagraph();
                XSLFTextRun rusRun = para.addNewTextRun();
                rusRun.setText(rs.getString("rus"));
                rusRun.setFontSize(20.0);
                rusRun.setFontColor(new Color(0, 0, 200));
                rusRun.setBold(true);

                XSLFTextRun sepRun = para.addNewTextRun();
                sepRun.setText("  -  ");
                sepRun.setFontSize(20.0);

                XSLFTextRun engRun = para.addNewTextRun();
                engRun.setText(rs.getString("eng"));
                engRun.setFontSize(20.0);
                engRun.setFontColor(new Color(0, 128, 0));

                count++;
            }

            rs.close();
            conn.close();
        } catch (Exception e) {
            XSLFSlide errorSlide = ppt.createSlide();
            XSLFTextBox errBox = errorSlide.createTextBox();
            errBox.setAnchor(new java.awt.Rectangle(50, 200, 620, 60));
            XSLFTextParagraph errPara = errBox.addNewTextParagraph();
            XSLFTextRun errRun = errPara.addNewTextRun();
            errRun.setText("Ошибка БД: " + e.getMessage());
            errRun.setFontSize(18.0);
            errRun.setFontColor(new Color(255, 0, 0));
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        response.setHeader("Content-Disposition", "attachment; filename=\"dictionary.pptx\"");

        ServletOutputStream sos = response.getOutputStream();
        ppt.write(sos);
        sos.flush();
        ppt.close();
    }
}
