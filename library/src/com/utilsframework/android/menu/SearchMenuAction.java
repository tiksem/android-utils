package com.utilsframework.android.menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import com.utilsframework.android.R;

/**
 * Created by CM on 2/26/2015.
 */
public class SearchMenuAction {
    private SearchListener searchListener;

    public SearchListener getSearchListener() {
        return searchListener;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public SearchMenuAction(MenuInflater inflater, Menu menu) {
        inflater.inflate(getSearchMenuId(), menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(searchListener != null){
                    searchListener.onSearch(query);
                }

                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected int getSearchMenuId() {
        return R.menu.search;
    }
}