package io.github.lewismcgeary.androidgameoflife;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Button letsPlayButton = (Button)findViewById(R.id.lets_play_button);
        final AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        letsPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setFixedScreenOrientation();
                //startTransition();
                appBarLayout.setExpanded(false, true);
            }
        });
    }

    @Override
    protected void onResume() {
        //allows screen to rotate again. Screen orientation is fixed while transitioning between
        //activities
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onResume();
    }

    private void startTransition(){
        CardView introCardView = (CardView)findViewById(R.id.intro_card_view);
        ImageView logo = (ImageView)findViewById(R.id.app_logo);
        Intent intent = new Intent(this, LifeGameActivity.class);
        Pair<View, String> pair1 = Pair.create((View)introCardView, getString(R.string.card_view_transition_name));
        Pair<View, String> pair3 = Pair.create((View)logo, getString(R.string.logo_transition_name));
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair3);
        ActivityCompat.startActivity(this, intent, options.toBundle());
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
}
