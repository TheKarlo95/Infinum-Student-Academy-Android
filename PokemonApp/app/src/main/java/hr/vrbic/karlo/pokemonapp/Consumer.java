package hr.vrbic.karlo.pokemonapp;

public interface Consumer<T> {

    void accept(T t);

}