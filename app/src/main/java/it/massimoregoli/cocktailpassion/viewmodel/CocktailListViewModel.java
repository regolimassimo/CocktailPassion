package it.massimoregoli.cocktailpassion.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.massimoregoli.cocktailpassion.entities.Cocktail;

public class CocktailListViewModel extends ViewModel {
  private MutableLiveData<List<Cocktail>> cocktails;
  public LiveData<List<Cocktail>> getCocktails() {
    if(cocktails == null)
      cocktails = new MutableLiveData<>();
    return cocktails;
  }

  public void setCocktails(List<Cocktail> cocktails) {
    this.cocktails.setValue(cocktails);
  }
}