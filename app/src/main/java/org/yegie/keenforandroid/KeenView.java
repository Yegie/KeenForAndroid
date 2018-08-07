package org.yegie.keenforandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * A view class that handles screen touches and renders the game model
 * Mostly working for both vertical and horizontal, but the activities
 * are currently hard coded to be vertical.
 *
 * Created by Sergey on 5/19/2016.
 */
public class KeenView extends View implements GestureDetector.OnGestureListener {

    //all of the private variables needed to draw the game field and buttons
    private final float PADDING = 0.01f;
    @SuppressWarnings("FieldCanBeLocal")
    private final float PADDING_GRID = 0.03f;
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
    private float buttonUndoStartX, buttonUndoStartY,buttonUndoEndX,buttonUndoEndY,buttonUndoPanelHeight,buttonUndoPanelWidth;

    /**
     * Interface for passing grid touches to the controller
     * this is implemented by the controller
     */
    interface OnGridClickListener {

        void onGridClick(int x, int y);

        void onButtonClicked(int i);

        void onUndoButtonClick();

        void onEndScreenClick();
    }

    //constructor that sets up some of the basic variables
    public KeenView(Context context,KeenModel gameState)
    {
        super(context);
        this.setClickable(true);

        //in theory a view should ask the controller for any data contained in this
        //but it is a lot faster to just let the view have a copy.
        //if a client really cared about proper MVC this would have to be rewriten.
        this.gameState = gameState;
        this.size = gameState.getSize();
        //--

        init();
        this.gestureDetector=new GestureDetector(context,this);
    }

    //initialization of the paints used for the canvas
    private void init()
    {

        DisplayMetrics dm = getResources().getDisplayMetrics();

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

    //sets up the listener
    public void setOnGridClickListener(OnGridClickListener listener) {
        this.onGridClickListener=listener;
    }

    //sets up all of the info needed to draw on the canvas.
    //its really just keeping most of the math in one place
    @Override
    public void onSizeChanged (int w, int h, int oldW, int oldH)
    {
        if(w > 0 && h > 0)
        {

            screenHeight = h;
            screenWidth = w;

            if(h>w)
            {
                gridStartX = w-w*(1-PADDING_GRID);
                gridEndX   = w-w*PADDING_GRID;
                //noinspection SuspiciousNameCombination
                gridStartY = gridStartX;
                gridEndY   = gridStartY + (gridEndX-gridStartX);
            }
            else
            {
                gridStartX = h-h*(1-PADDING_GRID);
                gridEndX   = h-h*PADDING_GRID;
                //noinspection SuspiciousNameCombination
                gridStartY = gridStartX;
                gridEndY   = gridStartY + (gridEndX-gridStartX);
            }
            gridSize = (gridEndX-gridStartX);

            if(h>w)
            {
                buttonStartY      = gridSize +3*PADDING*w+w*0.05f;
                buttonStartX      = w-w*(1-PADDING);
                buttonEndX        = w-w*PADDING;
                buttonUndoStartX  = w/8;
                buttonUndoEndX    = w*7/8;
                buttonPanelWidth  = buttonEndX-buttonStartX;
                buttonSize        = (buttonPanelWidth - w*PADDING*(size-1))/(float) size;
                buttonEndY        = buttonStartY + buttonSize/1.4f;
                buttonUndoStartY  = buttonEndY + 3*PADDING*w;
                buttonPanelHeight = buttonEndY - buttonStartY;
                buttonUndoEndY    = buttonUndoStartY + buttonSize/1.4f;
                buttonUndoPanelHeight = buttonUndoEndY - buttonUndoStartY;
                buttonUndoPanelWidth  = buttonUndoEndX-buttonUndoStartX;
            } else
            {
                buttonStartX      = gridSize +3*PADDING*h+h*0.05f;
                buttonStartY = buttonUndoStartY = h-h*(1-PADDING);
                buttonEndY   = buttonUndoEndY   = h-h*PADDING;
                buttonPanelHeight  = buttonEndY-buttonStartY;
                buttonSize        = (buttonPanelHeight - h*PADDING*(size-1))/(float) size;
                buttonEndX        = buttonStartX + buttonSize/1.2f;
                buttonUndoStartX  = buttonEndX + 3*PADDING*w;
                buttonPanelWidth = buttonEndX - buttonStartX;
                buttonUndoEndX    = buttonUndoStartX + buttonPanelWidth;
                buttonUndoPanelHeight  = buttonUndoEndY-buttonUndoStartY;
                buttonUndoPanelWidth = buttonUndoEndX - buttonUndoStartX;

            }

        }
    }

    //this actually draws the grid, guesses, answers, and buttons
    //it mostly uses the numbers set up in init and just draws lines between them
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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

        drawGuess(canvas);

        if(gameState.getPuzzleWon())
        {
            TextGuessPaint.setColor(Color.WHITE);
            canvas.drawRect(buttonStartX+2,buttonStartY+2,buttonEndX-2,buttonUndoEndY-2,TextGuessPaint);
            canvas.drawRect(buttonStartX+2,buttonStartY+2,buttonEndX-2,buttonUndoEndY-2,ThinGridPaint);
            TextGuessPaint.setColor(Color.BLACK);
            canvas.drawText("You Won",screenWidth/2,buttonStartY+(buttonPanelHeight+buttonUndoPanelHeight)/2f - ((TextGuessPaint.descent() + TextGuessPaint.ascent()) / 2),TextGuessPaint);
        } else {
            drawButtonPanel(canvas);

            canvas.drawRect(buttonUndoStartX,buttonUndoStartY,buttonUndoEndX,buttonUndoEndY,ThinGridPaint);

            float xPos = buttonUndoStartX+(buttonUndoPanelWidth/2);
            float yPos = buttonUndoStartY+(buttonUndoPanelHeight / 2) - ((TextGuessPaint.descent() + TextGuessPaint.ascent()) / 2);
            canvas.drawText("undo",xPos,yPos,TextGuessPaint);
        }

    }

    //could be contained in the on draw, just separated for organizational purposes
    //this draws the partial and final guesses on the grid
    private void drawGuess(Canvas canvas) {

        for(short y = 0; y < size; y++)
        {
            for(short x = 0; x < size; x++)
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

    //helper method used to determine whether the current number is valid
    private boolean isValidGuess(short x, short y) {

        //in a latin square (note: not necessarily a correct number)
        for(short i = 0; i < size; i++)
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

        //checking if the current region satisfies the clue
        KeenModel.Zone curZone = gameState.getCell(x,y).zone;
        int curZoneCode = curZone.code;
        KeenModel.Zone.Type type = curZone.zoneType;
        int result = gameState.getCell(x,y).finalGuessValue;

        for(short ix = 0; ix < size; ++ix)
        {
            for (short iy = 0; iy < size; ++iy)
            {
                KeenModel.GridCell cell = gameState.getCell(ix,iy);

                if(curZoneCode == cell.zone.code)
                {
                    if(cell.finalGuessValue == -1)
                    {
                        //this works because if any cell in the current zone is missing a final
                        //guess we don't want to highlight incorrect results
                        return true;
                    }

                    //current cell value is already in "result"
                    if(ix == x && iy == y)
                        continue;

                    switch (type) {
                        case ADD:
                            result += cell.finalGuessValue;
                            break;
                        case MINUS:
                            if (result > cell.finalGuessValue)
                                result -= cell.finalGuessValue;
                            else
                                result = cell.finalGuessValue - result;
                            break;
                        case TIMES:
                            result *= cell.finalGuessValue;
                            break;
                        case DIVIDE:
                            if (result > cell.finalGuessValue) {
                                if (result % cell.finalGuessValue != 0)
                                    return false;
                                result /= cell.finalGuessValue;
                            }
                            else {
                                if (cell.finalGuessValue % result != 0)
                                    return false;
                                result = cell.finalGuessValue / result;
                            }
                            break;
                    }
                }
            }
        }

        return result == curZone.expectedValue;
    }

    //could be contained in the on draw, just separated for organizational purposes
    //this draws number button panel
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

    //could be contained in the on draw, just separated for organizational purposes
    //this draws the thicker boarders for zones and their labels
    private void drawZoneBordersAndLabels(Canvas canvas) {
        KeenModel.Zone[] zones = gameState.getGameZones();
        boolean[] drawn = new boolean[zones.length];

        for(short y = 0; y < size; y++)
        {
            for(short x = 0; x < size; x++)
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
                    if(!(curCell.zone.code == gameState.getCell(x,(short)(y+1)).zone.code))
                    {
                        canvas.drawLine(gridStartX+x/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,gridStartX+(x+1)/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,ThickGridPaint);
                    }
                }
                if(x!=size-1)
                {
                    if(!(curCell.zone.code == gameState.getCell((short)(x+1),y).zone.code))
                    {
                        canvas.drawLine(gridStartX+(x+1)/(float)size* gridSize,gridStartY+y/(float)size* gridSize,gridStartX+(x+1)/(float)size* gridSize,gridStartY+(y+1)/(float)size* gridSize,ThickGridPaint);
                    }
                }

                //int index = Arrays.asList(zones).indexOf(curCell.zone);
                int index = -1;
                for(int i = 0; i<zones.length && index == -1; ++i)
                {
                    if(zones[i].code == curCell.zone.code) index = i;
                }
                if(index <0 ){
                    Log.d("IndexError","someone screwed up");
                }

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

    //helper method that checks if there are coordinates currently selected by the user
    private boolean hasActiveCords()
    {
        int x = gameState.getActiveX();
        int y = gameState.getActiveY();

        return x >= 0 && y >= 0;
    }

    //the following methods all handle various gesture events by either ignoring them
    //or reporting them to the listener by calling the appropriate methods
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        float x=motionEvent.getX();
        float y=motionEvent.getY();

        // Calculate x and y into grid x and grid y
        
        if(gameState.getPuzzleWon())
        {
            onGridClickListener.onEndScreenClick();
        }else {

            if (cordsWithinGrid(x, y)) {

                x = x - gridStartX;
                y = y - gridStartY;

                int gridX = (int) (size * x / gridSize);
                int gridY = (int) (size * y / gridSize);

                if (onGridClickListener != null)
                    onGridClickListener.onGridClick(gridX, gridY);
            } else if (cordsAreNumbers(x, y)) {
                int num;
                if (screenHeight > screenWidth) {
                    x -= buttonStartX;
                    x /= buttonSize + (PADDING * screenWidth);
                    num = (int) x + 1;
                } else {
                    y -= buttonStartY;
                    y /= buttonSize + (PADDING * screenHeight);
                    num = (int) y + 1;
                }

                if (onGridClickListener != null)
                    onGridClickListener.onButtonClicked(num);
            } else if (cordsAreUndo(x, y)) {
                onGridClickListener.onUndoButtonClick();
            } else {
                if (onGridClickListener != null)
                    onGridClickListener.onGridClick(-1, -1);
            }

        }

        return true;
    }

    private boolean cordsAreUndo(float x, float y) {

        return !(x > buttonUndoEndX || x < buttonUndoStartX) && !(y > buttonUndoEndY || y < buttonUndoStartY);

    }

    private boolean cordsAreNumbers(float x, float y) {

        return !(x > buttonEndX || x < buttonStartX) && !(y > buttonEndY || y < buttonStartY);

    }

    private boolean cordsWithinGrid(float x, float y) {
        return !(x > gridEndX || x < gridStartX) && !(y > gridEndY || y < gridStartY);
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
