package ni.com.jdreyes.scannerapp.models.wrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataWrapper <T> {
    private T data;
}
