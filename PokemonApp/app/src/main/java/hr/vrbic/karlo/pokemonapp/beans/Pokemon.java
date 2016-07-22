package hr.vrbic.karlo.pokemonapp.beans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.utilities.Numbers;
import hr.vrbic.karlo.pokemonapp.utilities.Strings;

/**
 * {@code Pokemon} is a class that contains all information about one Pokemon.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see Parcelable
 * @see Comparable
 */
public class Pokemon implements Parcelable, Comparable<Pokemon> {

    /**
     * Creator of the {@code Pokemon}.
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

    /**
     * Name of the Pokemon.
     */
    @NonNull
    private String name;
    /**
     * Height of the Pokemon. (in meters)
     */
    private double height;
    /**
     * Weight of the Pokemon. (in kilograms)
     */
    private double weight;
    /**
     * Category of the Pokemon.
     */
    @NonNull
    private String category;
    /**
     * Abilities of the Pokemon.
     */
    @NonNull
    private String abilities;
    /**
     * Gender of the Pokemon.
     */
    private String gender;
    /**
     * Description of the Pokemon.
     */
    @NonNull
    private String description;
    /**
     * Image of the Pokemon.
     */
    @NonNull
    private Bitmap image;

    /**
     * Constructs a new {@code Pokemon} with specified parameters
     *
     * @param context     the context in which this Pokemon is constructed; used for error messages
     * @param name        the name of the Pokemon
     * @param height      the height of the Pokemon (in meters)
     * @param weight      the weight of the Pokemon (in kilograms)
     * @param category    the category of the Pokemon
     * @param abilities   the abilities of the Pokemon
     * @param gender      the gender of the Pokemon.
     * @param description the description of the Pokemon
     * @param image       the image of the Pokemon
     * @throws NullPointerException     if any of the parameters are {@code null}
     * @throws IllegalArgumentException if parameters {@code height} or {@code weight} aren't positive numbers
     */
    public Pokemon(@NonNull Context context,
                   @NonNull String name,
                   double height,
                   double weight,
                   @NonNull String category,
                   @NonNull String abilities,
                   @NonNull String gender,
                   @NonNull String description,
                   @NonNull Bitmap image) {
        this.name = Strings.requireNonNullAndNonEmpty(name, context.getString(R.string.name_null),
                context.getString(R.string.name_empty));
        this.height = Numbers.requirePositive(height, context.getString(R.string.height_positive));
        this.weight = Numbers.requirePositive(weight, context.getString(R.string.weight_positive));
        this.category = Strings.requireNonNullAndNonEmpty(category, context.getString(R.string.category_null),
                context.getString(R.string.category_empty));
        this.abilities = Strings.requireNonNullAndNonEmpty(abilities, context.getString(R.string.abilities_null),
                context.getString(R.string.abilities_empty));
        this.gender = Strings.requireNonNullAndNonEmpty(gender, context.getString(R.string.gender_null),
                context.getString(R.string.gender_empty));
        this.description = Strings.requireNonNullAndNonEmpty(description, context.getString(R.string.description_null),
                context.getString(R.string.description_empty));
        this.image = Objects.requireNonNull(image, context.getString(R.string.image_null));
    }

    /**
     * Constructs a new {@code Pokemon} with specified {@linkplain Parcel} object {@code in}.
     *
     * @param in {@linkplain Parcel} object of {@code Pokemon}
     */
    protected Pokemon(Parcel in) {
        this.name = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
        this.category = in.readString();
        this.abilities = in.readString();
        this.gender = in.readString();
        this.description = in.readString();
        byte[] bytes = in.createByteArray();
        this.image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
    @NonNull
    public String getCategory() {
        return category;
    }

    /**
     * Returns the category.
     *
     * @return the category
     */
    @NonNull
    public String getAbilities() {
        return abilities;
    }

    /**
     * Returns the gender.
     *
     * @return the gender
     */
    @NonNull
    public String getGender() {
        return gender;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    @NonNull
    public String getDescription() {
        return description;
    }

    /**
     * Returns the image.
     *
     * @return the image
     */
    @NonNull
    public Bitmap getImage() {
        return image;
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
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", category='" + category + '\'' +
                ", abilities='" + abilities + '\'' +
                ", description='" + description + '\'' +
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
        dest.writeString(this.category);
        dest.writeString(this.abilities);
        dest.writeString(this.gender);
        dest.writeString(this.description);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        dest.writeByteArray(bytes);
    }

}
