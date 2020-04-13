package leo14.untyped.typed.lambda

import leo14.bigDecimal
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.lambda2.value
import leo14.lambda2.valueTerm
import leo14.untyped.*
import leo14.untyped.typed.javaType
import leo14.untyped.typed.numberType
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.math.BigDecimal

val Typed.javaApply: Typed?
	get() =
		null
			?: applyJavaIntNumber
			?: applyNumberIntJava
			?: applyJavaClassName
			?: applyJavaClassNameText
			?: applyJavaClassField
			?: applyJavaClassConstructor
			?: applyJavaClassMethod
			?: applyJavaFieldGet
			?: applyJavaConstructorInvoke
			?: applyJavaMethodInvoke

val Typed.applyJavaIntNumber: Typed?
	get() =
		matchPrefix(javaName) {
			matchPrefix(intName) {
				matchNumber {
					javaType.typed(apply { (value as BigDecimal).intValueExact().valueTerm })
				}
			}
		}

val Typed.applyNumberIntJava: Typed?
	get() =
		matchPrefix(numberName) {
			matchPrefix(intName) {
				matchJava {
					numberType.typed(apply { (value as Int).bigDecimal.valueTerm })
				}
			}
		}

val Typed.applyJavaClassName: Typed?
	get() =
		matchJavaClassName { valueTerm }

val Typed.applyJavaClassNameText: Typed?
	get() =
		matchJavaClassNameText { javaStringClassTerm }

val Typed.applyJavaClassField: Typed?
	get() =
		matchJavaClassField { javaClassField(it) }

val Typed.applyJavaClassConstructor: Typed?
	get() =
		matchInfix(constructorName) { rhs ->
			matchPrefix(className) {
				matchJava {
					let { classTerm ->
						rhs.matchPrefix("parameters") {
							javaArrayOrNull<Class<*>>(javaClassParameterTypeLine)?.let { parameters ->
								javaConstructorType.typed(
									fn { classTerm ->
										fn { parameters ->
											(classTerm.value as Class<*>)
												.getConstructor(*parameters.value as Array<Class<*>>).valueTerm
										}
									}.invoke(classTerm).invoke(parameters.term))
							}
						}
					}
				}
			}
		}

val Typed.applyJavaClassMethod: Typed?
	get() =
		matchInfixOrPrefix(methodName) { rhs ->
			matchPrefix(className) {
				matchJava {
					let { class_ ->
						rhs.matchInfix("parameters") { parameters ->
							matchPrefix(nameName) {
								matchText {
									let { name ->
										parameters.javaArrayOrNull<Class<*>>(javaClassParameterTypeLine)?.let { parameters ->
											javaMethodType.typed(
												fn { class_ ->
													fn { name ->
														fn { parameters ->
															(class_.value as Class<*>)
																.getMethod(name.value as String, *parameters.value as Array<Class<*>>).valueTerm
														}
													}
												}.invoke(class_).invoke(name).invoke(parameters.term))
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Typed.applyJavaConstructorInvoke: Typed?
	get() =
		matchInfixOrPrefix(invokeName) { rhs ->
			matchPrefix(constructorName) {
				matchJava {
					let { constructorTerm ->
						rhs.matchPrefix("parameters") {
							javaArrayOrNull<Any?>(javaParameterTypeLine)?.let { parameters ->
								javaType.typed(
									fn { constructorTerm ->
										fn { parameters ->
											(constructorTerm.value as Constructor<*>)
												.newInstance(*(parameters.value as Array<*>)).valueTerm
										}
									}.invoke(constructorTerm).invoke(parameters.term))
							}
						}
					}
				}
			}
		}

val Typed.applyJavaFieldGet: Typed?
	get() =
		matchInfix(getName) { rhs ->
			matchPrefix(fieldName) {
				matchJava {
					let { field ->
						rhs.matchJava {
							let { obj ->
								javaType.typed(
									fn { field ->
										fn { obj ->
											(field.value as Field).get(obj.value).valueTerm
										}
									}.invoke(field).invoke(obj)
								)
							}
						}
					}
				}
			}
		}

val Typed.applyJavaMethodInvoke: Typed?
	get() =
		matchInfix(invokeName) { rhs ->
			matchPrefix(methodName) {
				matchJava {
					let { method ->
						rhs.matchInfix("parameters") { parameters ->
							matchPrefix(objectName) {
								matchJava {
									let { object_ ->
										parameters.javaArrayOrNull<Any?>(javaParameterTypeLine)?.let { parameters ->
											javaType.typed(
												fn { method ->
													fn { object_ ->
														fn { parameters ->
															(method.value as Method)
																.invoke(object_.value, *(parameters.value as Array<*>)).valueTerm
														}
													}
												}.invoke(method).invoke(object_).invoke(parameters.term))
										}
									}
								}
							}
						}
					}
				}
			}
		}

