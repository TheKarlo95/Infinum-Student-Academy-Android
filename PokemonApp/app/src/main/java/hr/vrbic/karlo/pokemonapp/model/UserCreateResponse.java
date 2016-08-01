package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCreateResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public User getUser() {
        return new User(getUsername(), getEMail(), getAuthToken());
    }

    public String getId() {
        return data.id;
    }

    public String getEMail() {
        return data.attributes.email;
    }

    public String getUsername() {
        return data.attributes.username;
    }

    public String getAuthToken() {
        return data.attributes.authToken;
    }


    public class Data {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("attributes")
        @Expose
        public Attributes attributes;

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

    }
}
