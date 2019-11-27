package leo14.lambda

import leo14.native.Native
import leo14.native.nativeEvaluator

val Term<Native>.nativeValue: Value<Native> get() = value(nativeEvaluator)
val Term<Native>.nativeEval: Term<Native> get() = nativeValue.term
