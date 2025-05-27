package packages.DB;

import packages.Classes.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> searchBooks(String category, String keyword) {
        List<Book> list = new ArrayList<>();

        String sql = "select * from book where " + category + " LIKE ?";
        try (Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    list.add(Book.builder()
                            .id(rs.getInt("id"))
                            .title(rs.getString("title"))
                            .author(rs.getString("author"))
                            .publisher(rs.getString("publisher"))
                            .build());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return list;
    }

}
