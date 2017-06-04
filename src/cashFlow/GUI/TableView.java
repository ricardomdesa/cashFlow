/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashFlow.GUI;

import Tables.CashFlowInfo;
import Tables.CategoryTable;
import Tables.ExpenseTable;
import db.Control.ModelControl;
import db.tableInterfaces.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import utils.TableUtils;

/**
 *
 * @author F98877A
 */
public class TableView extends javax.swing.JPanel {

    protected List tableHeader;

    /**
     * Creates new form TableView
     */
    public TableView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stsPanel = new javax.swing.JPanel();
        stsScroll = new javax.swing.JScrollPane();
        stsTable = new javax.swing.JTable();

        stsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        stsScroll.setViewportView(stsTable);

        javax.swing.GroupLayout stsPanelLayout = new javax.swing.GroupLayout(stsPanel);
        stsPanel.setLayout(stsPanelLayout);
        stsPanelLayout.setHorizontalGroup(
            stsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        stsPanelLayout.setVerticalGroup(
            stsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel stsPanel;
    private javax.swing.JScrollPane stsScroll;
    private javax.swing.JTable stsTable;
    // End of variables declaration//GEN-END:variables

    public void fillExpensesTable(int month) throws Exception {

        List expenses = null;
        List ord = new ArrayList();
        ExpenseTable expenseTable = null;
        CategoryTable categ = null;

        tableHeader = new ArrayList();

        for (String ExpColumn : CashFlowInfo.ExpColumns) {
            tableHeader.add(ExpColumn);
            ord.add(ExpColumn);
        }

        try {
            stsTable = TableUtils.create(tableHeader, ord, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TableView.class.getName()).log(Level.SEVERE, null, ex);
        }
        stsScroll.setViewportView(stsTable);

        expenses = ModelControl.selectWithParam(ExpenseTable.class, month);

        for (int it = 0; it < expenses.size(); it++) {

            if (expenses.get(it) instanceof ExpenseTable) {
                expenseTable = (ExpenseTable) expenses.get(it);

                TableModel categoryLoad = ModelControl.load(CategoryTable.class, expenseTable.getCategory());

                if (categoryLoad instanceof CategoryTable) {
                    categ = (CategoryTable) categoryLoad;
                }

                TableUtils.addRow(stsTable);

                TableUtils.setCellValue(expenseTable.getDescription(), stsTable, it, tableHeader.get(1));
                TableUtils.setCellValue(expenseTable.getValue(), stsTable, it, tableHeader.get(2));
                TableUtils.setCellValue(categ.getCategory(), stsTable, it, tableHeader.get(3));
                TableUtils.setCellValue(expenseTable.getValue(), stsTable, it, tableHeader.get(4));

            }
        }

        stsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        stsTable.getTableHeader().setReorderingAllowed(false);

    }

}
