package it.massimoregoli.cocktailpassion.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import it.massimoregoli.cocktailpassion.R;
import it.massimoregoli.cocktailpassion.entities.Cocktail;
import it.massimoregoli.cocktailpassion.requests.CocktailAPI;

public class CocktailDetailsActivity extends AppCompatActivity {
  ConstraintLayout cl;
  Holder holder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cocktail_details);
    cl = findViewById(R.id.clDrinkLayout);
    holder = new Holder(cl);
    Cocktail cocktail = getIntent().getParcelableExtra("cocktail");

    if(cocktail.isFull()) {
      CocktailAPI cocktailAPI = new CocktailAPI(this, "1") {


        @Override
        public void fillLayout(List<Cocktail> cocktails) {
          holder.fillLayout(cocktails.get(0));
        }
      };
      cocktailAPI.lookup("" + cocktail.idDrink);
    } else {
      holder.fillLayout(cocktail);
    }

  }

  class Holder {
    final TextView tv;
    final TextView tvIngredients;
    final TextView tvInstructions;
    final ImageView iv;

    Holder(View view) {
      tv = view.findViewById(R.id.tvDrinkName);
      tvIngredients = view.findViewById(R.id.tvIngredients);
      tvInstructions = view.findViewById(R.id.tvPreparation);
      iv = view.findViewById(R.id.ivDrink);
    }
//    final TextView tv = cl.findViewById(R.id.tvDrinkName);
//    final TextView tvIngredients = cl.findViewById(R.id.tvIngredients);
//    final TextView tvInstructions = cl.findViewById(R.id.tvPreparation);
//    final ImageView iv = cl.findViewById(R.id.ivDrink);

    void fillLayout(Cocktail cocktail) {
      tv.setText(cocktail.strDrink);
      tvInstructions.setText(cocktail.strInstructions);
      tvIngredients.setText(Html.fromHtml(cocktail.getIngredients(getApplicationContext()),
          Html.FROM_HTML_MODE_COMPACT));

      ImageRequest request = new ImageRequest(cocktail.strDrinkThumb,
          new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
              iv.setImageBitmap(bitmap);
            }
          }, 0, 0, null, Bitmap.Config.ARGB_8888,
          new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
              iv.setImageResource(R.drawable.ic_alcolico);
            }
          });
      RequestQueue queue = Volley.newRequestQueue(CocktailDetailsActivity.this);
      queue.add(request);
    }
  }
}
