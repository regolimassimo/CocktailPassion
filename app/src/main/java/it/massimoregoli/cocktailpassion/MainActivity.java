package it.massimoregoli.cocktailpassion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.massimoregoli.cocktailpassion.activities.CocktailDetailsActivity;
import it.massimoregoli.cocktailpassion.adapters.CocktailAdapter;
import it.massimoregoli.cocktailpassion.entities.Cocktail;
import it.massimoregoli.cocktailpassion.requests.CocktailAPI;
import it.massimoregoli.cocktailpassion.interfaces.SelectMode;
import it.massimoregoli.cocktailpassion.viewmodel.CocktailListViewModel;

public class MainActivity extends AppCompatActivity
    implements SelectMode {
  Holder holder;
  CocktailAdapter cocktailAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    holder = new Holder();
    CocktailListViewModel model =
        new ViewModelProvider(this).get(CocktailListViewModel.class);
    model.getCocktails().observe(this, new Observer<List<Cocktail>>() {
      @Override
      public void onChanged(List<Cocktail> item) {
        holder.setList(item);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  ActionMode mActionMode;

  private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      mode.getMenuInflater().inflate(R.menu.menu_context, menu);
      return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      switch (item.getItemId()) {
        case R.id.delete_all:
          cocktailAdapter.deleteAllSelected();
          mode.finish();
          return true;
        case R.id.add_fav:
          Toast.makeText(MainActivity.this, "ToDo", Toast.LENGTH_LONG).show();
        case R.id.unselect_all:
          cocktailAdapter.deselectAll();
          mode.finish();
          return true;
        case R.id.select_all:
          cocktailAdapter.selectAll();
          return true;
        default:
          return false;
      }
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
      mActionMode = null;
    }
  };
  @Override
  public void onSelect(int size) {
    if (mActionMode != null) {
      if(size == 0) { // No Selections
        mActionMode.finish(); // Close Menu
      }
      return;
    }
    mActionMode = startSupportActionMode(mActionModeCallback);
  }

  @Override
  protected void onStop() {
    super.onStop();
    CocktailListViewModel clm =
        new ViewModelProvider(MainActivity.this)
            .get(CocktailListViewModel.class);
    clm.setCocktails(cocktailAdapter.getCocktails());
  }

  class Holder implements View.OnClickListener, TextView.OnEditorActionListener {
    RecyclerView rv;
    Button btnSearch;
    RadioButton rbByName, rbByIngredient;
    EditText etSearch;
    CocktailAPI api;
    Holder() {
      rv = findViewById(R.id.rvCocktails);
      btnSearch = findViewById(R.id.btnSearch);
      rbByIngredient = findViewById(R.id.rbByIngredient);
      rbByName = findViewById(R.id.rbByName);
      etSearch = findViewById(R.id.etSearch);
      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
      rv.setLayoutManager(layoutManager);

      api = new CocktailAPI(getApplicationContext(), "1") {
        @Override
        public void fillLayout(List<Cocktail> cocktails) {
            cocktailAdapter = new CocktailAdapter(MainActivity.this, cocktails, MainActivity.this) {
              @Override
              public void onClickAdapterCallBack(Cocktail cocktail) {
                onClickAPICallBack(cocktail);
              }
            };
            rv.setAdapter(cocktailAdapter);
        }
      };

      btnSearch.setOnClickListener(this);
      etSearch.setOnEditorActionListener(this);
      Drawable drw = ContextCompat.getDrawable(getBaseContext(), R.drawable.my_divider);
      if(drw != null) {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getBaseContext(),
            DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(drw);
        rv.addItemDecoration(itemDecorator);
      }
    }

    void onClickAPICallBack(Cocktail cocktail) {
      Intent intent = new Intent(MainActivity.this, CocktailDetailsActivity.class);
      intent.putExtra("cocktail", cocktail);
      MainActivity.this.startActivity(intent);
    }

    void hideKeyboard() {
      InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      //Find the currently focused view, so we can grab the correct window token from it.
      View view = getCurrentFocus();
      //If no view currently has focus, create a new one, just so we can grab a window token from it
      if (view == null) {
        view = new View(MainActivity.this);
      }
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
      if(v.getId() == R.id.btnSearch) {
        search();
        hideKeyboard();
      }
    }

    private void search() {
      if(rbByName.isChecked()) {
        api.search(etSearch.getText().toString());
      } else {
        api.filter(etSearch.getText().toString());
      }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
      if (actionId == EditorInfo.IME_ACTION_SEARCH
          || actionId == EditorInfo.IME_ACTION_DONE) {
        search();

        return false;
      }
      return false;
    }

    void setList(List<Cocktail> cocktails) {
      if (cocktails == null)
        return;
      cocktailAdapter =
          new CocktailAdapter(MainActivity.this,
              cocktails,
              MainActivity.this) {
        @Override
        public void onClickAdapterCallBack(Cocktail cocktail) {
          onClickAPICallBack(cocktail);
        }
      };
      rv.setAdapter(cocktailAdapter);
    }
  }
}
