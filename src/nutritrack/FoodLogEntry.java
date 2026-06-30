package nutritrack;

public class FoodLogEntry {
    private Food   food;
    private double inputValue;
    private boolean isCount;
    private double effectiveGrams;

    public FoodLogEntry(Food food, double inputValue) {
        this.food       = food;
        this.inputValue = inputValue;
        this.isCount    = food.isCountable();

        if (isCount) {
            this.effectiveGrams = inputValue * food.getGramsPerUnit();
        } else {
            this.effectiveGrams = inputValue;
        }
    }

    public Food   getFood()           { return food; }
    public double getInputValue()     { return inputValue; }
    public boolean isCount()          { return isCount; }
    public double getEffectiveGrams() { return effectiveGrams; }

    public double getCalories()  { return (food.getCaloriesPer100g()  * effectiveGrams) / 100.0; }
    public double getProtein()   { return (food.getProteinPer100g()   * effectiveGrams) / 100.0; }
    public double getCarbs()     { return (food.getCarbsPer100g()     * effectiveGrams) / 100.0; }
    public double getFat()       { return (food.getFatPer100g()       * effectiveGrams) / 100.0; }
    public double getFiber()     { return (food.getFiberPer100g()     * effectiveGrams) / 100.0; }
    public double getMagnesium() { return (food.getMagnesiumPer100g() * effectiveGrams) / 100.0; }

    public String getDisplayAmount() {
        if (isCount) {
            int count = (int) inputValue;
            return count + " × " + food.getUnitLabel();
        } else {
            return String.format("%.0fg", effectiveGrams);
        }
    }

    @Override
    public String toString() {
        return String.format("%s  —  %s  |  %.0f kcal  |  P: %.1fg  C: %.1fg  F: %.1fg",
                food.getName(), getDisplayAmount(),
                getCalories(), getProtein(), getCarbs(), getFat());
    }
}