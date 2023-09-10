package com.dzh.influxdb2.core.sqlBuild.func;

import com.dzh.influxdb2.core.sqlBuild.Isql;

public interface Compare<T> extends Isql {
    Compare<T> and();

    Compare<T> or();

    // ==
    Compare<T> eq(SFunction<T> sf, Object val);

    // !=
    Compare<T> ne(SFunction<T> sf, Object val);

    // <
    Compare<T> lt(SFunction<T> sf, Object val);

    // <=
    Compare<T> le(SFunction<T> sf, Object val);

    // >
    Compare<T> gt(SFunction<T> sf, Object val);

    // >=
    Compare<T> ge(SFunction<T> sf, Object val);
}
