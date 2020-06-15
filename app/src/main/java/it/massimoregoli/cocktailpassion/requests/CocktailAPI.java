package it.massimoregoli.cocktailpassion.requests;

import android.content.Context;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.massimoregoli.cocktailpassion.entities.Cocktail;

public abstract class CocktailAPI implements Response.Listener<String> {
  private String URL = "https://www.thecocktaildb.com/api/json/v1/%s/%s.php?%s=%s";
  private String apiKey;
  private String object;
  private RequestQueue queue;


  public abstract void fillLayout(List<Cocktail> cocktails);

  protected CocktailAPI(Context context, String apiKey) {
    this.apiKey = apiKey;
    Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024); // 10MB
    Network network = new BasicNetwork(new HurlStack());
    queue = new RequestQueue(cache, network);
    queue.start();
  }

  public void search(String q) {
    object = "drinks";
    String url = String.format(Locale.getDefault(), URL,
        apiKey, "search", "s", q);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        this, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    queue.add(stringRequest);
  }

  public void lookup(String iid) {
    object = "drinks";
    String url = String.format(Locale.getDefault(), URL,
        apiKey, "lookup", "i", iid);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        this, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    queue.add(stringRequest);
  }

  public void filter(String q) {
    object = "drinks";
    String url = String.format(Locale.getDefault(), URL,
        apiKey, "filter", "i", q);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
this, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });
    queue.add(stringRequest);

  }



  @Override
  public void onResponse(String response) {
    try {
      ArrayList<Cocktail> ret;
      JSONObject jsonObject = new JSONObject(response);
      String list = jsonObject.getJSONArray(object).toString();
      Gson gson = new Gson();
      Type type = new TypeToken<List<Cocktail>>(){}.getType();
      ret =  gson.fromJson(list, type);
      fillLayout(ret);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
