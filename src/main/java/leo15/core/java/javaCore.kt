package leo15.core.java

import leo.java.lang.typeClassOrNull
import leo14.lambda2.valueApply
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.javaName
import leo14.untyped.nameName
import leo15.*

class JavaCore(
	val typeClassFn: String.() -> Any?,
	val textClassFn: Any?.() -> Any?,
	val classFieldFn: Any?.(Any?) -> Any?)

fun JavaCore.apply(typed: Typed): Typed? =
	null
		?: applyTypeClass(typed)
		?: applyTextClass(typed)
		?: applyClassField(typed)

fun JavaCore.applyTypeClass(typed: Typed): Typed? =
	typed.matchPrefix(javaName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					typed(className lineTo typeClassFn().valueJavaTyped)
				}
			}
		}
	}

fun JavaCore.applyTextClass(typed: Typed): Typed? =
	typed.matchPrefix(javaName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					let { nameTerm ->
						typed(className lineTo nameTerm.valueApply(textClassFn).javaTyped)
					}
				}
			}
		}
	}

fun JavaCore.applyClassField(typed: Typed): Typed? =
	typed.matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchJava {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { nameTerm ->
								typed(fieldName lineTo classTerm.valueApply(nameTerm, classFieldFn).javaTyped)
							}
						}
					}
				}
			}
		}
	}
