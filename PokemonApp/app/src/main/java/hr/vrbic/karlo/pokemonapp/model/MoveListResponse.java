package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheKarlo95 on 29.7.2016..
 */
public class MoveListResponse {

    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    public List<Ability> getAbilities() {
        List<Ability> abilities = null;
        if(!data.isEmpty()) {
            abilities = new ArrayList<>();
            for (Data ability : data) {
                abilities.add(new Ability(Integer.parseInt(ability.id), ability.attributes.name));
            }
        }

        return abilities;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
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
