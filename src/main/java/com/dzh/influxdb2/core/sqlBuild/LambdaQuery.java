package com.dzh.influxdb2.core.sqlBuild;

import cn.hutool.core.util.StrUtil;
import com.dzh.influxdb2.core.common.LambdaUtils;
import com.dzh.influxdb2.core.sqlBuild.func.Compare;
import com.dzh.influxdb2.core.sqlBuild.func.SFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LambdaQuery<T> extends AbstractQuery<T, LambdaQuery<T>> {

    public LambdaQuery<T> filter(Compare fn) {
        String sql = fn.getSql();
        if (StrUtil.isEmpty(sql)) return this;
        super.filter(sql);
        return this.typedThis;
    }

    public LambdaQuery<T> group(SFunction<T>... fs) {
        List<String> columns = SFsToString(fs);
        super.group(columns);
        return this.typedThis;
    }

    public LambdaQuery<T> sort(Boolean desc, SFunction<T>... fs) {
        List<String> columns = SFsToString(fs);
        super.sort(columns, desc);
        return this.typedThis;
    }

    /**
     * id,name,...,time,
     *
     * @param fs
     * @return
     */
    @NotNull
    private List<String> SFsToString(SFunction<T>[] fs) {
        List<String> columns = new ArrayList<>();
        for (SFunction<T> f : fs) {
            columns.add(LambdaUtils.convertToFieldName(f));
        }
        return columns;
    }

    public LambdaQuery<T> count(SFunction<T> fs) {
        return super.count(LambdaUtils.convertToFieldName(fs));
    }
}
