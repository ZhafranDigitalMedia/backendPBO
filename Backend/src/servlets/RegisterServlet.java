package servlets;

import utils.DatabaseConnection;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String no_telp = request.getParameter("no_telp"); // pakai nama yang SAMA dengan Postman

        System.out.println("DEBUG - name: " + name);
        System.out.println("DEBUG - email: " + email);
        System.out.println("DEBUG - password: " + password);
        System.out.println("DEBUG - no_telp: " + no_telp);

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String query = "INSERT INTO users (name, email, password, no_telp) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, no_telp != null ? no_telp : "");

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                out.println("{\"message\": \"✅ Registrasi berhasil!\"}");
            } else {
                out.println("{\"message\": \"❌ Registrasi gagal!\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}
