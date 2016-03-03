package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class LifeGridFragment extends Fragment implements GameStateCallback {

    private OnFragmentInteractionListener mListener;

    GridPresenter worldGridPresenter;
    LifeGridLayout worldGridLayout;
    ViewTreeObserver.OnGlobalLayoutListener listener;

    public LifeGridFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LifeGridFragment.
     */
    public static LifeGridFragment newInstance() {
        LifeGridFragment fragment = new LifeGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_life_grid, container, false);
        worldGridLayout = (LifeGridLayout)view.findViewById(R.id.life_grid_layout);
        worldGridLayout.setCallback(this);
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpGrid();
            }
        };
        //wait until layout is drawn before running setup calculations on its measurements
        worldGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        ImageView touchIcon = (ImageView)view.findViewById(R.id.touch_icon);
        AnimatedVectorDrawableCompat touchAnimation = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.touch_animated_vector);
        touchIcon.setImageDrawable(touchAnimation);
        touchAnimation.start();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        worldGridPresenter.pauseGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(worldGridPresenter != null) {
            worldGridPresenter.resumeGame();
        }
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
        //calculate the margins needed to centre the cells in the layout
        float unusedWidthDp = gridWidth - numberOfColumns * cellSize;
        float unusedHeightDp = gridHeight - numberOfRows * cellSize;
        float unusedWidthPx = unusedWidthDp * (densityOfScreen / 160);
        float unusedHeightPx = unusedHeightDp * (densityOfScreen / 160);
        int leftRightMargin = (int) unusedWidthPx / 2;
        int topBottomMargin = (int) unusedHeightPx / 2;
        //apply the calculated values
        FrameLayout.LayoutParams worldGridLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        worldGridLayoutParams.setMargins(leftRightMargin, topBottomMargin, leftRightMargin, topBottomMargin);
        worldGridLayout.setLayoutParams(worldGridLayoutParams);
        worldGridLayout.setColumnCount(numberOfColumns);
        worldGridLayout.setRowCount(numberOfRows);
        worldGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        worldGridPresenter = new GridPresenter(worldGridLayout, moveDuration);
        worldGridPresenter.setInitialState();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(worldGridPresenter != null) {
            worldGridPresenter.resetGrid();
        }
    }

    @Override
    public void gameOver() {
        mListener.gameOver();
    }

    @Override
    public void noCellsWereSelected() {
        mListener.noCellsWereSelected();
    }

    @Override
    public void cellDrawingInProgress() {
        mListener.cellDrawingInProgress();
    }

    @Override
    public void cellDrawingFinished() {
        mListener.cellDrawingFinished();
    }

    @Override
    public void gameStarted() {
        mListener.gameStarted();
    }

    public interface OnFragmentInteractionListener {

        void gameOver();

        void noCellsWereSelected();

        void cellDrawingInProgress();

        void cellDrawingFinished();

        void gameStarted();
    }
}
