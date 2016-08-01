package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoveResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public String getMoveName() {
        return data.attributes.name;
    }

    public class Data {

        @SerializedName("attributes")
        @Expose
        private Attributes attributes;

    }

    public class Attributes {

        @SerializedName("name")
        @Expose
        private String name;

    }
}
