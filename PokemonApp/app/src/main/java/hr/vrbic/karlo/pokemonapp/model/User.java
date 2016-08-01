package hr.vrbic.karlo.pokemonapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String username;
    private String email;
    private String authToken;

    public User(String username, String email, String authToken) {
        this.username = username;
        this.email = email;
        this.authToken = authToken;
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.email = in.readString();
        this.authToken = in.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getAuthorization() {
        return String.format("Token token=%s, email=%s", authToken, email);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.authToken);
    }

}
