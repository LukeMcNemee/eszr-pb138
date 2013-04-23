/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.tablemodel;

import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Filip Bogyai
 */
public class IngredientsTableModel extends AbstractTableModel {
    
    private static final long serialVersionUID = 1L;

    private List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    //ID fname lname address city country tel. mail
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Unit";
            case 2:
                return "Amount";                    
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ingredient ingredient = ingredients.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ingredient.getName();
            case 1:
                return ingredient.getUnit();
            case 2:
                return ingredient.getAmount();            
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            ingredients.add(ingredient);
            int lastRow = getRowCount() - 1;
            fireTableRowsInserted(lastRow, lastRow);
        }
    }

    public void updateIngredient(Ingredient ingredient, int row) {
        if (ingredient != null) {
            ingredients.set(row, ingredient);
            fireTableRowsUpdated(row, row);
        }
    }

    public void setIngredients(List<Ingredient> ingredients) {
        if (ingredients != null) {
            this.ingredients = new ArrayList<>(ingredients);
            fireTableDataChanged();
        }
    }

    public void removeIngredient(Ingredient ingredient, int row) {
        if (ingredient != null) {
            ingredients.remove(ingredient);
            fireTableRowsDeleted(row, row);
        }
    }
}

