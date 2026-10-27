import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class MyServlet extends HttpServlet {

    private String getDbPath() {
        String path = getServletContext().getRealPath("/WEB-INF/dictionary.db");
        return path;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String rusWord = request.getParameter("txt");
        String engWord = request.getParameter("eng_txt");

        String result = "";
        String direction = "";

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + getDbPath());
            Statement st = conn.createStatement();

            if (rusWord != null && !rusWord.trim().isEmpty()) {
                direction = "рус -> англ";
                ResultSet rs = st.executeQuery(
                    "SELECT eng FROM dictionary WHERE rus = '" + rusWord.trim().toLowerCase() + "'");
                if (rs.next()) {
                    result = rs.getString("eng");
                } else {
                    result = "Слово не найдено";
                }
                rs.close();
            } else if (engWord != null && !engWord.trim().isEmpty()) {
                direction = "англ -> рус";
                ResultSet rs = st.executeQuery(
                    "SELECT rus FROM dictionary WHERE eng = '" + engWord.trim().toLowerCase() + "'");
                if (rs.next()) {
                    result = rs.getString("rus");
                } else {
                    result = "Word not found";
                }
                rs.close();
            }

            conn.close();
        } catch (Exception e) {
            result = "Ошибка БД: " + e.getMessage();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Результат перевода</title>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head>");
        out.println("<body bgcolor='#aaccff'>");
        out.println("<Font color='green' size='8'>Результат перевода</Font><br><br>");
        out.println("<Font color='blue' size='6'>Направление: " + direction + "</Font><br>");
        if (rusWord != null && !rusWord.trim().isEmpty()) {
            out.println("<Font color='blue' size='6'>Слово: " + rusWord + "</Font><br>");
        } else if (engWord != null && !engWord.trim().isEmpty()) {
            out.println("<Font color='blue' size='6'>Word: " + engWord + "</Font><br>");
        }
        out.println("<Font color='red' size='6'>Перевод: " + result + "</Font><br><br>");
        out.println("<a href='dictionary.html'><Font size='5'>Назад к словарю</Font></a>");
        out.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
