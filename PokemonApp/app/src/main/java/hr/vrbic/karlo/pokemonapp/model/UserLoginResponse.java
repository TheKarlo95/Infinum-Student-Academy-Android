package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public User getUser() {
        return new User(data.attributes.username, data.attributes.email, data.attributes.authToken);
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("attributes")
        @Expose
        private Attributes attributes;

        public String getId() {
            return id;
        }

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
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("auth-token")
        @Expose
        private String authToken;

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getAuthToken() {
            return authToken;
        }
    }

}
