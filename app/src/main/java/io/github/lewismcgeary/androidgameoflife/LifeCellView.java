package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Lewis on 17/11/15.
 */
public class LifeCellView extends ImageView {
    AnimatedVectorDrawable animatedCellDrawableBorn;
    AnimatedVectorDrawable animatedCellDrawableDie;

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    Boolean state = false;

    public LifeCellView(Context context) {
        super(context);
        setImageResource(R.drawable.life_cell_animated_vector);
        animatedCellDrawableBorn = (AnimatedVectorDrawable) getDrawable();
        animatedCellDrawableDie = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.life_cell_animated_vector_die);

    }

    public LifeCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.life_cell_animated_vector);
        animatedCellDrawableBorn = (AnimatedVectorDrawable) getDrawable();
        animatedCellDrawableDie = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.life_cell_animated_vector_die);

    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);

    }
    public void makeCellViewLive() {
        if(getState() == false) {
            //setImageResource(R.drawable.life_cell_animated_vector);
            setImageDrawable(animatedCellDrawableBorn);
            //animatedCellDrawableBorn = (AnimatedVectorDrawable) getDrawable();
            animatedCellDrawableBorn.start();
            setState(true);
        }
    }
    public void makeCellViewDead() {
        if (getState()) {
            //setImageResource(R.drawable.life_cell_animated_vector_die);
            setImageDrawable(animatedCellDrawableDie);
            //animatedCellDrawableDie = (AnimatedVectorDrawable) getDrawable();
            animatedCellDrawableDie.start();
            setState(false);
        }
    }
}
