package utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Tab;

//显示CPU的使用情况
public class Table {
    private int startAddress;
    private int length;
    private String status;
    private String name;

    public Table(){

    }

    public Table(int startAddress,int length,String status,String name){
        this.startAddress = startAddress;
        this.length = length;
        this.status = status;
        this.name = name;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
