package leo14.untyped.typed

import leo.base.fold
import leo14.lambda.runtime.Value

fun eval(vararg values: Value) = nullValue.fold(values) { valueApply(it) }
operator fun String.invoke(vararg values: Value) = this fieldTo eval(*values)
