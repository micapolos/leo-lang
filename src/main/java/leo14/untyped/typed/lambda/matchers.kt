package leo14.untyped.typed.lambda

import leo.java.lang.typeClassOrNull
import leo14.lambda2.Term
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.type

fun Typed.matchNativeClassName(classFn: Class<*>.() -> Term): Typed? =
	matchPrefix(nativeName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					type(className lineTo nativeType).typed(classFn())
				}
			}
		}
	}

fun Typed.matchNativeClassNameText(termFn: Term.() -> Term): Typed? =
	matchPrefix(nativeName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					typed(className lineTo termFn().nativeTyped)
				}
			}
		}
	}

fun Typed.matchNativeClassField(fn: Term.(Term) -> Term): Typed? =
	matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchNative {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { fieldNameTerm ->
								typed(fieldName lineTo classTerm.fn(fieldNameTerm).nativeTyped)
							}
						}
					}
				}
			}
		}
	}
