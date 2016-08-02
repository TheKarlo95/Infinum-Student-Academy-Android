package hr.vrbic.karlo.pokemonapp.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.util.List;

import hr.vrbic.karlo.pokemonapp.model.Ability;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class AbilityListConverter extends TypeConverter<String, List> {

    @Override
    public String getDBValue(List model) {
        return new Gson().toJson(model);
    }

    @Override
    public List getModelValue(String data) {
        return new Gson().fromJson(data, new TypeToken<List<Ability>>() {{}}.getType());
    }
}