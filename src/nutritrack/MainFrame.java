package nutritrack;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class MainFrame extends JFrame {

    private static final Color BG_DARK = new Color(12, 12, 14);
    private static final Color BG_PANEL = new Color(22, 22, 26);
    private static final Color BG_CARD = new Color(30, 30, 36);
    private static final Color BG_INPUT = new Color(40, 40, 48);
    private static final Color ACCENT_WHITE = new Color(235, 235, 220);
    private static final Color ACCENT_CREAM = new Color(210, 200, 180);
    private static final Color ACCENT_GREEN = new Color(140, 200, 140);
    private static final Color ACCENT_BLUE = new Color(120, 160, 210);
    private static final Color ACCENT_ORANGE = new Color(210, 160, 90);
    private static final Color ACCENT_RED = new Color(200, 100, 90);
    private static final Color TEXT_PRIMARY = new Color(235, 235, 220);
    private static final Color TEXT_MUTED = new Color(140, 138, 130);
    private static final Color TEXT_DIM = new Color(90, 90, 85);
    private static final Color BORDER_COLOR = new Color(48, 48, 55);
    private static final Color DIVIDER = new Color(55, 55, 62);

    private List<FoodLogEntry> logEntries = new ArrayList<>();
    private NutrientCalculator calculator = new NutrientCalculator();

    private JTextField weightField;
    private JComboBox<String> weightUnitBox;
    private JComboBox<NutrientCalculator.ActivityLevel> activityBox;
    private JLabel profileStatusLabel;

    private JComboBox<Food> foodBox;
    private JTextField amountField;
    private JLabel amountLabel;
    private JLabel amountHintLabel;
    private JLabel inputModeTag;
    private JLabel previewLabel;
    private JPanel previewPanel;

    private DefaultTableModel tableModel;
    private JTable logTable;

    private NutrientBar barCalories, barProtein, barCarbs,
            barFat, barFiber, barMagnesium;

    public MainFrame() {
        setTitle("NutriTrack — Gym Nutrition Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 780);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        buildUI();
        loadAllFoods();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_PANEL);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER));
        h.setPreferredSize(new Dimension(0, 56));

        JLabel logo = new JLabel("  💪  NutriTrack");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(ACCENT_WHITE);

        JLabel tag = new JLabel("Gym Nutrition Calculator   ");
        tag.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tag.setForeground(TEXT_MUTED);

        h.add(logo, BorderLayout.WEST);
        h.add(tag, BorderLayout.EAST);
        return h;
    }

    private JSplitPane buildCenter() {
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        sp.setDividerLocation(360);
        sp.setDividerSize(1);
        sp.setBorder(null);
        sp.setBackground(DIVIDER);
        return sp;
    }

    // ══════════════════════════════════════════════════════════
    //  LEFT PANEL
    // ══════════════════════════════════════════════════════════
    private JPanel buildLeftPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_PANEL);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(BG_PANEL);
        inner.setBorder(new EmptyBorder(20, 20, 12, 20));

        // ── YOUR PROFILE ──────────────────────────────────────
        inner.add(sectionLabel("YOUR PROFILE"));
        inner.add(Box.createVerticalStrut(12));

        // Weight + unit row
        JPanel weightRow = new JPanel(new BorderLayout(6, 0));
        weightRow.setOpaque(false);
        weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        weightRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel weightLeft = new JPanel(new BorderLayout(0, 4));
        weightLeft.setOpaque(false);
        weightLeft.add(smallLabel("Body Weight"), BorderLayout.NORTH);
        weightField = new JTextField();
        styleInput(weightField);
        weightLeft.add(weightField, BorderLayout.CENTER);

        JPanel weightRight = new JPanel(new BorderLayout(0, 4));
        weightRight.setOpaque(false);
        weightRight.setPreferredSize(new Dimension(64, 58));
        weightRight.add(smallLabel("Unit"), BorderLayout.NORTH);
        weightUnitBox = new JComboBox<>(new String[]{"kg", "lbs"});
        styleComboBox(weightUnitBox);
        weightRight.add(weightUnitBox, BorderLayout.CENTER);

        weightRow.add(weightLeft, BorderLayout.CENTER);
        weightRow.add(weightRight, BorderLayout.EAST);
        inner.add(weightRow);
        inner.add(Box.createVerticalStrut(10));

        // Activity level
        JPanel actPanel = new JPanel(new BorderLayout(0, 4));
        actPanel.setOpaque(false);
        actPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        actPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actPanel.add(smallLabel("Activity Level"), BorderLayout.NORTH);
        activityBox = new JComboBox<>();
        for (NutrientCalculator.ActivityLevel l : NutrientCalculator.ActivityLevel.values()) {
            activityBox.addItem(l);
        }
        styleComboBox(activityBox);
        actPanel.add(activityBox, BorderLayout.CENTER);
        inner.add(actPanel);
        inner.add(Box.createVerticalStrut(12));

        // Apply button
        JButton applyBtn = new JButton("⚡  Apply & Recalculate Targets");
        styleBtn(applyBtn, BG_INPUT, ACCENT_BLUE);
        applyBtn.addActionListener(e -> applyProfile());
        inner.add(applyBtn);
        inner.add(Box.createVerticalStrut(6));

        // Status label
        profileStatusLabel = new JLabel("Using default targets — enter weight to personalise");
        profileStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        profileStatusLabel.setForeground(TEXT_DIM);
        profileStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(profileStatusLabel);

        inner.add(Box.createVerticalStrut(18));
        inner.add(hairline());
        inner.add(Box.createVerticalStrut(18));

        // ── SELECT FOOD ───────────────────────────────────────
        inner.add(sectionLabel("SELECT FOOD"));
        inner.add(Box.createVerticalStrut(12));

        // Food dropdown
        JPanel foodPanel = new JPanel(new BorderLayout(0, 4));
        foodPanel.setOpaque(false);
        foodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        foodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodPanel.add(smallLabel("Food Item"), BorderLayout.NORTH);
        foodBox = new JComboBox<>();
        styleComboBox(foodBox);
        foodBox.addActionListener(e -> onFoodSelected());
        foodPanel.add(foodBox, BorderLayout.CENTER);
        inner.add(foodPanel);
        inner.add(Box.createVerticalStrut(10));

        // Amount label row with badge
        JPanel amtLabelRow = new JPanel(new BorderLayout());
        amtLabelRow.setOpaque(false);
        amtLabelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        amtLabelRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        amountLabel = new JLabel("Amount (grams)");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        amountLabel.setForeground(TEXT_MUTED);
        amtLabelRow.add(amountLabel, BorderLayout.WEST);

        inputModeTag = new JLabel(" BY WEIGHT ");
        inputModeTag.setFont(new Font("Segoe UI", Font.BOLD, 9));
        inputModeTag.setForeground(BG_DARK);
        inputModeTag.setBackground(ACCENT_BLUE);
        inputModeTag.setOpaque(true);
        inputModeTag.setBorder(new EmptyBorder(1, 5, 1, 5));
        amtLabelRow.add(inputModeTag, BorderLayout.EAST);
        inner.add(amtLabelRow);
        inner.add(Box.createVerticalStrut(4));

        // Amount input
        amountField = new JTextField("100");
        styleInput(amountField);
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updatePreview();
            }
        });
        inner.add(amountField);

        // Hint
        amountHintLabel = new JLabel(" ");
        amountHintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        amountHintLabel.setForeground(ACCENT_ORANGE);
        amountHintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(Box.createVerticalStrut(3));
        inner.add(amountHintLabel);
        inner.add(Box.createVerticalStrut(12));

        // Preview card
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(BG_CARD);
        previewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        previewPanel.setPreferredSize(new Dimension(0, 240));

        previewLabel = new JLabel(
                "<html><center><font color='#555555'>Select a food and enter<br>amount to see preview</font></center></html>",
                SwingConstants.CENTER);
        previewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        previewPanel.add(previewLabel, BorderLayout.CENTER);

        JPanel previewCard = new JPanel(new BorderLayout());
        previewCard.setBackground(BG_CARD);
        previewCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)));
        previewCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        previewCard.setPreferredSize(new Dimension(0, 260));
        previewCard.add(previewPanel, BorderLayout.CENTER);
        inner.add(previewCard);

        // Add to Log button
        JButton addBtn = new JButton("＋  Add to Log");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBackground(ACCENT_GREEN);
        addBtn.setForeground(BG_DARK);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addFoodToLog());
        inner.add(addBtn);
        inner.add(Box.createVerticalStrut(8));

        // Clear log button
        JButton clearBtn = new JButton("🗑   Clear All Logs");
        styleBtn(clearBtn, new Color(38, 22, 22), ACCENT_RED);
        clearBtn.addActionListener(e -> clearLog());
        inner.add(clearBtn);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PANEL);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    // ══════════════════════════════════════════════════════════
    //  RIGHT PANEL
    // ══════════════════════════════════════════════════════════
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);
        panel.add(buildLogTable(), BorderLayout.CENTER);
        panel.add(buildNutrientSummary(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildLogTable() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PANEL);
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_PANEL);
        topBar.setBorder(new EmptyBorder(14, 16, 10, 16));

        JLabel title = new JLabel("Daily Food Log");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(ACCENT_WHITE);
        topBar.add(title, BorderLayout.WEST);

        JLabel hint = new JLabel("Double-click a row to remove");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(TEXT_DIM);
        topBar.add(hint, BorderLayout.EAST);

        wrapper.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Food Item", "Amount", "Calories", "Protein (g)", "Carbs (g)", "Fat (g)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        logTable = new JTable(tableModel);
        styleTable(logTable);

        logTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = logTable.getSelectedRow();
                    if (row >= 0) {
                        removeEntry(row);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(logTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBackground(BG_CARD);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildNutrientSummary() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_PANEL);
        outer.setBorder(new EmptyBorder(14, 20, 16, 20));

        JLabel title = new JLabel("Daily Nutrient Progress  —  vs your personalised targets");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(ACCENT_CREAM);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        outer.add(title, BorderLayout.NORTH);

        JPanel bars = new JPanel();
        bars.setLayout(new BoxLayout(bars, BoxLayout.Y_AXIS));
        bars.setOpaque(false);

        barCalories = new NutrientBar("Calories", "kcal", NutrientCalculator.DEFAULT_CALORIES, new Color(210, 160, 80));
        barProtein = new NutrientBar("Protein", "g", NutrientCalculator.DEFAULT_PROTEIN, new Color(130, 190, 130));
        barCarbs = new NutrientBar("Carbs", "g", NutrientCalculator.DEFAULT_CARBS, new Color(110, 150, 200));
        barFat = new NutrientBar("Fat", "g", NutrientCalculator.DEFAULT_FAT, new Color(200, 130, 90));
        barFiber = new NutrientBar("Fiber", "g", NutrientCalculator.DEFAULT_FIBER, new Color(150, 120, 190));
        barMagnesium = new NutrientBar("Magnesium", "mg", NutrientCalculator.DEFAULT_MAGNESIUM, new Color(100, 185, 185));

        for (NutrientBar b : new NutrientBar[]{barCalories, barProtein, barCarbs, barFat, barFiber, barMagnesium}) {
            bars.add(b);
            bars.add(Box.createVerticalStrut(5));
        }

        outer.add(bars, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        bar.setBackground(BG_DARK);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DIVIDER));
        JLabel lbl = new JLabel(
                "NutriTrack v3.0   |   Data: USDA FoodData Central   |   Targets: WHO / ACSM standards   |   Double-click any row to delete");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_DIM);
        bar.add(lbl);
        return bar;
    }

    // ══════════════════════════════════════════════════════════
    //  Logic
    // ══════════════════════════════════════════════════════════
    private void loadAllFoods() {
        foodBox.removeAllItems();
        for (Food f : FoodDatabase.getAllFoods()) {
            foodBox.addItem(f);
        }
        onFoodSelected();
    }

    private void applyProfile() {
        String txt = weightField.getText().trim();
        if (txt.isEmpty()) {
            showError("Please enter your body weight.");
            return;
        }

        double w;
        try {
            w = Double.parseDouble(txt);
            if (w <= 0 || w > 500) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid weight (e.g. 75).");
            return;
        }

        double kg = weightUnitBox.getSelectedItem().equals("lbs") ? w / 2.2 : w;
        NutrientCalculator.ActivityLevel lvl
                = (NutrientCalculator.ActivityLevel) activityBox.getSelectedItem();

        calculator.updateTargets(kg, lvl);

        barCalories.setTarget(calculator.getTargetCalories());
        barProtein.setTarget(calculator.getTargetProtein());
        barCarbs.setTarget(calculator.getTargetCarbs());
        barFat.setTarget(calculator.getTargetFat());
        barFiber.setTarget(calculator.getTargetFiber());
        barMagnesium.setTarget(calculator.getTargetMagnesium());

        refreshNutrientBars();

        profileStatusLabel.setText(String.format(
                "✓  Targets set for %.1f kg  —  %s", kg, lvl.label));
        profileStatusLabel.setForeground(ACCENT_GREEN);
    }

    private void onFoodSelected() {
        Food food = (Food) foodBox.getSelectedItem();
        if (food == null) {
            return;
        }

        if (food.isCountable()) {
            amountLabel.setText("Number of Items");
            inputModeTag.setText(" BY COUNT ");
            inputModeTag.setBackground(ACCENT_ORANGE);
            amountField.setText("1");
            amountHintLabel.setText("1 " + food.getUnitLabel());
        } else {
            amountLabel.setText("Amount (grams)");
            inputModeTag.setText(" BY WEIGHT ");
            inputModeTag.setBackground(ACCENT_BLUE);
            amountField.setText("100");
            amountHintLabel.setText(" ");
        }
        updatePreview();
    }

    private void updatePreview() {
        Food food = (Food) foodBox.getSelectedItem();
        if (food == null) {
            return;
        }

        double v;
        try {
            v = Math.max(0, Double.parseDouble(amountField.getText().trim()));
        } catch (NumberFormatException ex) {
            v = 0;
        }

        if (food.isCountable() && v > 0) {
            amountHintLabel.setText(String.format("%.0f x %s  ->  approx %.0fg total",
                    v, food.getUnitLabel(), v * food.getGramsPerUnit()));
        } else if (!food.isCountable()) {
            amountHintLabel.setText(" ");
        }

        FoodLogEntry tmp = new FoodLogEntry(food, v);
        String disp = food.isCountable()
                ? String.format("%.0f item(s) = %.0fg", v, tmp.getEffectiveGrams())
                : String.format("%.0fg", tmp.getEffectiveGrams());

        // Build preview panel manually instead of HTML
        previewPanel.removeAll();
        previewPanel.setLayout(new BorderLayout(0, 6));
        previewPanel.setBackground(BG_CARD);

        // Title
        JLabel nameL = new JLabel(food.getName(), SwingConstants.CENTER);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameL.setForeground(ACCENT_GREEN);

        JLabel dispL = new JLabel(disp, SwingConstants.CENTER);
        dispL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dispL.setForeground(TEXT_MUTED);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        nameL.setAlignmentX(Component.CENTER_ALIGNMENT);
        dispL.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(nameL);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(dispL);

        previewPanel.add(titlePanel, BorderLayout.NORTH);

        // Nutrients grid
        String[] labels = {"Calories", "Protein", "Carbs", "Fat", "Fiber", "Magnesium"};
        String[] values = {
            String.format("%.0f kcal", tmp.getCalories()),
            String.format("%.1f g", tmp.getProtein()),
            String.format("%.1f g", tmp.getCarbs()),
            String.format("%.1f g", tmp.getFat()),
            String.format("%.1f g", tmp.getFiber()),
            String.format("%.1f mg", tmp.getMagnesium())
        };

        JPanel grid = new JPanel(new GridLayout(6, 2, 0, 1));
        grid.setBackground(BG_CARD);

        for (int i = 0; i < labels.length; i++) {
            Color rowBg = (i % 2 == 0) ? new Color(28, 28, 34) : new Color(36, 36, 44);

            JLabel lbl = new JLabel("  " + labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(TEXT_MUTED);
            lbl.setBackground(rowBg);
            lbl.setOpaque(true);
            lbl.setPreferredSize(new Dimension(0, 28));

            JLabel val = new JLabel(values[i] + "  ", SwingConstants.RIGHT);
            val.setFont(new Font("Segoe UI", Font.BOLD, 12));
            val.setForeground(TEXT_PRIMARY);
            val.setBackground(rowBg);
            val.setOpaque(true);

            grid.add(lbl);
            grid.add(val);
        }

        previewPanel.add(grid, BorderLayout.CENTER);
        previewPanel.revalidate();
        previewPanel.repaint();
    }

    private void addFoodToLog() {
        Food food = (Food) foodBox.getSelectedItem();
        if (food == null) {
            showError("Please select a food item.");
            return;
        }

        double v;
        try {
            v = Double.parseDouble(amountField.getText().trim());
            if (v <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showError(food.isCountable()
                    ? "Enter a valid number of items (e.g. 2)."
                    : "Enter a valid amount in grams (e.g. 150).");
            return;
        }

        FoodLogEntry entry = new FoodLogEntry(food, v);
        logEntries.add(entry);
        tableModel.addRow(new Object[]{
            food.getName(),
            entry.getDisplayAmount(),
            String.format("%.0f kcal", entry.getCalories()),
            String.format("%.1f", entry.getProtein()),
            String.format("%.1f", entry.getCarbs()),
            String.format("%.1f", entry.getFat())
        });

        refreshNutrientBars();
        amountField.setText(food.isCountable() ? "1" : "100");
        amountField.requestFocus();
        updatePreview();
    }

    private void removeEntry(int row) {
        logEntries.remove(row);
        tableModel.removeRow(row);
        refreshNutrientBars();
    }

    private void clearLog() {
        if (logEntries.isEmpty()) {
            return;
        }
        int c = JOptionPane.showConfirmDialog(this,
                "Clear all food entries from today's log?",
                "Clear Log", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            logEntries.clear();
            tableModel.setRowCount(0);
            refreshNutrientBars();
        }
    }

    private void refreshNutrientBars() {
        calculator.calculate(logEntries);
        barCalories.update(calculator.getTotalCalories());
        barProtein.update(calculator.getTotalProtein());
        barCarbs.update(calculator.getTotalCarbs());
        barFat.update(calculator.getTotalFat());
        barFiber.update(calculator.getTotalFiber());
        barMagnesium.update(calculator.getTotalMagnesium());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // ══════════════════════════════════════════════════════════
    //  Styling helpers
    // ══════════════════════════════════════════════════════════
    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel smallLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    private void styleInput(JTextField f) {
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)));
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setBackground(BG_INPUT);
        box.setForeground(TEXT_PRIMARY);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        box.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleBtn(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleTable(JTable t) {
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(30);
        t.setGridColor(DIVIDER);
        t.setSelectionBackground(new Color(48, 52, 62));
        t.setSelectionForeground(ACCENT_WHITE);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader h = t.getTableHeader();
        h.setBackground(BG_PANEL);
        h.setForeground(TEXT_MUTED);
        h.setFont(new Font("Segoe UI", Font.BOLD, 11));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER));

        t.getColumnModel().getColumn(0).setPreferredWidth(210);
        t.getColumnModel().getColumn(1).setPreferredWidth(150);
        t.getColumnModel().getColumn(2).setPreferredWidth(95);
        t.getColumnModel().getColumn(3).setPreferredWidth(90);
        t.getColumnModel().getColumn(4).setPreferredWidth(85);
        t.getColumnModel().getColumn(5).setPreferredWidth(75);

        DefaultTableCellRenderer centre = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                setBackground(sel ? new Color(48, 52, 62)
                        : (row % 2 == 0 ? BG_CARD : new Color(34, 34, 40)));
                setForeground(TEXT_PRIMARY);
                setHorizontalAlignment(col == 0 ? LEFT : CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < 6; i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(centre);
        }
    }

    private JPanel previewCard(JLabel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 12, 10, 12)));
        card.add(content, BorderLayout.CENTER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        card.setPreferredSize(new Dimension(0, 260));
        return card;
    }

    private JSeparator hairline() {
        JSeparator s = new JSeparator();
        s.setForeground(DIVIDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }
}
