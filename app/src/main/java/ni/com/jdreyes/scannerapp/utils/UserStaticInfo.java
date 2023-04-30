package ni.com.jdreyes.scannerapp.utils;

public class UserStaticInfo {

  private static String baseAuth;

  public static String getAuth() {
    return baseAuth;
  }

  public static void setBaseAuth(String baseAuth) {
    UserStaticInfo.baseAuth = baseAuth;

  }
}
