package com.dzh.influxdb2.core.sqlBuild.func;

import com.dzh.influxdb2.core.sqlBuild.Isql;

public interface Compare<T, Children extends Compare<T, Children>> extends Isql {
    Children and();

    Children or();

    // ==
    Children eq(SFunction<T> sf, Object val);

    // !=
    Children ne(SFunction<T> sf, Object val);

    // <
    Children lt(SFunction<T> sf, Object val);

    // <=
    Children le(SFunction<T> sf, Object val);

    // >
    Children gt(SFunction<T> sf, Object val);

    // >=
    Children ge(SFunction<T> sf, Object val);
}
