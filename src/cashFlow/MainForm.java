/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashFlow;

import Tables.AccountTable;
import Tables.CashFlowInfo;
import Tables.ExpenseTable;
import Tables.IncomeTable;
import cashFlow.GUI.AddAccountScreen;
import cashFlow.GUI.AddScreen;
import cashFlow.GUI.StatementsScreen;
import cashFlow.Listeners.ValuesChangeAction;
import cashFlow.Listeners.ValuesChangeEvent;
import db.Control.ModelControl;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author F98877A
 */
public class MainForm extends javax.swing.JFrame implements ValuesChangeEvent {

    TrayIcon trayIcon;
    SystemTray tray;
    AccountTable mainAccount = null;
    AddScreen addScreen = new AddScreen();
    AddAccountScreen addAccountScreen = new AddAccountScreen();

    /**
     * Creates new form main
     */
    public MainForm() {

        super("CashFlow");

        initComponents();

        configScreenItens();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        accValueLbl = new javax.swing.JLabel();
        accTotIncomeLbl = new javax.swing.JLabel();
        accTotExpenseLbl = new javax.swing.JLabel();
        accountNameLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addButton.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        addButton.setForeground(new java.awt.Color(0, 0, 204));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jLabel1.setText("Cash Flow");

        jLabel2.setFont(new java.awt.Font("Lucida Console", 1, 14)); // NOI18N
        jLabel2.setText("Visão geral");

        jLabel3.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        jLabel3.setText("Contas");

        jLabel4.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 0));
        jLabel4.setText("Receitas");

        jLabel5.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("Despesas");

        accValueLbl.setFont(new java.awt.Font("Lucida Console", 0, 11)); // NOI18N
        accValueLbl.setText("R$ ");

        accTotIncomeLbl.setFont(new java.awt.Font("Lucida Console", 1, 11)); // NOI18N
        accTotIncomeLbl.setForeground(new java.awt.Color(0, 153, 0));
        accTotIncomeLbl.setText("R$ ");
        accTotIncomeLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                accTotIncomeLblMouseReleased(evt);
            }
        });

        accTotExpenseLbl.setFont(new java.awt.Font("Lucida Console", 1, 11)); // NOI18N
        accTotExpenseLbl.setForeground(new java.awt.Color(255, 0, 0));
        accTotExpenseLbl.setText("R$ ");
        accTotExpenseLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                accTotExpenseLblMouseReleased(evt);
            }
        });

        accountNameLbl.setText(".");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(accountNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addGap(113, 113, 113)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(accValueLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(accTotExpenseLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(accTotIncomeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(accValueLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountNameLbl)
                .addGap(13, 13, 13)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(accTotIncomeLbl))
                .addGap(12, 12, 12)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(accTotExpenseLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(addButton)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        addScreen.setAccount(mainAccount);
        addScreen.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void accTotIncomeLblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accTotIncomeLblMouseReleased
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "incomes");
    }//GEN-LAST:event_accTotIncomeLblMouseReleased

    private void accTotExpenseLblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accTotExpenseLblMouseReleased
        // TODO add your handling code here:
        StatementsScreen screen = new StatementsScreen();
        screen.setVisible(true);
    }//GEN-LAST:event_accTotExpenseLblMouseReleased

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accTotExpenseLbl;
    private javax.swing.JLabel accTotIncomeLbl;
    private javax.swing.JLabel accValueLbl;
    private javax.swing.JLabel accountNameLbl;
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

    private void UpdateOverviewValues(CashFlowInfo.addScreenOp operation) {

        switch (operation) {
            case ACCOUNT:
                if (isThereAccount()) {
                    accountNameLbl.setText(mainAccount.getName());
                } else {
                    new AddAccountScreen().setVisible(true);
                }
                break;

            case EXPENSE:
                ExpenseTable exp = new ExpenseTable();

                try {
                    exp = (ExpenseTable) ModelControl.load(ExpenseTable.class, 1);
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                Double expValue = exp.getValue();
                mainAccount.setValue(mainAccount.getValue() - expValue);
                mainAccount.setTotalExpense(mainAccount.getTotalExpense() + expValue);

                break;
            case INCOME:
                IncomeTable inc = new IncomeTable();

                try {
                    inc = (IncomeTable) ModelControl.load(IncomeTable.class, 1);
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                Double incValue = inc.getValue();
                mainAccount.setValue(mainAccount.getValue() + incValue);
                mainAccount.setTotalIncome(mainAccount.getTotalIncome() + incValue);

                break;
            default:
                break;

        }

        if (mainAccount.getValue() >= 0) {
            mainAccount.setStatus(CashFlowInfo.POSITIVE);
        } else {
            mainAccount.setStatus(CashFlowInfo.NEGATIVE);
        }
        accValueLbl.setText("R$ " + String.valueOf(mainAccount.getValue()));
        accTotExpenseLbl.setText("R$ " + String.valueOf(mainAccount.getTotalExpense()));
        accTotIncomeLbl.setText("R$ " + String.valueOf(mainAccount.getTotalIncome()));

        try {
            ModelControl.update(mainAccount);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isThereAccount() {

        try {
            mainAccount = (AccountTable) ModelControl.load(AccountTable.class, 1);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainAccount != null;
    }

    @Override
    public void setValuesChanged(ValuesChangeAction panel) {

        if (panel instanceof javax.swing.JFrame) {
            String childName = panel.getClass().getSimpleName();
            switch (childName) {
                case "AddAccountScreen":
                    UpdateOverviewValues(CashFlowInfo.addScreenOp.ACCOUNT);
                    break;
                case "AddExpenseScreen":
                    UpdateOverviewValues(CashFlowInfo.addScreenOp.EXPENSE);
                    break;
                case "AddIncomeScreen":
                    UpdateOverviewValues(CashFlowInfo.addScreenOp.INCOME);
                    break;
                default:
                    break;

            }
        }

    }

    private void configScreenItens() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("LOCK.png"));
        new ModelControl();
        addScreen.setPanelToChange(this);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        accTotIncomeLbl.setToolTipText("Click aqui para ver as movimentações!");
        accTotExpenseLbl.setToolTipText("Click aqui para ver as movimentações!");

        if (!isThereAccount()) {
            addAccountScreen.setPanelToChange(this);
            addAccountScreen.setVisible(true);
        } else {
            UpdateOverviewValues(CashFlowInfo.addScreenOp.ACCOUNT);
        }
    }

}
