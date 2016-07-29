package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCreateResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("attributes")
        @Expose
        public Attributes attributes;

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
        public String email;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("auth-token")
        @Expose
        public String authToken;

        public String getAuthToken() {
            return authToken;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }
    }
}
