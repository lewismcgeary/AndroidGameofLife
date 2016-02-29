package io.github.lewismcgeary.androidgameoflife;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity implements IntroFragment.OnFragmentInteractionListener, LifeGridFragment.OnFragmentInteractionListener {

    AppBarLayout appBarLayout;
    FloatingActionButton startResetFab;
    String startButtonText;
    String resetButtonText;
    AnimatedVectorDrawableCompat playIcon;
    AnimatedVectorDrawableCompat resetIcon;
    Snackbar snack;

    LifeGridFragment lifeGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFixedScreenOrientation(false);
        IntroFragment fragment = IntroFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //When fresh activity is created, clear out any fragments in back stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        setAppBarDragging(false);
        initialiseButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showButtonInStartMode();
        startResetFab.hide();
        appBarLayout.setExpanded(true, true);
        setFixedScreenOrientation(false);

    }

    private void setAppBarDragging(final boolean newValue) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return newValue;
            }
        });
        params.setBehavior(behavior);
    }

    private void initialiseButton(){
        startResetFab = (FloatingActionButton)findViewById(R.id.start_reset_fab);
        startButtonText = getString(R.string.start_button_text);
        resetButtonText = getString(R.string.reset_button_text);
        playIcon = AnimatedVectorDrawableCompat.create(this, R.drawable.fab_reset_to_play_animated_vector);
        resetIcon = AnimatedVectorDrawableCompat.create(this, R.drawable.fab_play_to_reset_animated_vector);
        startResetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startResetFab.getTag().equals(startButtonText)) {
                    showButtonInResetMode();
                    lifeGridFragment.worldGridPresenter.passLiveCellsToModelAndStartGame();
                } else {
                    showButtonInStartMode();
                    lifeGridFragment.worldGridPresenter.resetGrid();
                }
            }
        });
        showButtonInStartMode();
    }

    private void showButtonInStartMode(){
        startResetFab.setTag(startButtonText);
        startResetFab.setImageDrawable(playIcon);
        playIcon.start();
    }

    private void showButtonInResetMode(){
        startResetFab.setTag(resetButtonText);
        startResetFab.setImageDrawable(resetIcon);
        resetIcon.start();
    }

    private void startTransition(){
        lifeGridFragment = LifeGridFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(Build.VERSION.SDK_INT >= 21) {
            TransitionSet gridTransition = new TransitionSet();
            gridTransition.setDuration(600);
            gridTransition.addTransition(new ChangeBounds());
            gridTransition.addTransition((new ChangeTransform()));
            lifeGridFragment.setSharedElementEnterTransition(gridTransition);
        }


        fragmentTransaction.addSharedElement(findViewById(R.id.intro_card_view), getString(R.string.card_view_transition_name));
        fragmentTransaction.replace(R.id.container, lifeGridFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        appBarLayout.setExpanded(false, true);
    }

    private void setFixedScreenOrientation(boolean lockorientation){
        if(lockorientation) {
            //stop screen from rotating during game
            int orientation = getWindowManager().getDefaultDisplay().getRotation();

            switch (orientation) {
                case Surface.ROTATION_180:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_270:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
                case Surface.ROTATION_0:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case Surface.ROTATION_90:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    boolean fabShouldBeShown;
    //this listener deals with occasions where button remains in the wrong shown/hidden state
    //which happens if show or hide is called while it is still animating
    FloatingActionButton.OnVisibilityChangedListener fabVisibilityListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);
            if(!fabShouldBeShown){
                fab.hide();
            }
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            if(fabShouldBeShown){
                fab.show();
            }
        }
    };

    @Override
    public void cellDrawingInProgress() {
        fabShouldBeShown = false;
        startResetFab.hide(fabVisibilityListener);
    }

    @Override
    public void cellDrawingFinished() {
        fabShouldBeShown = true;
        //Fab can be stuck in wrong position when snackbar appears and disappears. this check ensures
        //fab is back in the correct location
        if(snack != null) {
            if(!snack.isShown()) {
                startResetFab.setTranslationY(0.0f);
            }
        }
        startResetFab.show(fabVisibilityListener);
    }

    @Override
    public void gameOver(){
        snack = Snackbar.make(startResetFab, R.string.game_over_snackbar_text, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        snack.show();
        showButtonInStartMode();
    }

    @Override
    public void noCellsWereSelected() {
        snack = Snackbar.make(startResetFab, R.string.starting_blank_game_snackbar_text , Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        snack.show();
        showButtonInStartMode();
    }

    @Override
    public void letsPlay() {
        setFixedScreenOrientation(true);
        startTransition();
    }

    @Override
    public void readRules() {
        Intent intent = new Intent(this, ExplainRulesActivity.class);
        startActivity(intent);
    }


}
