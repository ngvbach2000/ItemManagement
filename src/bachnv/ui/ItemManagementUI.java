/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.ui;

import bachnv.tblitems.ItemsDAO;
import bachnv.tblitems.ItemsDTO;
import bachnv.tblitems.ItemsFullModel;
import bachnv.tblsuppliers.SuppliersDAO;
import bachnv.tblsuppliers.SuppliersDTO;
import bachnv.tblsuppliers.SuppliersFullModel;
import bachnv.util.DBAccess;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author ngvba
 */
public class ItemManagementUI extends javax.swing.JDialog {

    /**
     * Creates new form ItemManagementUI
     */
    LoginUI loginFrom;
    ItemsDAO items;
    SuppliersDAO suppliers;
    ItemsFullModel itemsModel;
    SuppliersFullModel suppliersModel;
    boolean addNewItem = false;
    boolean addNewSup = false;
    final int SUCCESS = 1;

    public ItemManagementUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        //check connection to database
        checkConnection();

        //check login
        //when fullnanem is empty --> return to login form
        loginFrom = new LoginUI();
        try {
            loginFrom = (LoginUI) parent;
            if (loginFrom.fullName == null || loginFrom.fullName == "") {
                this.dispose();
                loginFrom.setVisible(true);
            } else if (!loginFrom.fullName.isEmpty()) {
                lbWelcome.setText("Welcome, " + loginFrom.fullName);
                loginFrom.setVisible(false);
            }
        } catch (ClassCastException e) {
            loginFrom.setVisible(true);
            this.dispose();
        }

        //define dao & model
        suppliers = new SuppliersDAO();
        suppliersModel = new SuppliersFullModel(suppliers);
        items = new ItemsDAO();
        itemsModel = new ItemsFullModel(items);
        loadFromDB();

        //Cant edit code when click add new
        txtSupCode.setEditable(false);
        txtItemCode.setEditable(false);
        if (suppliers.isEmpty()) {
            cbxItemSup.setEnabled(false);
        }

        //Selection in table
        tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    }

    //setup item, supplier, combobox model
    private void setupModel() {
        tblItems.setModel(itemsModel);
        cbxItemSup.setModel(new DefaultComboBoxModel(suppliers));
        tblSuppliers.setModel(suppliersModel);
        cbxItemSup.setSelectedIndex(-1);
    }

    //load data from database
    private void loadFromDB() {
        //clear all vector
        suppliers.removeAllElements();
        items.removeAllElements();

        //load form database via jdbc
        try {
            suppliers.loadFromDB();
            items.loadFromDB(suppliers);
        } catch (SQLException ex) {
            Logger.getLogger(ItemManagementUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //check data has supplier to set enable combobox
        cbxItemSup.setEnabled(true);
        if (suppliers.isEmpty()) {
            cbxItemSup.setEnabled(false);
        }

        //setup data model to table
        setupModel();
        tblSuppliers.updateUI();
        tblItems.updateUI();
    }

    //check validate items
    private boolean validItemData() {

        //check code cannot empty
        //check code max length 10
        //check code contains special characters
        String code = txtItemCode.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item code cannot empty!");
            txtItemCode.requestFocus();
            return false;
        } else if (!code.matches("\\w{0,10}")) {
            JOptionPane.showMessageDialog(this, "Code max length is 10, not contains special characters!");
            txtItemCode.requestFocus();
            return false;
        }

        //check name cannot empty
        //check name max length 50
        String itemName = txtItemName.getText().trim();
        if (itemName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item name cannot empty!");
            txtItemName.requestFocus();
            return false;
        } else if (!itemName.matches(".{0,50}")) {
            JOptionPane.showMessageDialog(this, "Item name max length is 50!");
            txtItemName.requestFocus();
            return false;
        }

        //check unit cannot empty
        //check unit max length 50
        String unit = txtItemUnit.getText().trim();
        if (unit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unit cannot empty!");
            txtItemUnit.requestFocus();
            return false;
        } else if (!unit.matches(".{0,50}")) {
            JOptionPane.showMessageDialog(this, "Unit max length is 50!");
            txtItemUnit.requestFocus();
            return false;
        }

        //check price cannot empty
        //check price is nummber
        //check price length
        String price = txtItemPrice.getText().trim();
        if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Price cannot empty!");
            txtItemPrice.requestFocus();
            return false;
        } else if (!price.matches("\\d+[.]?\\d*")) {
            JOptionPane.showMessageDialog(this, "Price must be number and bigger than 0!");
            txtItemPrice.requestFocus();
            return false;
        } else if (price.length() > 20) {
            JOptionPane.showMessageDialog(this, "Price is too long, please input shorter price!");
            txtItemPrice.requestFocus();
            return false;
        }

        return true;
    }

    //check validate supplier
    private boolean validSupData() {

        //check code cant empty
        //check code max length 10
        //check contains special characters
        String code = txtSupCode.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Supplier code cannot empty!");
            txtSupCode.requestFocus();
            return false;
        } else if (!code.matches("\\w{0,10}")) {
            JOptionPane.showMessageDialog(this, "Code max length is 10, not contains special characters!");
            txtSupCode.requestFocus();
            return false;
        }

        //check name cant empty
        //check name max length 50
        String supName = txtSupName.getText().trim();
        if (supName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Supplier name cannot empty!");
            txtSupName.requestFocus();
            return false;
        } else if (!supName.matches(".{1,50}")) {
            JOptionPane.showMessageDialog(this, "Supplier name max length is 50!");
            txtSupName.requestFocus();
            return false;
        }

        //check address cant empty
        //check address max length 50
        String address = txtSupAddress.getText().trim();
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot empty!");
            txtSupAddress.requestFocus();
            return false;
        }
        if (!address.matches(".{1,50}")) {
            JOptionPane.showMessageDialog(this, "Address max length is 50!");
            txtSupAddress.requestFocus();
            return false;
        }

        return true;
    }

    //check connection to database after do action
    private void checkConnection() {
        try {
            Connection con = DBAccess.openConnection();
            //if lost connect to database --> exit the application
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Loss connection to database!");
                JOptionPane.showMessageDialog(this, "Please connect to database and open application again!");
                System.exit(0);
            }
        } catch (Exception e) {
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

        lbWelcome = new javax.swing.JLabel();
        tpnManager = new javax.swing.JTabbedPane();
        pnSuppliers = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSuppliers = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSupCode = new javax.swing.JTextField();
        txtSupName = new javax.swing.JTextField();
        txtSupAddress = new javax.swing.JTextField();
        ckbSupCollaborating = new javax.swing.JCheckBox();
        btnSupAdd = new javax.swing.JButton();
        btnSupDelete = new javax.swing.JButton();
        btnSupSave = new javax.swing.JButton();
        pnItems = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtItemUnit = new javax.swing.JTextField();
        txtItemPrice = new javax.swing.JTextField();
        cbxItemSup = new javax.swing.JComboBox<>();
        ckbItemSup = new javax.swing.JCheckBox();
        btnItemAdd = new javax.swing.JButton();
        btnItemDelete = new javax.swing.JButton();
        btnItemSave = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lbWelcome.setForeground(new java.awt.Color(255, 0, 0));
        lbWelcome.setText("Welcome, ");

        tpnManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tpnManagerMouseClicked(evt);
            }
        });

        pnSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnSuppliersMouseClicked(evt);
            }
        });

        tblSuppliers.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSuppliersMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblSuppliers);

        jLabel1.setText("Code:");

        jLabel2.setText("Name:");

        jLabel3.setText("Address:");

        jLabel4.setText("Collaborating:");

        btnSupAdd.setText("Add new");
        btnSupAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupAddActionPerformed(evt);
            }
        });

        btnSupDelete.setText("Delete");
        btnSupDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupDeleteActionPerformed(evt);
            }
        });

        btnSupSave.setText("Save");
        btnSupSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupCode)
                            .addComponent(txtSupName)
                            .addComponent(txtSupAddress)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbSupCollaborating)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSupAdd)
                        .addGap(18, 18, 18)
                        .addComponent(btnSupSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(btnSupDelete)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSupAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ckbSupCollaborating))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupAdd)
                    .addComponent(btnSupDelete)
                    .addComponent(btnSupSave))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnSuppliersLayout = new javax.swing.GroupLayout(pnSuppliers);
        pnSuppliers.setLayout(pnSuppliersLayout);
        pnSuppliersLayout.setHorizontalGroup(
            pnSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSuppliersLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnSuppliersLayout.setVerticalGroup(
            pnSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSuppliersLayout.createSequentialGroup()
                .addGroup(pnSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnSuppliersLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        tpnManager.addTab("Suppliers", pnSuppliers);

        pnItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnItemsMouseClicked(evt);
            }
        });

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
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
        tblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblItemsMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblItems);

        jLabel5.setText("Code:");

        jLabel6.setText("Name:");

        jLabel7.setText("Supplier:");

        jLabel8.setText("Unit:");

        jLabel9.setText("Price:");

        jLabel10.setText("Supplying:");

        cbxItemSup.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnItemAdd.setText("Add new");
        btnItemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemAddActionPerformed(evt);
            }
        });

        btnItemDelete.setText("Delete");
        btnItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDeleteActionPerformed(evt);
            }
        });

        btnItemSave.setText("Save");
        btnItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtItemCode)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(ckbItemSup)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtItemName)
                            .addComponent(cbxItemSup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtItemUnit)
                            .addComponent(txtItemPrice)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnItemAdd)
                        .addGap(16, 16, 16)
                        .addComponent(btnItemSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(btnItemDelete)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxItemSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtItemUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(ckbItemSup))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnItemAdd)
                    .addComponent(btnItemDelete)
                    .addComponent(btnItemSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnItemsLayout = new javax.swing.GroupLayout(pnItems);
        pnItems.setLayout(pnItemsLayout);
        pnItemsLayout.setHorizontalGroup(
            pnItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnItemsLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnItemsLayout.setVerticalGroup(
            pnItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnItemsLayout.createSequentialGroup()
                .addGroup(pnItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnItemsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tpnManager.addTab("Items", pnItems);

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tpnManager)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbWelcome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tpnManager)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbWelcome)
                    .addComponent(btnLogout)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupAddActionPerformed
        //check cnnection to database
        //clear selection in table
        //clear data in texfield
        checkConnection();
        tblSuppliers.clearSelection();
        addNewSup = true;
        txtSupCode.setText("");
        txtSupCode.setEditable(true);
        txtSupCode.requestFocus();
        txtSupName.setText("");
        txtSupAddress.setText("");
        ckbSupCollaborating.setSelected(true);
    }//GEN-LAST:event_btnSupAddActionPerformed

    private void btnSupSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupSaveActionPerformed
        //check connection to database
        checkConnection();
        //if not press add new --> update
        if (addNewSup == false && tblSuppliers.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select supplier row to update");
            return;
        }
        //check validate
        if (!validSupData()) {
            return;
        }
        //get data in textfield 
        String supCode = txtSupCode.getText();
        String supName = txtSupName.getText();
        String address = txtSupAddress.getText();
        boolean colloborating = ckbSupCollaborating.isSelected();
        SuppliersDTO supplier = new SuppliersDTO(supCode, supName, address, colloborating);
        try {
            //if press add new --> add to data
            if (addNewSup == true) {
                //check code duplicate
                if (SuppliersDAO.getSupByCode(supCode) != null) {
                    JOptionPane.showMessageDialog(this, "Code is duplicated");
                    return;
                }
                //add new supplier and clean textfield
                if (SuppliersDAO.insert(supplier)) {
                    addNewSup = false;
                    JOptionPane.showMessageDialog(this, "Data saved!");
                    txtSupCode.setText("");
                    txtSupCode.setEditable(false);
                    txtSupName.setText("");
                    txtSupAddress.setText("");
                    ckbSupCollaborating.setSelected(false);
                    tblSuppliers.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot insert Supplier");
                }
                //if not press add new --> update
            } else {
                //update supplier and clean textfield
                if (SuppliersDAO.update(supplier)) {
                    JOptionPane.showMessageDialog(this, "Update data saved!");
                    txtSupCode.setText("");
                    txtSupCode.setEditable(false);
                    txtSupName.setText("");
                    txtSupAddress.setText("");
                    ckbSupCollaborating.setSelected(false);
                    tblSuppliers.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot update because Supplier code " + supCode + " doesn't exist");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManagementUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadFromDB();
    }//GEN-LAST:event_btnSupSaveActionPerformed

    private void btnSupDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupDeleteActionPerformed
        //check connection to database
        checkConnection();
        //check user is selected row on table
        if (tblSuppliers.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select supplier row to delete!");
            return;
        }
        String supCode = txtSupCode.getText();
        //confirm message
        //if yes --> delete
        int r = JOptionPane.showConfirmDialog(this, "Do you want to remove", "Remove", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            try {
                //if supplier includes items --> not delete
                if (SuppliersDAO.getItemBySupCode(supCode) != null) {
                    JOptionPane.showMessageDialog(this, "Item are use a supplier");
                    return;
                }
                //delete supplier and clean textfield
                if (SuppliersDAO.delete(supCode)) {
                    JOptionPane.showMessageDialog(this, "Delete!");
                    txtSupCode.setText("");
                    txtSupCode.setEditable(false);
                    txtSupName.setText("");
                    txtSupAddress.setText("");
                    ckbSupCollaborating.setSelected(false);
                    tblSuppliers.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete fail!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ItemManagementUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadFromDB();
    }//GEN-LAST:event_btnSupDeleteActionPerformed

    private void btnItemAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAddActionPerformed
        //check connection to database
        checkConnection();
        //check if supplier not available
        if (suppliers.size() == 0) {
            JOptionPane.showMessageDialog(this, "Please add new suppliers first!");
            return;
        }
        //clean textfield
        addNewItem = true;
        tblItems.clearSelection();
        txtItemCode.setText("");
        txtItemCode.setEditable(true);
        txtItemCode.requestFocus();
        txtItemName.setText("");
        cbxItemSup.setSelectedIndex(0);
        txtItemUnit.setText("");
        txtItemPrice.setText("");
        ckbItemSup.setSelected(true);
    }//GEN-LAST:event_btnItemAddActionPerformed

    private void btnItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemSaveActionPerformed
        //check connection to database
        checkConnection();
        //if not press add new --> update
        if (addNewItem == false && tblItems.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select Item row to update");
            return;
        }
        //check validate
        if (!validItemData()) {
            return;
        }
        //get information
        String itemCode = txtItemCode.getText();
        String itemName = txtItemName.getText();
        SuppliersDTO supplier = (SuppliersDTO) cbxItemSup.getSelectedItem();
        String unit = txtItemUnit.getText();
        float price = Float.parseFloat(txtItemPrice.getText());
        boolean supplying = ckbItemSup.isSelected();
        ItemsDTO item = new ItemsDTO(itemCode, itemName, supplier, unit, price, supplying);
        try {
            //if press add new --> add new item
            if (addNewItem == true) {
                //check code duplicated
                if (ItemsDAO.getItemByCode(this.suppliers, itemCode) != null) {
                    JOptionPane.showMessageDialog(this, "Code is duplicated");
                    return;
                }
                //Inster item to database
                if (ItemsDAO.insert(item)) {
                    addNewItem = false;
                    JOptionPane.showMessageDialog(this, "Data saved!");
                    tblItems.clearSelection();
                    txtItemCode.setText("");
                    txtItemCode.setEditable(false);
                    txtItemName.setText("");
                    cbxItemSup.setSelectedIndex(-1);
                    txtItemUnit.setText("");
                    txtItemPrice.setText("");
                    ckbItemSup.setSelected(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Please insert Item Code");
                }
            } else {
                // if not press add new --> update
                if (ItemsDAO.update(item)) {
                    JOptionPane.showMessageDialog(this, "Update Data saved!");
                    tblItems.clearSelection();
                    txtItemCode.setText("");
                    txtItemCode.setEditable(false);
                    txtItemName.setText("");
                    cbxItemSup.setSelectedIndex(-1);
                    txtItemUnit.setText("");
                    txtItemPrice.setText("");
                    ckbItemSup.setSelected(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot update because Item code " + itemCode + " doesn't exist");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManagementUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadFromDB();
    }//GEN-LAST:event_btnItemSaveActionPerformed

    private void btnItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDeleteActionPerformed
        //check connection to database
        checkConnection();
        //check user is selected row on table
        if (tblItems.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select Item to delete");
            return;
        }
        String itemCode = txtItemCode.getText();
        //comfirm message
        //if yes --> remove item
        int r = JOptionPane.showConfirmDialog(this, "Do you want to remove", "Remove", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            //detele item in database and clean textfield
            try {
                if (ItemsDAO.delete(itemCode)) {
                    JOptionPane.showMessageDialog(this, "Delete!");
                    txtItemCode.setText("");
                    txtItemCode.setEditable(false);
                    txtItemName.setText("");
                    cbxItemSup.setSelectedIndex(-1);
                    txtItemUnit.setText("");
                    txtItemPrice.setText("");
                    ckbItemSup.setSelected(false);
                    tblItems.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete fail!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ItemManagementUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadFromDB();
    }//GEN-LAST:event_btnItemDeleteActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        //check connection to database
        checkConnection();
        //Confirm message
        //The application not responsible for data without pressing "Save"
        int r = JOptionPane.showConfirmDialog(this, "Do you want to exit without saving?", "Exit", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            //set fullname = null
            //return to login form
            loginFrom.fullName = "";
            this.dispose();
            loginFrom.setVisible(true);
        } else {
            this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
            return;
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //check connection to database
        checkConnection();
        //Confirm message
        //The application not responsible for data without pressing "Save"
        int r = JOptionPane.showConfirmDialog(this, "Do you want to exit without saving?", "Exit", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
            return;
        }
    }//GEN-LAST:event_formWindowClosing

    private void pnSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnSuppliersMouseClicked
        //refresh all information when double click to panel
        if (evt.getClickCount() == 2) {
            tblItems.clearSelection();
            txtItemCode.setText("");
            txtItemCode.setEditable(false);
            txtItemName.setText("");
            cbxItemSup.setSelectedIndex(-1);
            txtItemUnit.setText("");
            txtItemPrice.setText("");
            ckbItemSup.setSelected(false);
            addNewItem = false;
            tblSuppliers.clearSelection();
            txtSupCode.setText("");
            txtSupCode.setEditable(false);
            txtSupName.setText("");
            txtSupAddress.setText("");
            ckbSupCollaborating.setSelected(false);
            addNewSup = false;
        }
    }//GEN-LAST:event_pnSuppliersMouseClicked

    private void pnItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnItemsMouseClicked
        //refresh all information when double click to panel
        if (evt.getClickCount() == 2) {
            tblItems.clearSelection();
            txtItemCode.setText("");
            txtItemCode.setEditable(false);
            txtItemName.setText("");
            cbxItemSup.setSelectedIndex(-1);
            txtItemUnit.setText("");
            txtItemPrice.setText("");
            ckbItemSup.setSelected(false);
            addNewItem = false;
            tblSuppliers.clearSelection();
            txtSupCode.setText("");
            txtSupCode.setEditable(false);
            txtSupName.setText("");
            txtSupAddress.setText("");
            ckbSupCollaborating.setSelected(false);
            addNewSup = false;
        }
    }//GEN-LAST:event_pnItemsMouseClicked

    private void tpnManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpnManagerMouseClicked
        //refresh all information when click to table panel
        tblItems.clearSelection();
        txtItemCode.setText("");
        txtItemCode.setEditable(false);
        txtItemName.setText("");
        cbxItemSup.setSelectedIndex(-1);
        txtItemUnit.setText("");
        txtItemPrice.setText("");
        ckbItemSup.setSelected(false);
        addNewItem = false;
        tblSuppliers.clearSelection();
        txtSupCode.setText("");
        txtSupCode.setEditable(false);
        txtSupName.setText("");
        txtSupAddress.setText("");
        ckbSupCollaborating.setSelected(false);
        addNewSup = false;
    }//GEN-LAST:event_tpnManagerMouseClicked

    private void tblSuppliersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSuppliersMouseReleased
        //check connection to database
        //cannot add new
        //cannot edit code
        //get supplier at posision and show to textfield
        checkConnection();
        addNewSup = false;
        txtSupCode.setEditable(false);
        int pos = tblSuppliers.getSelectedRow();
        SuppliersDTO supplier = suppliersModel.getSuppliers().get(pos);
        txtSupCode.setText(supplier.getSupCode());
        txtSupName.setText(supplier.getSupName());
        txtSupAddress.setText(supplier.getAddress());
        ckbSupCollaborating.setSelected(supplier.isColloborating());
    }//GEN-LAST:event_tblSuppliersMouseReleased

    private void tblItemsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseReleased
        //check connection to database
        //cannot add new
        //cannot edit code
        //get item at posision and show to textfield
        checkConnection();
        addNewItem = false;
        int pos = tblItems.getSelectedRow();
        txtItemCode.setEditable(false);
        ItemsDTO item = itemsModel.getItems().get(pos);
        txtItemCode.setText(item.getItemCode());
        txtItemName.setText(item.getItemName());
        int index = suppliers.find(item.getSuppiler().getSupCode());
        cbxItemSup.setSelectedIndex(index);
        txtItemUnit.setText("" + item.getUnit());
        txtItemPrice.setText("" + item.getPrice());
        ckbItemSup.setSelected(item.isSupplying());
    }//GEN-LAST:event_tblItemsMouseReleased

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
            java.util.logging.Logger.getLogger(ItemManagementUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemManagementUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemManagementUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemManagementUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ItemManagementUI dialog = new ItemManagementUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(false);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnItemAdd;
    private javax.swing.JButton btnItemDelete;
    private javax.swing.JButton btnItemSave;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSupAdd;
    private javax.swing.JButton btnSupDelete;
    private javax.swing.JButton btnSupSave;
    private javax.swing.JComboBox<String> cbxItemSup;
    private javax.swing.JCheckBox ckbItemSup;
    private javax.swing.JCheckBox ckbSupCollaborating;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbWelcome;
    private javax.swing.JPanel pnItems;
    private javax.swing.JPanel pnSuppliers;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblSuppliers;
    private javax.swing.JTabbedPane tpnManager;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrice;
    private javax.swing.JTextField txtItemUnit;
    private javax.swing.JTextField txtSupAddress;
    private javax.swing.JTextField txtSupCode;
    private javax.swing.JTextField txtSupName;
    // End of variables declaration//GEN-END:variables
}
