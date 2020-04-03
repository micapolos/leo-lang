package leo14.lambda.runtime

import leo14.lambda.runtime.internal._invoke

inline fun fn(noinline fn: (Any?) -> Any?): Any? = fn
inline operator fun Any?.invoke(any: Any?): Any? =
	(this as (Any?) -> Any)._invoke(any)
