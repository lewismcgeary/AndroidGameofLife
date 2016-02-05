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
        addSlide(AppIntroFragment.newInstance("test", "A cell with fewer than two live neighbours dies", R.drawable.life_cell_vector, R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance("test2", "A live cell with two or three live neighbours survives", R.drawable.life_cell_vector, R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance("test3", "A cell with more than three live neighbours will die", R.drawable.life_cell_vector, R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance("test4", "A dead cell with three live neighbours will come to life", R.drawable.life_cell_vector, R.color.colorPrimaryDark));

    }

    @Override
    public void onDonePressed() {
        onBackPressed();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
