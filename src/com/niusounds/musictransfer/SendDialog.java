package com.niusounds.musictransfer;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.app.DialogFragment;
import android.net.Uri;
import android.nfc.NfcAdapter;

@EFragment(R.layout.activity_send)
public class SendDialog extends DialogFragment {
	@FragmentArg
	String musicTitle;
	@FragmentArg
	String musicData;

	@AfterViews
	void init() {
		getDialog().setTitle(musicTitle);

		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
		adapter.setBeamPushUris(new Uri[] { Uri.fromFile(new File(musicData)) }, getActivity());
	}
}
