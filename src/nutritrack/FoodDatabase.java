package nutritrack;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabase {

    private static final List<Food> foods = new ArrayList<>();

    static {
        // ── Proteins ──────────────────────────────────────────
        foods.add(new Food("Eggs (whole)",        "Protein", 155, 13.0,  1.1, 11.0,  0.0, 12,
                           true, 60, "large egg (≈60g)"));
        foods.add(new Food("Egg Whites",          "Protein",  52, 11.0,  0.7,  0.2,  0.0, 11,
                           true, 33, "egg white (≈33g)"));
        foods.add(new Food("Chicken Breast (cooked)", "Protein", 165, 31.0,  0.0,  3.6, 0.0, 29));
        foods.add(new Food("Tuna (canned in water)",  "Protein", 116, 26.0,  0.0,  1.0, 0.0, 35));
        foods.add(new Food("Salmon (cooked)",         "Protein", 208, 20.0,  0.0, 13.0, 0.0, 27));
        foods.add(new Food("Ground Beef (lean 90%)",  "Protein", 218, 26.0,  0.0, 12.0, 0.0, 22));
        foods.add(new Food("Whey Protein Powder",     "Protein", 370, 80.0,  6.0,  3.0, 0.0, 80));
        foods.add(new Food("Greek Yogurt (plain)",    "Protein",  59, 10.0,  3.6,  0.4, 0.0, 11));
        foods.add(new Food("Cottage Cheese",          "Protein",  98, 11.0,  3.4,  4.3, 0.0,  8));
        foods.add(new Food("Paneer",                  "Protein", 265, 18.0,  1.2, 20.0, 0.0,  8));

        // ── Carbs & Grains ────────────────────────────────────
        foods.add(new Food("Banana",            "Carbs",  89, 1.1, 23.0, 0.3, 2.6, 27,
                           true, 118, "medium banana (≈118g)"));
        foods.add(new Food("Whole Wheat Bread", "Carbs", 247, 9.0, 48.0, 3.0, 6.0, 76,
                           true, 30, "slice (≈30g)"));
        foods.add(new Food("White Rice (cooked)",   "Carbs", 130, 2.7, 28.0, 0.3,  1.8, 12));
        foods.add(new Food("Brown Rice (cooked)",   "Carbs", 112, 2.6, 23.5, 0.9,  1.8, 44));
        foods.add(new Food("Oats (dry)",            "Carbs", 389,17.0, 66.0, 7.0, 10.6,138));
        foods.add(new Food("Sweet Potato (cooked)", "Carbs",  90, 2.0, 20.7, 0.1,  3.3, 27));
        foods.add(new Food("Pasta (cooked)",        "Carbs", 158, 5.8, 31.0, 0.9,  1.8, 18));

        // ── Fats ──────────────────────────────────────────────
        foods.add(new Food("Almonds",   "Fats", 579, 21.0, 22.0, 50.0, 12.5, 270,
                           true, 1.2, "almond (≈1.2g each)"));
        foods.add(new Food("Walnuts",   "Fats", 654, 15.0, 14.0, 65.0,  6.7, 158,
                           true, 3.5, "walnut half (≈3.5g)"));
        foods.add(new Food("Peanut Butter", "Fats", 588, 25.0, 20.0, 50.0,  6.0, 168));
        foods.add(new Food("Avocado",       "Fats", 160,  2.0,  9.0, 15.0,  7.0,  29));
        foods.add(new Food("Olive Oil",     "Fats", 884,  0.0,  0.0,100.0,  0.0,   0));

        // ── Vegetables ────────────────────────────────────────
        foods.add(new Food("Broccoli",              "Vegetables",  34, 2.8,  7.0, 0.4, 2.6, 21));
        foods.add(new Food("Spinach (raw)",         "Vegetables",  23, 2.9,  3.6, 0.4, 2.2, 79));
        foods.add(new Food("Lentils (cooked)",      "Vegetables", 116, 9.0, 20.0, 0.4, 7.9, 36));
        foods.add(new Food("Chickpeas (cooked)",    "Vegetables", 164, 9.0, 27.0, 2.6, 7.6, 48));
        foods.add(new Food("Kidney Beans (cooked)", "Vegetables", 127, 9.0, 22.8, 0.5, 7.4, 45));

        // ── Dairy ─────────────────────────────────────────────
        foods.add(new Food("Whole Milk",     "Dairy",  61, 3.2,  4.8,  3.3, 0.0, 11));
        foods.add(new Food("Skimmed Milk",   "Dairy",  34, 3.4,  5.0,  0.1, 0.0, 11));
        foods.add(new Food("Cheddar Cheese", "Dairy", 402,25.0,  1.3, 33.0, 0.0, 28));
    }

    public static List<Food> getAllFoods() {
        return new ArrayList<>(foods);
    }

    public static List<String> getCategories() {
        List<String> cats = new ArrayList<>();
        cats.add("All");
        for (Food f : foods) {
            if (!cats.contains(f.getCategory())) cats.add(f.getCategory());
        }
        return cats;
    }

    public static List<Food> getFoodsByCategory(String category) {
        if (category.equals("All")) return getAllFoods();
        List<Food> result = new ArrayList<>();
        for (Food f : foods) {
            if (f.getCategory().equals(category)) result.add(f);
        }
        return result;
    }
}