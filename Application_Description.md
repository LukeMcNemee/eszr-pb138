**Contents**
  * <a href='https://code.google.com/p/eszr-pb138/wiki/Application_Description?ts=1370266667#GUI'>Simplified application GUI</a>
    * <a href='https://code.google.com/p/eszr-pb138/wiki/Application_Description?ts=1370266667#Recipes'>Recipes bar</a>
      * <a href='https://code.google.com/p/eszr-pb138/wiki/Application_Description?ts=1370266667#Functions'>Recipe functions</a>
    * <a href='https://code.google.com/p/eszr-pb138/wiki/Application_Description?ts=1370266667#Ingredients'>Ingredients bar</a>
      * <a href='https://code.google.com/p/eszr-pb138/wiki/Application_Description?ts=1370266667#Manipulations'>Manipulations with ingredients</a>


---

# GUI #
_How it looks like_

## Recipes ##

This bar is used for all operations with recipes. By choosing any of the saved recipes in the left part of application, ingredients needed for preparing the recipe are shown in the table.

### Functions ###


<li><b>Check Ingredients</b></li>
Choosing this functions will recolor the background of recipe.<br>
<font color='lime'>Green</font> color signalizes possibility to cook the recipe.<br>
<font color='red'>Red</font> color informs user that some ingredients needed to cook the recipe are not available.<br><br>
<li><b>Maximum Portions</b></li>
Signalizes the maximum number of portions that can be cooked with stored ingredients.<br>
Signalized by number appearing next to the button.<br><br>
<li><b>Cook</b></li>
Subtracts ingredients needed for cooking number of portions given by the spinner next to the button. Spinner's value can be modified by visible arrows, keyboard arrows or by writing the value.<br><br>
<li><b>Import from XML</b></li>
<i>For advanced users</i><br>
First way of adding new recipes to the application. XML file used for import must be of valid <a href='https://code.google.com/p/eszr-pb138/wiki/XMLRecipeData'>pattern</a>.<br>
Available for <a href='https://googledrive.com/host/0B_UKPQ-6wAceTVZIcVZ0SFRva28/'>download</a><br><br>
<li><b>Create New Recipe</b></li>
Another way of adding new recipes. More user-friendly way. By clicking on this button, new window will appear. In this window, user must select a name for the recipe and add at least one ingredient. User must select ingredient from the list that is in the warehouse. If user wants to create recipe with ingredient which is not present in the warehouse, it must first be added there. Contrary to the XML file import where the recipe can be made from anything. By clicking on <b>Save</b>, recipe will be saved to application.<br>
If user already pressed the Check Ingredients button, user can immediately see whether the created recipe can be cooked or not.<br><br>
<li><b>Edit Recipe</b></li>
This function allows user to edit recipes to their liking. User must first select the recipe that needs to be edited in the similar way as in the beginning. New window is similar to the creating window with editing and deleting ingredient possibilities. User can only delete or edit one ingredient. If more is selected, only the first one will be edited or deleted.<br><br>
<li><b>Delete Recipe</b></li>
Pressing this button while having some recipe selected results in deletion of this recipe.<br><br>

<h2>Ingredients</h2>


This bar works with ingredients stored in the warehouse.<br>
<br>
<h3>Manipulations</h3>


<li><b>Add Ingredient</b></li>
Easier way of adding ingredients and the slower one. User can only add one ingredient at a time by naming the ingredient and setting its properties (unit and amount).<br><br>

<li><b>Edit Ingredient</b></li>
Only one ingredient can be edited at a time. If more are selected, only the first one will be edited.<br><br>

<li><b>Import from XML</b></li>
<i>For advanced users</i><br>
Imported XML must follow <a href='https://code.google.com/p/eszr-pb138/wiki/XMLWarehouseData'>pattern</a>.<br>
Available for <a href='https://googledrive.com/host/0B_UKPQ-6wAceTVZIcVZ0SFRva28/'>download</a><br> Each ingredient that is already in the warehouse will have its amount raised by the amount in XML. Any ingredient not present in the warehouse will be added.