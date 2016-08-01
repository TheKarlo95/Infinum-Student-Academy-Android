package hr.vrbic.karlo.pokemonapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import hr.vrbic.karlo.pokemonapp.checkboxlist.CheckBoxItem;
import hr.vrbic.karlo.pokemonapp.database.AppDatabase;

@Table(database = AppDatabase.class)
public class Ability extends BaseModel implements CheckBoxItem, Parcelable {

    public static final Parcelable.Creator<Ability> CREATOR = new Parcelable.Creator<Ability>() {
        @Override
        public Ability createFromParcel(Parcel source) {
            return new Ability(source);
        }

        @Override
        public Ability[] newArray(int size) {
            return new Ability[size];
        }
    };

    @SerializedName("id")
    @PrimaryKey
    int id;
    @SerializedName("name")
    @Column
    String name;
    private boolean isSelected;

    public Ability(int id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    protected Ability(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public Ability(int id, String name) {
        this(id, name, false);
    }

    public Ability() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

}
