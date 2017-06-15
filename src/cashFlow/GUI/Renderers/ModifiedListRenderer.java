/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashFlow.GUI.Renderers;

import Tables.AccountTable;
import Tables.CategoryTable;
import java.awt.Component;
import java.time.Month;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author f98877a
 */
public class ModifiedListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (value instanceof CategoryTable) {
            value = ((CategoryTable) value).getCategory();
        } else if (value instanceof AccountTable) {
            value = ((AccountTable) value).getName();
        } else if (value instanceof Month) {
            value = ((Month) value).name();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

}
