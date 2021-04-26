package com.bin.david.form.data.format.title;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

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

public class MTitleDrawFormat implements ITitleDrawFormat {

    private boolean isDrawBg;
    private Map<String, SoftReference<String[]>> valueMap; //避免产生大量对象

    public MTitleDrawFormat() {
        valueMap = new HashMap<>();
    }

    @Override
    public int measureWidth(Column column, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        if (TextUtils.isEmpty(column.getParentColumnName())) {
            int width = DrawUtils.getMultiTextWidth(paint, getSplitString(TextUtils.isEmpty(column.getColumnName()) ? "" : column.getColumnName()));
            if (column.isFixedWidth())//判断是否是名称列
                return Math.max(width, column.getFixedWidth());
            else
                return width;
        } else {
            int parentWdith = DrawUtils.getMultiTextWidth(paint, getSplitString(TextUtils.isEmpty(column.getParentColumnName()) ? "" : column.getParentColumnName()));
            int childWdith = DrawUtils.getMultiTextWidth(paint, getSplitString(TextUtils.isEmpty(column.getColumnName()) ? "" : column.getColumnName()));
            if (column.isFixedWidth())//判断是否是名称列
                return Math.max(Math.max(parentWdith, childWdith), column.getFixedWidth());
            else
                return Math.max(parentWdith, childWdith);
        }
    }

    @Override
    public int measureHeight(Column column, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return DrawUtils.getMultiTextHeight(paint, getSplitString(column.getColumnName()));
    }

    @Override
    public void draw(Canvas c, Column column, Rect rect, TableConfig config) {
        Paint paint = config.getPaint();
        boolean isDrawBg = drawBackground(c, column, rect, config);
        config.getColumnTitleStyle().fillPaint(paint);
        ICellBackgroundFormat<Column> backgroundFormat = config.getColumnCellBackgroundFormat();
        paint.setTextSize(paint.getTextSize() * config.getZoom());
        if (isDrawBg && backgroundFormat.getTextColor(column) != TableConfig.INVALID_COLOR) {
            paint.setColor(backgroundFormat.getTextColor(column));
        }
        drawText(c, column, rect, paint);
    }

    public void draw2(Canvas c, Column column, Rect rect, TableConfig config) {
        Paint paint = config.getPaint();
        boolean isDrawBg = drawBackground(c, column, rect, config);
        config.getColumnTitleStyle().fillPaint(paint);
        ICellBackgroundFormat<Column> backgroundFormat = config.getColumnCellBackgroundFormat();
        paint.setTextSize(paint.getTextSize() * config.getZoom());
        if (isDrawBg && backgroundFormat.getTextColor(column) != TableConfig.INVALID_COLOR) {
            paint.setColor(backgroundFormat.getTextColor(column));
        }
        drawText2(c, column, rect, paint);
    }

    private void drawText(Canvas c, Column column, Rect rect, Paint paint) {
        if (column.getTitleAlign() != null) { //如果列设置Align ，则使用列的Align
            paint.setTextAlign(column.getTitleAlign());
        }
//        c.drawText(column.getColumnName(), DrawUtils.getTextCenterX(rect.left, rect.right, paint), DrawUtils.getTextCenterY((rect.bottom + rect.top) / 2, paint), paint);
        drawText(c, column.getColumnName(), rect, paint);
    }

    private void drawText2(Canvas c, Column column, Rect rect, Paint paint) {
        if (column.getTitleAlign() != null) { //如果列设置Align ，则使用列的Align
            paint.setTextAlign(column.getTitleAlign());
        }
//        c.drawText(column.getColumnName(), DrawUtils.getTextCenterX(rect.left, rect.right, paint), DrawUtils.getTextCenterY((rect.bottom + rect.top) / 2, paint), paint);
        drawText2(c, column.getColumnName(), rect, paint);
    }

    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
        DrawUtils.drawMultiText(c, paint, rect, getSplitString(value));
    }

    protected void drawText2(Canvas c, String value, Rect rect, Paint paint) {
        DrawUtils.drawMultiText2(c, paint, rect, getSplitString(value));
    }

    public boolean drawBackground(Canvas c, Column column, Rect rect, TableConfig config) {
        ICellBackgroundFormat<Column> backgroundFormat = config.getColumnCellBackgroundFormat();
        if (isDrawBg && backgroundFormat != null) {
            backgroundFormat.drawBackground(c, rect, column, config.getPaint());
            return true;
        }
        return false;
    }

    protected String[] getSplitString(String val) {
        String[] values = null;
        if (val != null) {
            if (valueMap.get(val) != null) {
                values = valueMap.get(val).get();
            }
            if (values == null) {
                values = val.split("\n");
                valueMap.put(val, new SoftReference<>(values));
            }
        } else {
            values = new String[]{""};
        }
        return values;
    }

    public boolean isDrawBg() {
        return isDrawBg;
    }

    public void setDrawBg(boolean drawBg) {
        isDrawBg = drawBg;
    }
}
