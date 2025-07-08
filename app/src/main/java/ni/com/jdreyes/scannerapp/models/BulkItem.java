package ni.com.jdreyes.scannerapp.models;

public class BulkItem {
    String barcode;

    public BulkItem(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
