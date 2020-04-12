@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.java.lang.typeClassOrNull
import leo14.Begin
import leo14.Literal
import leo14.bigDecimal
import leo14.lambda.runtime.Value
import leo14.untyped.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

data class Compiled(val type: Type, val expression: Expression)
data class CompiledLink(val lhs: Compiled, val line: CompiledLine)
data class CompiledFunction(val function: TypeFunction, val expression: Expression)
data class CompiledAnything(val expression: Expression)
data class CompiledLine(val typeLine: TypeLine, val expression: Expression)
data class CompiledField(val name: String, val rhs: Compiled)

infix fun Type.compiled(expression: Expression) = Compiled(this, expression)
infix fun Type.compiled(value: Value) = Compiled(this, expression(value))
infix fun Type.compiled(evaluate: Evaluate) = Compiled(this, expression(evaluate))
infix fun Compiled.linkTo(line: CompiledLine) = CompiledLink(this, line)
infix fun TypeFunction.compiled(expression: Expression) = CompiledFunction(this, expression)
infix fun TypeLine.compiled(expression: Expression) = CompiledLine(this, expression)
infix fun String.fieldTo(rhs: Compiled) = CompiledField(this, rhs)
fun anythingCompiled(expression: Expression) = CompiledAnything(expression)

inline fun Compiled.apply(rhs: Compiled, type: Type, crossinline fn: Value.(Value) -> Value): Compiled =
	type.compiled(expression.doApply(rhs.expression, fn))

val emptyCompiled: Compiled = emptyType.compiled(null)
val nothingCompiled = nothingType.compiled { null!! }
val Compiled.isEmpty get() = type.isEmpty
val Compiled.value: Value get() = expression.value

val String.compiled get() = textType.compiled(this)
val Int.compiled get() = numberType.compiled(bigDecimal)

val Compiled.evaluate: Compiled get() = type.compiled(expression.evaluate)
val Compiled.typed: Typed get() = type typed value

fun Compiled.apply(literal: Literal): Compiled =
	if (isEmpty) emptyType.plus(literal.valueTypeLine).compiled { literal.javaValue }
	else type.plus(literal.valueTypeLine).compiled { value to literal.javaValue }

fun Compiled.apply(begin: Begin, rhs: Compiled): Compiled =
	null
		?: applyFunctionApply(rhs)
		?: append(begin, rhs)

val Compiled.apply: Compiled
	get() =
		null
			?: applyListOf
			?: applyListPlus
			?: applyNativeNull
			?: applyArrayJavaList
			?: applyNativeClassNameText
			?: applyNativeClassPrimitive
			?: applyNativeClassField
			?: applyClassNativeConstructor
			?: applyClassNativeConstructorParameterList
			?: applyClassNativeMethod
			?: applyClassNativeMethodParameterList
			?: applyFieldNativeGet
			?: applyNativeConstructorInvoke
			?: applyNativeConstructorInvokeParameterList
			?: applyNativeMethodInvoke
			?: applyNativeMethodInvokeParameterList
			?: applyGet
			?: this

val Compiled.applyGet: Compiled?
	get() =
		type.linkOrNull?.onlyLineOrNull?.fieldOrNull?.let { field ->
			field.rhs.linkOrNull?.onlyLineOrNull?.fieldOrNull?.rhs?.let { rhs ->
				rhs.compiled(expression).select(field.name)
			}
		}

val Compiled.applyListOf: Compiled?
	get() =
		type.matchPrefix(listName) {
			matchPrefix(ofName) {
				matchStatic {
					// TODO: Represent array of static type as Int = array size.
					type(listName lineTo repeating.toType).compiled(null)
				}
			}
		}

val Compiled.applyListPlus: Compiled?
	get() =
		type.matchInfix(plusName) { rhs ->
			let { listType ->
				matchList {
					ifOrNull(this == rhs) {
						// TODO: Represent list with static items as Int = array size
						this@applyListPlus.linkApply(listType) { this to it }
					}
				}
			}
		}

val Compiled.applyNativeNull: Compiled?
	get() =
		type.matchPrefix(nativeName) {
			match(nullName) {
				javaType.compiled(null)
			}
		}

val Compiled.applyArrayJavaList: Compiled?
	get() =
		type.matchPrefix(arrayName) {
			matchPrefix(valueJavaName) {
				matchPrefix(listName) {
					matchRepeating {
						matchLine {
							type(arrayName lineTo javaType).compiled(expression.array)
						}
					}
				}
			}
		}

val Compiled.applyNativeConstructorInvoke: Compiled?
	get() =
		type.matchPrefix(invokeName) {
			matchPrefix(constructorName) {
				matchJava {
					javaType.compiled(expression.doApply { (this as Constructor<*>).newInstance() })
				}
			}
		}

val Compiled.applyNativeConstructorInvokeParameterList: Compiled?
	get() =
		type.matchInfix(invokeName) { rhs ->
			matchPrefix(constructorName) {
				matchJava {
					rhs.matchPrefix(parameterName) {
						matchPrefix(listName) {
							matchRepeating {
								matchJava {
									linkApply(javaType) { rhs ->
										rhs.valueListAsArray.let { array ->
											(this as Constructor<*>).newInstance(*array)
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Compiled.applyNativeMethodInvoke: Compiled?
	get() =
		type.matchInfix(invokeName) { rhs ->
			matchPrefix(methodName) {
				matchJava {
					rhs.matchJava {
						linkApply(javaType) { (this as Method).invoke(it) }
					}
				}
			}
		}

val Compiled.applyNativeMethodInvokeParameterList: Compiled?
	get() =
		type.matchInfix(invokeName) { rhs ->
			matchPrefix(methodName) {
				matchJava {
					rhs.matchInfix(parameterName) { parameter ->
						matchPrefix(objectName) {
							matchJava {
								parameter.matchList {
									matchJava {
										javaType.compiled(expression.doApply {
											this as Pair<*, *>
											val method = first as Method
											val rhs = second as Pair<*, *>
											val object_ = rhs.first
											val args = rhs.second.valueListAsArray
											method.invoke(object_, *args)
										})
									}
								}
							}
						}
					}
				}
			}
		}

val Compiled.applyNativeClassNameText: Compiled?
	get() =
		type.matchPrefix(nativeName) {
			matchPrefix(className) {
				matchPrefix(nameName) {
					matchText {
						type(className lineTo javaType).compiled(expression.doApply {
							asString.loadClass
						})
					}
				}
			}
		}

val Compiled.applyNativeClassPrimitive: Compiled?
	get() =
		type.matchPrefix(nativeName) {
			matchPrefix(className) {
				matchName {
					typeClassOrNull?.let { class_ ->
						type(className lineTo javaType).compiled(expression(class_))
					}
				}
			}
		}

val Compiled.applyNativeClassField: Compiled?
	get() =
		type.matchInfix(fieldName) { field ->
			matchPrefix(className) {
				matchJava {
					field.matchPrefix(nameName) {
						matchText {
							linkApply(type(fieldName lineTo javaType)) { name ->
								(this as Class<*>).getField(name as String)
							}
						}
					}
				}
			}
		}

val Compiled.applyClassNativeConstructor: Compiled?
	get() =
		type.matchPrefix(constructorName) {
			matchPrefix(className) {
				matchJava {
					type(constructorName lineTo javaType)
						.compiled(expression.doApply { (this as Class<*>).getConstructor() })
				}
			}
		}

val Compiled.applyClassNativeConstructorParameterList: Compiled?
	get() =
		type.matchInfix(constructorName) { rhs ->
			matchPrefix(className) {
				matchJava {
					rhs.matchPrefix(parameterName) {
						matchList {
							matchPrefix(className) {
								matchJava {
									linkApply(type(constructorName lineTo javaType)) { rhs ->
										rhs.valueListAsArray.let { array ->
											(this as Class<*>).getConstructor(*((array.toList() as List<Class<*>>).toTypedArray()))
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Compiled.applyClassNativeMethod: Compiled?
	get() =
		type.matchInfix(methodName) { rhs ->
			matchPrefix(className) {
				matchJava {
					rhs.matchPrefix(nameName) {
						matchText {
							linkApply(type(methodName lineTo javaType)) { name ->
								(this as Class<*>).getMethod(name as String)
							}
						}
					}
				}
			}
		}

val Compiled.applyClassNativeMethodParameterList: Compiled?
	get() =
		type.matchInfix(methodName) { rhs ->
			matchPrefix(className) {
				matchJava {
					rhs.matchInfix(parameterName) { parameter ->
						matchPrefix(nameName) {
							matchText {
								parameter.matchList {
									matchPrefix(className) {
										matchJava {
											type(methodName lineTo javaType).compiled(expression.doApply {
												this as Pair<*, *>
												val class_ = first as Class<*>
												val rhs = second as Pair<*, *>
												val name = rhs.first as String
												val types = (rhs.second.valueListAsArray.asArray.toList() as List<Class<*>>).toTypedArray()
												class_.getMethod(name, *types)
											})
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Compiled.applyFieldNativeGet: Compiled?
	get() =
		type.matchInfix(getName) { rhs ->
			matchPrefix(fieldName) {
				matchJava {
					rhs.matchJava {
						linkApply(javaType) { rhs ->
							(this as Field).get(rhs)
						}
					}
				}
			}
		}


fun Compiled.applyFunctionApply(rhs: Compiled): Compiled? =
	type.functionOrNull?.let { function ->
		notNullIf(function.from == rhs.type) {
			function.to.compiled(expression.invoke(rhs.expression))
		}
	}

fun Compiled.append(literal: Literal): Compiled =
	type.plus(literal.valueTypeLine).let { newType ->
		if (type.isEmpty) newType.compiled(literal.javaValue)
		else newType.compiled(expression.doApply(literal.expression) { this to it })
	}

fun Compiled.append(begin: Begin, rhs: Compiled): Compiled =
	type.plus(begin.string lineTo rhs.type).let { newType ->
		if (type.isStatic) newType.compiled(rhs.expression)
		else newType.compiled(expression.doApply(rhs.expression) { this to it })
	}

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type is EmptyType) { fn() }

fun Compiled.matchFunction(fn: (TypeFunction, Expression) -> Compiled?): Compiled? =
	(type as? FunctionType)?.function?.let { function -> fn(function, expression) }

fun Compiled.linkApply(targetType: Type, fn: Value.(Value) -> Value): Compiled? =
	type.linkOrNull?.let { link ->
		if (link.lhs.isStatic || link.line.isStatic)
			targetType.compiled(expression.doApply { fn(this) })
		else
			targetType.compiled(
				expression.doApply {
					(this as Pair<*, *>)
					first.fn(second)
				})
	}

val Compiled.linkOrNull: CompiledLink?
	get() =
		(type as? LinkType)?.link?.let { link ->
			if (link.lhs.isStatic || link.line.isStatic)
				link.lhs.compiled(expression) linkTo link.line.compiled(expression)
			else
				link.lhs.compiled(expression.doApply { (this as Pair<*, *>).first }) linkTo
					link.line.compiled(expression.doApply { (this as Pair<*, *>).second })
		}

val CompiledLine.fieldOrNull: CompiledField?
	get() =
		(typeLine as? FieldTypeLine)?.field?.let { field ->
			field.name fieldTo field.rhs.compiled(expression)
		}

fun Compiled.matchInfix(name: String, fn: Compiled.(Compiled) -> Compiled?): Compiled? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.name == name) {
				link.lhs.fn(field.rhs)
			}
		}
	}

fun Compiled.matchPrefix(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	matchInfix(name) { rhs ->
		matchEmpty {
			fn(rhs)
		}
	}

fun Compiled.matchNative(fn: Compiled.() -> Compiled?): Compiled? =
	ifOrNull(type == javaType) {
		fn()
	}

fun Compiled.get(name: String): Compiled? =
	linkOrNull?.let { link ->
		link.lhs.matchEmpty {
			link.line.fieldOrNull?.rhs?.select(name)
		}
	}

fun Compiled.select(name: String): Compiled? =
	linkOrNull?.select(name)

fun CompiledLink.select(name: String): Compiled? =
	line.select(name) ?: lhs.select(name)

fun CompiledLine.select(name: String): Compiled? =
	when (name) {
		nativeName ->
			notNullIf(typeLine == javaTypeLine) {
				javaType.compiled(expression)
			}
		functionName -> TODO()
		else -> fieldOrNull?.select(name)
	}

fun CompiledField.select(name: String): Compiled? =
	notNullIf(this.name == name) {
		emptyType.plus(name lineTo rhs.type).compiled(rhs.expression)
	}

fun Compiler.append(literal: Literal): Compiler =
	copy(compiled = compiled.append(literal))

val CompiledLine.rhsOrNull: Compiled?
	get() =
		typeLine.fieldOrNull?.let { field ->
			field.rhs.compiled(expression)
		}
