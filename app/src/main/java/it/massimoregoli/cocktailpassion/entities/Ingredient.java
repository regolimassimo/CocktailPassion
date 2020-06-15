package it.massimoregoli.cocktailpassion.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
  private String idIngredient;
  private String strIngredient;
  private String strDescription;
  private String strType;
  private String strAlcohol;
  private String strABV;

  private Ingredient(Parcel in) {
    idIngredient = in.readString();
    strIngredient = in.readString();
    strDescription = in.readString();
    strType = in.readString();
    strAlcohol = in.readString();
    strABV = in.readString();
  }

  public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
    @Override
    public Ingredient createFromParcel(Parcel in) {
      return new Ingredient(in);
    }

    @Override
    public Ingredient[] newArray(int size) {
      return new Ingredient[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(idIngredient);
    dest.writeString(strIngredient);
    dest.writeString(strDescription);
    dest.writeString(strType);
    dest.writeString(strAlcohol);
    dest.writeString(strABV);
  }
}
