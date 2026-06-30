package nutritrack;

import java.util.List;

/**
 * Calculates aggregate nutritional totals and personalized daily targets
 * based on the user's body weight and activity level.
 */
public class NutrientCalculator {

    // ── Activity levels ───────────────────────────────────────
    public enum ActivityLevel {
        SEDENTARY        ("Sedentary (minimal activity)",          0.8,  1.2, 30,  3.5),
        MODERATELY_ACTIVE("Moderately Active / Older Adult",       1.1,  1.4, 35,  4.0),
        ACTIVE           ("Active / Endurance Athlete",            1.5,  1.6, 45,  4.5),
        MUSCLE_BUILDING  ("Muscle Building / Weight Loss",         1.9,  1.8, 55,  5.0);

        public final String label;
        public final double proteinFactor;   // g per kg bodyweight
        public final double fatFactor;       // g per kg bodyweight
        public final double fiberTarget;     // g per day
        public final double calorieFactor;   // kcal per kg bodyweight (rough TDEE)

        ActivityLevel(String label, double proteinFactor, double fatFactor,
                      double fiberTarget, double calorieFactor) {
            this.label          = label;
            this.proteinFactor  = proteinFactor;
            this.fatFactor      = fatFactor;
            this.fiberTarget    = fiberTarget;
            this.calorieFactor  = calorieFactor;
        }

        @Override public String toString() { return label; }
    }

    // ── Default targets (used before weight is entered) ───────
    public static final double DEFAULT_CALORIES   = 2500;
    public static final double DEFAULT_PROTEIN    = 150;
    public static final double DEFAULT_CARBS      = 300;
    public static final double DEFAULT_FAT        = 70;
    public static final double DEFAULT_FIBER      = 30;
    public static final double DEFAULT_MAGNESIUM  = 400;

    // ── Live targets (updated when user sets weight) ──────────
    private double targetCalories  = DEFAULT_CALORIES;
    private double targetProtein   = DEFAULT_PROTEIN;
    private double targetCarbs     = DEFAULT_CARBS;
    private double targetFat       = DEFAULT_FAT;
    private double targetFiber     = DEFAULT_FIBER;
    private double targetMagnesium = DEFAULT_MAGNESIUM;

    // ── Totals ────────────────────────────────────────────────
    private double totalCalories, totalProtein, totalCarbs,
                   totalFat, totalFiber, totalMagnesium;

    public NutrientCalculator() { resetTotals(); }

    /**
     * Recalculates daily targets based on weight (kg) and activity level.
     * Magnesium is fixed at 400mg (RDA — not weight-dependent).
     */
    public void updateTargets(double weightKg, ActivityLevel level) {
        targetProtein   = weightKg * level.proteinFactor;
        targetFat       = weightKg * level.fatFactor;
        targetCalories  = weightKg * level.calorieFactor * 10; // kcal
        targetFiber     = level.fiberTarget;
        targetMagnesium = 400;

        // Carbs fill the remaining calories after protein + fat
        // Protein = 4 kcal/g, Fat = 9 kcal/g, Carbs = 4 kcal/g
        double remainingCals = targetCalories
                - (targetProtein * 4)
                - (targetFat * 9);
        targetCarbs = Math.max(50, remainingCals / 4);
    }

    public void calculate(List<FoodLogEntry> entries) {
        resetTotals();
        for (FoodLogEntry e : entries) {
            totalCalories  += e.getCalories();
            totalProtein   += e.getProtein();
            totalCarbs     += e.getCarbs();
            totalFat       += e.getFat();
            totalFiber     += e.getFiber();
            totalMagnesium += e.getMagnesium();
        }
    }

    private void resetTotals() {
        totalCalories = totalProtein = totalCarbs =
        totalFat = totalFiber = totalMagnesium = 0;
    }

    // ── Totals getters ────────────────────────────────────────
    public double getTotalCalories()  { return totalCalories; }
    public double getTotalProtein()   { return totalProtein; }
    public double getTotalCarbs()     { return totalCarbs; }
    public double getTotalFat()       { return totalFat; }
    public double getTotalFiber()     { return totalFiber; }
    public double getTotalMagnesium() { return totalMagnesium; }

    // ── Target getters ────────────────────────────────────────
    public double getTargetCalories()  { return targetCalories; }
    public double getTargetProtein()   { return targetProtein; }
    public double getTargetCarbs()     { return targetCarbs; }
    public double getTargetFat()       { return targetFat; }
    public double getTargetFiber()     { return targetFiber; }
    public double getTargetMagnesium() { return targetMagnesium; }

    // ── Progress % ────────────────────────────────────────────
    public int getCaloriesPct()  { return pct(totalCalories,  targetCalories); }
    public int getProteinPct()   { return pct(totalProtein,   targetProtein); }
    public int getCarbsPct()     { return pct(totalCarbs,     targetCarbs); }
    public int getFatPct()       { return pct(totalFat,       targetFat); }
    public int getFiberPct()     { return pct(totalFiber,     targetFiber); }
    public int getMagnesiumPct() { return pct(totalMagnesium, targetMagnesium); }

    private int pct(double val, double target) {
        return (int) Math.min(100, (val / target) * 100);
    }
}