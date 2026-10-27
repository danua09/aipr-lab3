import java.sql.*;

public class InitDB {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        String dbPath = args.length > 0 ? args[0] : "dictionary.db";
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement st = conn.createStatement();

        st.executeUpdate("CREATE TABLE IF NOT EXISTS dictionary ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "rus TEXT NOT NULL, "
            + "eng TEXT NOT NULL)");

        st.executeUpdate("DELETE FROM dictionary");

        String[][] words = {
            {"кот", "cat"}, {"собака", "dog"}, {"дом", "house"},
            {"книга", "book"}, {"вода", "water"}, {"огонь", "fire"},
            {"дерево", "tree"}, {"солнце", "sun"}, {"луна", "moon"},
            {"звезда", "star"}, {"небо", "sky"}, {"земля", "earth"},
            {"река", "river"}, {"море", "sea"}, {"гора", "mountain"},
            {"цветок", "flower"}, {"птица", "bird"}, {"рыба", "fish"},
            {"хлеб", "bread"}, {"молоко", "milk"}
        };

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO dictionary (rus, eng) VALUES (?, ?)");
        for (String[] w : words) {
            ps.setString(1, w[0]);
            ps.setString(2, w[1]);
            ps.executeUpdate();
        }

        System.out.println("Database created with " + words.length + " words: " + dbPath);
        conn.close();
    }
}
