package hr.vrbic.karlo.pokemonapp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import hr.vrbic.karlo.pokemonapp.PokemonApp;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.database.AbilityListConverter;
import hr.vrbic.karlo.pokemonapp.database.AppDatabase;
import hr.vrbic.karlo.pokemonapp.database.CategoryListConverter;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.NumberUtils;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;

/**
 * {@code PokemonInteractor} is a class that contains all information about one PokemonInteractor.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see Parcelable
 * @see Comparable
 */
@Table(database = AppDatabase.class)
public class Pokemon extends BaseModel implements Parcelable, Comparable<Pokemon> {

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

    public static final int DEFAULT_IMAGE = R.drawable.ic_person_details;

    @PrimaryKey(autoincrement = true)
    private long id;

    @NonNull
    @Column
    private String name;
    @Column
    private double height;
    @Column
    private double weight;
    @Column(typeConverter = CategoryListConverter.class)
    private List categories;
    @Column(typeConverter = AbilityListConverter.class)
    private List abilities;
    @Column
    private String description;
    @Column
    private String imageUri;

    public Pokemon(@NonNull String name,
                   double height,
                   double weight,
                   List<Category> categories,
                   List<Ability> abilities,
                   String description,
                   String imageUri) {
        Context context = PokemonApp.getContext();
        this.name = StringUtils.requireNonNullAndNonEmpty(name, context.getString(R.string.name_null),
                context.getString(R.string.name_empty));
        this.height = NumberUtils.requireNonNegative(height, context.getString(R.string.height_positive));
        this.weight = NumberUtils.requireNonNegative(weight, context.getString(R.string.weight_positive));
        if (categories != null) {
            this.categories = new ArrayList<>(categories);
        } else {
            this.categories = new ArrayList<>();
        }
        if (abilities != null) {
            this.abilities = new ArrayList<>(abilities);
        } else {
            this.abilities = new ArrayList<>();
        }
        this.description = description;
        if (imageUri != null) {
            this.imageUri = imageUri;
        } else {
            final Resources resources = context.getResources();
            this.imageUri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(resources.getResourcePackageName(DEFAULT_IMAGE))
                    .appendPath(resources.getResourceTypeName(DEFAULT_IMAGE))
                    .appendPath(resources.getResourceEntryName(DEFAULT_IMAGE))
                    .build().toString();
        }
    }

    public Pokemon() {
    }

    protected Pokemon(@NonNull Parcel in) {
        this.name = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
        in.readList(this.categories, Integer.class.getClassLoader());
        in.readList(this.abilities, Integer.class.getClassLoader());
        this.description = in.readString();
        this.imageUri = in.readString();
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getImageUriWithEndpoint() {
        return ApiManager.API_ENDPOINT + getImageUri();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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
        return "PokemonInteractor{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", categories='" + categories + '\'' +
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
        dest.writeList(this.categories);
        dest.writeList(this.abilities);
        dest.writeString(this.description);
        dest.writeString(this.imageUri);
    }

}
