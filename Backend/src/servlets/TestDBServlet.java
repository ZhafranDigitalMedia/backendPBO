package servlets;

import utils.DatabaseConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.Connection;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = DatabaseConnection.initializeDatabase();
            if (conn != null) {
                out.println("<h2>✅ Koneksi ke database berhasil!</h2>");
            } else {
                out.println("<h2>❌ Gagal koneksi ke database!</h2>");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
