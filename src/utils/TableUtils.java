/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

//import dta.appl.model.Removable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Rogério Lecarião Leite
 * @contact <rogerio.leite@edc.eng.br> or <rogerio_lecariao_leite@hotmail.com>
 */
public class TableUtils {

    private TableUtils() {
    }

    public static List<DefaultCellTable> createCellTables(
            javax.swing.JTable table,
            Object columnIdentifier,
            List rowsData) throws Exception {

        List<DefaultCellTable> columnCells = new ArrayList();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        int column = table.getColumn(columnIdentifier).getModelIndex();
        Class columnClass = model.getColumnClass(column);

        try {
            for (Object columnVariable : rowsData) {

                DefaultCellTable defaultCellTable;
                defaultCellTable = createCellTable(columnClass, columnVariable);
                columnCells.add(defaultCellTable);
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new Exception("Fail to create DefaultCellTable list={" + rowsData + "}", ex);
        }

        return columnCells;
    }

    public static DefaultCellTable createCellTable(
            Class columnClass,
            Object columnVariable) throws InstantiationException, IllegalAccessException {

        Object object = columnClass.newInstance();
        DefaultCellTable defaultCellTable;
        if (object instanceof DefaultCellTable) {
            defaultCellTable = (DefaultCellTable) object;
        } else {
            defaultCellTable = new DefaultCellTable();
        }

        defaultCellTable.setCellValue(columnVariable);

        return defaultCellTable;
    }

    public static void populate(
            javax.swing.JTable table,
            List rowsObject) throws Exception {

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();

        int firstRow = model.getRowCount();
        model.setRowCount(firstRow + rowsObject.size());

        for (int column = 0; column < table.getColumnCount(); column++) {
            Object columnIdentifier = table.getColumnModel().getColumn(column).getIdentifier();
            for (int row = 0; row < rowsObject.size(); row++) {
                Object rowObject = rowsObject.get(row);
                TableUtils.setCellValue(rowObject, table, row, columnIdentifier);
            }
        }
    }

//    public static void setCellTableComboBox(
//            javax.swing.JTable table,
//            Object columnIdentifier,
//            List listData) throws Exception {
//
//        List<DefaultCellTable> comboBoxList = TableUtils.createCellTables(table, columnIdentifier, listData);
//        int column = table.getColumn(columnIdentifier).getModelIndex();
//        javax.swing.table.TableColumn tableColumn = table.getColumnModel().getColumn(column);
//        CellTableComboBoxRenderer comboBoxRenderer = new CellTableComboBoxRenderer(comboBoxList, columnIdentifier);
//        tableColumn.setCellRenderer(comboBoxRenderer);
//        tableColumn.setCellEditor(new javax.swing.DefaultCellEditor(comboBoxRenderer));
//    }
//    public static void setCellTableNumber(
//            javax.swing.JTable table,
//            Object columnIdentifier) {
//
//        int column = table.getColumn(columnIdentifier).getModelIndex();
//        javax.swing.table.TableColumn tableColumn = table.getColumnModel().getColumn(column);
//        tableColumn.setCellRenderer(table.getDefaultRenderer(Integer.class));
//        tableColumn.setCellEditor(new CellTableNumberEditor());
//    }
//    public static void setCellTableBoolean(
//            javax.swing.JTable table,
//            Object columnIdentifier) {
//
//        int column = table.getColumn(columnIdentifier).getModelIndex();
//        javax.swing.table.TableColumn tableColumn = table.getColumnModel().getColumn(column);
//        CellTableBooleanRenderer cellTableBooleanRenderer = new CellTableBooleanRenderer();
//        tableColumn.setCellRenderer(cellTableBooleanRenderer);
//        tableColumn.setCellEditor(new CellTableBooleanEditor(cellTableBooleanRenderer));
//    }
    public static javax.swing.JTable create(
            Map<Object, Class> header,
            List order,
            List rowsData) throws Exception {

        List columnClass = new ArrayList();
        Vector columnIdentifiers = new Vector();

        for (Object columnIdentifier : order) {
            if (header.containsKey(columnIdentifier)) {
                columnIdentifiers.add(columnIdentifier);
                columnClass.add(header.get(columnIdentifier));
            }
        }

        ExtendedDefaultTableModel model = new ExtendedDefaultTableModel(columnIdentifiers, columnClass, 0);
        javax.swing.JTable table = new javax.swing.JTable(model);

        TableUtils.populate(table, rowsData);

        return table;
    }

    public static Object getCellValue(
            javax.swing.JTable table,
            int row,
            Object columnIdentifier) {
        int idxColumn = table.getColumn(columnIdentifier).getModelIndex();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        Object cellValue = model.getValueAt(row, idxColumn);

        DefaultCellTable cellTable = null;
        if (cellValue instanceof DefaultCellTable) {
            cellTable = (DefaultCellTable) cellValue;
            cellValue = cellTable.getCellValue();
        }

        return cellValue;

    }

    public static Object setCellValue(
            Object value,
            javax.swing.JTable table,
            int row,
            Object columnIdentifier) throws Exception {
        int idxColumn = table.getColumn(columnIdentifier).getModelIndex();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();

        Class columnClass = model.getColumnClass(idxColumn);
        Object oldValue = model.getValueAt(row, idxColumn);
        Object newValue = value;

        if (!(value instanceof DefaultCellTable)) {
            newValue = TableUtils.createCellTable(columnClass, value);
        }

        if (oldValue instanceof DefaultCellTable) {
            DefaultCellTable oldCell = (DefaultCellTable) oldValue;
            DefaultCellTable newCell = (DefaultCellTable) newValue;

            if (oldCell.getCellValue() != newCell.getCellValue()) {
                model.setValueAt(newValue, row, idxColumn);
            } else {
                TableUtils.repaintCell(table, row, columnIdentifier);
            }

        } else {
            model.setValueAt(newValue, row, idxColumn);
        }

        return oldValue;
    }

    public static void repaintCellValue(Object value, javax.swing.JTable table, Object columnIdentifier) {
        int idxColumn = table.getColumn(columnIdentifier).getModelIndex();
        int row = TableUtils.find(table, idxColumn, value);

        if (row >= 0) {

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();

            Object oldCell = model.getValueAt(row, idxColumn);
            Object oldValue = oldCell;

            if (oldValue instanceof DefaultCellTable) {
                oldValue = ((DefaultCellTable) oldCell).getCellValue();
            }
            if (oldValue == value) {
                TableUtils.repaintCell(table, row, columnIdentifier);
            }
        }
    }

    public static void repaintCell(javax.swing.JTable table, int row, Object columnIdentifier) {
        int columnModel = table.getColumn(columnIdentifier).getModelIndex();
        table.tableChanged(new javax.swing.event.TableModelEvent(
                table.getModel(),
                row,
                row,
                columnModel,
                javax.swing.event.TableModelEvent.UPDATE));
    }

    public static void repaintRow(javax.swing.JTable table, int row) {
        table.tableChanged(new javax.swing.event.TableModelEvent(
                table.getModel(),
                row,
                row,
                javax.swing.event.TableModelEvent.ALL_COLUMNS,
                javax.swing.event.TableModelEvent.UPDATE));
    }

    public static void addRow(javax.swing.JTable table) {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        table.clearSelection();
        model.addRow(new Object[]{});
        table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
    }

//    public static void removeSelection(javax.swing.JTable table, Removable removable) {
//
//        int[] rows = table.getSelectedRows();
//        for (int i = rows.length - 1; i >= 0; i--) {
//            Object cellObject = table.getModel().getValueAt(rows[i], 0);
//            removable.remove(cellObject);
//        }
//        table.clearSelection();
//    }
    public static int find(javax.swing.JTable table, int column, Object object) {
        int index = -1;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();

        for (int row = 0; (index < 0) && (row < model.getRowCount()); row++) {
            Object value = model.getValueAt(row, column);
            if (value instanceof DefaultCellTable) {
                if (((DefaultCellTable) value).getCellValue() == object) {
                    index = row;
                }
            } else if (value == object) {
                index = row;
            }
        }

        return index;
    }

    public static int find(javax.swing.JTable table, Object columnIdentifier, Object object) {
        int idxColumn = table.getColumn(columnIdentifier).getModelIndex();
        return find(table, idxColumn, object);
    }

    public static void removeRow(javax.swing.JTable table, Object columnIdentifier, Object object) {

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        int row = find(table, columnIdentifier, object);

        if (row >= 0) {
            model.removeRow(row);
        }
    }

    public static void setMinMaxWidth(javax.swing.JTable table, Object columnIdentifier, int minWidth, int maxWidth) {
        int column = table.getColumn(columnIdentifier).getModelIndex();

        table.getColumnModel().getColumn(column).setMinWidth(minWidth);
        table.getColumnModel().getColumn(column).setPreferredWidth(maxWidth);
        table.getColumnModel().getColumn(column).setMaxWidth(maxWidth);
    }
}
