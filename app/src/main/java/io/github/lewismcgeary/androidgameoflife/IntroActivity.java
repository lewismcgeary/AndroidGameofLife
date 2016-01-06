package io.github.lewismcgeary.androidgameoflife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

public class IntroActivity extends AppCompatActivity {
    FloatingActionButton introDoneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        introDoneButton = (FloatingActionButton)findViewById(R.id.intro_done_button);
        introDoneButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startTransition();
                return false;
            }
        });
    }
    private void startTransition(){
        CardView introCardView = (CardView)findViewById(R.id.intro_card_view);
        Intent intent = new Intent(this, LifeGameActivity.class);
        Pair<View, String> pair1 = Pair.create((View)introCardView, getString(R.string.card_view_transition_name));
        Pair<View, String> pair2 = Pair.create((View)introDoneButton, getString(R.string.fab_transition_name));
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair2);
        ActivityCompat.startActivity(this, intent, options.toBundle());

    }
}
