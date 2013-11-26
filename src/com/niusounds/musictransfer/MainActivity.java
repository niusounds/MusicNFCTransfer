package com.niusounds.musictransfer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.app.ListFragment;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

@EActivity(R.layout.activity_main)
//@OptionsMenu(R.menu.main)
public class MainActivity extends Activity implements OnLoadCompleteListener<Cursor>, OnItemClickListener {
	private static final String[] FROM = { MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.TITLE };
	private static final int[] TO = { android.R.id.text1, android.R.id.text2 };
	private static final String SORT_ORDER_ALBUM = MediaStore.Audio.Media.ALBUM_KEY + ", " + MediaStore.Audio.Media.TRACK;
	private ListFragment list = new ListFragment();
	private CursorLoader loader;
	private SimpleCursorAdapter adapter;

	@AfterViews
	void init() {
		getFragmentManager().beginTransaction().replace(R.id.fragment_container, list).commit();

		loader = new CursorLoader(this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM },
				null, null, SORT_ORDER_ALBUM);
		loader.startLoading();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loader.registerListener(0, this);
	}

	@Override
	protected void onPause() {
		loader.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		if (adapter == null) {
			adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, FROM, TO, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		} else {
			adapter.swapCursor(cursor);
		}
		list.setListAdapter(adapter);
		list.getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
		Cursor cursor = (Cursor) list.getAdapter().getItem(pos);
		SendDialog_.builder().musicTitle(cursor.getString(1)).musicData(cursor.getString(2)).build().show(getFragmentManager(), "dialog");
	}
}
