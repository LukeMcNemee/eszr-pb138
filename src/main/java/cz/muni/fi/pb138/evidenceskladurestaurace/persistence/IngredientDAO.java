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
     * Returns Ingredient with given id.
     * 
     * @param id primary key of requested Ingredient.
     * @return Ingredient with given id or null if such Ingredient doesn't exist.
     * @throws DataAccessException in case of error.
     */
    Ingredient get(Long id);
    
    /**
     * Updates existing Ingredient.
     * 
     * @param Ingredient Ingredient to update (specified by id) with new attributes.
     * @throws DataAccessException in case of error.
     */
    void update(Ingredient Ingredient);
    
    /**
     * Removes existing Ingredient.
     * 
     * @param Ingredient Ingredient to remove (specified by id).
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
    
    /**
     * Returns list of Ingredients with given name
     * 
     * @param name name of requested Ingredients
     * @return all Ingredients with given name or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    List<Ingredient> findIngredientsByName(String name);
}

