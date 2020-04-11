package leo14.untyped.typed.lambda.core.java

import leo.base.notNullIf
import leo.java.lang.typeClassOrNull
import leo14.lambda2.valueApply
import leo14.lambda2.valueTerm
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

fun JavaCore.apply(compiled: Compiled): Compiled? =
	null
		?: applyTypeClass(compiled)
		?: applyTextClass(compiled)
		?: applyClassField(compiled)

fun JavaCore.applyBitNative(compiled: Compiled): Compiled? =
	notNullIf(compiled.type == bitType) {
		type("bit" lineTo nativeType).compiled(compiled.term.valueApply(bitIntFn))
	}

fun JavaCore.applyTypeClass(compiled: Compiled): Compiled? =
	compiled.matchPrefix(nativeName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					type(className lineTo nativeType).compiled(typeClassFn().valueTerm)
				}
			}
		}
	}

fun JavaCore.applyTextClass(compiled: Compiled): Compiled? =
	compiled.matchPrefix(nativeName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					let { nameTerm ->
						type(className lineTo nativeType).compiled(nameTerm.valueApply(textClassFn))
					}
				}
			}
		}
	}

fun JavaCore.applyClassField(compiled: Compiled): Compiled? =
	compiled.matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchNative {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { nameTerm ->
								type(fieldName lineTo nativeType).compiled(classTerm.valueApply(nameTerm, classFieldFn))
							}
						}
					}
				}
			}
		}
	}
