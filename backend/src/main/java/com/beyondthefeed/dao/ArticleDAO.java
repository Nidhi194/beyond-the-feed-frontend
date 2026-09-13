package com.beyondthefeed.dao;

import com.beyondthefeed.model.Article;
import com.beyondthefeed.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    private static final String LIST_SQL = "SELECT a.id, a.title, a.slug, a.excerpt, a.body, c.name AS category, c.slug AS category_slug, COALESCE(u.name, 'Beyond the Feed') AS author, a.read_time, a.image_url, a.featured, a.source, a.source_url, a.published_at FROM articles a JOIN categories c ON a.category_id = c.id LEFT JOIN users u ON a.author_id = u.id WHERE a.published = TRUE ORDER BY a.featured DESC, a.published_at DESC, a.created_at DESC";

    public List<Article> findPublished() throws SQLException {
        List<Article> articles = new ArrayList<Article>();
        try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement(LIST_SQL); ResultSet results = statement.executeQuery()) {
            while (results.next()) articles.add(map(results));
        }
        return articles;
    }

    public Article findBySlug(String slug) throws SQLException {
        String sql = LIST_SQL.replace("ORDER BY a.featured DESC, a.published_at DESC, a.created_at DESC", "AND a.slug = ?");
        try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, slug);
            try (ResultSet results = statement.executeQuery()) { return results.next() ? map(results) : null; }
        }
    }

    private Article map(ResultSet result) throws SQLException {
        return new Article(result.getInt("id"), result.getString("title"), result.getString("slug"), result.getString("excerpt"), result.getString("body"), result.getString("category"), result.getString("category_slug"), result.getString("author"), result.getString("read_time"), result.getString("image_url"), result.getBoolean("featured"), result.getString("source"), result.getString("source_url"), result.getString("published_at"));
    }
}
