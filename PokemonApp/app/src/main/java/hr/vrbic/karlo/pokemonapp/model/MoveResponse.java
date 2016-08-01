package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.structure.BaseModel;

public class MoveResponse  extends BaseModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Move getMove() {
        return new Move(data.attributes.name);
    }

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
