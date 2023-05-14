package ni.com.jdreyes.scannerapp.fragments.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAdapter<T> extends ArrayAdapter<T> {
    protected Context context;
    protected List<T> data;
    LayoutInflater inflater;
    private final boolean useEmptyField;


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected AbstractAdapter(Context context,
                              int textViewResourceId, List<T> data, T emptyData) {
        super(context, textViewResourceId, data);
        this.context = context;
        this.data = new ArrayList<>();
        this.data.add(emptyData);
        this.data.addAll(data);
        this.useEmptyField = true;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    protected AbstractAdapter(Context context,
                              int textViewResourceId, List<T> data) {
        super(context, textViewResourceId, data);
        this.context = context;
        this.data = data;
        this.useEmptyField = false;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public abstract String getPlaceHolder();
    public abstract String onSelectShowText(T t);

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public T getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        TypedValue a = new TypedValue();
//        if (position == 0 && a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT)
//            label.setTextColor(Color.GRAY);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)

        setLabelText(label,position);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        setLabelText(label,position);
        return label;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setLabelText(TextView label, Integer position) {
        if (position == 0 && useEmptyField)
            label.setText(getPlaceHolder());
        else
            label.setText(onSelectShowText(getItem(position)));

    }
}

