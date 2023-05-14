package ni.com.jdreyes.scannerapp.models.wrapper;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDataWrapper<T> {
    private List<T> data;
}
