package nutritrack;

import java.awt.*;
import javax.swing.*;

/**
 * A styled nutrient progress bar — target is now dynamic.
 */
public class NutrientBar extends JPanel {

    private String      label;
    private String      unit;
    private double      value;
    private double      target;
    private Color       barColor;
    private JProgressBar progressBar;
    private JLabel      valueLabel;
    private JLabel      nameLabel;

    public NutrientBar(String label, String unit, double initialTarget, Color barColor) {
        this.label    = label;
        this.unit     = unit;
        this.target   = initialTarget;
        this.barColor = barColor;
        this.value    = 0;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);

        nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(new Color(220, 220, 220));
        nameLabel.setPreferredSize(new Dimension(110, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(280, 18));
        progressBar.setBackground(new Color(50, 50, 60));
        progressBar.setForeground(barColor);
        progressBar.setBorderPainted(false);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        add(progressBar, gbc);

        valueLabel = new JLabel(formatValue());
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueLabel.setForeground(new Color(180, 180, 180));
        valueLabel.setPreferredSize(new Dimension(160, 20));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        add(valueLabel, gbc);
    }

    /** Call this when the target changes (e.g. user updates weight). */
    public void setTarget(double newTarget) {
        this.target = newTarget;
        refresh();
    }

    public void update(double value) {
        this.value = value;
        refresh();
    }

    private void refresh() {
        int pct = (target > 0) ? (int) Math.min(100, (value / target) * 100) : 0;
        progressBar.setValue(pct);
        valueLabel.setText(formatValue());

        if (pct >= 100) {
            progressBar.setForeground(new Color(255, 90, 90));
        } else if (pct >= 80) {
            progressBar.setForeground(barColor.brighter());
        } else {
            progressBar.setForeground(barColor);
        }
    }

    private String formatValue() {
        if (unit.equals("kcal")) {
            return String.format("%.0f / %.0f %s", value, target, unit);
        } else if (unit.equals("mg")) {
            return String.format("%.1f / %.0f %s", value, target, unit);
        } else {
            return String.format("%.1f / %.0f %s", value, target, unit);
        }
    }
}