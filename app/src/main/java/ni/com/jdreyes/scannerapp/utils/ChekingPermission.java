package ni.com.jdreyes.scannerapp.utils;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChekingPermission {
    private static final String[] REQUIRED_SDK_PERMISSIONS =
            new String[] {Manifest.permission.CAMERA};

    public static void checkPermissions(FragmentActivity fragmentActivity) {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(fragmentActivity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions =
                    missingPermissions.toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(fragmentActivity, permissions, 3);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            fragmentActivity.onRequestPermissionsResult(3, REQUIRED_SDK_PERMISSIONS, grantResults);
        }
    }
}
