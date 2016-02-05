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
        CustomAppIntroFragment summaryFragment = CustomAppIntroFragment.newInstance(getString(R.string.rules_summary_title), getString(R.string.rules_summary_text), R.drawable.life_logo_no_shadow_96, ContextCompat.getColor(getApplicationContext(), R.color.rulesViewPagerBackground));
        addSlide(summaryFragment);
        addSlide(AppIntroFragment.newInstance(getString(R.string.rules_fewer_than_two_title), getString(R.string.rules_fewer_than_two_text), R.drawable.rules_fewer_than_two_neighbours, ContextCompat.getColor(getApplicationContext(), R.color.rulesViewPagerBackground)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.rules_two_or_three_title), getString(R.string.rules_two_or_three_text), R.drawable.rules_two_or_three_neighbours, ContextCompat.getColor(getApplicationContext(), R.color.rulesViewPagerBackground)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.rules_greater_than_three_title), getString(R.string.rules_greater_than_three_text), R.drawable.rules_greater_than_three, ContextCompat.getColor(getApplicationContext(), R.color.rulesViewPagerBackground)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.rules_exactly_three_title), getString(R.string.rules_exactly_three_text), R.drawable.rules_exactly_three, ContextCompat.getColor(getApplicationContext(), R.color.rulesViewPagerBackground)));

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
