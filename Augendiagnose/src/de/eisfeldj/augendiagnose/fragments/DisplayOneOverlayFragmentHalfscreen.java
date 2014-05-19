package de.eisfeldj.augendiagnose.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.eisfeldj.augendiagnose.Application;
import de.eisfeldj.augendiagnose.R;

/**
 * Variant of DisplayOneOverlayFragment that is intended for a half screen
 * 
 * @author Joerg
 */
public class DisplayOneOverlayFragmentHalfscreen extends DisplayOneOverlayFragment {

	/**
	 * Inflate View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (Application.isLandscape()) {
			return inflater.inflate(R.layout.fragment_display_one_overlay_portrait, container, false);
		}
		else {
			return inflater.inflate(R.layout.fragment_display_one_overlay_landscape, container, false);
		}
	}

	/**
	 * Return the level from which on the utilities are shown. 1 means: don't show. 2 means: show only on full screen. 3
	 * means: show always.
	 * 
	 * @return
	 */
	@Override
	protected int getShowUtilitiesLimitLevel() {
		return 3;
	}

}