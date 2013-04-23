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
public interface RecipeDAO {
    
    /**
     * Adds new recipe to the database.
     * 
     * @param recipe recipe to add.
     * @throws DataAccessException in case of error.
     */
    void create(Recipe recipe);
    
    /**
     * Returns recipe with given id.
     * 
     * @param id primary key of requested recipe.
     * @return recipe with given id or null if such recipe doesn't exist.
     * @throws DataAccessException in case of error.
     */
    Recipe get(Long id);
    
    /**
     * Updates existing recipe.
     * 
     * @param recipe recipe to update (specified by id) with new attributes.
     * @throws DataAccessException in case of error.
     */
    void update(Recipe recipe);
    
    /**
     * Removes existing recipe.
     * 
     * @param recipe recipe to remove (specified by id).
     * @throws DataAccessException in case of error.
     */
    void delete(Recipe recipe);
    
    /**
     * Returns list of all recipes in the database.
     * 
     * @return all recipes in the DB or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    List<Recipe> findAll();
    
    /**
     * Returns list of recipes with given name
     * 
     * @param name name of requested recipes
     * @return all recipes with given name or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    List<Recipe> findRecipesByName(String name);
}


