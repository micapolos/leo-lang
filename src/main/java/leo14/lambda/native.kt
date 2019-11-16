package leo14.lambda

import leo14.native.Native
import leo14.native.invoke

val Term<Native>.nativeValue: Value<Native> get() = value { invoke(it) }
val Term<Native>.nativeEval: Term<Native> get() = nativeValue.evalTerm
