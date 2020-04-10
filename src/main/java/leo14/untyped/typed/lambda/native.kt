package leo14.untyped.typed.lambda

import leo.java.lang.typeClassOrNull
import leo14.lambda2.value
import leo14.untyped.className
import leo14.untyped.nativeName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.type

val Compiled.applyNativeClassName: Compiled?
	get() =
		matchPrefix(nativeName) {
			matchPrefix(className) {
				matchName {
					typeClassOrNull?.let { class_ ->
						type(className lineTo nativeType).compiled(value(class_))
					}
				}
			}
		}