package com.beyondthefeed.servlet;

import com.beyondthefeed.util.DatabaseConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/api/admin/articles/*")
public class ArticleAdminServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { JsonNode input = mapper.readTree(request.getReader()); String sql = "INSERT INTO articles (category_id, author_id, title, slug, excerpt, body, image_url, read_time, featured) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; executeWrite(input, sql, false, response); }
    @Override protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException { JsonNode input = mapper.readTree(request.getReader()); String sql = "UPDATE articles SET category_id=?, title=?, slug=?, excerpt=?, body=?, image_url=?, read_time=?, featured=? WHERE id=?"; executeWrite(input, sql, true, response); }
    @Override protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException { try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement("DELETE FROM articles WHERE id=?")) { statement.setInt(1, Integer.parseInt(request.getPathInfo().substring(1))); statement.executeUpdate(); response.setStatus(204); } catch (Exception exception) { response.sendError(400, "Unable to delete article"); } }
    private void executeWrite(JsonNode input, String sql, boolean update, HttpServletResponse response) throws IOException { try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement(sql)) { statement.setInt(1, input.get("categoryId").asInt()); int offset = 0; if (!update) { statement.setInt(2, input.get("authorId").asInt()); offset = 1; } statement.setString(2 + offset, input.get("title").asText()); statement.setString(3 + offset, input.get("slug").asText()); statement.setString(4 + offset, input.get("excerpt").asText()); statement.setString(5 + offset, input.get("body").asText()); statement.setString(6 + offset, input.get("imageUrl").asText()); statement.setString(7 + offset, input.get("readTime").asText()); statement.setBoolean(8 + offset, input.path("featured").asBoolean(false)); if (update) statement.setInt(9, input.get("id").asInt()); statement.executeUpdate(); response.setStatus(update ? 200 : 201); mapper.writeValue(response.getWriter(), "Article saved"); } catch (Exception exception) { response.sendError(400, "Unable to save article"); } }
}
