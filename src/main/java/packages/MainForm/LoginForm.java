package packages.MainForm;

import net.miginfocom.swing.MigLayout;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JDialog {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public LoginForm(JFrame parent) {
        super(parent, "로그인 / 회원가입", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);

        // 위쪽 여백을 위한 panel
        JPanel outerPanel = new JPanel(new MigLayout("insets 100", "[grow,fill]", "[grow]"));

        cardPanel.add(createLoginPanel(), "login");
        cardPanel.add(createSignupPanel(), "signup");

        outerPanel.add(cardPanel, "grow");
        add(outerPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, insets 0", // 상하좌우 여백
                "[grow,fill]",               // 1열 구조
                "10[]10[]10[]10[]15[]"
        ));

        // 🔹 아이디 입력
        JTextField idField = new JTextField();
        idField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "아이디 입력");
        idField.setPreferredSize(new Dimension(0, 35));
        panel.add(idField, "wmin 250, growx");

        // 🔹 비밀번호 입력 + 👁 버튼
        JPasswordField pwField = new JPasswordField();
        pwField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "비밀번호 입력");
        pwField.setPreferredSize(new Dimension(0, 35));
        JButton eyeBtn = new JButton("👁");
        eyeBtn.setPreferredSize(new Dimension(40, 35));

        JPanel pwPanel = new JPanel(new MigLayout("insets 0", "[grow][40!]", "[]"));
        pwPanel.add(pwField, "wmin 250, growx");
        pwPanel.add(eyeBtn);
        panel.add(pwPanel);

        // 🔹 로그인 버튼
        JButton loginBtn = new JButton("로그인");
        loginBtn.setPreferredSize(new Dimension(0, 40));
        loginBtn.setFocusPainted(false);
        loginBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(loginBtn, "wmin 250, growx");

        // 🔹 아이디 저장 + 찾기 링크
        JCheckBox saveId = new JCheckBox("아이디 저장");
        JLabel findId = new JLabel("아이디 찾기");
        JLabel findPw = new JLabel("비밀번호 찾기");

        findId.setForeground(Color.GRAY);
        findPw.setForeground(Color.GRAY);
        findId.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        findPw.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel optionPanel = new JPanel(new MigLayout("insets 0", "[grow][grow]", "[]"));
        optionPanel.add(saveId, "align left");

        JPanel rightPanel = new JPanel(new MigLayout("insets 0", "[]10[]", "[]"));
        rightPanel.setOpaque(false);
        rightPanel.add(findId);
        rightPanel.add(findPw);

        optionPanel.add(rightPanel, "align right");
        panel.add(optionPanel);

        // 🔹 회원가입 버튼
        JButton joinBtn = new JButton("회원가입");
        joinBtn.setPreferredSize(new Dimension(0, 40));
        joinBtn.setFocusPainted(false);
        panel.add(joinBtn, "wmin 250, growx");

        // 🔁 동작 설정
        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(pwField.getPassword());
            JOptionPane.showMessageDialog(this, "입력된 ID: " + id + "\n비밀번호: " + pw);
            dispose();
        });

        joinBtn.addActionListener(e -> cardLayout.show(cardPanel, "signup"));

        return panel;
    }


    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, insets 0",
                "[grow,fill]",
                "10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]"));

        // 🔹 아이디 입력 + 중복확인
        JTextField idField = new JTextField();
        idField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "아이디 입력");
        JButton checkBtn = new JButton("중복확인");
        idField.setPreferredSize(new Dimension(0, 35));
        checkBtn.setPreferredSize(new Dimension(80, 35));

        JPanel idPanel = new JPanel(new MigLayout("insets 0", "[grow][80!]", "[]"));
        idPanel.add(idField, "wmin 250, growx");
        idPanel.add(checkBtn, "w 80!");
        panel.add(idPanel);

        // 🔹 아이디 사용 가능 메시지
        JLabel idMsg = new JLabel("사용 가능한 아이디입니다.");
        idMsg.setForeground(Color.BLUE);
        panel.add(idMsg, "gapleft 5, wrap");

        // 🔹 비밀번호 입력 + 보기 버튼
        JPasswordField pwField = new JPasswordField();
        pwField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "비밀번호 입력");
        JButton pwEye = new JButton("👁");
        pwField.setPreferredSize(new Dimension(0, 35));
        pwEye.setPreferredSize(new Dimension(40, 35));

        JPanel pwPanel = new JPanel(new MigLayout("insets 0", "[grow][40!]", "[]"));
        pwPanel.add(pwField, "wmin 250, growx");
        pwPanel.add(pwEye, "w 40!");
        panel.add(pwPanel);

        // 🔹 비밀번호 확인 + 보기 버튼
        JPasswordField pwConfirm = new JPasswordField();
        pwConfirm.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "비밀번호 확인");
        JButton pwEye2 = new JButton("👁");
        pwConfirm.setPreferredSize(new Dimension(0, 35));
        pwEye2.setPreferredSize(new Dimension(40, 35));

        JPanel pwConfirmPanel = new JPanel(new MigLayout("insets 0", "[grow][40!]", "[]"));
        pwConfirmPanel.add(pwConfirm, "wmin 250, growx");
        pwConfirmPanel.add(pwEye2, "w 40!");
        panel.add(pwConfirmPanel);

        // 🔹 비밀번호 확인 메시지
        JLabel pwMsg = new JLabel("올바른 비밀번호입니다.");
        pwMsg.setForeground(Color.BLUE);
        panel.add(pwMsg, "gapleft 5, wrap");

        // 🔹 이메일 입력
        JTextField emailField = new JTextField();
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "이메일 입력");
        emailField.setPreferredSize(new Dimension(0, 35));
        panel.add(emailField, "wmin 250, growx");

        // 🔹 이름 입력
        JTextField nameField = new JTextField();
        nameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "이름 입력");
        nameField.setPreferredSize(new Dimension(0, 35));
        panel.add(nameField, "wmin 250, growx");

        // 🔹 생년 + 성별
        JTextField birthField = new JTextField();
        birthField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "출생년도 입력");
        JToggleButton malebtn = new JToggleButton("남");
        JToggleButton femalebtn = new JToggleButton("여");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(malebtn);
        genderGroup.add(femalebtn);

        birthField.setPreferredSize(new Dimension(0, 35));
        malebtn.setPreferredSize(new Dimension(50, 35));
        femalebtn.setPreferredSize(new Dimension(50, 35));

        JPanel sexPanel = new JPanel(new MigLayout("insets 0", "[50!][50!]", "[]"));
        sexPanel.add(malebtn);
        sexPanel.add(femalebtn);

        JPanel birthSexPanel = new JPanel(new MigLayout("insets 0", "[grow][]", "[]"));
        birthSexPanel.add(birthField, "wmin 150, growx");
        birthSexPanel.add(sexPanel, "gapleft 50");
        panel.add(birthSexPanel);

        // 🔹 회원가입 + 돌아가기 버튼
        JButton signupBtn = new JButton("회원가입");
        JButton backBtn = new JButton("뒤로가기");
        signupBtn.setPreferredSize(new Dimension(0, 40));
        backBtn.setPreferredSize(new Dimension(0, 40));

        JPanel btnPanel = new JPanel(new MigLayout("insets 0", "[grow][grow]", "[]"));
        btnPanel.add(signupBtn, "growx");
        btnPanel.add(backBtn, "growx");
        panel.add(btnPanel);

        // 🔁 뒤로가기 동작
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "login"));

        return panel;
    }


}
