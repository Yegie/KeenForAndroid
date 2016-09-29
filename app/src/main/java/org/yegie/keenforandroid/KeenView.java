package org.yegie.keenforandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

/**
 * Created by Sergey on 5/19/2016.
 */
public class KeenView extends View implements GestureDetector.OnGestureListener {

    private final float PADDING = 0.01f;
    private Paint ThinGridPaint;
    private Paint ThickGridPaint;
    private Paint ActiveGridPaint;
    private Paint TextGridPaint;
    private Paint TextGuessPaint;
    private Paint TextGuessSmallPaint;
    private KeenModel gameState;
    private int size;
    private GestureDetector gestureDetector;
    private OnGridClickListener onGridClickListener=null;

    private float screenWidth, screenHeight;
    private float gridStartX, gridEndX, gridStartY, gridEndY, gridSize;
    private float buttonStartX, buttonEndX, buttonStartY, buttonEndY, buttonPanelWidth, buttonPanelHeight, buttonSize;

    /** Interface for passing grid touches to the controller
     */
    interface OnGridClickListener {
        /**
         * When a grid cell clicked.
         * @param x     X coord
         * @param y     Y coord
         */
        void onGridClick(int x, int y);

        /**
         * When a button with a digit clicked.
         * @param i    Button number
         */
        void onButtonClicked(int i);
    }

    public KeenView(Context context,KeenModel gameState)
    {
        super(context);
        this.setClickable(true);
        this.gameState = gameState;
        this.size = gameState.getSize();
        init(context);
        this.gestureDetector=new GestureDetector(context,this);
    }

    private void init(Context context)
    {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float textSize = dm.densityDpi/6f;

        ThinGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ThinGridPaint.setColor(Color.BLACK);
        ThinGridPaint.setStrokeWidth(dm.densityDpi/8f/(float)size);

        TextGridPaint = new Paint(ThinGridPaint);
        //TextGridPaint.setTextSize(dm.densityDpi/12f);
        TextGridPaint.setTextSize(dm.densityDpi/2.4f/(float)size);

        TextGuessPaint = new Paint(ThinGridPaint);
        //TextGuessPaint.setTextSize(dm.densityDpi/4f);
        TextGuessPaint.setTextSize(dm.densityDpi/(float)size);
        TextGuessPaint.setTextAlign(Paint.Align.CENTER);

        TextGuessSmallPaint = new Paint(ThinGridPaint);
        //TextGuessSmallPaint.setTextSize(dm.densityDpi/10f);
        TextGuessSmallPaint.setTextSize(dm.densityDpi/1.9f/(float)size);
        TextGuessSmallPaint.setTextAlign(Paint.Align.CENTER);

        ThickGridPaint = new Paint(ThinGridPaint);
        ThickGridPaint.setStrokeWidth(dm.densityDpi/2.8f/(float)size);
        ThickGridPaint.setStrokeCap(Paint.Cap.ROUND);


        ActiveGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ActiveGridPaint.setStyle(Paint.Style.FILL);
        ActiveGridPaint.setColor(Color.argb(127,100,100,100));

        ThinGridPaint.setStyle(Paint.Style.STROKE);

    }

    public void setOnGridClickListener(OnGridClickListener listener) {
        this.onGridClickListener=listener;
    }

    @Override
    public void onSizeChanged (int w, int h, int oldW, int oldH)
    {
        if(w > 0 && h > 0)
        {

            screenHeight = h;
            screenWidth = w;

            if(h>w)
            {
                gridStartX = w-w*(1-PADDING);
                gridEndX   = w-w*PADDING;
                gridStartY = gridStartX;
                gridEndY   = gridStartY + (gridEndX-gridStartX);
            }
            else
            {
                gridStartX = h-h*(1-PADDING);
                gridEndX   = h-h*PADDING;
                gridStartY = gridStartX;
                gridEndY   = gridStartY + (gridEndX-gridStartX);
            }
            gridSize = (gridEndX-gridStartX);

            if(h>w)
            {
                buttonStartY      = gridSize +3*PADDING*w+w*0.05f;
                buttonStartX      = w-w*(1-PADDING);
                buttonEndX        = w-w*PADDING;
                buttonPanelWidth  = buttonEndX-buttonStartX;
                buttonSize        = (buttonPanelWidth - w*PADDING*(size-1))/(float) size;
                buttonEndY        = buttonStartY + buttonSize/1.2f;
                buttonPanelHeight = buttonEndY - buttonStartY;
            } else
            {
                buttonStartX      = gridSize +3*PADDING*h+h*0.05f;
                buttonStartY      = h-h*(1-PADDING);
                buttonEndY        = h-h*PADDING;
                buttonPanelHeight  = buttonEndY-buttonStartY;
                buttonSize        = (buttonPanelHeight - h*PADDING*(size-1))/(float) size;
                buttonEndX        = buttonStartX + buttonSize/1.2f;
                buttonPanelWidth = buttonEndX - buttonStartX;
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(gameState.getPuzzleWon())
        {
            TextGuessPaint.setColor(Color.argb(255, 100, 100, 100));
            canvas.drawRect(0,0,screenWidth,screenHeight,TextGuessPaint);
            TextGuessPaint.setColor(Color.WHITE);
            canvas.drawText("You Win",screenWidth/2,screenHeight/2,TextGuessPaint);
            TextGuessPaint.setColor(Color.BLACK);
        } else
        {

            if(hasActiveCords())
            {
                int X = gameState.getActiveX();
                int Y = gameState.getActiveY();

                if(gameState.getFinalGuess())
                {
                    canvas.drawRect(gridStartX+X/(float)size* gridSize,gridStartY+Y/(float)size* gridSize,gridStartX+(X+1)/(float)size* gridSize,gridStartY+(Y+1)/(float)size* gridSize,ActiveGridPaint);
                }else
                {
                    Path points = new Path();
                    points.moveTo(gridStartX+X/(float)size* gridSize,gridStartY+Y/(float)size* gridSize);
                    points.lineTo(gridStartX+((float)X+0.4f)/(float)size* gridSize,gridStartY+Y/(float)size* gridSize);
                    points.lineTo(gridStartX+X/(float)size* gridSize,gridStartY+((float)Y+0.4f)/(float)size* gridSize);
                    points.close();

                    canvas.drawPath(points,ActiveGridPaint);
                }

            }
            for(int i = 0; i < size; i++)
                canvas.drawLine(gridStartX,gridStartY+i/(float)size* gridSize,gridEndX,gridStartY+i/(float)size* gridSize,ThinGridPaint);
            for(int i = 0; i < size; i++)
                canvas.drawLine(gridStartX+i/(float)size* gridSize,gridStartY,gridStartX+i/(float)size* gridSize,gridEndY,ThinGridPaint);

            drawZoneBordersAndLabels(canvas);

            drawButtonPanel(canvas);

            drawGuess(canvas);

        }

    }

    private void drawGuess(Canvas canvas) {

        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {

                KeenModel.GridCell curCell = gameState.getCell(x,y);

                if(curCell.finalGuessValue != -1)
                {
                    float xPos = gridStartX+(x*gridSize/(float)size)+(gridSize / (float)size / 2);
                    float yPos = gridStartY+(y*gridSize/(float)size)+(gridSize / (float)size / 2) - ((TextGuessPaint.descent() + TextGuessPaint.ascent()) / 2);

                    if(isValidGuess(x,y)) {
                        TextGuessPaint.setColor(Color.argb(255, 0, 88, 0));
                    } else
                    {
                        TextGuessPaint.setColor(Color.argb(255, 88, 0, 0));
                    }

                    canvas.drawText(""+curCell.finalGuessValue,xPos,yPos,TextGuessPaint);

                    TextGuessPaint.setColor(Color.BLACK);

                } else
                {

                    TextGuessSmallPaint.setColor(Color.argb(255,0,88,0));

                    for(int i = 0; i<3; i++)
                    {
                        for(int j = 0; j<3; j++)
                        {
                            int out = (i*3+(j+1));

                            if(gameState.getCell(x,y).guesses[out-1]) {
                                float xPos = gridStartX + ((x + 0.25f * (j - 1)) * gridSize / (float) size) + (gridSize / (float) size / 2);
                                float yPos = gridStartY + ((y + 0.1f + 0.2f * (i - 1)) * gridSize / (float) size) + (gridSize / (float) size / 2) - ((TextGuessSmallPaint.descent() + TextGuessSmallPaint.ascent()) / 2);

                                canvas.drawText("" + out, xPos, yPos, TextGuessSmallPaint);
                            }
                        }
                    }

                    TextGuessSmallPaint.setColor(Color.BLACK);
                }
            }
        }
    }

    private boolean isValidGuess(int x, int y) {
        for(int i = 0; i < size; i++)
        {
            if(i != x && gameState.getCell(i,y).finalGuessValue == gameState.getCell(x,y).finalGuessValue)
            {
                return false;
            }
            if(i != y && gameState.getCell(x,i).finalGuessValue == gameState.getCell(x,y).finalGuessValue)
            {
                return false;
            }
        }
        return true;
    }

    private void drawButtonPanel(Canvas canvas) {

        if(buttonPanelWidth>buttonPanelHeight)
        {
            for(int i = 0; i < size; i++)
            {
                float x1=buttonStartX+(screenWidth*PADDING+buttonSize)*i;
                float x2=x1+buttonSize;
                canvas.drawRect(x1,buttonStartY,x2,buttonEndY,ThinGridPaint);

                float xPos = buttonStartX+(i*buttonPanelWidth/(float)size)+(buttonPanelWidth / (float)size / 2);
                float yPos = buttonStartY+(buttonPanelHeight / 2) - ((TextGuessPaint.descent() + TextGuessPaint.ascent()) / 2);
                canvas.drawText(""+(i+1),xPos,yPos,TextGuessPaint);
            }

        }else
        {
            for(int i = 0; i < size; i++)
            {
                float y1 = buttonStartY + (screenHeight * PADDING + buttonSize) * i;
                float y2 = y1 + buttonSize;
                canvas.drawRect(buttonStartX, y1, buttonEndX, y2, ThinGridPaint);

                float xPos = buttonStartX + (buttonPanelWidth / 2);
                float yPos = buttonStartY + (i*buttonPanelHeight/(float)size) + (buttonPanelHeight/ (float)size / 2) - ((TextGuessPaint.descent() + TextGuessPaint.ascent()) / 2);
                canvas.drawText(""+(i+1),xPos,yPos,TextGuessPaint);
            }
        }
    }


    private void drawZoneBordersAndLabels(Canvas canvas) {
        KeenModel.Zone[] zones = gameState.getGameZones();
        boolean[] drawn = new boolean[zones.length];

        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {
                KeenModel.GridCell curCell = gameState.getCell(x,y);

                if(y == 0)
                {
                    canvas.drawLine(gridStartX+x/(float)size* gridSize,gridStartY+y/(float)size* gridSize,gridStartX+(x+1)/(float)size* gridSize,gridStartY+y/(float)size* gridSize,ThickGridPaint);
                }
                if(x == 0)
                {
                    canvas.drawLine(gridStartX+x/(float)size* gridSize,gridStartY+y/(float)size* gridSize,gridStartX+x/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,ThickGridPaint);
                }
                if(y!=size-1)
                {
                    if(!(curCell.zone == gameState.getCell(x,y+1).zone))
                    {
                        canvas.drawLine(gridStartX+x/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,gridStartX+(x+1)/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,ThickGridPaint);
                    }
                }
                if(x!=size-1)
                {
                    if(!(curCell.zone == gameState.getCell(x+1,y).zone))
                    {
                        canvas.drawLine(gridStartX+(x+1)/(float)size* gridSize,gridStartY+y/(float)size* gridSize,gridStartX+(x+1)/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,ThickGridPaint);
                    }
                }

                int index = Arrays.asList(zones).indexOf(curCell.zone);

                if(!drawn[index])
                {

                    canvas.drawText(zones[index].toString(),gridStartX+((float)x+0.05f+(float)Math.pow(size*2.5,-1))/(float)size* gridSize,gridStartY+((float)y+0.2f+(float)Math.pow(size*3,-1))/(float)size* gridSize,TextGridPaint);

                    drawn[index] = true;
                }

            }
        }

        canvas.drawLine(gridEndX,gridStartY,gridEndX,gridEndY,ThickGridPaint);
        canvas.drawLine(gridStartX,gridEndY,gridEndX,gridEndY,ThickGridPaint);

    }

    /**
     * checks if the gamestate has a set of active cordiantes.
     *
     */
    private boolean hasActiveCords()
    {
        int x = gameState.getActiveX();
        int y = gameState.getActiveY();

        if(x >= 0 && y >= 0)
            return true;
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        float x=motionEvent.getX();
        float y=motionEvent.getY();

        // Calculate x and y into grid x and grid y
        //
        if(cordsWithinGrid(x,y)) {

            x = x - gridStartX;
            y = y - gridStartY;

            int gridX = (int)(size*x/ gridSize);
            int gridY = (int)(size*y/ gridSize);

            if (onGridClickListener != null)
                onGridClickListener.onGridClick(gridX, gridY);
        }
        else if(cordsAreNumbers(x,y))
        {
            int num;
            if(screenHeight>screenWidth){
                x -= buttonStartX;
                x /= buttonSize+(PADDING*screenWidth);
                num = (int) x+1;
            }else
            {
                y -= buttonStartY;
                y /= buttonSize+(PADDING*screenHeight);
                num = (int) y+1;
            }




            if (onGridClickListener != null)
                onGridClickListener.onButtonClicked(num);
        }
        else {
            if (onGridClickListener != null)
                onGridClickListener.onGridClick(-1, -1);
        }


        return true;
    }

    private boolean cordsAreNumbers(float x, float y) {

        if(x>buttonEndX || x<buttonStartX)
            return false;
        if(y>buttonEndY || y<buttonStartY)
            return false;

        return true;
    }

    private boolean cordsWithinGrid(float x, float y) {
        if(x>gridEndX || x<gridStartX)
            return false;
        if(y>gridEndY || y<gridStartY)
            return false;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
