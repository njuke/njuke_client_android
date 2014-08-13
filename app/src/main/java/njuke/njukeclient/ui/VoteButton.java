package njuke.njukeclient.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import njuke.njukeclient.R;

/**
 * Created by erik on 13/08/14.
 */
public class VoteButton extends Button {
    public static final String TAG = VoteButton.class.getSimpleName();
    private boolean isClicked;

    public VoteButton(Context context) {
        super(context);
    }

    public VoteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VoteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean performClick() {
        isClicked = !isClicked;
        updateUI();
        return super.performClick();
    }

    public void changeNumber(boolean inc) {
        String text = getText().toString().replace("+", "").trim();

        int currentNumber = Integer.valueOf(text);
        if (inc) {
            currentNumber++;
        } else {
            currentNumber--;
        }

        setText("+" + currentNumber);
    }

    public void setState(boolean isClicked) {
        this.isClicked = isClicked;
        updateUI();
    }

    private void updateUI() {
        int bg = isClicked ? R.drawable.vote_button_clicked : R.drawable.vote_button;
        int color = isClicked ? getResources().getColor(R.color.text_color_primary_light)
                              : getResources().getColor(R.color.text_color_primary_dark);
        setBackgroundResource(bg);
        setTextColor(color);
        setTypeface(null, isClicked ? Typeface.BOLD : Typeface.NORMAL);
    }
}
