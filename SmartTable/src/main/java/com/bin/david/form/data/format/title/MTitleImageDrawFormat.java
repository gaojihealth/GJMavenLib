package com.bin.david.form.data.format.title;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.R;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.exception.TableException;

/**
 * Created by huang on 2017/10/30.
 */

public abstract class MTitleImageDrawFormat extends ImageResTitleDrawFormat {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private MTitleDrawFormat textDrawFormat;
    private int drawPadding;
    private int direction;
    private int verticalPadding;
    private int horizontalPadding;
    private boolean isDouble = false;
    private Rect rect;

    public MTitleImageDrawFormat(int imageWidth, int imageHeight, int drawPadding) {
        this(imageWidth, imageHeight, LEFT, drawPadding);

    }

    public MTitleImageDrawFormat(int imageWidth, int imageHeight, int direction, int drawPadding) {
        super(imageWidth, imageHeight);
        textDrawFormat = new MTitleDrawFormat();
        this.direction = direction;
        this.drawPadding = drawPadding;
        if (direction > BOTTOM || direction < LEFT) {
            throw new TableException("Please set the direction less than 3 greater than 0");
        }
        rect = new Rect();
    }

    public MTitleImageDrawFormat(int imageWidth, int imageHeight, int direction, int drawPadding, boolean isDouble) {
        super(imageWidth, imageHeight);
        textDrawFormat = new MTitleDrawFormat();
        this.direction = direction;
        this.drawPadding = drawPadding;
        if (direction > BOTTOM || direction < LEFT) {
            throw new TableException("Please set the direction less than 3 greater than 0");
        }
        rect = new Rect();
        this.isDouble = isDouble;
    }

    @Override
    public int measureWidth(Column column, TableConfig config) {
        int textWidth = textDrawFormat.measureWidth(column, config);
        horizontalPadding = config.getColumnTitleHorizontalPadding();
        if (direction == LEFT || direction == RIGHT) {
            return getImageWidth() + textWidth + drawPadding;
        } else {
            return Math.max(super.measureWidth(column, config), textWidth);
        }
    }

    @Override
    public int measureHeight(Column column, TableConfig config) {
        int imgHeight = super.measureHeight(column, config);
        int textHeight = textDrawFormat.measureHeight(column, config);
        verticalPadding = config.getColumnTitleVerticalPadding();
        if (direction == TOP || direction == BOTTOM) {
            return getImageHeight() + textHeight + drawPadding;
        } else {
            return Math.max(imgHeight, textHeight);
        }
    }

    @Override
    public void draw(Canvas c, Column column, Rect rect, TableConfig config) {
        if (config.getTableTitleBgColors() != null && config.getTableTitleBgColors().length > 1) {
            if (!column.isParent() && isDouble) {
                Paint paint = config.getPaint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(config.getTableTitleBgColors()[0]);
                Rect wRect = new Rect(rect.left, rect.top, rect.right - 1, rect.bottom);
                c.drawRect(wRect, paint);
            } else {
                Paint paint = config.getPaint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(config.getTableTitleBgColors()[1]);
                Rect wRect = new Rect(rect.left, rect.top, rect.right - 1, rect.bottom);
                c.drawRect(wRect, paint);
            }
        } else {
            if (!column.isParent() && isDouble) {
                Paint paint = config.getPaint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(getContext().getResources().getColor(R.color.smart_color_ccffffff));
                Rect wRect = new Rect(rect.left, rect.top, rect.right - 1, rect.bottom);
                c.drawRect(wRect, paint);
            } else {
                setDrawBackground(true);
                drawBackground(c, column, rect, config);
            }
        }
        setDrawBackground(false);
        textDrawFormat.setDrawBg(false);
        if (getBitmap(column) == null) {
//            textDrawFormat.draw(c, column, rect, config);
            textDrawFormat.draw2(c, column, rect, config);
            return;
        }

        int width, imgLeft, imgRight, textWidth, height, imgTop, imgBottom, textHeight;
        switch (direction) {
            case LEFT:
                width = (int) (measureWidth(column, config) * config.getZoom());
                imgLeft = rect.left + (rect.right - rect.left - width) / 2;
                imgRight = (int) (imgLeft + getImageWidth() * config.getZoom());
                this.rect.set(imgLeft, rect.top, imgRight, rect.bottom);
                super.draw(c, column, this.rect, config);
                textWidth = (int) (textDrawFormat.measureWidth(column, config) * config.getZoom());
                this.rect.set(imgRight + drawPadding, rect.top, imgRight + drawPadding + textWidth, rect.bottom);
                textDrawFormat.draw(c, column, this.rect, config);
                break;
            case RIGHT:
                width = (int) (measureWidth(column, config) * config.getZoom());
                imgRight = rect.right - (rect.right - rect.left - width) / 2;
                imgLeft = (int) (imgRight - getImageWidth() * config.getZoom());
                this.rect.set(imgLeft, rect.top, imgRight, rect.bottom);
                super.draw(c, column, this.rect, config);
                textWidth = (int) (textDrawFormat.measureWidth(column, config) * config.getZoom());
                this.rect.set(imgLeft - drawPadding - textWidth, rect.top, imgLeft - drawPadding, rect.bottom);
                textDrawFormat.draw(c, column, this.rect, config);

                break;
            case TOP:
                height = (int) (measureHeight(column, config) * config.getZoom());
                imgTop = rect.top + (rect.top - rect.bottom - height) / 2;
                imgBottom = (int) (imgTop + getImageHeight() * config.getZoom());
                this.rect.set(rect.left, imgTop, rect.right, imgBottom);
                textDrawFormat.draw(c, column, this.rect, config);
                textHeight = (int) (textDrawFormat.measureHeight(column, config) * config.getZoom());
                this.rect.set(rect.left, imgBottom + drawPadding, rect.right, imgBottom + drawPadding + textHeight);
                super.draw(c, column, this.rect, config);
                break;
            case BOTTOM:
                height = (int) (measureHeight(column, config) * config.getZoom());
                imgBottom = rect.bottom - (rect.bottom - rect.top - height) / 2;
                imgTop = (int) (imgBottom - getImageHeight() * config.getZoom());
                this.rect.set(rect.left, imgTop, rect.right, imgBottom);
                textDrawFormat.draw(c, column, this.rect, config);
                textHeight = (int) (textDrawFormat.measureHeight(column, config) * config.getZoom());
                this.rect.set(rect.left, imgTop - drawPadding - textHeight, rect.right, imgTop - drawPadding);
                super.draw(c, column, this.rect, config);
                break;

        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
