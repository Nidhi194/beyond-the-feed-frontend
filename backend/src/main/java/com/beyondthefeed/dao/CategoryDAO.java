package com.beyondthefeed.dao;

import com.beyondthefeed.model.Category;
import com.beyondthefeed.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<Category>();
        String sql = "SELECT id, name, slug, description FROM categories ORDER BY id";
        try (Connection connection = DatabaseConnection.open(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet results = statement.executeQuery()) {
            while (results.next()) categories.add(new Category(results.getInt("id"), results.getString("name"), results.getString("slug"), results.getString("description")));
        }
        return categories;
    }
}
