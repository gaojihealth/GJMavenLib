package com.bin.david.form.data.format.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.utils.DensityUtils;
import com.bin.david.form.utils.DrawUtils;

import java.util.List;

/**
 * Created by huang on 2017/10/30.
 * 多行文字格式化
 */

public class MultiLineDrawFormat<T> extends TextDrawFormat<T> {

    private int width = 0;
    private int color = -1;
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private List<String> mDatas;
    private int mLeftPadding = 0;
    private boolean singleLeft = false;

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width) {
        this.width = width;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width, int color, List<String> datas) {
        this.width = width;
        this.color = color;
        this.mDatas = datas;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width, int color, List<String> datas, int leftPadding) {
        this.width = width;
        this.color = color;
        this.mDatas = datas;
        this.mLeftPadding = leftPadding;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width, int color, List<String> datas, int leftPadding, boolean singleLeft) {
        this.width = width;
        this.color = color;
        this.mDatas = datas;
        this.mLeftPadding = leftPadding;
        this.singleLeft = singleLeft;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width, int color, int leftPadding) {
        this.width = width;
        this.color = color;
        this.mLeftPadding = leftPadding;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param width 指定宽度 px
     */
    public MultiLineDrawFormat(int width, int color) {
        this.width = width;
        this.color = color;
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param dpWidth 指定宽度 dp
     */
    public MultiLineDrawFormat(Context context, int dpWidth) {
        this.width = DensityUtils.dp2px(context, dpWidth);
    }

    /**
     * 多行文字格式化构造方法
     *
     * @param dpWidth 指定宽度 dp
     */
    public MultiLineDrawFormat(Context context, int dpWidth, int color, List<String> datas) {
        this.width = DensityUtils.dp2px(context, dpWidth);
        this.color = color;
        this.mDatas = datas;
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {
        return width;
    }

    @Override
    public int measureHeight(Column<T> column, int position, TableConfig config) {
        config.getContentStyle().fillPaint(textPaint);
        StaticLayout sl = new StaticLayout(column.format(position), textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        return sl.getHeight();
    }

    @Override
    public void draw(Canvas c, Rect rect, CellInfo<T> cellInfo, TableConfig config) {
        setTextPaint(config, cellInfo, textPaint);
        if (mDatas != null && mDatas.size() > 0) {
            if (cellInfo.row < mDatas.size()) {
                if (mDatas.get(cellInfo.row) != null) {
                    if (!TextUtils.isEmpty(mDatas.get(cellInfo.row))) {
                        if (Integer.parseInt(mDatas.get(cellInfo.row)) != 2) {
                            textPaint.setColor(color);
                            setTextPaint(config, cellInfo, textPaint, color);
                        }
                    }
                }
            }
        }

        if (cellInfo.column.getTextAlign() != null) {
            textPaint.setTextAlign(cellInfo.column.getTextAlign());
        }
        int hPadding = (int) (config.getHorizontalPadding() * config.getZoom());
        int realWidth = rect.width() - 2 * hPadding - mLeftPadding * 2;
        StaticLayout staticLayout = new StaticLayout(cellInfo.column.format(cellInfo.row), textPaint, realWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        c.save();
        c.translate(DrawUtils.getTextCenterX(rect.left + mLeftPadding + hPadding, singleLeft ? (rect.right - hPadding) : (rect.right - mLeftPadding - hPadding), textPaint), rect.top + (rect.height() - staticLayout.getHeight()) / 2);
        staticLayout.draw(c);
        c.restore();
    }
}



