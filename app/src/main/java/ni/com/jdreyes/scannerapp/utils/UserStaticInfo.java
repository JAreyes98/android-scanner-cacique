package ni.com.jdreyes.scannerapp.utils;

import android.util.Base64;

import com.jdreyes.rifas.model.pojo.UserResponse;

import lombok.Getter;
import lombok.Setter;

public class UserStaticInfo {
  @Getter private static String userName;
  private static String password;
  @Setter private static String baseAuth;

  @Getter @Setter private static UserResponse userResponse;

  public static void setUserInfo(String userName, String password) {
    UserStaticInfo.userName = userName;
    UserStaticInfo.password = password;
    if (userName != null && password != null)
      UserStaticInfo.setBaseAuth(
          "Basic "
              + Base64.encodeToString(
                  userName.concat(":").concat(password).getBytes(), Base64.NO_WRAP));
  }

  public static String getAuth() {
    return baseAuth;
  }

  public static String getUserName() {
    return userName == null ? baseAuth == null ? null : new String(Base64.decode(baseAuth.replace("Basic ", ""), 0)).split(":")[0] : userName;
  }

  public static void setBaseAuth(String baseAuth) {
    UserStaticInfo.baseAuth = baseAuth;
    UserStaticInfo.userName = getUserName();

  }
}
