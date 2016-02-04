package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LifeGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LifeGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LifeGridFragment extends Fragment implements GameStateCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    GridPresenter worldGridPresenter;
    LifeGridLayout worldGridLayout;

    public LifeGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LifeGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LifeGridFragment newInstance(String param1, String param2) {
        LifeGridFragment fragment = new LifeGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_life_grid, container, false);
        worldGridLayout = (LifeGridLayout)view.findViewById(R.id.life_grid_layout);
        worldGridLayout.setCallback(this);
        worldGridLayout.post(new Runnable() {
            @Override
            public void run() {
                setUpGrid();
            }
        });
        return view;
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
        worldGridPresenter = new GridPresenter(worldGridLayout, moveDuration);
        worldGridPresenter.setInitialState();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void gameOver();

        void noCellsWereSelected();

        void cellDrawingInProgress();

        void cellDrawingFinished();
    }
}
