package com.example.akanksha.pocketlab;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.lang.Math;

/**
 * Created by Akanksha on 4/26/2016.
 */

// http://stackoverflow.com/questions/8974088/how-to-create-a-resizable-rectangle-with-user-touch-events-on-android

public class DrawView extends ImageView {

    Point[] points = new Point[4];
    double[] colorsRGB = new double[3];
    ColorSensor_ViewImage myContext;

    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    private ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = 0;

    private int ballCount = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    private int oldX = 0;
    private int oldY = 0;
    private boolean moveRectangle = false;

    public DrawView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        myContext = (ColorSensor_ViewImage) this.getContext();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myContext = (ColorSensor_ViewImage) this.getContext();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        myContext = (ColorSensor_ViewImage) this.getContext();
    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(points[3]==null) //point4 null when user did not touch and move on screen.
            return;
        int left, top, right, bottom;
        left = points[0].x;
        top = points[0].y;
        right = points[0].x;
        bottom = points[0].y;
        for (int i = 1; i < points.length; i++) {
            left = left > points[i].x ? points[i].x:left;
            top = top > points[i].y ? points[i].y:top;
            right = right < points[i].x ? points[i].x:right;
            bottom = bottom < points[i].y ? points[i].y:bottom;
        }
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);

        //draw stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#AAFFFFFF"));
        paint.setStrokeWidth(2);
        canvas.drawRect(
                left + colorballs.get(0).getWidthOfBall() / 2,
                top + colorballs.get(0).getWidthOfBall() / 2,
                right + colorballs.get(2).getWidthOfBall() / 2,
                bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
        //draw the corners
        BitmapDrawable bitmap = new BitmapDrawable();
        // draw the balls on the canvas
        paint.setColor(Color.parseColor("#FF00D8FF"));
        paint.setTextSize(18);
        paint.setStrokeWidth(0);
        for (int i =0; i < colorballs.size(); i ++) {
            ColorBall ball = colorballs.get(i);
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    paint);
            //canvas.drawText("" + (i), ball.getX(), ball.getY(), paint);
        }
    } // onDraw()

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
                // a ball
                if (points[0] == null) {
                    //initialize rectangle.
                    points[0] = new Point();
                    points[0].x = X;
                    points[0].y = Y;

                    points[1] = new Point();
                    points[1].x = X;
                    points[1].y = Y + 30;

                    points[2] = new Point();
                    points[2].x = X + 30;
                    points[2].y = Y + 30;

                    points[3] = new Point();
                    points[3].x = X +30;
                    points[3].y = Y;

                    balID = 2;
                    groupId = 1;
                    // declare each ball with the ColorBall class
                    for (Point pt : points) {
                        colorballs.add(new ColorBall(getContext(), R.mipmap.ball, pt, ballCount++));
                    }
                }
                else {
                    balID = -1;
                    groupId = -1;

                    // check to see if touch is inside rectangle
                    int leftBoundX = Math.min(colorballs.get(0).getX(), colorballs.get(3).getX()) + colorballs.get(0).getWidthOfBall()/2;
                    int rightBoundX = Math.max(colorballs.get(0).getX(), colorballs.get(3).getX()) - colorballs.get(0).getWidthOfBall()/2;
                    int topBoundY = Math.max(colorballs.get(0).getY(), colorballs.get(1).getY()) - colorballs.get(0).getHeightOfBall()/2;
                    int bottomBoundY = Math.min(colorballs.get(0).getY(), colorballs.get(1).getY()) + colorballs.get(0).getHeightOfBall()/2;

                    if(X >= leftBoundX && X <= rightBoundX && Y <= topBoundY && Y >= bottomBoundY)
                    {
                        moveRectangle = true;
                    }
                    else {
                        //otherwise, resize rectangle
                        for (int i = colorballs.size() - 1; i >= 0; i--) {
                            ColorBall ball = colorballs.get(i);
                            // check if inside the bounds of the ball (circle)
                            // get the center for the ball
                            int centerX = ball.getX();
                            int centerY = ball.getY();
                            paint.setColor(Color.CYAN);
                            // calculate the radius from the touch to the center of the
                            // ball
                            double radCircle = Math
                                    .sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                                            * (centerY - Y)));

                            if (radCircle < ball.getWidthOfBall()) {

                                balID = ball.getID();
                                if (balID == 1 || balID == 3) {
                                    groupId = 2;
                                } else {
                                    groupId = 1;
                                }
                                invalidate();
                                break;
                            }
                            invalidate();
                        }
                    } // else resize rectangle
                } // else point[0] != null

                oldX = X;
                oldY = Y;

                Log.d("DEBUG","moveRectangle = " + moveRectangle);

                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                //Log.d("MSG","action_move");

                if (balID > -1) {
                    // move the balls the same as the finger
                    if(X > this.getWidth()-5)
                    {
                        X = this.getWidth()-5;
                    }
                    if(Y > this.getHeight()-5)
                    {
                        Y = this.getHeight()-5;
                    }

                    colorballs.get(balID).setX(X);
                    colorballs.get(balID).setY(Y);

                    paint.setColor(Color.CYAN);
                    if (groupId == 1) {
                        colorballs.get(1).setX(colorballs.get(0).getX());
                        colorballs.get(1).setY(colorballs.get(2).getY());
                        colorballs.get(3).setX(colorballs.get(2).getX());
                        colorballs.get(3).setY(colorballs.get(0).getY());
                    } else {
                        colorballs.get(0).setX(colorballs.get(1).getX());
                        colorballs.get(0).setY(colorballs.get(3).getY());
                        colorballs.get(2).setX(colorballs.get(3).getX());
                        colorballs.get(2).setY(colorballs.get(1).getY());
                    }

                    invalidate();
                }
                else if(points[0] != null && moveRectangle)
                {
                    int deltaX = X - oldX;
                    int deltaY = Y - oldY;

                    colorballs.get(0).setX(colorballs.get(0).getX() + deltaX);
                    colorballs.get(0).setY(colorballs.get(0).getY() + deltaY);
                    colorballs.get(1).setX(colorballs.get(1).getX() + deltaX);
                    colorballs.get(1).setY(colorballs.get(1).getY() + deltaY);
                    colorballs.get(2).setX(colorballs.get(2).getX() + deltaX);
                    colorballs.get(2).setY(colorballs.get(2).getY() + deltaY);
                    colorballs.get(3).setX(colorballs.get(3).getX() + deltaX);
                    colorballs.get(3).setY(colorballs.get(3).getY() + deltaY);

                    invalidate();
                }

                oldX = X;
                oldY = Y;

                break;

            case MotionEvent.ACTION_UP:
                moveRectangle = false;

                long reds = 0;
                long greens = 0;
                long blues = 0;

                float r = 0;
                float g = 0;
                float b = 0;

                int startX = Math.min(colorballs.get(2).getX(), colorballs.get(1).getX());
                int startY = Math.min(colorballs.get(1).getY(), colorballs.get(0).getY());
                int endX = Math.max(colorballs.get(2).getX(), colorballs.get(1).getX());
                int endY = Math.max(colorballs.get(1).getY(), colorballs.get(0).getY());

                if(startX < 0)
                {
                    startX = 0;
                }
                if(startY < 0)
                {
                    startY = 0;
                }
                if(endX > this.getWidth())
                {
                    endX = this.getWidth()-1;
                }
                if(endY > this.getHeight())
                {
                    endY = this.getHeight()-1;
                }

                long numPixels = 0;
                Bitmap bm = ((BitmapDrawable)this.getDrawable()).getBitmap();

                for(int i = startX; i <= endX; i++)
                {
                    for(int j = startY; j <= endY; j++)
                    {
                        numPixels++;
                        //int c = myContext.getBitmapPixel(i,j);
                        int c = bm.getPixel(
                                    (int) ((double) i * ((double) bm.getWidth() / this.getWidth())),
                                    (int) ((double) j * ((double) bm.getHeight()/this.getHeight()))
                                );
                        reds += Color.red(c);
                        greens += Color.green(c);
                        blues += Color.blue(c);
                        //Log.d("MSG","i = " + i + ", j = "+j);
                    }
                }

                r = ((float) reds)/numPixels;
                g = ((float) greens) / numPixels;
                b = ((float) blues) / numPixels;

                //myContext.setColorText("Red: " + r + "\nGreen: " + g + "\nBlue: " + b);
                //myContext.setColorboxColor(r,g,b);
                myContext.setColor(r,g,b);

                break;
        } // switch(eventAction)
        // redraw the canvas
        invalidate();
        return true;

    } // onTouchEvent()


    public static class ColorBall {

        Bitmap bitmap;
        Context mContext;
        Point point;
        int id;
        //static int count = 0;

        public ColorBall(Context context, int resourceId, Point point, int id) {
            this.id = id;
            Bitmap imgBitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId);
            bitmap = Bitmap.createScaledBitmap(imgBitmap,(int)(0.3*imgBitmap.getWidth()),(int)(0.3*imgBitmap.getHeight()),true);
            mContext = context;
            this.point = point;
        }

        public int getWidthOfBall() {
            return bitmap.getWidth();
        }

        public int getHeightOfBall() {
            return bitmap.getHeight();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public int getX() {
            return point.x;
        }

        public int getY() {
            return point.y;
        }

        public int getID() {
            return id;
        }

        public void setX(int x) {
            point.x = x;
        }

        public void setY(int y) {
            point.y = y;
        }
    } // class ColorBall
} // class DrawView