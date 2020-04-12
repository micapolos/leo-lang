package leo14.untyped.typed.lambda.core.java

import leo.base.notNullIf
import leo.java.lang.typeClassOrNull
import leo14.lambda2.valueApply
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.bitType
import leo14.untyped.typed.lambda.*
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.type

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
		type("bit" lineTo nativeType).typed(typed.term.valueApply(bitIntFn))
	}

fun JavaCore.applyTypeClass(typed: Typed): Typed? =
	typed.matchPrefix(nativeName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					typed(className lineTo typeClassFn().nativeTyped)
				}
			}
		}
	}

fun JavaCore.applyTextClass(typed: Typed): Typed? =
	typed.matchPrefix(nativeName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					let { nameTerm ->
						typed(className lineTo nameTerm.valueApply(textClassFn).nativeTyped)
					}
				}
			}
		}
	}

fun JavaCore.applyClassField(typed: Typed): Typed? =
	typed.matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchNative {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { nameTerm ->
								typed(fieldName lineTo classTerm.valueApply(nameTerm, classFieldFn).nativeTyped)
							}
						}
					}
				}
			}
		}
	}
