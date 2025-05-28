package packages.MainForm;

import packages.Classes.Book;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import packages.DB.BookDAO;
import packages.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManagerApp extends JFrame {
    private JPanel topBar;
    private JPanel mainContentPanel; //
    private JComboBox<String> sortCombo;
    private JComboBox<String> orderCombo;
    private JPanel topResultPanel;
    private JScrollPane scrollPane;
    private JPanel resultContentPanel;

    private List<Book> lastSearchResults = null;
    private String lastKeyword = "";

    public BookManagerApp() {
        setTitle("도서 관리 프로그램 v1.0");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.width * 0.75), (int)(screenSize.height * 0.75));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        //  topBar 구성
        topBar = new JPanel(new MigLayout("fillx", "", "[]"));
        topBar.add(new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/icon.png"))
                        .getImage().getScaledInstance(70, 50, Image.SCALE_SMOOTH)
        )), "align left");

        JPanel searchPanel = new JPanel(new MigLayout("", "[][][]", "[]"));
        // 1. 필드 선언
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("제목", "title");
        categoryMap.put("저자", "author");
        categoryMap.put("출판사", "publisher");

// 2. 콤보박스에 표시할 항목
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"제목", "저자", "출판사"});

        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("검색");

        searchPanel.add(categoryBox);
        searchPanel.add(searchField, "wmin 250");
        searchPanel.add(searchBtn);
        topBar.add(Box.createHorizontalGlue(), "pushx");
        topBar.add(searchPanel, "align center");

        JButton loginBtn = new JButton("로그인");
        topBar.add(loginBtn, "align right, gapleft 50");

        add(topBar, BorderLayout.NORTH);
        //보더로 바꿔서 탑바 고정시킴..
        mainContentPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[]"));
        add(mainContentPanel, BorderLayout.CENTER);

        //  초기화면
        showMainView();

        //  콤보박스 초기화
        sortCombo = new JComboBox<>(new String[]{"정렬없음", "제목순", "재고순"});
        orderCombo = new JComboBox<>(new String[]{"오름차순", "내림차순"});

        //  검색 이벤트
        searchBtn.addActionListener(e -> {
            String displayText = (String) categoryBox.getSelectedItem(); // ex. "제목"
            String category = categoryMap.get(displayText); // 실제 DB 컬럼명 ex. "title"
            String keyword = searchField.getText();
            String sortBy = (String) sortCombo.getSelectedItem();
            String orderBy = (String) orderCombo.getSelectedItem();

            BookDAO dao = new BookDAO();
            List<Book> results = dao.searchBooks(category, keyword, sortBy, orderBy);

            lastSearchResults = results;
            lastKeyword = keyword;

            showSearchResults(results, keyword);
        });
    }

    private void showMainView() {
        mainContentPanel.removeAll();

        JLabel bestTitle = new JLabel("최근 베스트셀러");
        bestTitle.setFont(bestTitle.getFont().deriveFont(Font.BOLD, 20f));
        mainContentPanel.add(bestTitle, "align left");

        JPanel bookPanel = new JPanel(new MigLayout("", "[][][]", "[]"));
        bookPanel.add(new JButton("<"), "align left, split 3");

        JPanel bookRow = new JPanel(new MigLayout("", "[][][][][]", "[]"));
        List<Book> books = List.of(
                Book.builder().title("베스트셀러 절대로 읽지 마라").imagePath("book1.jpg").build(),
                Book.builder().title("세이노의 가르침").imagePath("book2.png").build(),
                Book.builder().title("죽고 싶지만 떡볶이는 먹고 싶어.").imagePath("book3.jpeg").build(),
                Book.builder().title("돈의 심리학").imagePath("book4.jpg").build()
        );
        for (Book book : books) {
            JPanel card = createBookCard(book.getTitle(), book.getImagePath());
            bookRow.add(card);
        }
        bookPanel.add(bookRow, "align center");
        bookPanel.add(new JButton(">"));

        mainContentPanel.add(bookPanel, "align center");

        JLabel totalRental = new JLabel("누적 대여량 높은 순");
        totalRental.setFont(totalRental.getFont().deriveFont(Font.BOLD, 20f));
        mainContentPanel.add(totalRental, "align left");

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showSearchResults(List<Book> results, String keyword) {
        mainContentPanel.removeAll();

        topResultPanel = new JPanel(new MigLayout("fillx", "[grow][]", "[]"));
        JLabel resultMsg = new JLabel("검색어 '" + keyword + "' 총 " + results.size() + "건 검색되었습니다.");
        resultMsg.setFont(resultMsg.getFont().deriveFont(Font.BOLD, 16f));
        topResultPanel.add(resultMsg, "align left");

        JPanel filterPanel = new JPanel();
        filterPanel.add(sortCombo);
        filterPanel.add(orderCombo);
        JComboBox<String> countCombo = new JComboBox<>(new String[]{"10건", "20건", "30건", "50건"});
        filterPanel.add(countCombo);
        topResultPanel.add(filterPanel, "align right");

        resultContentPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[]10[]"));
        for (Book book : results) {
            JPanel row = new JPanel(new MigLayout("", "[][grow]", "[]"));
            URL imgURL = getClass().getResource("/" + book.getImagePath());
            JLabel imgLabel = new JLabel();
            if (imgURL != null) {
                imgLabel.setIcon(new ImageIcon(
                        new ImageIcon(imgURL).getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH)
                ));
            } else {
                imgLabel.setText("이미지 없음");
            }

            String text = "<html><b>" + book.getTitle() + "</b>"
                    + "<br>  저자: " + book.getAuthor()
                    + "<br>  출판사: " + book.getPublisher()
                    + "<br>  재고수량: " + book.getStock()
                    + "<br>  대여 가능 여부: " + (book.getStock() > 0 ? "가능" : "불가") + "</html>";
            JLabel infoLabel = new JLabel(text);

            row.add(imgLabel);
            row.add(infoLabel, "growx");

            row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showBookDetail(book);
                }
            });

            resultContentPanel.add(row, "growx");
        }

        scrollPane = new JScrollPane(resultContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel bottomPanel = new JPanel(new MigLayout("fillx", "[grow][]", "[]"));
        JButton backBtn = new JButton("뒤로가기");
        backBtn.addActionListener(ev -> showMainView());
        bottomPanel.add(new JLabel());
        bottomPanel.add(backBtn, "align right");

        mainContentPanel.add(topResultPanel, "growx");
        mainContentPanel.add(scrollPane, "grow");
        mainContentPanel.add(bottomPanel, "growx");

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showBookDetail(Book book) {
        mainContentPanel.removeAll();

        JPanel detailPanel = new JPanel(new MigLayout("wrap 2,align center, insets 20",
                                                    "[grow 0]20[grow 0]",
                                                      "[]20[]20[]20[]"  ));

        URL imgURL = getClass().getResource("/" + book.getImagePath());
        JLabel imgLabel = new JLabel();
        if (imgURL != null) {
            imgLabel.setIcon(new ImageIcon(
                    new ImageIcon(imgURL).getImage().getScaledInstance(160, 220, Image.SCALE_SMOOTH)
            ));
        } else {
            imgLabel.setText("이미지 없음");
        }
        detailPanel.add(imgLabel, "span 1 5");

        detailPanel.add(new JLabel("<html><h2>" + book.getTitle() + "</h2></html>"), "wrap");
        detailPanel.add(new JLabel("저자: " + book.getAuthor()), "wrap");
        detailPanel.add(new JLabel("출판사: " + book.getPublisher()), "wrap");
        detailPanel.add(new JLabel("재고수량: " + book.getStock()), "wrap");
        detailPanel.add(new JLabel("대여가능여부: " + (book.getStock() > 0 ? "가능" : "불가")), "wrap");

        // 버튼 패널
        // 버튼 패널
        JPanel btnPanel = new JPanel(new MigLayout("", "[]10[]", "[]")); // 버튼 사이 10px 간격
        btnPanel.setOpaque(false); // 배경 없애기 (선택)

// 버튼들
        JButton rentBtn = new JButton("대여하기");
        rentBtn.setPreferredSize(new Dimension(120, 40));
        JButton returnBtn = new JButton("반납하기");
        returnBtn.setPreferredSize(new Dimension(120, 40));

        btnPanel.add(rentBtn);
        btnPanel.add(returnBtn);

// 📌 detailPanel에 가운데 정렬로 추가
        detailPanel.add(btnPanel, "span, align center, wrap");

        JButton backBtn = new JButton("뒤로가기");
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.addActionListener(e -> showSearchResults(lastSearchResults, lastKeyword));
        detailPanel.add(backBtn, "span, align right");
        mainContentPanel.add(detailPanel, "grow");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel createBookCard(String title, String imagePath) {
        JPanel panel = new JPanel(new MigLayout("wrap 1", "center", "[]10[]"));
        URL imgURL = getClass().getResource("/" + imagePath);
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
