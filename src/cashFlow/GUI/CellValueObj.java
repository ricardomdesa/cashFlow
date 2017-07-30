/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashFlow.GUI;

/**
 *
 * @author F98877A
 */
public class CellValueObj {

    private String value;
    private int ObjId;
    private int control;

    public CellValueObj() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getObjId() {
        return ObjId;
    }

    public void setObjId(int ObjId) {
        this.ObjId = ObjId;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

}
