package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
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
        setImageResource(R.drawable.cell_vector_drawable_dead);
        animatedCellDrawableBorn = AnimatedVectorDrawableCompat.create(context, R.drawable.cell_animated_vector);
        animatedCellDrawableDie = AnimatedVectorDrawableCompat.create(context, R.drawable.cell_animated_vector_die);
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
