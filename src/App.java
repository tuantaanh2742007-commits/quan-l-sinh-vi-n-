import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class App extends JFrame {
    private final List<Student> students = new ArrayList<>();
    private final List<Subject> subjects = new ArrayList<>();
    private final List<Grade> grades = new ArrayList<>();

    public App() {
        super("Quan Ly Sinh Vien");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Sinh vien", createStudentPanel());
        tabs.addTab("Mon hoc", createSubjectPanel());
        tabs.addTab("Diem", createGradePanel());
        add(tabs);
    }

    private JPanel createStudentPanel() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Ma sinh vien", "Ho va ten", "Lop", "Nganh"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTextField ma = new JTextField();
        JTextField ten = new JTextField();
        JTextField lop = new JTextField();
        JTextField nganh = new JTextField();
        JTextField search = new JTextField();
        JTable table = new JTable(model);

        JPanel form = form(new String[]{"Ma sinh vien", "Ho va ten", "Lop", "Nganh"},
                ma, ten, lop, nganh);
        JButton them = new JButton("Them");
        JButton sua = new JButton("Sua");
        JButton xoa = new JButton("Xoa");
        JButton lamMoi = new JButton("Lam moi");
        JButton tim = new JButton("Tim kiem");

        them.addActionListener(e -> {
            if (ma.getText().trim().isEmpty() || ten.getText().trim().isEmpty()) {
                message("Nhap ma sinh vien va ho ten.");
                return;
            }
            for (Student student : students) {
                if (student.id.equalsIgnoreCase(ma.getText().trim())) {
                    message("Ma sinh vien da ton tai.");
                    return;
                }
            }
            students.add(new Student(ma.getText().trim(), ten.getText().trim(),
                    lop.getText().trim(), nganh.getText().trim()));
            loadStudents(model, "");
            clear(ma, ten, lop, nganh);
        });

        sua.addActionListener(e -> {
            Optional<Student> found = students.stream()
                    .filter(s -> s.id.equalsIgnoreCase(ma.getText().trim()))
                    .findFirst();
            if (found.isEmpty()) {
                message("Khong tim thay ma sinh vien.");
                return;
            }
            found.get().name = ten.getText().trim();
            found.get().className = lop.getText().trim();
            found.get().major = nganh.getText().trim();
            loadStudents(model, "");
        });

        xoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                message("Hay chon sinh vien can xoa.");
                return;
            }
            String id = model.getValueAt(row, 0).toString();
            if (confirm("Xoa sinh vien " + id + "?")) {
                students.removeIf(s -> s.id.equals(id));
                grades.removeIf(g -> g.studentId.equals(id));
                loadStudents(model, "");
                clear(ma, ten, lop, nganh);
            }
        });

        tim.addActionListener(e -> loadStudents(model, search.getText().trim()));
        lamMoi.addActionListener(e -> {
            clear(ma, ten, lop, nganh, search);
            loadStudents(model, "");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                ma.setText(value(model, row, 0));
                ten.setText(value(model, row, 1));
                lop.setText(value(model, row, 2));
                nganh.setText(value(model, row, 3));
            }
        });

        loadStudents(model, "");
        return panelWithTable(form, buttonRow(them, sua, xoa, lamMoi),
                table, search, tim);
    }

    private JPanel createSubjectPanel() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Ma mon hoc", "Ten mon hoc", "So tin chi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTextField ma = new JTextField();
        JTextField ten = new JTextField();
        JTextField tinChi = new JTextField();
        JTextField search = new JTextField();
        JTable table = new JTable(model);

        JPanel form = form(new String[]{"Ma mon hoc", "Ten mon hoc", "So tin chi"},
                ma, ten, tinChi);
        JButton them = new JButton("Them");
        JButton sua = new JButton("Sua");
        JButton xoa = new JButton("Xoa");
        JButton lamMoi = new JButton("Lam moi");
        JButton tim = new JButton("Tim kiem");

        them.addActionListener(e -> {
            if (ma.getText().trim().isEmpty() || ten.getText().trim().isEmpty()) {
                message("Nhap ma mon hoc va ten mon hoc.");
                return;
            }
            for (Subject subject : subjects) {
                if (subject.id.equalsIgnoreCase(ma.getText().trim())) {
                    message("Ma mon hoc da ton tai.");
                    return;
                }
            }
            try {
                subjects.add(new Subject(ma.getText().trim(), ten.getText().trim(),
                        Integer.parseInt(tinChi.getText().trim())));
                loadSubjects(model, "");
                clear(ma, ten, tinChi);
            } catch (NumberFormatException ex) {
                message("So tin chi phai la mot so.");
            }
        });

        sua.addActionListener(e -> {
            Optional<Subject> found = subjects.stream()
                    .filter(s -> s.id.equalsIgnoreCase(ma.getText().trim()))
                    .findFirst();
            if (found.isEmpty()) {
                message("Khong tim thay ma mon hoc.");
                return;
            }
            try {
                found.get().name = ten.getText().trim();
                found.get().credits = Integer.parseInt(tinChi.getText().trim());
                loadSubjects(model, "");
            } catch (NumberFormatException ex) {
                message("So tin chi phai la mot so.");
            }
        });

        xoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                message("Hay chon mon hoc can xoa.");
                return;
            }
            String id = model.getValueAt(row, 0).toString();
            if (confirm("Xoa mon hoc " + id + "?")) {
                subjects.removeIf(s -> s.id.equals(id));
                grades.removeIf(g -> g.subjectId.equals(id));
                loadSubjects(model, "");
                clear(ma, ten, tinChi);
            }
        });

        tim.addActionListener(e -> loadSubjects(model, search.getText().trim()));
        lamMoi.addActionListener(e -> {
            clear(ma, ten, tinChi, search);
            loadSubjects(model, "");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                ma.setText(value(model, row, 0));
                ten.setText(value(model, row, 1));
                tinChi.setText(value(model, row, 2));
            }
        });

        loadSubjects(model, "");
        return panelWithTable(form, buttonRow(them, sua, xoa, lamMoi),
                table, search, tim);
    }

    private JPanel createGradePanel() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Ma sinh vien", "Ho ten", "Ma mon hoc", "Ten mon hoc", "Diem"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTextField maSV = new JTextField();
        JTextField maMH = new JTextField();
        JTextField diem = new JTextField();
        JTextField search = new JTextField();
        JTable table = new JTable(model);

        JPanel form = form(new String[]{"Ma sinh vien", "Ma mon hoc", "Diem"},
                maSV, maMH, diem);
        JButton luu = new JButton("Luu diem");
        JButton xoa = new JButton("Xoa");
        JButton lamMoi = new JButton("Lam moi");
        JButton tim = new JButton("Tim kiem");

        luu.addActionListener(e -> {
            String studentId = maSV.getText().trim();
            String subjectId = maMH.getText().trim();
            if (!students.stream().anyMatch(s -> s.id.equalsIgnoreCase(studentId))) {
                message("Ma sinh vien khong ton tai.");
                return;
            }
            if (!subjects.stream().anyMatch(s -> s.id.equalsIgnoreCase(subjectId))) {
                message("Ma mon hoc khong ton tai.");
                return;
            }
            try {
                double score = Double.parseDouble(diem.getText().trim());
                if (score < 0 || score > 10) {
                    message("Diem phai nam trong khoang 0 - 10.");
                    return;
                }
                grades.removeIf(g -> g.studentId.equalsIgnoreCase(studentId)
                        && g.subjectId.equalsIgnoreCase(subjectId));
                grades.add(new Grade(studentId, subjectId, score));
                loadGrades(model, "");
                clear(maSV, maMH, diem);
            } catch (NumberFormatException ex) {
                message("Diem phai la mot so.");
            }
        });

        xoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                message("Hay chon diem can xoa.");
                return;
            }
            String studentId = model.getValueAt(row, 0).toString();
            String subjectId = model.getValueAt(row, 2).toString();
            if (confirm("Xoa diem nay?")) {
                grades.removeIf(g -> g.studentId.equals(studentId)
                        && g.subjectId.equals(subjectId));
                loadGrades(model, "");
                clear(maSV, maMH, diem);
            }
        });

        tim.addActionListener(e -> loadGrades(model, search.getText().trim()));
        lamMoi.addActionListener(e -> {
            clear(maSV, maMH, diem, search);
            loadGrades(model, "");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                maSV.setText(value(model, row, 0));
                maMH.setText(value(model, row, 2));
                diem.setText(value(model, row, 4));
            }
        });

        loadGrades(model, "");
        return panelWithTable(form, buttonRow(luu, xoa, lamMoi),
                table, search, tim);
    }

    private void loadStudents(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        String text = keyword.toLowerCase(Locale.ROOT);
        for (Student s : students) {
            if ((s.id + s.name + s.className + s.major).toLowerCase(Locale.ROOT).contains(text)) {
                model.addRow(new Object[]{s.id, s.name, s.className, s.major});
            }
        }
    }

    private void loadSubjects(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        String text = keyword.toLowerCase(Locale.ROOT);
        for (Subject s : subjects) {
            if ((s.id + s.name).toLowerCase(Locale.ROOT).contains(text)) {
                model.addRow(new Object[]{s.id, s.name, s.credits});
            }
        }
    }

    private void loadGrades(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        String text = keyword.toLowerCase(Locale.ROOT);
        for (Grade g : grades) {
            Student student = students.stream()
                    .filter(s -> s.id.equalsIgnoreCase(g.studentId)).findFirst().orElse(null);
            Subject subject = subjects.stream()
                    .filter(s -> s.id.equalsIgnoreCase(g.subjectId)).findFirst().orElse(null);
            if (student == null || subject == null) continue;
            String rowText = (g.studentId + student.name + g.subjectId
                    + subject.name).toLowerCase(Locale.ROOT);
            if (rowText.contains(text)) {
                model.addRow(new Object[]{
                        g.studentId, student.name, g.subjectId, subject.name, g.score
                });
            }
        }
    }

    private static JPanel form(String[] labels, JTextField... fields) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thong tin"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < fields.length; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 0;
            panel.add(new JLabel(labels[i]), gc);
            gc.gridx = 1;
            gc.weightx = 1;
            panel.add(fields[i], gc);
        }
        return panel;
    }

    private static JPanel buttonRow(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JButton button : buttons) panel.add(button);
        return panel;
    }

    private JPanel panelWithTable(JPanel form, JPanel actions, JTable table,
                                  JTextField search, JButton tim) {
        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(actions, BorderLayout.SOUTH);

        JPanel searchBar = new JPanel(new BorderLayout(8, 8));
        searchBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        searchBar.add(new JLabel("Tu khoa:"), BorderLayout.WEST);
        searchBar.add(search, BorderLayout.CENTER);
        searchBar.add(tim, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(top, BorderLayout.NORTH);
        panel.add(searchBar, BorderLayout.CENTER);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static void clear(JTextField... fields) {
        for (JTextField field : fields) field.setText("");
    }

    private static String value(DefaultTableModel model, int row, int column) {
        Object value = model.getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private boolean confirm(String text) {
        return JOptionPane.showConfirmDialog(this, text, "Xac nhan",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private static class Student {
        String id;
        String name;
        String className;
        String major;

        Student(String id, String name, String className, String major) {
            this.id = id;
            this.name = name;
            this.className = className;
            this.major = major;
        }
    }

    private static class Subject {
        String id;
        String name;
        int credits;

        Subject(String id, String name, int credits) {
            this.id = id;
            this.name = name;
            this.credits = credits;
        }
    }

    private static class Grade {
        String studentId;
        String subjectId;
        double score;

        Grade(String studentId, String subjectId, double score) {
            this.studentId = studentId;
            this.subjectId = subjectId;
            this.score = score;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
