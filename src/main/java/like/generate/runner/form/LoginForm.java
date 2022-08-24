package like.generate.runner.form;

/**
 * @author chenaiquan
 */
public class LoginForm {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public LoginForm setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginForm setPassword(String password) {
        this.password = password;
        return this;
    }
}
