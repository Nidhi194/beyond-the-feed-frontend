package com.beyondthefeed.servlet;

import com.beyondthefeed.dao.ArticleDAO;
import com.beyondthefeed.model.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/api/articles/*")
public class ArticleServlet extends HttpServlet {
    private final ArticleDAO articleDAO = new ArticleDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String path = request.getPathInfo();
            if (path != null && path.length() > 1) {
                Article article = articleDAO.findBySlug(path.substring(1));
                if (article == null) { response.setStatus(HttpServletResponse.SC_NOT_FOUND); mapper.writeValue(response.getWriter(), Collections.singletonMap("message", "Article not found")); return; }
                mapper.writeValue(response.getWriter(), article);
            } else {
                List<Article> articles = articleDAO.findPublished();
                mapper.writeValue(response.getWriter(), articles);
            }
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(response.getWriter(), Collections.singletonMap("message", "Unable to load articles"));
            log("Article query failed", exception);
        }
    }
}
