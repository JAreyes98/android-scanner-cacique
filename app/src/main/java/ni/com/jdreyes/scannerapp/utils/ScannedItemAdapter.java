package ni.com.jdreyes.scannerapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ni.com.jdreyes.scannerapp.R;

public class ScannedItemAdapter extends RecyclerView.Adapter<ScannedItemAdapter.ViewHolder> {
    private List<ScannedItem> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcodeText, timestampText;

        public ViewHolder(View view) {
            super(view);
            barcodeText = view.findViewById(R.id.barcodeText);
            timestampText = view.findViewById(R.id.timestampText);
        }
    }

    public ScannedItemAdapter(List<ScannedItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scanned, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScannedItem item = items.get(position);
        holder.barcodeText.setText(item.getBarcode());
        holder.timestampText.setText(String.format("%s - %s | %s", String.valueOf(position + 1), item.getBarcode(), item.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
