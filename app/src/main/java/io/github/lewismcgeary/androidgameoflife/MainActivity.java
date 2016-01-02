package io.github.lewismcgeary.androidgameoflife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
GridPresenter worldGridPresenter;
    List<GridCoordinates> selectedStartingLiveCells = new ArrayList<>();
    LifeGridLayout worldGridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        worldGridLayout = (LifeGridLayout)findViewById(R.id.life_grid_layout);
        worldGridPresenter = new GridPresenter(this, worldGridLayout);
        worldGridPresenter.setInitialState();
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldGridPresenter.passLiveCellsToModelAndStartGame();
            }
        });
        Button resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldGridPresenter.resetGrid();
            }
        });
    }
}
