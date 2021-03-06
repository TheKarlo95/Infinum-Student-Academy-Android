package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {

    @SerializedName("data")
    @Expose
    private Data data;

    public UserLoginRequest(String email, String password) {
        this.data = new Data();
        this.data.type = "session";
        this.data.attributes = new Attributes(email, password);
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

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;

        public Attributes(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
