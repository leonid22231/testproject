package crosti.nuli.daimpre.fragments.game.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;

public class RadioButtonCenter extends AppCompatRadioButton {

    private Drawable buttonDrawable;


    public RadioButtonCenter(Context context) {
        super(context);
    }

    public RadioButtonCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioButtonCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        if (buttonDrawable != null) {
            int iconHeight = buttonDrawable.getIntrinsicHeight();
            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int width = getWidth();
            float totalWidth = buttonWidth + getPaint().measureText(getText().toString()) + getPaddingLeft() + getPaddingRight() + getCompoundDrawablePadding();

            if (totalWidth >= width) { super.onDraw(canvas); }
            else {
                int yTop = 0;
                int height = getHeight();
                int availableSpace = (int) ((width - totalWidth) / 2);
                int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
                int rightWidth = availableSpace + buttonWidth;

                switch (verticalGravity) {
                    case Gravity.BOTTOM:
                        yTop = height - iconHeight;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        yTop = (height - iconHeight) / 2;
                        break;
                }

                setButtonDrawable(android.R.color.transparent);
                buttonDrawable.setState(getDrawableState());
                buttonDrawable.setBounds(availableSpace, yTop, rightWidth, yTop + iconHeight);
                buttonDrawable.draw(canvas);

                float yPos = (height / 2 - (getPaint().descent() + getPaint().ascent()) / 2);

                canvas.drawText(getText().toString(), ((float) (rightWidth + getCompoundDrawablePadding())), yPos, getPaint());
            }
        } else {buttonDrawable = CompoundButtonCompat.getButtonDrawable(this); invalidate();}
    }
}