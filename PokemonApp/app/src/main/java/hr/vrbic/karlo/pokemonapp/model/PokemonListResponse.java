package hr.vrbic.karlo.pokemonapp.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheKarlo95 on 28.7.2016..
 */
public class PokemonListResponse {

    @SerializedName("data")
    @Expose
    private List<PokemonData> data = new ArrayList<>();
    @SerializedName("links")
    @Expose
    private Links links;

    public List<Pokemon> getPokemonList() {
        List<Pokemon> output = new ArrayList<>();

        for (PokemonData pokemonData : data) {
            Pokemon pokemon = new Pokemon(pokemonData.attributes.name,
                    pokemonData.attributes.height,
                    pokemonData.attributes.weight,
                    pokemonData.relationships.types.getTypeIDs(),
                    pokemonData.relationships.moves.getMovesIDs(),
                    pokemonData.attributes.description,
                    pokemonData.attributes.imageUrl);
            output.add(pokemon);
        }

        return output;
    }

    public class PokemonData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("attributes")
        @Expose
        private Attributes attributes;
        @SerializedName("relationships")
        @Expose
        private Relationships relationships;

    }

    public class Attributes {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("base-experience")
        @Expose
        private int baseExperience;
        @SerializedName("is-default")
        @Expose
        private boolean isDefault;
        @SerializedName("order")
        @Expose
        private int order;
        @SerializedName("height")
        @Expose
        private double height;
        @SerializedName("weight")
        @Expose
        private double weight;
        @SerializedName("created-at")
        @Expose
        private String createdAt;
        @SerializedName("updated-at")
        @Expose
        private String updatedAt;
        @SerializedName("image-url")
        @Expose
        private Uri imageUrl;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("total-vote-count")
        @Expose
        private int totalVoteCount;

    }

    public class Relationships {

        @SerializedName("types")
        @Expose
        private Types types;
        @SerializedName("moves")
        @Expose
        private Moves moves;

    }

    public class Types {

        @SerializedName("data")
        @Expose
        private List<Type> data = new ArrayList<>();

        public List<Integer> getTypeIDs() {
            if (!data.isEmpty()) {
                List<Integer> typesList = new ArrayList<>();
                for (Type type : data) {
                    typesList.add(Integer.valueOf(type.id));
                }
                return typesList;
            } else {
                return null;
            }
        }
    }

    public class Type {

        @SerializedName("id")
        @Expose
        private String id;

    }

    public class Moves {

        @SerializedName("data")
        @Expose
        private List<Move> data = new ArrayList<>();

        public List<Integer> getMovesIDs() {
            if (!data.isEmpty()) {
                List<Integer> typesList = new ArrayList<>();
                for (Move move : data) {
                    typesList.add(Integer.valueOf(move.id));
                }
                return typesList;
            } else {
                return null;
            }
        }
    }

    public class Move {

        @SerializedName("id")
        @Expose
        private String id;

    }


    public class Links {

        @SerializedName("self")
        @Expose
        private Uri self;
        @SerializedName("next")
        @Expose
        private Uri next;
        @SerializedName("last")
        @Expose
        private Uri last;

    }

}
