package io.github.lewismcgeary.androidgameoflife;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    GridPresenter worldGridPresenter;
    LifeGridLayout worldGridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        worldGridLayout = (LifeGridLayout)findViewById(R.id.life_grid_layout);
        worldGridLayout.post(new Runnable() {
            @Override
            public void run() {
                setUpGrid();
            }
        });
        final FloatingActionButton startResetFab = (FloatingActionButton)findViewById(R.id.start_reset_fab);
        final Button startResetButton = (Button)findViewById(R.id.start_reset_button);
        final String startButtonText = getString(R.string.start_button_text);
        final String resetButtonText = getString(R.string.reset_button_text);
        final Drawable playIcon = getDrawable(R.drawable.ic_play_arrow_24dp);
        final Drawable resetIcon = getDrawable(R.drawable.ic_replay_24dp);
        startResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startResetButton.getText().equals(startButtonText)) {
                    startResetButton.setText(resetButtonText);
                    worldGridPresenter.passLiveCellsToModelAndStartGame();
                } else {
                    startResetButton.setText(startButtonText);
                    worldGridPresenter.resetGrid();
                }
            }
        });
        startResetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startResetFab.getTag().equals(startButtonText)) {
                    startResetFab.setTag(resetButtonText);
                    startResetFab.setImageDrawable(resetIcon);
                    worldGridPresenter.passLiveCellsToModelAndStartGame();
                } else {
                    startResetFab.setTag(startButtonText);
                    startResetFab.setImageDrawable(playIcon);
                    worldGridPresenter.resetGrid();
                }
            }
        });
    }
    private void setUpGrid() {
        int densityOfScreen = DisplayMetrics.DENSITY_DEFAULT;
        int cellSize = (int) getResources().getDimension(R.dimen.cell_size);
        int moveDuration = getResources().getInteger(R.integer.move_duration);
        //get the width and height of grid in dp
        int gridWidth = worldGridLayout.getMeasuredWidth() / (densityOfScreen / 160);
        int gridHeight = worldGridLayout.getMeasuredHeight() / (densityOfScreen / 160);
        //calculate number of columns and rows that will fit on screen
        int numberOfColumns = gridWidth/cellSize;
        int numberOfRows = gridHeight/cellSize;
        worldGridLayout.setColumnCount(numberOfColumns);
        worldGridLayout.setRowCount(numberOfRows);
        worldGridPresenter = new GridPresenter(worldGridLayout, moveDuration);
        worldGridPresenter.setInitialState();
    }
}
