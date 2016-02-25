package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Lewis on 17/11/15.
 */
public class LifeCellView extends ImageView {
    private AnimatedVectorDrawableCompat animatedCellDrawableBorn;
    private AnimatedVectorDrawableCompat animatedCellDrawableDie;
    private Boolean cellAlive = false;

    public LifeCellView(Context context) {
        super(context);
        initialiseDrawables(context);
    }

    public LifeCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialiseDrawables(context);
    }

    private void initialiseDrawables(Context context){
        setImageResource(R.drawable.life_cell_vector_dead);
        animatedCellDrawableBorn = (AnimatedVectorDrawableCompat) ContextCompat.getDrawable(context, R.drawable.life_cell_animated_vector);
        animatedCellDrawableDie = (AnimatedVectorDrawableCompat) ContextCompat.getDrawable(context, R.drawable.life_cell_animated_vector_die);
    }

    public void makeCellViewLive() {
        if(!isCellAlive()) {
            setImageDrawable(animatedCellDrawableBorn);
            animatedCellDrawableBorn.start();
            setCellAlive(true);
        }
    }
    public void makeCellViewDead() {
        if (isCellAlive()) {
            setImageDrawable(animatedCellDrawableDie);
            animatedCellDrawableDie.start();
            setCellAlive(false);
        }
    }

    public Boolean isCellAlive() {
        return cellAlive;
    }

    public void setCellAlive(Boolean cellAlive) {
        this.cellAlive = cellAlive;
    }
}
