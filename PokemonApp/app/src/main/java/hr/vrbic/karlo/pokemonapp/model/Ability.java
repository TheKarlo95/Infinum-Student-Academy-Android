package hr.vrbic.karlo.pokemonapp.model;

import android.support.annotation.NonNull;

public class Ability {

    int id;
    @NonNull
    String name;

    public Ability(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

}
