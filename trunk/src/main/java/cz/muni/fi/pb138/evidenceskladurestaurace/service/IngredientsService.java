/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.service;

import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Ingredient;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.IngredientDAO;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Recipe;

/**
 *
 * @author Filip Bogyai
 */
public class IngredientsService {
      
    private IngredientDAO ingredientDAO;

    public IngredientsService(IngredientDAO inredientDAO) {
        
        this.ingredientDAO = inredientDAO;
    }
    
    public Boolean checkAllIngredients(Recipe recipe, int count){         
        for(Ingredient ingredient : recipe.getIngredients()){
            if(!checkIngredient(ingredient, count))
                return false;
        }
        return true;
    }
    
    public Boolean checkIngredient(Ingredient ingredient, int count){  
        Ingredient ingredientFromDB;
        try{
            ingredientFromDB = ingredientDAO.findIngredientsByName(ingredient.getName());
        }catch(NullPointerException ex){
            return false;
        }
        return (ingredientFromDB.getAmount() >= (ingredient.getAmount()*count));
    }
    
    public void cook(Recipe recipe, int count){
        for(Ingredient ingredient : recipe.getIngredients()){
           Ingredient ingredientFromDB = ingredientDAO.findIngredientsByName(ingredient.getName()); 
           ingredientFromDB.setAmount(ingredientFromDB.getAmount()-(ingredient.getAmount()*count));
           ingredientDAO.update(ingredientFromDB);
        }
    }
    
    public int maximumPortions(Recipe recipe){
        int max = 0;
        if(! checkAllIngredients(recipe, 1))
            return 0;
        while(checkAllIngredients(recipe, max)){
            max++;
        }
        return max-1;
    }
}
