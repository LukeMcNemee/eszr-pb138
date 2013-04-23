/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.persistence;

import java.util.Objects;

/**
 *
 * @author Filip Bogyai
 */
public class Ingredient {
        
    private String name;
    
    private Unit unit;
    
    private int amount;

    public Ingredient() {
    }

    public Ingredient(String name, Unit unit, int amount) {
        this.name = name;
        this.unit = unit;
        this.amount = amount;
    }    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ingredient)) {
            return false;
        }
        Ingredient other = (Ingredient) obj;
        if (this.name != null && !Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ingredient{name=" + name + ", unit=" + unit + ", amount=" + amount + '}';
    }

       
}
