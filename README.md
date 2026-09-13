# Beyond the Feed

![React](https://img.shields.io/badge/frontend-React-blue) ![Java](https://img.shields.io/badge/backend-Java%208-orange) ![MySQL](https://img.shields.io/badge/database-MySQL-lightgrey)

A full-stack digital magazine exploring Instagram's influence on modern life — creativity, online communities, business, digital habits, trends, and the changing ways we connect and express ourselves through social media.


## Features

- Browse articles by category
- Read full article bodies with metadata
- Comment on articles (approved comments shown)
- Seeded preview content when API isn't running

## Structure

- `frontend/` React, JSX, Bootstrap, React Router, CSS and API integration
- `backend/` Java 8 Maven WAR, Servlet MVC, JDBC and JSP
- `database/` MySQL schema, foreign keys, constraints and seed content

## Prerequisites

- Node.js 18+ and npm
- Java 8 (JDK)
- Maven 3.6+
- MySQL 8+
- Tomcat 9+

## Run the frontend

```
cd frontend
npm install
npm run dev
```

The UI calls `GET /api/articles`. It shows seeded preview content until the Java API is available.

## Core API flow

- `GET /api/categories` loads category names and descriptions from MySQL.
- `GET /api/articles` loads published articles, including their category and full body.
- `GET /api/articles/{slug}` loads one article when a detail route needs it.
- `GET /api/comments?articleId={id}` loads approved comments for an article.
- `POST /api/comments` saves a comment with `{ "articleId": 1, "body": "..." }`.

The React app calls these endpoints through the Vite `/api` proxy. Java Servlets handle HTTP, DAOs contain the SQL, and `DatabaseConnection` supplies the JDBC connection.

### Example: GET /api/articles response

```json
[
  {
    "id": 1,
    "slug": "algorithm-anxiety",
    "title": "Algorithm Anxiety",
    "category": "Digital Habits",
    "body": "..."
  }
]
```

### Example: POST /api/comments request

```json
{
  "articleId": 1,
  "body": "Great read!"
}
```

## Run the API

1. Create `database/schema.sql` in MySQL.
2. Set `DB_URL`, `DB_USER`, and `DB_PASSWORD` in `backend/src/main/java/com/beyondthefeed/util/DatabaseConnection.java` or supply equivalent environment variables.
3. Build `cd backend; mvn package` and deploy `target/beyond-the-feed-api.war` to Tomcat 9+.
4. Point the frontend dev proxy to the Tomcat port in `frontend/vite.config.js`.

## Troubleshooting

- **Tomcat 404 on `/api/*`**: confirm WAR deployed and context path matches proxy config in `vite.config.js`.
- **MySQL connection refused**: check `DB_URL`, `DB_USER`, `DB_PASSWORD` and that MySQL service running.
- **CORS errors in dev**: confirm Vite `/api` proxy target points at running Tomcat port.

## Contributing

Pull requests welcome. For big changes, open an issue first to discuss.

## License

<!-- Add license, e.g. MIT -->
This project is unlicensed. Add a `LICENSE` file to set terms.