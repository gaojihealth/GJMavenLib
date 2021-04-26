package com.bin.david.form.data.format.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.bin.david.form.R;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.utils.DensityUtils;
import com.bin.david.form.utils.DrawUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huang on 2017/10/30.
 */

public class FirTextDrawFormat<T> implements IDrawFormat<T> {
    private Context mContext;
    private int firColumnWdith = -1;
    private Map<String, SoftReference<String[]>> valueMap; //避免产生大量对象

    public FirTextDrawFormat(Context context, int firColumnWdith) {
        mContext = context;
        valueMap = new HashMap<>();
        this.firColumnWdith = firColumnWdith;
    }

    public FirTextDrawFormat(Context context) {
        mContext = context;
        valueMap = new HashMap<>();
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {
        if (firColumnWdith <= 0) {
            Paint paint = config.getPaint();
            config.getContentStyle().fillPaint(paint);
            return DrawUtils.getMultiTextWidth(paint, getSplitString(column.format(position)));
        } else
            return firColumnWdith;
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
        int rectsize = DensityUtils.dp2px(mContext, 8);//
        float rectRoundSize = DensityUtils.dp2px(mContext, 4);//设置圆角矩形大小
        int fontSize = DensityUtils.dp2px(mContext, 12);//设置字体颜色
        RectF paintRect = new RectF(rect.centerX() - rectsize, rect.centerY() - rectsize, rect.centerX() + rectsize, rect.centerY() + rectsize);
        int rectColor, paintColor;
        switch (cellInfo.value) {
            case "0":
                rectColor = R.color.smart_transparent;
                paintColor = R.color.smart_transparent;
                break;
            case "1":
                rectColor = R.color.smart_home_color;
                paintColor = R.color.smart_color_ffffff;
                break;
            case "2":
                rectColor = R.color.smart_color_FB8C54;
                paintColor = R.color.smart_color_ffffff;
                break;
            case "3":
                rectColor = R.color.smart_color_DDB674;
                paintColor = R.color.smart_color_ffffff;
                break;
            case "4":
                rectColor = R.color.smart_color_1990FF;
                paintColor = R.color.smart_color_ffffff;
                break;
            case "5":
                rectColor = R.color.smart_color_1990FF;
                paintColor = R.color.smart_color_ffffff;
                break;
            default:
                rectColor = R.color.smart_transparent;
                paintColor = R.color.smart_color_333333;
                break;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mContext.getResources().getColor(rectColor));
        c.drawRoundRect(paintRect, rectRoundSize, rectRoundSize, paint);

        paint.setColor(mContext.getResources().getColor(paintColor));
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (paintRect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        c.drawText(cellInfo.value, paintRect.centerX(), baseLineY, paint);
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
