package leo15

import leo.java.lang.typeClassOrNull
import leo15.lambda.Term

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
			matchJava {
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
