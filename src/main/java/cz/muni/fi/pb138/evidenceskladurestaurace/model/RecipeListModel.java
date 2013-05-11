/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.model;

import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Recipe;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;


/**
 *
 * @author fbogyai
 */
public class RecipeListModel extends AbstractListModel {

    private List<Recipe> recipes = new ArrayList<>();
    
    @Override
    public int getSize() {
        return recipes.size();
    }

    @Override
    public Object getElementAt(int index) {
        return recipes.get(index).getName();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        if (recipes!=null){
            this.recipes = new ArrayList<>(recipes);
        fireContentsChanged(this, 0, recipes.size());
        }
    }
    
    
}
