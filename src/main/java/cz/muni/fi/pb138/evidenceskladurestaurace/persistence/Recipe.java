/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.persistence;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Filip Bogyai
 */
public class Recipe {
    
    private String name;
    
    private List<Ingredient> ingredients;

    public Recipe() {
    }

    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) object;
        if (this.name != null && !Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
    
    
 }
