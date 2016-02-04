package io.github.lewismcgeary.androidgameoflife;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity implements IntroFragment.OnFragmentInteractionListener, LifeGridFragment.OnFragmentInteractionListener {

    AppBarLayout appBarLayout;
    FloatingActionButton startResetFab;
    String startButtonText;
    String resetButtonText;
    Drawable playIcon;
    Drawable resetIcon;
    Snackbar snack;

    LifeGridFragment lifeGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        IntroFragment fragment = IntroFragment.newInstance(null, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        initialiseButton();
        /** Button letsPlayButton = (Button)findViewById(R.id.lets_play_button);
        final AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        letsPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFixedScreenOrientation();
                startTransition();
                //appBarLayout.setExpanded(false, true);
            }
        });*/
    }

    @Override
    protected void onResume() {
        //allows screen to rotate again. Screen orientation is fixed while transitioning between
        //activities
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showButtonInStartMode();
        startResetFab.hide();
        appBarLayout.setExpanded(true, true);

    }

    private void initialiseButton(){
        startResetFab = (FloatingActionButton)findViewById(R.id.start_reset_fab);
        startButtonText = getString(R.string.start_button_text);
        resetButtonText = getString(R.string.reset_button_text);
        playIcon = getDrawable(R.drawable.ic_play_arrow_24dp);
        resetIcon = getDrawable(R.drawable.ic_replay_24dp);
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
    }

    private void showButtonInStartMode(){
        startResetFab.setTag(startButtonText);
        startResetFab.setImageDrawable(playIcon);
    }

    private void showButtonInResetMode(){
        startResetFab.setTag(resetButtonText);
        startResetFab.setImageDrawable(resetIcon);
    }

    private void startTransition(){
        CardView introCardView = (CardView)findViewById(R.id.intro_card_view);
        ImageView logo = (ImageView)findViewById(R.id.app_logo);
        /** Intent intent = new Intent(this, LifeGameActivity.class);
        Pair<View, String> pair1 = Pair.create((View)introCardView, getString(R.string.card_view_transition_name));
        Pair<View, String> pair3 = Pair.create((View)logo, getString(R.string.logo_transition_name));
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair3);
        ActivityCompat.startActivity(this, intent, options.toBundle());*/

        TransitionSet gridTransition = new TransitionSet();
        gridTransition.setDuration(600);
        gridTransition.addTransition(new ChangeBounds());
        gridTransition.addTransition((new ChangeTransform()));
        lifeGridFragment = LifeGridFragment.newInstance(null, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        lifeGridFragment.setSharedElementEnterTransition(gridTransition);
        fragmentTransaction.addSharedElement(findViewById(R.id.intro_card_view), getString(R.string.card_view_transition_name));
        fragmentTransaction.replace(R.id.container, lifeGridFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        appBarLayout.setExpanded(false, true);
    }

    private void setFixedScreenOrientation(){
        //setting fixed orientation before switching to the next activity avoids an error if device
        //is in a different orientation when back button is pressed to return to this activity
        int orientation = getWindowManager().getDefaultDisplay().getRotation();

        switch(orientation) {
            case Surface.ROTATION_180:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
            case Surface.ROTATION_270:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case  Surface.ROTATION_0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Surface.ROTATION_90:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        setFixedScreenOrientation();
        startTransition();
    }
}
