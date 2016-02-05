package io.github.lewismcgeary.androidgameoflife;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Lewis on 05/02/2016.
 */
public class ExplainRulesActivity extends AppIntro2 {


    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("test", "test description", R.drawable.life_cell_vector, R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance("test2", "test description2", R.drawable.life_cell_vector, R.color.colorPrimaryDark));
    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
