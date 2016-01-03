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
        final Button startResetButton = (Button)findViewById(R.id.start_reset_button);
        final String startButtonText = getString(R.string.start_button_text);
        final String resetButtonText = getString(R.string.reset_button_text);
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
    }
}
