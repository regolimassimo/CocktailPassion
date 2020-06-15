package it.massimoregoli.cocktailpassion.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import java.util.List;

import it.massimoregoli.cocktailpassion.R;
import it.massimoregoli.cocktailpassion.entities.Cocktail;
import it.massimoregoli.cocktailpassion.interfaces.SelectMode;

public abstract class CocktailAdapter
    extends RecyclerView.Adapter<CocktailAdapter.Holder>
    implements View.OnClickListener, View.OnLongClickListener {
  private final List<Cocktail> cocktails;
  private RequestQueue queue;

  public abstract void onClickAdapterCallBack(Cocktail cocktail);

  private SelectMode mListener;
  private SparseBooleanArray selectedList = new SparseBooleanArray();

  public void deleteAllSelected() {
    if (selectedList.size()==0) { return; }
    for (int index = cocktails.size()-1; index >=0; index--) {
      if (selectedList.get(index,false)) {
        remove(index);
      }
    }
    selectedList.clear();
  }

  public List<Cocktail> getCocktails() {
    return cocktails;
  }

  private void remove(int position) {
    cocktails.remove(position);
    notifyItemRemoved(position);
  }

  protected CocktailAdapter(Context context, List<Cocktail> all, SelectMode listener) {
    cocktails = all;
    mListener = listener;
// Uso della cache
    Cache cache = new DiskBasedCache(context.getCacheDir(), 16 * 1024 * 1024); // 16MB
    Network network = new BasicNetwork(new HurlStack());
    queue = new RequestQueue(cache, network);
    queue.start();
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    CardView cl;
    cl = (CardView) LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_cocktail, parent, false);
    cl.setOnClickListener(this);
    cl.setOnLongClickListener(this);
    return new Holder(cl);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    Cocktail cocktail = cocktails.get(position);
    holder.tvDrink.setText(cocktail.strDrink);  // cocktail.getDrinkName()
    getImage(position, cocktail.strDrinkThumb, holder.ivAlcoholic);  // cocktail.getThumb()
    holder.ivAlcoholic.setImageResource(R.drawable.ic_alcolico);
    boolean isSelected = selectedList.get(position,false);
    if(isSelected) {
      holder.clContainer.setSelected(true);
      holder.tvDrink.setTypeface(null, Typeface.BOLD);
    } else {
      holder.clContainer.setSelected(false);
      holder.tvDrink.setTypeface(null, Typeface.NORMAL);
    }
  }

  private void getImage(int position, String strDrinkThumb, final ImageView mImageView) {
    queue.cancelAll(position);
    ImageRequest request = new ImageRequest(strDrinkThumb,
        new Response.Listener<Bitmap>() {
          @Override
          public void onResponse(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
          }
        }, 0, 0, null, Bitmap.Config.ARGB_8888,
        new Response.ErrorListener() {
          public void onErrorResponse(VolleyError error) {
            mImageView.setImageResource(R.drawable.ic_alcolico);
          }
        });
    request.setTag(position);
    queue.add(request);
  }

  @Override
  public int getItemCount() {
    return cocktails.size();
  }

  @Override
  public void onClick(View v) {
    if(selectedList.size() > 0) {
     onLongClick(v);
    } else {
      int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
      Cocktail cocktail = cocktails.get(position);
      onClickAdapterCallBack(cocktail);
    }
  }

  @Override
  public boolean onLongClick(View v) {
    int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);

    boolean isSel = selectedList.get(position, false);
    if(isSel) {
      v.setSelected(false);
      selectedList.delete(position);
    } else {
      v.setSelected(true);
      selectedList.put(position, true);
    }
    if (mListener != null) {
      mListener.onSelect(selectedList.size());  // Callback verso MainActivity
    }
    notifyDataSetChanged();
    return true;
  }

  public void deselectAll() {
    selectedList.clear();

    notifyDataSetChanged();
  }

  public void selectAll() {
    for(int i = 0; i < cocktails.size(); i++) {
      selectedList.put(i, true);
    }
    notifyDataSetChanged();
  }

  static class Holder extends RecyclerView.ViewHolder {
    final TextView tvDrink;
    final ImageView ivAlcoholic;
    ConstraintLayout  clContainer;

    Holder(@NonNull View itemView) {
      super(itemView);
      tvDrink = itemView.findViewById(R.id.tvDrink);
      ivAlcoholic = itemView.findViewById(R.id.ivAlcoholic);
      clContainer = itemView.findViewById(R.id.clItemDrinkLayout);
    }
  }
}

