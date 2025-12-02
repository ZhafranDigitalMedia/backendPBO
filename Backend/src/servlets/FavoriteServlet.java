package servlets;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DatabaseConnection;
import jakarta.servlet.http.HttpServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/api/favorites"})
public class FavoriteServlet extends HttpServlet {

    // GET -> ambil daftar favorite
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String userIdParam = req.getParameter("userId");
        JSONArray jsonArr = new JSONArray();

        String sql;
        if (userIdParam != null && !userIdParam.isEmpty()) {
            sql = "SELECT id, user_id, film_title, poster FROM favorites WHERE user_id = ? ORDER BY id DESC";
        } else {
            sql = "SELECT id, user_id, film_title, poster FROM favorites ORDER BY id DESC";
        }

        try (Connection conn = DatabaseConnection.initializeDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (userIdParam != null && !userIdParam.isEmpty()) {
                ps.setInt(1, Integer.parseInt(userIdParam));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getInt("id"));
                    obj.put("user_id", rs.getInt("user_id"));
                    obj.put("film_title", rs.getString("film_title"));
                    obj.put("poster", rs.getString("poster"));

                    jsonArr.put(obj);
                }
            }

            resp.getWriter().write(jsonArr.toString());
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Database error\"}");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    // POST -> tambah favorite baru
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = req.getReader()) {
            while ((line = br.readLine()) != null) sb.append(line);
        }

        JSONObject body = new JSONObject(sb.toString());

        if (!body.has("userId") || !body.has("filmTitle")) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"userId dan filmTitle wajib\"}");
            return;
        }

        int userId = body.getInt("userId");
        String filmTitle = body.getString("filmTitle");
        String poster = body.optString("poster", "");

        try (Connection conn = DatabaseConnection.initializeDatabase();) {

            // Cek duplikat
            String checkSql = "SELECT id FROM favorites WHERE user_id = ? AND film_title = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, userId);
                ps.setString(2, filmTitle);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        resp.setStatus(409);
                        resp.getWriter().write("{\"error\":\"Film sudah ada di favorites\"}");
                        return;
                    }
                }
            }

            // Insert data baru
            String insertSql = "INSERT INTO favorites (user_id, film_title, poster) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setString(2, filmTitle);
                ps.setString(3, poster);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        body.put("id", keys.getInt(1));
                    }
                }
            }

            resp.setStatus(201);
            resp.getWriter().write(body.toString());

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Database error\"}");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    // DELETE -> hapus favorite
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Parameter id wajib\"}");
            return;
        }

        int id = Integer.parseInt(idParam);

        try (Connection conn = DatabaseConnection.initializeDatabase();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM favorites WHERE id = ?")) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Favorite tidak ditemukan\"}");
                return;
            }

            resp.getWriter().write("{\"message\":\"Favorite dihapus\"}");

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Database error\"}");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
