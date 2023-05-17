package ni.com.jdreyes.scannerapp.utils;

import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import ni.com.jdreyes.scannerapp.fragments.adapters.AbstractAdapter;
import ni.com.jdreyes.scannerapp.models.wrapper.ListDataWrapper;
import ni.com.jdreyes.scannerapp.utils.enums.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface CatalogCallBack {

     default <T> Callback<ListDataWrapper<T>> catalogCallback(Class<? extends AbstractAdapter> adapterClass, Context context, Spinner spinner) {
        return new Callback<ListDataWrapper<T>>() {
            @Override
            public void onResponse(Call<ListDataWrapper<T>> call, Response<ListDataWrapper<T>> response) {
                HttpStatus httpStatus = HttpStatus.resolve(response.code());
                if (response.isSuccessful() && httpStatus == HttpStatus.OK) {

                    AbstractAdapter adapter = null;
                    try {
                        adapter = adapterClass.getConstructor(Context.class, int.class, List.class).newInstance(
                                context
                                ,android.R.layout.simple_spinner_item
                                , response.body().getData()
                        );

                        if (response.body().getData() == null || response.body().getData().size() == 0){
                            Toast.makeText(context, "No se encontraron registros", Toast.LENGTH_LONG).show();
                        }
                    } catch (IllegalAccessException | java.lang.InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ListDataWrapper<T>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
