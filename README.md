# 💪 NutriTrack — Gym Nutrition Calculator
### Java Swing Desktop Application | CSE Java Project (20 Marks)

---

## Overview
NutriTrack is a desktop GUI application built with **Java Swing** that allows gym enthusiasts and fitness-conscious individuals to track their daily nutritional intake. Users can select food items, enter the quantity consumed, and instantly see calculated values for key nutrients alongside progress bars showing how much of their daily targets they've hit.

---

## Features
- **30 common gym foods** across 5 categories (Protein, Carbs, Fats, Vegetables, Dairy)
- **6 tracked nutrients:** Calories, Protein, Carbohydrates, Fat, Fiber, Magnesium
- **Category filter** to quickly find foods
- **Live preview** — nutrient values update as you type the amount
- **Daily Log Table** — add multiple foods, remove by double-clicking
- **Progress bars** showing % of daily recommended intake
- **Color-coded warnings** when a nutrient exceeds the daily target
- Clean, dark-themed UI — no external libraries required

---

## OOP Concepts Used (for marks)
| Concept | Where Used |
|---|---|
| **Encapsulation** | `Food`, `FoodLogEntry`, `NutrientCalculator` — all data private with getters |
| **Abstraction** | `NutrientCalculator` hides all math; `FoodDatabase` hides data source |
| **Inheritance** | `MainFrame extends JFrame`, `NutrientBar extends JPanel` |
| **Polymorphism** | Swing event listeners, `JComboBox<Food>` using `toString()` |
| **Separation of Concerns** | Model (`Food`), Data (`FoodDatabase`), Logic (`NutrientCalculator`), UI (`MainFrame`) |

---

## Project Structure
```
NutriTrack/
├── src/nutritrack/
│   ├── Main.java              ← Entry point
│   ├── MainFrame.java         ← Swing GUI (main window)
│   ├── Food.java              ← Model: food item with nutrients/100g
│   ├── FoodDatabase.java      ← Static food database (30 items)
│   ├── FoodLogEntry.java      ← Log entry: food + grams → calculated nutrients
│   ├── NutrientCalculator.java← Calculates totals + daily % targets
│   └── NutrientBar.java       ← Custom Swing progress bar component
├── NutriTrack.jar             ← Runnable JAR (double-click to run)
└── README.md
```

---

## How to Run

### Option A — Double-click the JAR
```
NutriTrack.jar   ← just double-click (Java must be installed)
```

### Option B — Command line
```bash
java -jar NutriTrack.jar
```

### Option C — Compile from source
```bash
# Compile
javac -d out src/nutritrack/*.java

# Run
java -cp out nutritrack.Main
```

---

## How to Use
1. **Select a category** from the dropdown (or leave as "All")
2. **Select a food item** from the food list
3. **Enter the amount in grams** (e.g., 150 for 150g of chicken)
4. **See the live preview** showing nutrient values for that amount
5. Click **"+ Add to Log"** to add it to your daily log
6. Repeat for all foods eaten — totals update automatically
7. **Double-click any row** to remove a food item
8. Click **"Clear Log"** to start fresh

---

## Daily Targets (Reference Values)
| Nutrient | Daily Target | Source |
|---|---|---|
| Calories | 2500 kcal | General active adult |
| Protein | 150 g | ~1.2g/kg for 125 lb person |
| Carbohydrates | 300 g | 45–65% of 2500 kcal |
| Fat | 70 g | 25–30% of 2500 kcal |
| Fiber | 30 g | WHO recommendation |
| Magnesium | 400 mg | RDA for adult males |

---

## Technologies
- **Language:** Java 17+
- **GUI:** Java Swing (built-in JDK — no external libraries)
- **IDE:** Any (IntelliJ IDEA / Eclipse / VS Code + Extension Pack for Java)
- **Build:** javac + jar (standard JDK tools)

---

*Nutritional data sourced from USDA FoodData Central (approximate values).*

