package cz.muni.fi.pb138.evidenceskladurestaurace.persistence;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Adam Studenic
 */
public class IngredientDAOImpl implements IngredientDAO {

    private Document doc;
   
    @Override
    public Document getDoc() {
        return doc;
    }
        
    @Override
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    @Override
    public void create(Ingredient ingredient) {

        Element ingr = doc.createElement("ingredient");
        ingr.setTextContent(ingredient.getName());

        Element unit = doc.createElement("unit");
        unit.setTextContent(ingredient.getUnit().toString());

        Element amount = doc.createElement("amount");
        amount.setTextContent(String.valueOf(ingredient.getAmount()));

        Element product = doc.createElement("product");
        product.appendChild(ingr);
        product.appendChild(unit);
        product.appendChild(amount);

        Element products = doc.getDocumentElement();
        products.appendChild(product);

    }

    //should return null?
    @Override
    public Ingredient findIngredientsByName(String name) {
        Unit unit = null;
        int amount = 0;

        Element product = getProductElementByName(name);
        NodeList productItems = product.getChildNodes();
        for (int i = 0; i < productItems.getLength(); i++) {
            Element item = null;
            if (productItems.item(i) instanceof Element) {
                item = (Element) productItems.item(i);
            }

            if (item.getNodeName().equals("unit")) {
                String unitName = item.getTextContent();
                switch (unitName) {
                    case "g":
                        unit = Unit.g;
                        break;
                    case "kg":
                        unit = Unit.kg;
                        break;
                    case "ml":
                        unit = Unit.ml;
                        break;
                    case "l":
                        unit = Unit.l;
                        break;
                    case "pcs":
                        unit = Unit.pcs;
                        break;
                    default:
                        unit = Unit.g;
                        break;
                }
            }

            if (item.getNodeName().equals("amount")) {
                amount = Integer.parseInt(item.getTextContent());
            }
        }

        return new Ingredient(name, unit, amount);

    }

    @Override
    public void update(Ingredient ingredient) {
        //TODO update name?unit?
        Element product = getProductElementByName(ingredient.getName());
        NodeList productItems = product.getChildNodes();

        for (int i = 0; i < productItems.getLength(); i++) {
            Element item = null;
            if (productItems.item(i) instanceof Element) {
                item = (Element) productItems.item(i);
            }

            if (item.getNodeName().equals("amount")) {
                item.setNodeValue(String.valueOf(ingredient.getAmount())); //should += add or just replace
            }
        }
        /*
         write to DOM?
         */
    }

    @Override
    public void delete(Ingredient ingredient) {
        Element product = getProductElementByName(ingredient.getName());
        doc.getDocumentElement().removeChild(product);
    }

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredientList = new ArrayList<>();
        NodeList nameList = doc.getElementsByTagName("ingredient");

        for (int i = 0; i < nameList.getLength(); i++) {
            Element item = null;
            if (nameList.item(i) instanceof Element) {
                item = (Element) nameList.item(i);
            }
            Ingredient ingredient = findIngredientsByName(item.getTextContent());
            ingredientList.add(ingredient);
        }

        return ingredientList;
    }

    public Element getProductElementByName(String name) {

        Element el = null;
        NodeList productList = doc.getElementsByTagName("product");

        for (int i = 0; i < productList.getLength(); i++) {
            if (productList.item(i) instanceof Element) {
                NodeList productItems = productList.item(i).getChildNodes();

                for (int j = 0; j < productItems.getLength(); j++) {
                    if (productItems.item(j) instanceof Element) {
                        Element item = (Element) productItems.item(j);

                        if (item.getTextContent().equals(name)) {
                            el = (Element) productList.item(i);
                        }
                    }
                }
            }
        }
        return el;
    }
}
