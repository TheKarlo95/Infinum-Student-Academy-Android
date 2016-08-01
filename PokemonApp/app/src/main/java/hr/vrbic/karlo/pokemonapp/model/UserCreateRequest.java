package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;

public class UserCreateRequest {

    @SerializedName("data")
    @Expose
    private Data data;

    public UserCreateRequest(String email, String username, String password, String passwordConfirmation) {
        StringUtils.requireNonNullAndNonEmpty(email, "E-mail cannot be null", "E-mail cannot be empty");
        StringUtils.requireNonNullAndNonEmpty(username, "Nickname cannot be null", "Nickname cannot be empty");
        StringUtils.requireNonNullAndNonEmpty(password, "Password cannot be null", "Password cannot be empty");
        StringUtils.requireNonNullAndNonEmpty(passwordConfirmation, "Password confirmation cannot be null",
                "Password confirmation cannot be empty");

        if (!password.equals(passwordConfirmation)) {
            throw new IllegalArgumentException("Password and password confirmation must be equal");
        }

        this.data = new Data();
        this.data.type = "users";
        this.data.attributes = new Attributes(username, email, password, passwordConfirmation);
    }

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("attributes")
        @Expose
        private Attributes attributes;

        public String getType() {
            return type;
        }

        public Attributes getAttributes() {
            return attributes;
        }

    }

    public class Attributes {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("password_confirmation")
        @Expose
        private String passwordConfirmation;

        public Attributes(String username, String email, String password, String passwordConfirmation) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.passwordConfirmation = passwordConfirmation;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getPasswordConfirmation() {
            return passwordConfirmation;
        }

    }

}
