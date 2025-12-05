package servlets;

import utils.DatabaseConnection;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.sql.*;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                // ambil data user lengkap
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                String noTelp = rs.getString("no_telp");

                // generate token sederhana
                String token = java.util.UUID.randomUUID().toString();

                // objek user
                JSONObject userObj = new JSONObject();
                userObj.put("id", id);
                userObj.put("name", name);
                userObj.put("email", email);
                userObj.put("role", role);
                userObj.put("no_telp", noTelp);

                // response JSON final
                JSONObject json = new JSONObject();
                json.put("message", "Login berhasil!");
                json.put("token", token);
                json.put("user", userObj);

                out.print(json.toString());

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Email atau password salah!\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
