package leo14.untyped.typed

import leo13.stack
import leo14.untyped.className
import leo14.untyped.constructorName
import leo14.untyped.nativeName

val Compiled<*>.applyNativeClass: Compiled<*>?
	get() =
		matchPrefix(className) { rhs ->
			rhs.matchPrefix(nativeName) { rhs ->
				rhs.matchText {
					emptyType
						.plus(className lineTo nativeType)
						.compiled(expression.doApply { stringClass })
				}
			}
		}

val Compiled<*>.applyClassConstructor: Compiled<*>?
	get() =
		matchPrefix(constructorName) { rhs ->
			rhs.matchPrefix(className) { rhs ->
				rhs.matchNative {
					emptyType
						.plus(constructorName lineTo nativeType)
						.compiled(expression.doApply { classConstructor(stack<Any?>()) })
				}
			}
		}

