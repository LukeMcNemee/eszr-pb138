package cz.muni.fi.pb138.evidenceskladurestaurace;

import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Ingredient;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.IngredientDAO;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.IngredientDAOImpl;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.RecipeDAO;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.RecipeDAOImpl;
import cz.muni.fi.pb138.evidenceskladurestaurace.model.IngredientsTableModel;
import cz.muni.fi.pb138.evidenceskladurestaurace.model.RecipeListModel;
import cz.muni.fi.pb138.evidenceskladurestaurace.persistence.Recipe;
import cz.muni.fi.pb138.evidenceskladurestaurace.service.IngredientsService;
import java.awt.Color;
import java.awt.Component;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.XMLConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author fbogyai
 */
public class RestaurantGUI extends javax.swing.JFrame {

    private static final String INGREDIENTS = "/Warehouse.xml";
    private static final String RECIPES = "/Receipts.xml";
    private static final String INGREDIENTS_SCHEMA = "/Warehouse_schema.xsd";
    private static final String RECIPES_SCHEMA = "/Receipts_schema.xsd";
    private Document ingredientDB;
    private Document recipeDB;
    private RecipeDAO recipeDAO = new RecipeDAOImpl();
    private IngredientDAO ingredientDAO = new IngredientDAOImpl();
    private IngredientsTableModel ingredientsTableModel = new IngredientsTableModel();
    private IngredientsTableModel recipeIngredientsTableModel = new IngredientsTableModel();
    private RecipeListModel recipeListModel = new RecipeListModel();
    private IngredientsService ingredientsService;

    /**
     * Creates new form RestaurantGUI
     */
    public RestaurantGUI() {

        try {
            URI ingredients = getClass().getResource(INGREDIENTS).toURI();
            URI recipes = getClass().getResource(RECIPES).toURI();

            recipeDB = setDocument(recipes);
            ingredientDB = setDocument(ingredients);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (ingredientDB == null) {
            System.err.print("No XML loaded");
        }
        ingredientDAO.setDoc(ingredientDB);
        recipeDAO.setDoc(recipeDB);
        ingredientsService = new IngredientsService(ingredientDAO);
        initComponents();
    }

    private Ingredient getSelectedIngredient(int row) {
        return ingredientDAO.findIngredientsByName(String.valueOf(ingredienceTable.getValueAt(row, 0)));
    }

    private void refreshIngredientsTable() {
        //pre kotrolny vypis
        List<Ingredient> temp = ingredientDAO.findAll();
        for (Ingredient i : temp) {
            System.out.println(i.toString());
        }

        ingredientsTableModel.setIngredients(ingredientDAO.findAll());

    }

    private void refreshRecipeList() {
        recipeListModel.setRecipes(recipeDAO.findAll());
    }

    private Document setDocument(URI uri) throws SAXException, ParserConfigurationException,
            IOException {
        Document doc;
        // Vytvorime instanci tovarni tridy
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Pomoci tovarni tridy ziskame instanci DocumentBuilderu
        DocumentBuilder builder = factory.newDocumentBuilder();
        // DocumentBuilder pouzijeme pro zpracovani XML dokumentu
        // a ziskame model dokumentu ve formatu W3C DOM
        doc = builder.parse(uri.toString());
        return doc;

    }

    public void serializetoXML(URI output, Document doc)
            throws IOException, TransformerConfigurationException, TransformerException {
        // Vytvorime instanci tovarni tridy
        TransformerFactory factory = TransformerFactory.newInstance();
        // Pomoci tovarni tridy ziskame instanci tzv. kopirovaciho transformeru
        Transformer transformer = factory.newTransformer();
        // Vstupem transformace bude dokument v pameti
        DOMSource source = new DOMSource(doc);
        // Vystupem transformace bude vystupni soubor
        StreamResult result = new StreamResult(output.toString());
        // Provedeme transformaci
        transformer.transform(source, result);
    }

    private class ImportRecipesSwingWorker extends SwingWorker<Void, Void>{
        
        private String path;
        public ImportRecipesSwingWorker(String path) {
            this.path = path;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            try {
                //import nacteme jako DOM tree
                URI file = new URI(path);
                Document doc = setDocument(file);

                //nacteme schema pro recepty
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Source schemaFile = new StreamSource(new File(getClass().getResource(RECIPES_SCHEMA).toURI()));
                Schema schema = factory.newSchema(schemaFile);

                //overime dokument oproti schema
                try {
                    Validator validator = schema.newValidator();
                    validator.validate(new DOMSource(doc));
                    //pokud neni dokument validni, vznika SAXException
                } catch (SAXException e) {
                    JOptionPane.showMessageDialog(rootPane, "Wrong file structure in" + e.toString(), "Warning", WIDTH, null);
                    throw e;
                }
                //pridame recepty do databaze
                RecipeDAOImpl imported = new RecipeDAOImpl();
                imported.setDoc(doc);
                List<Recipe> importedRecipes = imported.findAll();
                Iterator i = importedRecipes.iterator();
                while (i.hasNext()) {
                    recipeDAO.create((Recipe) i.next());
                }
                JOptionPane.showMessageDialog(rootPane, "Import finished", "Success", WIDTH, null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Import failed because:" + e.toString(), "Warning", WIDTH, null);
            }            
            return null;
        }        

        @Override
        protected void done() {
            refreshRecipeList();            
        }       
    }
    
    private class ImportIngredientsSwingWorker extends SwingWorker<Void, Void>{
        
        private String path;
        public ImportIngredientsSwingWorker(String path) {
            this.path = path;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            try {
                //import nacteme jako DOM tree
                URI file = new URI(path);
                Document doc = setDocument(file);

                //nacteme schema pro recepty
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Source schemaFile = new StreamSource(new File(getClass().getResource(INGREDIENTS_SCHEMA).toURI()));
                Schema schema = factory.newSchema(schemaFile);

                //overime dokument oproti schema
                try {
                    Validator validator = schema.newValidator();
                    validator.validate(new DOMSource(doc));
                    //pokud neni dokument validni, vznika SAXException
                } catch (SAXException e) {
                    JOptionPane.showMessageDialog(rootPane, "Wrong file structure in" + e.toString(), "Warning", WIDTH, null);
                    throw e;
                }
                //pridame recepty do databaze
                IngredientDAOImpl imported = new IngredientDAOImpl();
                imported.setDoc(doc);
                List<Ingredient> importedRecipes = imported.findAll();
                for (Ingredient next : importedRecipes) {
                    if (ingredientDAO.findAll().contains(next)) {
                        next.setAmount(ingredientDAO.findIngredientsByName(next.getName()).getAmount() + next.getAmount());
                        ingredientDAO.update(next);
                    } else {
                        ingredientDAO.create(next);
                    }
                }
                JOptionPane.showMessageDialog(rootPane, "Import finished", "Success", WIDTH, null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Import failed because:" + e.toString(), "Warning", WIDTH, null);
            }           
            return null;
        }        

        @Override
        protected void done() {
            refreshIngredientsTable();            
        }       
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        recipeList = new javax.swing.JList();
        cookingButton = new javax.swing.JButton();
        checkIngredientsButton = new javax.swing.JButton();
        importReceptButton = new javax.swing.JButton();
        createEditReceipt = new javax.swing.JButton();
        cookingSpinner = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        recipeIngredientsTable = new javax.swing.JTable();
        maxButton = new javax.swing.JButton();
        maxLabel = new javax.swing.JLabel();
        editRecipe = new javax.swing.JButton();
        deleteRecipe = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ingredienceTable = new javax.swing.JTable();
        newIngredientButton = new javax.swing.JButton();
        editIngredient = new javax.swing.JButton();
        importIngredientButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        recipeList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        recipeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                recipeListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(recipeList);

        cookingButton.setText("Cook");
        cookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cookingButtonActionPerformed(evt);
            }
        });

        checkIngredientsButton.setText("Check Ingredients");
        checkIngredientsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkIngredientsButtonActionPerformed(evt);
            }
        });

        importReceptButton.setText("Import from XML");
        importReceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importReceptButtonActionPerformed(evt);
            }
        });

        createEditReceipt.setText("Create new Recipe");
        createEditReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEditReceiptActionPerformed(evt);
            }
        });

        cookingSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        cookingSpinner.setName(""); // NOI18N

        recipeIngredientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(recipeIngredientsTable);

        maxButton.setText("Maximum portions");
        maxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxButtonActionPerformed(evt);
            }
        });

        editRecipe.setText("Edit Recipe");
        editRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRecipeActionPerformed(evt);
            }
        });

        deleteRecipe.setText("Delete Recipe");
        deleteRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRecipeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(createEditReceipt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editRecipe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteRecipe))
                            .addComponent(importReceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(cookingSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cookingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(checkIngredientsButton))
                                .addGap(18, 18, 18)
                                .addComponent(maxButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 94, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkIngredientsButton)
                            .addComponent(maxButton)
                            .addComponent(maxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cookingSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookingButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(importReceptButton)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createEditReceipt)
                            .addComponent(editRecipe)
                            .addComponent(deleteRecipe)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.addTab("Recepts", jPanel1);

        ingredienceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(ingredienceTable);

        newIngredientButton.setText("Add Ingredient");
        newIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newIngredientButtonActionPerformed(evt);
            }
        });

        editIngredient.setText("Edit Ingredient");
        editIngredient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editIngredientActionPerformed(evt);
            }
        });

        importIngredientButton.setText("Import from XML");
        importIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importIngredientButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editIngredient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newIngredientButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importIngredientButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(newIngredientButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editIngredient)
                        .addGap(18, 18, 18)
                        .addComponent(importIngredientButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.addTab("Ingredients", jPanel2);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Quit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            URI ingredients = getClass().getResource(INGREDIENTS).toURI();
            URI recipes = getClass().getResource(RECIPES).toURI();

            serializetoXML(recipes, recipeDB);
            serializetoXML(ingredients, ingredientDB);            

        } catch (Exception ex) {
            System.err.println("Cannot save application data to xml DB");
            ex.printStackTrace();
        }    
    }//GEN-LAST:event_formWindowClosing

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        if (tabbedPane.getSelectedIndex() == 0) {
            if (!recipeList.getModel().equals(recipeListModel)) {
                recipeList.setModel(recipeListModel);
            }
            if (!recipeIngredientsTable.getModel().equals(recipeIngredientsTableModel)) {
                recipeIngredientsTable.setModel(recipeIngredientsTableModel);

            }
            recipeList.setCellRenderer(new DefaultListCellRenderer());
            refreshRecipeList();
        }

        if (tabbedPane.getSelectedIndex() == 1) {
            if (!ingredienceTable.getModel().equals(ingredientsTableModel)) {
                ingredienceTable.setModel(ingredientsTableModel);
            }

            refreshIngredientsTable();

        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void importIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importIngredientButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            new ImportIngredientsSwingWorker(chooser.getSelectedFile().getPath()).execute();
        }
    }//GEN-LAST:event_importIngredientButtonActionPerformed

    private void editIngredientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editIngredientActionPerformed
        if (ingredienceTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a ingredient you wish to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            new IngredientDialog(ingredientsTableModel, ingredientDAO, getSelectedIngredient(ingredienceTable.getSelectedRow())).setVisible(true);
        }
    }//GEN-LAST:event_editIngredientActionPerformed

    private void newIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newIngredientButtonActionPerformed
        new IngredientDialog(ingredientsTableModel, ingredientDAO).setVisible(true);
    }//GEN-LAST:event_newIngredientButtonActionPerformed

    private void deleteRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRecipeActionPerformed
        if (recipeList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe you wish to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Recipe receipt = recipeDAO.findRecipesByName(String.valueOf(recipeList.getSelectedValue()));
            recipeDAO.delete(receipt);
            recipeListModel.setRecipes(recipeDAO.findAll());
            recipeList.setSelectedIndex(0);
        }
    }//GEN-LAST:event_deleteRecipeActionPerformed

    private void editRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRecipeActionPerformed
        if (recipeList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe you wish to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            new ReceiptDialog(recipeListModel, recipeIngredientsTableModel, recipeDAO, ingredientDAO, recipeDAO.findRecipesByName(String.valueOf(recipeList.getSelectedValue()))).setVisible(true);
        }
    }//GEN-LAST:event_editRecipeActionPerformed

    private void maxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxButtonActionPerformed
        if (recipeList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe you wish to cook.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Recipe recipe = recipeDAO.findRecipesByName(String.valueOf(recipeList.getSelectedValue()));
            int maximum = ingredientsService.maximumPortions(recipe);
            maxLabel.setText(String.valueOf(maximum));
        }
    }//GEN-LAST:event_maxButtonActionPerformed

    private void createEditReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createEditReceiptActionPerformed
        new ReceiptDialog(recipeListModel, recipeDAO, ingredientDAO).setVisible(true);
    }//GEN-LAST:event_createEditReceiptActionPerformed

    private void importReceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importReceptButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            new ImportRecipesSwingWorker(chooser.getSelectedFile().getPath()).execute();
        }
    }//GEN-LAST:event_importReceptButtonActionPerformed

    private void checkIngredientsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkIngredientsButtonActionPerformed
        System.err.println(recipeDAO.findRecipesByName("Játrové řízečky"));

        //recipeList.setBackground(Color.red);
        recipeList.setCellRenderer(new MyCellRenderer());
    }//GEN-LAST:event_checkIngredientsButtonActionPerformed

    private void cookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cookingButtonActionPerformed
        if (recipeList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe you wish to cook.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Recipe recipe = recipeDAO.findRecipesByName(String.valueOf(recipeList.getSelectedValue()));
            if (ingredientsService.checkAllIngredients(recipe, (int) cookingSpinner.getValue())) {
                ingredientsService.cook(recipe, (int) cookingSpinner.getValue());
            } else {
                JOptionPane.showMessageDialog(this, "Not enough ingredients to cook so many portions", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_cookingButtonActionPerformed

    private void recipeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_recipeListValueChanged
        if (recipeList.getValueIsAdjusting() == false) {
            Recipe recipe = recipeDAO.findRecipesByName(String.valueOf(recipeList.getSelectedValue()));
            recipeIngredientsTableModel.setIngredients(recipe.getIngredients());
        }
    }//GEN-LAST:event_recipeListValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RestaurantGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RestaurantGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RestaurantGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RestaurantGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RestaurantGUI().setVisible(true);

            }
        });
    }

    public class MyCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Color bg;
            Recipe recipe = recipeDAO.findRecipesByName(String.valueOf(value));
            if (ingredientsService.checkAllIngredients(recipe, 1)) {
                bg = Color.green;
            } else {
                bg = Color.red;
            }
            setBackground(bg);
            //setOpaque(true); // otherwise, it's transparent
            return this;  // DefaultListCellRenderer derived from JLabel, DefaultListCellRenderer.getListCellRendererComponent returns this as well.
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkIngredientsButton;
    private javax.swing.JButton cookingButton;
    private javax.swing.JSpinner cookingSpinner;
    private javax.swing.JButton createEditReceipt;
    private javax.swing.JButton deleteRecipe;
    private javax.swing.JButton editIngredient;
    private javax.swing.JButton editRecipe;
    private javax.swing.JButton importIngredientButton;
    private javax.swing.JButton importReceptButton;
    private javax.swing.JTable ingredienceTable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton maxButton;
    private javax.swing.JLabel maxLabel;
    private javax.swing.JButton newIngredientButton;
    private javax.swing.JTable recipeIngredientsTable;
    private javax.swing.JList recipeList;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
