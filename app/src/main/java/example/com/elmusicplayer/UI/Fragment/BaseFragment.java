package example.com.elmusicplayer.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    protected ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(rootViewLayout(), container, false);
        setUpViews();
        allViewsAreInitialized = true;
        return rootView;
    }


    protected abstract int rootViewLayout();

    protected abstract void setUpViews();

    protected boolean allViewsAreInitialized;

}
