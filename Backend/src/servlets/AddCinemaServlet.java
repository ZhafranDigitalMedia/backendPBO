package servlets;

import utils.DatabaseConnection;
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/add-cinema")
public class AddCinemaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Ambil data dari request body atau form
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String priceStr = request.getParameter("price");

        if (name == null || priceStr == null || name.isEmpty() || priceStr.isEmpty()) {
            out.println("{\"status\":\"error\",\"message\":\"Nama dan harga wajib diisi\"}");
            return;
        }

        try {
            int price = Integer.parseInt(priceStr);

            try (Connection conn = DatabaseConnection.initializeDatabase()) {
                String sql = "INSERT INTO cinemas (name, location, price) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, location);
                stmt.setInt(3, price);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    out.println("{\"status\":\"success\",\"message\":\"Cinema berhasil ditambahkan!\"}");
                } else {
                    out.println("{\"status\":\"error\",\"message\":\"Gagal menambahkan cinema.\"}");
                }

                stmt.close();
            }

        } catch (NumberFormatException e) {
            out.println("{\"status\":\"error\",\"message\":\"Harga harus berupa angka.\"}");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
