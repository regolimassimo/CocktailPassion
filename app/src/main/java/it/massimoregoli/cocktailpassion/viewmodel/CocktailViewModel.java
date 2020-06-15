package it.massimoregoli.cocktailpassion.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.massimoregoli.cocktailpassion.entities.Cocktail;

public class CocktailViewModel extends ViewModel {
  private MutableLiveData<Cocktail> info;

  public LiveData<Cocktail> getInfo() {
    if(info == null)
      info = new MutableLiveData<>();
    return info;
  }

  public void loadInfo(Cocktail cocktail) {
    if(info == null)
      info = new MutableLiveData<>();
    info.setValue(cocktail);
  }
}
