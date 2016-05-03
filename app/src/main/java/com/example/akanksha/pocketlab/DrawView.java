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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.Math;

/**
 * Created by Akanksha on 4/26/2016.
 */

// http://stackoverflow.com/questions/8974088/how-to-create-a-resizable-rectangle-with-user-touch-events-on-android

public class DrawView extends ImageView {

    Point[] points = new Point[4];
    double[] colorsRGB = new double[3];
    PhotoView myContext;

    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    private ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    public DrawView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        myContext = (PhotoView) this.getContext();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myContext = (PhotoView) this.getContext();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        myContext = (PhotoView) this.getContext();
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
        //fill the rectangle
        /*paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#11FFFFFF"));
        paint.setStrokeWidth(0);
        canvas.drawRect(
                left + colorballs.get(0).getWidthOfBall() / 2,
                top + colorballs.get(0).getWidthOfBall() / 2,
                right + colorballs.get(2).getWidthOfBall() / 2,
                bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
        */
        //draw the corners
        BitmapDrawable bitmap = new BitmapDrawable();
        // draw the balls on the canvas
        paint.setColor(Color.parseColor("#FF00D8FF"));
        //paint.setTextSize(18);
        //paint.setStrokeWidth(0);
        for (int i =0; i < colorballs.size(); i ++) {
            ColorBall ball = colorballs.get(i);
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    paint);

            //canvas.drawText("" + (i), ball.getX(), ball.getY(), paint);
        }
    }

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
                        colorballs.add(new ColorBall(getContext(), R.mipmap.ball, pt));
                    }
                } else {
                    //resize rectangle
                    balID = -1;
                    groupId = -1;
                    for (int i = colorballs.size()-1; i>=0; i--) {
                        ColorBall ball = colorballs.get(i);
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
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
                }
                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball


                if (balID > -1 && balID < 4) {
                    // move the balls the same as the finger
                    if(X >= myContext.getWindowWidth()-5)
                    {
                        X = myContext.getWindowWidth()-5;
                    }
                    if(Y >= myContext.getWindowHeight()-5)
                    {
                        Y = myContext.getWindowHeight()-5;
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

                break;

            case MotionEvent.ACTION_UP:
                long reds = 0;
                long greens = 0;
                long blues = 0;

                //int boxWidth = Math.abs(colorballs.get(2).getX() - colorballs.get(1).getX());
                //int boxHeight = Math.abs(colorballs.get(1).getY()-colorballs.get(0).getY());

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
                if(endX > myContext.getWindowWidth())
                {
                    endX = myContext.getWindowWidth()-1;
                }
                if(endY > myContext.getWindowHeight())
                {
                    endY = myContext.getWindowHeight()-1;
                }

                long numPixels = 0;

                for(int i = startX; i <= endX; i++)
                {
                    for(int j = startY; j <= endY; j++)
                    {
                        numPixels++;
                        int c = myContext.getBitmap().getPixel(i,j);
                        reds += Color.red(c);
                        greens += Color.green(c);
                        blues += Color.blue(c);
                        //Log.d("MSG","i = " + i + ", j = "+j);
                    }
                }

                Toast.makeText(myContext,"Red: " + ((float) reds) / numPixels + " Green: " + ((float) greens) / numPixels + " Blue: " + ((float) blues) / numPixels, Toast.LENGTH_LONG).show();
                //Toast.makeText(myContext, "x from " + startX + " to " + (endX) + "\ny from " + startY + " to " + (endY), Toast.LENGTH_LONG).show();

                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }


    public static class ColorBall {

        Bitmap bitmap;
        Context mContext;
        Point point;
        int id;
        static int count = 0;

        public ColorBall(Context context, int resourceId, Point point) {
            this.id = count++;
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
    }
}