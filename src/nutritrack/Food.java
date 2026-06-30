package nutritrack;

public class Food {
    private String name;
    private String category;
    private double caloriesPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;
    private double fiberPer100g;
    private double magnesiumPer100g;

    private boolean countable;
    private double  gramsPerUnit;
    private String  unitLabel;

    public Food(String name, String category,
                double calories, double protein,
                double carbs, double fat,
                double fiber, double magnesium) {
        this(name, category, calories, protein, carbs, fat, fiber, magnesium,
             false, 100, "100g");
    }

    public Food(String name, String category,
                double calories, double protein,
                double carbs, double fat,
                double fiber, double magnesium,
                boolean countable, double gramsPerUnit, String unitLabel) {
        this.name             = name;
        this.category         = category;
        this.caloriesPer100g  = calories;
        this.proteinPer100g   = protein;
        this.carbsPer100g     = carbs;
        this.fatPer100g       = fat;
        this.fiberPer100g     = fiber;
        this.magnesiumPer100g = magnesium;
        this.countable        = countable;
        this.gramsPerUnit     = gramsPerUnit;
        this.unitLabel        = unitLabel;
    }

    public String getName()             { return name; }
    public String getCategory()         { return category; }
    public double getCaloriesPer100g()  { return caloriesPer100g; }
    public double getProteinPer100g()   { return proteinPer100g; }
    public double getCarbsPer100g()     { return carbsPer100g; }
    public double getFatPer100g()       { return fatPer100g; }
    public double getFiberPer100g()     { return fiberPer100g; }
    public double getMagnesiumPer100g() { return magnesiumPer100g; }
    public boolean isCountable()        { return countable; }
    public double getGramsPerUnit()     { return gramsPerUnit; }
    public String getUnitLabel()        { return unitLabel; }

    @Override
    public String toString() { return name; }
}