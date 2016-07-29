package hr.vrbic.karlo.pokemonapp.model;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import hr.vrbic.karlo.pokemonapp.PokemonApp;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.NumberUtils;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;

/**
 * {@code PokemonData} is a class that contains all information about one PokemonData.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see Parcelable
 * @see Comparable
 */
public class Pokemon implements Parcelable, Comparable<Pokemon> {

    /**
     * Creator of the {@code PokemonData}.
     *
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel source) {
            return new Pokemon(source);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    private static final Uri DEFAULT_IMAGE = Uri.parse("android.resource://hr.vrbic.karlo.pokemonapp/drawable/" +
            "ic_person_details");

    @NonNull
    private String name;
    private double height;
    private double weight;
    private List<Integer> category;
    private List<Integer> abilities;
    private String description;
    private Uri imageUri;

    public Pokemon(@NonNull String name,
                   double height,
                   double weight,
                   List<Integer> category,
                   List<Integer> abilities,
                   String description,
                   Uri imageUri) {
        Context context = PokemonApp.getContext();
        this.name = StringUtils.requireNonNullAndNonEmpty(name, context.getString(R.string.name_null),
                context.getString(R.string.name_empty));
        this.height = NumberUtils.requirePositive(height, context.getString(R.string.height_positive));
        this.weight = NumberUtils.requirePositive(weight, context.getString(R.string.weight_positive));
        if(category != null) {
            this.category = new ArrayList<>(category);
        } else {
            this.category = new ArrayList<>();
        }
        if(abilities != null) {
            this.abilities = new ArrayList<>(abilities);
        } else {
            this.abilities = new ArrayList<>();
        }
        this.description = description;
        if (imageUri != null) {
            this.imageUri = imageUri;
        } else {
            this.imageUri = DEFAULT_IMAGE;
        }
    }

    /**
     * Constructs a new {@code PokemonData} with specified {@linkplain Parcel} object {@code in}.
     *
     * @param in {@linkplain Parcel} object of {@code PokemonData}
     * @throws NullPointerException if parameter {@code in} is {@code null}
     */
    protected Pokemon(@NonNull Parcel in) {
        this.name = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
        in.readList(this.category, Integer.class.getClassLoader());
        in.readList(this.abilities, Integer.class.getClassLoader());
        this.description = in.readString();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the height.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the weight.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the category.
     *
     * @return the category
     */
    public List<Integer> getCategories() {
        return category;
    }

    /**
     * Returns the category.
     *
     * @return the category
     */
    public List<Integer> getAbilities() {
        return abilities;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the image URI.
     *
     * @return the image URI
     */
    public Uri getImageUri() {
        return Uri.parse(ApiManager.API_ENDPOINT + imageUri.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Pokemon pokemon2 = (Pokemon) o;

        return new EqualsBuilder()
                .append(name, pokemon2.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "PokemonData{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", category='" + category + '\'' +
                ", abilities='" + abilities + '\'' +
                ", description='" + description + '\'' +
                ", imageUri=" + imageUri +
                '}';
    }

    @Override
    public int compareTo(@NonNull Pokemon another) {
        return this.name.compareTo(another.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.height);
        dest.writeDouble(this.weight);
        dest.writeList(this.category);
        dest.writeList(this.abilities);
        dest.writeString(this.description);
        dest.writeParcelable(this.imageUri, flags);
    }

}
