/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.persistence;

import java.util.List;

/**
 *
 * @author Filip Bogyai
 */
public interface IngredientDAO {
    
    /**
     * Adds new Ingredient to the database.
     * 
     * @param Ingredient Ingredient to add.
     * @throws DataAccessException in case of error.
     */
    void create(Ingredient Ingredient);
    
    /**
     * Returns list of Ingredients with given name
     * 
     * @param name name of requested Ingredients
     * @return all Ingredients with given name or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    Ingredient findIngredientsByName(String name);
    
    /**
     * Updates existing Ingredient.
     * 
     * @param Ingredient Ingredient to update (specified by name) with new attributes.
     * @throws DataAccessException in case of error.
     */
    void update(Ingredient Ingredient);
    
    /**
     * Removes existing Ingredient.
     * 
     * @param Ingredient Ingredient to remove (specified by name).
     * @throws DataAccessException in case of error.
     */
    void delete(Ingredient Ingredient);
    
    /**
     * Returns list of all Ingredients in the database.
     * 
     * @return all Ingredients in the DB or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    List<Ingredient> findAll();    
    
}

