package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Lewis on 17/11/15.
 */
public class LifeCellView extends ImageView {
    private AnimatedVectorDrawable animatedCellDrawableBorn;
    private AnimatedVectorDrawable animatedCellDrawableDie;
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
        animatedCellDrawableBorn = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.life_cell_animated_vector);
        animatedCellDrawableDie = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.life_cell_animated_vector_die);
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
