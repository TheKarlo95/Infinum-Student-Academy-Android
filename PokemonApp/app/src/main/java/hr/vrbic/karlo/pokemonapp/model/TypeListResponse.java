package hr.vrbic.karlo.pokemonapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TypeListResponse {

    @SerializedName("data")
    @Expose
    private List<Type> data = new ArrayList<Type>();

    public String getTypeName(int id) {
        return getTypeName(String.valueOf(id));
    }

    public String getTypeName(String id) {
        String name = null;

        for (Type type : data) {
            if (type.id.equals(id)) {
                name = type.attributes.name;
                break;
            }
        }

        return name;
    }

    public List<Category> getCategories() {
        List<Category> categories = null;

        if (!data.isEmpty()) {
            categories = new ArrayList<>();
            for (Type type : data) {
                categories.add(new Category(Integer.parseInt(type.id), type.attributes.name));
            }
        }

        return categories;
    }

    public class Type {

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

        public String getName() {
            return attributes.name;
        }
    }

    public class Attributes {

        @SerializedName("name")
        @Expose
        private String name;

    }

}
