package utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import java.util.Vector;

/**
 *
 */
public class ExtendedDefaultTableModel extends javax.swing.table.DefaultTableModel {

    protected Vector<Vector<Boolean>> header;
    protected Vector<Vector<Boolean>> canEdit;
    protected javax.swing.event.TableModelListener tableModelListener;
    protected static final int HEADER_SIZE = 1;
    protected List<Class<? extends DefaultCellTable>> columnClass;

    public ExtendedDefaultTableModel(List<Class<? extends DefaultCellTable>> columnClass) {
        this(columnClass, 0);
    }

    public ExtendedDefaultTableModel(List<Class<? extends DefaultCellTable>> columnClass, int rowCount) {
        super(rowCount, columnClass.size());
        createCanEdit();
        createTableModelListener();
        this.columnClass = columnClass;
    }

    public ExtendedDefaultTableModel(Vector columnNames, List<Class<? extends DefaultCellTable>> columnClass, int rowCount) {
        super(columnNames, rowCount);
        createCanEdit();
        createTableModelListener();
        this.columnClass = columnClass;
    }

    public ExtendedDefaultTableModel(Object[] columnNames, List<Class<? extends DefaultCellTable>> columnClass, int rowCount) {
        super(columnNames, rowCount);
        createCanEdit();
        createTableModelListener();
        this.columnClass = columnClass;
    }

    public ExtendedDefaultTableModel(Vector data, Vector columnNames, List<Class<? extends DefaultCellTable>> columnClass) {
        super(data, columnNames);
        createCanEdit();
        createTableModelListener();
        this.columnClass = columnClass;
    }

    public ExtendedDefaultTableModel(Object[][] data, Object[] columnNames, List<Class<? extends DefaultCellTable>> columnClass) {
        super(data, columnNames);
        createCanEdit();
        createTableModelListener();
        this.columnClass = columnClass;
    }

    private void createTableModelListener() {
        tableModelListener = new javax.swing.event.TableModelListener() {

            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                switch (evt.getType()) {
                    case javax.swing.event.TableModelEvent.INSERT://when inserted
                    {
                        changeCanEditRowsAdded(evt.getFirstRow(), evt.getLastRow());
                        break;
                    }
                    case javax.swing.event.TableModelEvent.UPDATE://when updated
                    {

                        break;
                    }
                    case javax.swing.event.TableModelEvent.DELETE://when deleted
                    {
                        changeCanEditRowsDeleted(evt.getFirstRow(), evt.getLastRow());
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        addTableModelListener(tableModelListener);
    }

    protected void changeCanEditRowsAdded(int firstRow, int lastRow) {
        int rowsAdded = lastRow - firstRow + 1;

        if (rowsAdded > 0) {
            int newLenght = canEdit.size() + rowsAdded;
            Vector<Vector<Boolean>> newCanEdit = new Vector(newLenght);

            for (int row = 0; row < firstRow; row++) {
                newCanEdit.add(canEdit.get(row));//copy first rows
            }

            for (int row = firstRow; row <= lastRow; row++) {
                newCanEdit.add((Vector<Boolean>) header.get(0).clone());//clone the header
            }

            for (int row = firstRow; row < canEdit.size(); row++) {
                newCanEdit.add(canEdit.get(row));//copy last rows
            }
            canEdit = newCanEdit;
        }
    }

    protected void changeCanEditRowsDeleted(int firstRow, int lastRow) {
        int rowsDeleted = lastRow - firstRow + 1;
        if ((rowsDeleted > 0) && (canEdit.size() > 0)) {
            int newLenght = canEdit.size() - rowsDeleted;
            Vector<Vector<Boolean>> newCanEdit = new Vector(newLenght);

            for (int row = 0; (row < firstRow) && (row < newLenght); row++) {
                newCanEdit.add(canEdit.get(row));//copy first rows
            }

            for (int row = lastRow + 1; row < canEdit.size(); row++) {
                newCanEdit.add(canEdit.get(row));//copy last rows
            }
            canEdit = newCanEdit;
        }
    }

    protected void createCanEdit() {
        int rowCount = getRowCount();
        int columnCount = getColumnCount();
        header = new Vector(HEADER_SIZE);
        canEdit = new Vector(rowCount);
        Vector<Boolean> rowHeader = new Vector(columnCount);

        for (int i = 0; i < columnCount; i++) {
            rowHeader.add(Boolean.TRUE);
        }
        header.add(rowHeader);

        for (int row = 0; row < rowCount; row++) {
            canEdit.add((Vector<Boolean>) rowHeader.clone());
        }
    }

    public void setCellEditable(int row, int column, boolean editable) {
        canEdit.get(row).set(column, editable);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return canEdit.get(row).get(column);
    }

    public void setColumnEditable(int column, boolean editable) {
        header.get(0).set(column, editable);
        for (int row = 0; row < canEdit.size(); row++) {
            setCellEditable(row, column, editable);
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return columnClass.get(columnIndex);
    }

    @Override
    public void setValueAt(Object newValue, int row, int column) {
        updateValueAt(newValue, row, column);
    }

    public void updateValueAt(Object newValue, int row, int column) {
        Object oldValue = this.getValueAt(row, column);
        if (oldValue instanceof DefaultCellTable) {
            DefaultCellTable oldCell = (DefaultCellTable) oldValue;
            if (newValue instanceof DefaultCellTable) {
                DefaultCellTable newCell = (DefaultCellTable) newValue;
                if (oldCell.getCellValue() != newCell.getCellValue()) {
                    oldCell.clone(newCell);
                }
                super.setValueAt(oldCell, row, column);
            }
        } else {
            super.setValueAt(newValue, row, column);
        }
    }
}
