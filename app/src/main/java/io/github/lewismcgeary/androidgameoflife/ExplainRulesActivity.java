package io.github.lewismcgeary.androidgameoflife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Lewis on 05/02/2016.
 */
public class ExplainRulesActivity extends AppIntro2 {


    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        CustomAppIntroFragment summaryFragment = CustomAppIntroFragment.newInstance("Game of Life", "In the Game of Life, the world is a grid of cells that are dead or alive. \n \nThis is a zero-player game.\n \nYou decide which cells are alive at the start of the game, but when play starts, the world evolves on its own following some simple rules...", R.drawable.life_logo_no_shadow_96, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light));
        addSlide(summaryFragment);
        addSlide(AppIntroFragment.newInstance("Loneliness", "A cell with fewer than two live neighbours dies", R.drawable.rules_fewer_than_two_neighbours, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light)));
        addSlide(AppIntroFragment.newInstance("Stability", "A live cell with two or three live neighbours survives", R.drawable.rules_two_or_three_neighbours, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light)));
        addSlide(AppIntroFragment.newInstance("Overcrowding", "A cell with more than three live neighbours dies", R.drawable.rules_greater_than_three, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light)));
        addSlide(AppIntroFragment.newInstance("Conception", "A dead cell with exactly three live neighbours comes to life", R.drawable.rules_exactly_three, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_orange_light)));

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
