package com.beyondthefeed.servlet;

import com.beyondthefeed.util.DatabaseConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getPathInfo(); JsonNode input = mapper.readTree(request.getReader()); response.setContentType("application/json");
        if ("/register".equals(action)) register(input, response); else if ("/login".equals(action)) login(input, response); else response.sendError(404);
    }
    private void register(JsonNode input, HttpServletResponse response) throws IOException { try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) { statement.setString(1, input.get("name").asText()); statement.setString(2, input.get("email").asText()); statement.setString(3, input.get("password").asText()); statement.executeUpdate(); response.setStatus(201); Map<String, String> result = new HashMap<String, String>(); result.put("message", "Account created"); mapper.writeValue(response.getWriter(), result); } catch (SQLException exception) { response.sendError(409, "Email already registered"); } }
    private void login(JsonNode input, HttpServletResponse response) throws IOException { try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement("SELECT id, name, email, role FROM users WHERE email = ? AND password_hash = ?")) { statement.setString(1, input.get("email").asText()); statement.setString(2, input.get("password").asText()); try (ResultSet result = statement.executeQuery()) { if (!result.next()) { response.sendError(401, "Invalid credentials"); return; } Map<String, Object> user = new HashMap<String, Object>(); user.put("id", result.getInt("id")); user.put("name", result.getString("name")); user.put("email", result.getString("email")); user.put("role", result.getString("role")); mapper.writeValue(response.getWriter(), user); } } catch (SQLException exception) { response.sendError(500, "Unable to login"); } }
}
