package hr.vrbic.karlo.pokemonapp.model;

import com.raizlabs.android.dbflow.structure.BaseModel;

public class Move extends BaseModel {

    private String name;

    public Move(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
