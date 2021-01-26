/*
 * Copyright 2021 Geert Lorang
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package be.lorang.nuplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.google.gson.Gson;

import be.lorang.nuplayer.R;
import be.lorang.nuplayer.model.Program;
import be.lorang.nuplayer.model.ProgramList;
import be.lorang.nuplayer.presenter.CatalogPresenter;

public class SearchFragment extends SearchSupportFragment
        implements SearchSupportFragment.SearchResultProvider {

    private static final String TAG = "SearchFragment";

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private boolean mResultsFound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());

        // set background color
        BackgroundManager mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(getResources().getColor(R.color.vrtnu_black_tint_2));
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        //Log.d(TAG, String.format("Search text changed: %s", newQuery));
        loadQuery(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Log.d(TAG, String.format("Search text submitted: %s", query));
        loadQuery(query);
        return true;
    }

    public boolean hasResults() {
        return mRowsAdapter.size() > 0 && mResultsFound;
    }

    private boolean hasPermission(final String permission) {
        final Context context = getActivity();
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }

    private void loadQuery(String query) {

        mRowsAdapter.clear();

        if (query.length() > 0 && !query.equals("nil")) {

            ProgramList programList = ProgramList.getInstance();
            CatalogPresenter presenter = new CatalogPresenter();
            HeaderItem headerItem = new HeaderItem("Search results");
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenter);
            for(Program program : programList.search(query)) {
                adapter.add(program);
            }

            mRowsAdapter.add(new ListRow(headerItem, adapter));
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Program) {
                Program program = (Program) item;

                Intent programIntent = new Intent(getActivity().getBaseContext(), ProgramActivity.class);
                programIntent.putExtra("PROGRAM_OBJECT", (new Gson()).toJson(program));
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle();
                startActivity(programIntent, bundle);
            }

        }
    }

}