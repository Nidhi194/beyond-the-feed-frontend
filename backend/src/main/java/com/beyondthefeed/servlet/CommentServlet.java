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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/api/comments/*")
public class CommentServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String articleId = request.getParameter("articleId");
        List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
        try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement("SELECT c.id, c.body, c.created_at, COALESCE(u.name, 'Reader') AS author FROM comments c LEFT JOIN users u ON c.user_id = u.id WHERE c.article_id = ? AND c.approved = TRUE ORDER BY c.created_at DESC")) {
            statement.setInt(1, Integer.parseInt(articleId)); try (ResultSet results = statement.executeQuery()) { while (results.next()) { Map<String, Object> item = new HashMap<String, Object>(); item.put("id", results.getInt("id")); item.put("body", results.getString("body")); item.put("author", results.getString("author")); item.put("createdAt", results.getTimestamp("created_at")); comments.add(item); } }
            response.setContentType("application/json"); mapper.writeValue(response.getWriter(), comments);
        } catch (Exception exception) { response.sendError(400, "Unable to load comments"); }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonNode input = mapper.readTree(request.getReader());
        if (input == null || !input.hasNonNull("articleId") || !input.hasNonNull("body") || input.get("body").asText().trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(response.getWriter(), Collections.singletonMap("message", "A comment is required"));
            return;
        }
        try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement("INSERT INTO comments (article_id, user_id, body) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, input.get("articleId").asInt());
            if (input.hasNonNull("userId")) statement.setInt(2, input.get("userId").asInt()); else statement.setNull(2, Types.INTEGER);
            statement.setString(3, input.get("body").asText().trim());
            statement.executeUpdate();
            response.setStatus(HttpServletResponse.SC_CREATED);
            Map<String, String> result = new HashMap<String, String>();
            result.put("message", "Comment added");
            mapper.writeValue(response.getWriter(), result);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(response.getWriter(), Collections.singletonMap("message", "Unable to save comment"));
            log("Comment insert failed", exception);
        }
    }
}
