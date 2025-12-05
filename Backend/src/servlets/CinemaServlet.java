package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import utils.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/cinema")
public class CinemaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String price = request.getParameter("price");

        if (name == null || location == null || price == null) {
            out.println("Missing parameters!");
            return;
        }

        try {
            Connection con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO cinema(name, location, price) VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, location);
            pst.setInt(3, Integer.parseInt(price));

            int rows = pst.executeUpdate();

            out.println("Inserted: " + rows + " row(s)");

            pst.close();
            con.close();

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
