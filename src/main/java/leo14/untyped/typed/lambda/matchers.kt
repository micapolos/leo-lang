package leo14.untyped.typed.lambda

import leo.java.lang.typeClassOrNull
import leo14.lambda2.Term
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.javaName
import leo14.untyped.nameName

fun Typed.matchJavaClassName(classFn: Class<*>.() -> Term): Typed? =
	matchPrefix(javaName) {
		matchPrefix(className) {
			matchName {
				typeClassOrNull
					?.run { typed(className lineTo classFn().javaTyped) }
					?: this@matchJavaClassName
			}
		}
	}

fun Typed.matchJavaClassNameText(termFn: Term.() -> Term): Typed? =
	matchPrefix(javaName) {
		matchPrefix(className) {
			matchPrefix(nameName) {
				matchText {
					typed(className lineTo termFn().javaTyped)
				}
			}
		}
	}

fun Typed.matchJavaClassField(fn: Term.(Term) -> Term): Typed? =
	matchInfix(fieldName) { field ->
		matchPrefix(className) {
			matchNative {
				let { classTerm ->
					field.matchPrefix(nameName) {
						matchText {
							let { fieldNameTerm ->
								typed(fieldName lineTo classTerm.fn(fieldNameTerm).javaTyped)
							}
						}
					}
				}
			}
		}
	}
