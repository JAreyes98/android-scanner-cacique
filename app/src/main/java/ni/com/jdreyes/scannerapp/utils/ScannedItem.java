package ni.com.jdreyes.scannerapp.utils;

public class ScannedItem {
    private int id;
    private String barcode;
    private String timestamp;

    public ScannedItem(int id, String barcode, String timestamp) {
        this.id = id;
        this.barcode = barcode;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getBarcode() { return barcode; }
    public String getTimestamp() { return timestamp; }
}
