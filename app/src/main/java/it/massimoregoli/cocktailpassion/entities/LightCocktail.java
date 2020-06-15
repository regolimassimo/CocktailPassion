package it.massimoregoli.cocktailpassion.entities;

// COSA SONO I PARCELABLE

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity
public class LightCocktail implements Parcelable {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name="_id")
  public int idDrink;
  @ColumnInfo(name="strDrink")
  public String strDrink;
  @ColumnInfo(name="strDrinkThumb")
  public String strDrinkThumb;

  public static final Creator<LightCocktail> CREATOR = new Creator<LightCocktail>() {
    @Override
    public LightCocktail createFromParcel(Parcel in) {
      return new LightCocktail(in);
    }

    @Override
    public LightCocktail[] newArray(int size) {
      return new LightCocktail[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(idDrink);
    dest.writeString(strDrink);
    dest.writeString(strDrinkThumb);
  }

  public LightCocktail() {

  }

  private LightCocktail(Parcel in) {
    idDrink = in.readInt();
    strDrink = in.readString();
    strDrinkThumb = in.readString();
  }
}

