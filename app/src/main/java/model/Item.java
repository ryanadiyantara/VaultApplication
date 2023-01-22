package model;

import java.io.Serializable;

public class Item implements Serializable {
    private int item_id;
    private String item_name;
    private String item_price;
    private byte[] item_pict;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public byte[] getItem_pict() {
        return item_pict;
    }

    public void setItem_pict(byte[] item_pict) {
        this.item_pict = item_pict;
    }
}
