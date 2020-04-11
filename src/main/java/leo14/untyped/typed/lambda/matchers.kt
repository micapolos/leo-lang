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

fun Compiled.matchNativeClassName(classFn: Class<*>.() -> Term): Compiled? =
	matchPrefix(nativeName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull?.run {
					type(className lineTo nativeType).compiled(classFn())
				}
			}
		}
	}

fun Compiled.matchNativeClassNameText(termFn: Term.() -> Term): Compiled? =
	matchPrefix(nativeName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					compiled(className lineTo termFn().nativeCompiled)
				}
			}
		}
	}

fun Compiled.matchNativeClassField(fn: Term.(Term) -> Term): Compiled? =
	matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchNative {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { fieldNameTerm ->
								compiled(fieldName lineTo classTerm.fn(fieldNameTerm).nativeCompiled)
							}
						}
					}
				}
			}
		}
	}
