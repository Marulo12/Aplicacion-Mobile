package bochassportclub.bsc.View;

public interface LoginView {
    //region Login
    void loginSuccess(int idJugador);
    void loginError();
    boolean loginValidations();
    //endregion

    //region SignUp
    boolean signUpValidations();
    boolean validatePass();
    void signUpSuccess(int idJugador);
    void signUpError();
    void duplicateUserName();
    void duplicateDniNumber();
    //endregion

    //region Funciones Comunes
    void showLogIn();
    void showSignUp();
    void showGetPass();
    void redirectMenu();
    void setSharedPreferences(String username, String pwd,int idJugador);
    void setCredencialsIfExist();
    //endregion

    //region GetPwd
    boolean getPwdValidations();
    //endregion
}
