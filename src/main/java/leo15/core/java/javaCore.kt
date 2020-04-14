package leo15.core.java

import leo.base.notNullIf
import leo.java.lang.typeClassOrNull
import leo14.lambda2.valueApply
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.javaName
import leo14.untyped.nameName
import leo14.untyped.typed.bitType
import leo14.untyped.typed.javaType
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.type
import leo15.*

class JavaCore(
	val bitIntFn: Any?.() -> Any?,
	val typeClassFn: String.() -> Any?,
	val textClassFn: Any?.() -> Any?,
	val classFieldFn: Any?.(Any?) -> Any?)

fun JavaCore.apply(typed: Typed): Typed? =
	null
		?: applyTypeClass(typed)
		?: applyTextClass(typed)
		?: applyClassField(typed)

fun JavaCore.applyBitNative(typed: Typed): Typed? =
	notNullIf(typed.type == bitType) {
		type("bit" lineTo javaType).typed(typed.term.valueApply(bitIntFn))
	}

fun JavaCore.applyTypeClass(typed: Typed): Typed? =
	typed.matchPrefix(javaName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					leo15.typed(className lineTo typeClassFn().valueJavaTyped)
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
						leo15.typed(className lineTo nameTerm.valueApply(textClassFn).javaTyped)
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
								leo15.typed(fieldName lineTo classTerm.valueApply(nameTerm, classFieldFn).javaTyped)
							}
						}
					}
				}
			}
		}
	}
