package servlets;

import utils.DatabaseConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TicketServlet extends HttpServlet {

    // =============================
    // POST -> Tambah tiket baru
    // =============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String userId = request.getParameter("user_id");
        String cinemaId = request.getParameter("cinema_id");
        String filmTitle = request.getParameter("film_title");
        String schedule = request.getParameter("schedule");
        String seat = request.getParameter("seat");
        String price = request.getParameter("price");

        if (userId == null || cinemaId == null || filmTitle == null || seat == null || price == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Missing required fields\"}");
            return;
        }

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "INSERT INTO tickets (user_id, cinema_id, film_title, schedule, seat, price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(userId));
            ps.setInt(2, Integer.parseInt(cinemaId));
            ps.setString(3, filmTitle);
            ps.setString(4, schedule);
            ps.setString(5, seat);
            ps.setInt(6, Integer.parseInt(price));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.print("{\"message\":\"Ticket created successfully\"}");
            } else {
                out.print("{\"error\":\"Failed to create ticket\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // =============================
    // GET -> Lihat tiket (user / admin)
    // =============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String userId = request.getParameter("user_id");
        String all = request.getParameter("all"); // untuk admin

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql;
            PreparedStatement ps;

            if (all != null && all.equalsIgnoreCase("true")) {
                // Admin melihat semua tiket
                sql = "SELECT t.*, u.name AS user_name, c.name AS cinema_name " +
                      "FROM tickets t " +
                      "JOIN users u ON t.user_id = u.id " +
                      "JOIN cinemas c ON t.cinema_id = c.id " +
                      "ORDER BY t.order_date DESC";
                ps = conn.prepareStatement(sql);
            } else if (userId != null) {
                // User melihat tiket miliknya
                sql = "SELECT t.*, c.name AS cinema_name " +
                      "FROM tickets t " +
                      "JOIN cinemas c ON t.cinema_id = c.id " +
                      "WHERE t.user_id = ? ORDER BY t.order_date DESC";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(userId));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Missing user_id or 'all=true'\"}");
                return;
            }

            ResultSet rs = ps.executeQuery();
            JSONArray tickets = new JSONArray();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("film_title", rs.getString("film_title"));
                obj.put("cinema_name", rs.getString("cinema_name"));
                obj.put("schedule", rs.getString("schedule"));
                obj.put("seat", rs.getString("seat"));
                obj.put("price", rs.getInt("price"));
                obj.put("order_date", rs.getString("order_date"));

                if (all != null && all.equalsIgnoreCase("true")) {
                    obj.put("user_name", rs.getString("user_name"));
                }

                tickets.put(obj);
            }

            out.print(tickets.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // =============================
    // DELETE -> Hapus tiket
    // =============================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Missing id\"}");
            return;
        }

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "DELETE FROM tickets WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.print("{\"message\":\"Ticket deleted successfully\"}");
            } else {
                out.print("{\"error\":\"Ticket not found\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
