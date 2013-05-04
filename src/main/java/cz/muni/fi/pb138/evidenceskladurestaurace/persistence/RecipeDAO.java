/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.evidenceskladurestaurace.persistence;

import java.util.List;
import org.w3c.dom.Document;

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
     * Returns list of recipes with given name
     * 
     * @param name name of requested recipes
     * @return all recipes with given name or empty list if there are none.
     * @throws DataAccessException in case of error.
     */
    Recipe findRecipesByName(String name);
    
    /**
     * Updates existing recipe.
     * 
     * @param recipe recipe to update (specified by name) with new attributes.
     * @throws DataAccessException in case of error.
     */
    void update(Recipe recipe);
    
    /**
     * Removes existing recipe.
     * 
     * @param recipe recipe to remove (specified by name).
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
    
    Document getDoc();
    
    void setDoc(Document doc);          
}


