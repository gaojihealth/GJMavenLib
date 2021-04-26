package com.bin.david.form.data.format.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.utils.DrawUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huang on 2017/10/30.
 */

public class TextWithColorDrawFormatNew<T> implements IDrawFormat<T> {

    private Map<String, SoftReference<String[]>> valueMap; //避免产生大量对象
    private int[] color;
    private boolean setColor = false;
    private int mWidth = -1;
    private String[][] mStatus;

    public TextWithColorDrawFormatNew() {
        valueMap = new HashMap<>();
    }

    public TextWithColorDrawFormatNew(int[] color) {
        valueMap = new HashMap<>();
        this.color = color;
        setColor = true;
    }

    public TextWithColorDrawFormatNew(int[] color, String[][] status) {
        valueMap = new HashMap<>();
        this.color = color;
        setColor = true;
        mStatus = status;
    }

    public TextWithColorDrawFormatNew(int width, int[] color) {
        valueMap = new HashMap<>();
        this.color = color;
        setColor = true;
        mWidth = width;
    }

    public TextWithColorDrawFormatNew(int width, int[] color, String[][] status) {
        valueMap = new HashMap<>();
        this.color = color;
        setColor = true;
        mWidth = width;
        mStatus = status;
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return mWidth > 0 && mWidth > DrawUtils.getMultiTextWidth(paint, getSplitString(column.format(position))) ? mWidth : DrawUtils.getMultiTextWidth(paint, getSplitString(column.format(position)));
    }


    @Override
    public int measureHeight(Column<T> column, int position, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return DrawUtils.getMultiTextHeight(paint, getSplitString(column.format(position)));
    }

    @Override
    public void draw(Canvas c, Rect rect, CellInfo<T> cellInfo, TableConfig config) {
        Paint paint = config.getPaint();
        setTextPaint(config, cellInfo, paint);
        if (cellInfo.column.getTextAlign() != null) {
            paint.setTextAlign(cellInfo.column.getTextAlign());
        }
        drawText(c, cellInfo.value, rect, paint, cellInfo);
    }

    protected void drawText(Canvas c, String value, Rect rect, Paint paint, CellInfo<T> cellInfo) {
        if (setColor)
            DrawUtils.drawMultiTextWithColor2(c, paint, rect, getSplitString(value), color, cellInfo, mStatus);
        else
            DrawUtils.drawMultiText(c, paint, rect, getSplitString(value));
    }


    public void setTextPaint(TableConfig config, CellInfo<T> cellInfo, Paint paint) {
        config.getContentStyle().fillPaint(paint);
        ICellBackgroundFormat<CellInfo> backgroundFormat = config.getContentCellBackgroundFormat();
        if (backgroundFormat != null && backgroundFormat.getTextColor(cellInfo) != TableConfig.INVALID_COLOR) {
            paint.setColor(backgroundFormat.getTextColor(cellInfo));
        }
        paint.setTextSize(paint.getTextSize() * config.getZoom());
    }

    protected String[] getSplitString(String val) {
        String[] values = null;
        if (valueMap.get(val) != null) {
            values = valueMap.get(val).get();
        }
        if (values == null) {
            values = val.split("\n");

            valueMap.put(val, new SoftReference<>(values));
        }
        return values;
    }
}
