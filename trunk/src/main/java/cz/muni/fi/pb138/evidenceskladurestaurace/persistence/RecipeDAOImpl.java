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
public class RecipeDAOImpl implements RecipeDAO {

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
    public void create(Recipe recipe) {

        List<Ingredient> ingredientList = recipe.getIngredients();

        Element parts = doc.createElement("parts");

        for (Ingredient ingredient : ingredientList) {
            Element ingr = doc.createElement("ingredient");
            ingr.setTextContent(ingredient.getName());

            Element unit = doc.createElement("unit");
            unit.setTextContent(ingredient.getUnit().toString());

            Element amount = doc.createElement("amount");
            amount.setTextContent(String.valueOf(ingredient.getAmount()));

            Element part = doc.createElement("part");
            part.appendChild(ingr);
            part.appendChild(unit);
            part.appendChild(amount);

            parts.appendChild(part);
        }

        Element name = doc.createElement("name");
        name.setTextContent(recipe.getName());

        Element receipt = doc.createElement("receipt");
        receipt.appendChild(name);
        receipt.appendChild(parts);

        Element receipts = doc.getDocumentElement();
        receipts.appendChild(receipt);

    }
    
    @Override
    public Recipe findRecipesByName(String name) {
        List<Ingredient> ingredientList = null;

        Element receipt = getReceiptElementByName(name);
        ingredientList = findAllIngredientsInReceiptElement(receipt);

        return new Recipe(name, ingredientList);

    }

    @Override
    public void update(Recipe recipe) {
        Element receipt = getReceiptElementByName(recipe.getName());
        NodeList parts = receipt.getElementsByTagName("part");
        List<Ingredient> ingredientList = recipe.getIngredients();

        for (int i = 0; i < parts.getLength(); i++) {
            Element part = null;
            if (parts.item(i) instanceof Element) {
                part = (Element) parts.item(i);
            }

            //parts have to be in the same order, refactor & test
            NodeList partItems = part.getChildNodes();
            for (int j = 0; j < partItems.getLength(); j++) {
                Element item = null;
                if (parts.item(i) instanceof Element) {
                    item = (Element) parts.item(i);
                }
                if (item.getNodeName().equals("amount")) {
                    item.setNodeValue(String.valueOf(ingredientList.get(i).getAmount())); //should += add or just replace !!ORDER
                }
            }
        }
    }

    @Override
    public void delete(Recipe recipe) {
        Element receipt = getReceiptElementByName(recipe.getName());
        doc.getDocumentElement().removeChild(receipt);
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> receiptList = new ArrayList<>();
        NodeList receiptNameList = doc.getElementsByTagName("name");

        for (int i = 0; i < receiptNameList.getLength(); i++) {
            Element item = null;
            if (receiptNameList.item(i) instanceof Element) {
                item = (Element) receiptNameList.item(i);
            }

            Recipe receipt = findRecipesByName(item.getTextContent());
            receiptList.add(receipt);
        }

        return receiptList;
    }

    public Element getReceiptElementByName(String name) {

        Element el = null;
        NodeList receiptList = doc.getElementsByTagName("receipt");

        for (int i = 0; i < receiptList.getLength(); i++) {
            if (receiptList.item(i) instanceof Element) {
                Element nameElement = (Element) receiptList.item(i).getFirstChild();

                if (nameElement.getTextContent().equals(name)) {
                    el = (Element) receiptList.item(i);
                }
            }
        }
        return el;
    }

    public List<Ingredient> findAllIngredientsInReceiptElement(Element receipt) {
        List<Ingredient> ingredientList = new ArrayList<>();

        NodeList parts = receipt.getElementsByTagName("part");

        for (int i = 0; i < parts.getLength(); i++) {
            Element part = null;
            if (parts.item(i) instanceof Element) {
                part = (Element) parts.item(i);
            }

            String ingredientName = null;
            Unit unit = null;
            int amount = 0;

            NodeList partItems = part.getChildNodes();
            for (int j = 0; j < partItems.getLength(); j++) {
                Element item = null;
                if (partItems.item(j) instanceof Element) {
                    item = (Element) partItems.item(i);
                }

                if (item.getNodeName().equals("ingredient")) {
                    ingredientName = item.getTextContent();
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
            ingredientList.add(new Ingredient(ingredientName, unit, amount));
        }

        return ingredientList;
    }
}
