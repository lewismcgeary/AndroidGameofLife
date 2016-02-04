package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Lewis on 03/02/2016.
 */
public class ShrinkingLogoBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private float startingY;

    public ShrinkingLogoBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        initialiseIfNeeded(child);
        float currentY = (int)child.getY();
        float scale = currentY/startingY;
        child.setScaleX(scale);
        child.setScaleY(scale);
        return true;
    }

    private void initialiseIfNeeded(ImageView child){
        if(startingY == 0){
            startingY = child.getY();
        }
    }
}
