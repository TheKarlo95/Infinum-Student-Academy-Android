package hr.vrbic.karlo.pokemonapp.fragments;

import android.support.v4.app.Fragment;

public abstract class AbstractFragment extends Fragment {

    public abstract String getFragmentTag();

    public abstract AbstractFragment copy();

}
