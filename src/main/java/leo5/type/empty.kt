package leo5.type

import leo.base.Empty
import leo5.Value
import leo5.empty
import leo5.script

fun Empty.compile(value: Value) = value.script.empty