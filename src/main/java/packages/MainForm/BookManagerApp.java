package packages.MainForm;

import packages.Classes.Book;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import packages.DB.BookDAO;
import packages.DB.DBConnector;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.awt.*;
import java.net.URL;

public class BookManagerApp extends JFrame{
    public BookManagerApp() {
        setTitle("도서 관리 프로그램 v1.0");

        //모니터 크기 기준 4/3 크기 설정
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.width * 0.75);
        int height = (int)(screenSize.height * 0.75);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        //DB 연결 테스트
        try (Connection conn = DBConnector.getConnection()) {
            System.out.println("DB 연결 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //debug로 셀 경계를 보여줌. 테스트 후 제거
        JPanel mainPanel = new JPanel(new MigLayout("wrap 1, de     bug","[grow]", "[]20[]20[]20[]"));
        add(mainPanel);

        JPanel topBar = new JPanel(new MigLayout("fillx", "", "[]"));
        topBar.add(new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/icon.png"))
                        .getImage()
                        .getScaledInstance(70, 50, Image.SCALE_SMOOTH)
        )), "align left");

        JPanel searchPanel = new JPanel(new MigLayout("", "[][][]", "[]"));
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"title", "author", "publisher"});
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("검색");

        searchBtn.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            String keyword = searchField.getText();

            BookDAO dao = new BookDAO();
            List<Book> results = dao.searchBooks(category, keyword);

            for (Book book : results) {
                System.out.println(book);
            }
        });

        searchPanel.add(categoryBox);
        searchPanel.add(searchField, "wmin 250");
        searchPanel.add(searchBtn);
        topBar.add(Box.createHorizontalGlue(), "pushx");
        topBar.add(searchPanel, "align center");

        JButton loginbtn = new JButton("로그인");
        loginbtn.addActionListener(e -> {
            new LoginForm(this).setVisible(true);
        });

        topBar.add(loginbtn, "align right, gapleft 50");

        mainPanel.add(topBar, "growx");

        // 첫 번째 세션
        JLabel bestTitle = new JLabel("최근 베스트셀러");
        bestTitle.setFont(bestTitle.getFont().deriveFont(Font.BOLD, 20f));
        mainPanel.add(bestTitle, "align left");

        JPanel bookPanel = new JPanel(new MigLayout("", "[][][]", "[]"));
        bookPanel.add(new JButton("<"), "align left, split 3");

        // 책 리스트 + 좌우 슬라이드 버튼
        JPanel bookRow = new JPanel(new MigLayout("", "[][][][][]", "[]"));
        // 추후 이미지 경로, 제목은 DB에서 가져와야 함.
        List<Book> books = List.of(
                Book.builder().title("베스트셀러 절대로 읽지 마라").imagePath("book1.jpg").build(),
                Book.builder().title("세이노의 가르침").imagePath("book2.png").build(),
                Book.builder().title("죽고 싶지만 떡볶이는 먹고 싶어.").imagePath("book3.jpeg").build(),
                Book.builder().title("돈의 심리학").imagePath("book4.jpg").build(),
                Book.builder().title("돈의 심리학").imagePath("book4.jpg").build()
        );
        for (Book book : books) {
            bookRow.add(createBookCard(book.getTitle(), book.getImagePath()));
        }
        bookPanel.add(bookRow, "align center");

        bookPanel.add(new JButton(">"));

        mainPanel.add(bookPanel, "align center");

        // 두 번째 세션
        JLabel totalRental = new JLabel("누적 대여량 높은 순");
        totalRental.setFont(totalRental.getFont().deriveFont(Font.BOLD, 20f));
        mainPanel.add(totalRental, "align left");
    }

    // 책 카드 구현
    private JPanel createBookCard(String title, String imagePath) {
        JPanel panel = new JPanel(new MigLayout("wrap 1", "center", "[]10[]"));
        URL imgURL =  getClass().getResource("/" + imagePath);
        JLabel image = new JLabel();
        if (imgURL != null) {
            image.setIcon(new ImageIcon(
                    new ImageIcon(imgURL).getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH)
            ));
        } else {
            image.setText("이미지 없음");
        }

        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(image);
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(BookManagerApp::new);
    }
}