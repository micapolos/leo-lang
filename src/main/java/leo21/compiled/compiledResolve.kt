package leo21.compiled

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.leonardoScript
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.token.processor.staticCompiled
import leo21.type.Type
import leo21.type.isEmpty
import leo21.type.lineTo
import leo21.type.numberType
import leo21.type.plus
import leo21.type.script
import leo21.type.stringType
import leo21.type.type

val Compiled.resolveGetOrNull: Compiled?
	get() =
		linkOrNull?.run {
			head.fieldCompiledOrNull?.let { fieldTyped ->
				ifOrNull(fieldTyped.rhsCompiled.type == type()) {
					tail.getOrNull(fieldTyped.field.name)
				}
			}
		}

val Compiled.resolveMakeOrNull: Compiled?
	get() =
		linkOrNull?.run {
			head.fieldCompiledOrNull?.let { fieldTyped ->
				ifOrNull(fieldTyped.rhsCompiled.type == type()) {
					tail.make(fieldTyped.field.name)
				}
			}
		}

val Compiled.resolve: Compiled
	get() =
		resolveOrNull ?: this

val Type.fn2Compiled: Compiled get() = fn(arg<Prim>(0)) of this

val Compiled.resolveLeonardoOrNull: Compiled?
	get() =
		notNullIf(type == type("leonardo" lineTo type())) {
			leonardoScript.staticCompiled
		}
val Compiled.resolveOrNull: Compiled?
	get() =
		null
			?: resolveGetOrNull
			?: resolveAsOrNull
			?: resolveLeonardoOrNull
			?: resolveFn1OrNull(numberType, "sinus", NumberSinusPrim, numberType)
			?: resolveFn1OrNull(numberType, "cosinus", NumberCosinusPrim, numberType)
			?: resolveFn2OrNull(numberType, "plus", numberType, NumberPlusNumberPrim, numberType)
			?: resolveFn2OrNull(numberType, "minus", numberType, NumberMinusNumberPrim, numberType)
			?: resolveFn2OrNull(numberType, "times", numberType, NumberTimesNumberPrim, numberType)
			?: resolveFn2OrNull(stringType, "plus", stringType, StringPlusStringPrim, stringType)
			?: resolveTypeOrNull
			?: resolveMakeOrNull

fun Compiled.resolveFn1OrNull(lhs: Type, name: String, prim: Prim, result: Type): Compiled? =
	notNullIf(type == lhs.plus(name lineTo type())) {
		fn(nativeTerm(prim).invoke(arg(0))).invoke(term).of(result)
	}

fun Compiled.resolveFn2OrNull(lhs: Type, name: String, rhs: Type, prim: Prim, result: Type): Compiled? =
	notNullIf(type == lhs.plus(name lineTo rhs)) {
		nativeTerm(prim).invoke(term).of(result)
	}

val Compiled.resolveAsOrNull: Compiled?
	get() =
		linkOrNull?.let { link ->
			link.head.fieldCompiledOrNull?.let { field ->
				ifOrNull(field.field.name == "as") {
					field.rhsCompiled.linkOrNull?.let { rhsLink ->
						ifOrNull(rhsLink.tail.type == type()) {
							rhsLink.head.fieldCompiledOrNull?.let { rhsField ->
								rhsField.field.name.let { name ->
									rhsField.rhsCompiled.linkOrNull.let { rhsLink ->
										if (rhsLink == null) link.tail.make(name)
										else ifOrNull(rhsLink.tail.type == type()) {
											rhsLink.head.fieldCompiledOrNull?.let { rhsField ->
												ifOrNull(rhsField.field.name == "to") {
													rhsField.rhsCompiled.linkOrNull?.let { rhsRhsLink ->
														ifOrNull(rhsRhsLink.tail.type == type()) {
															link.tail.plus(rhsRhsLink.head).make(name)
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

fun Compiled.resolveOp1OrNull(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	resolveOp1OrNull { lhs, resolvedName ->
		ifOrNull(name == resolvedName) {
			fn(lhs)
		}
	}

fun Compiled.resolveOp2OrNull(name: String, fn: (Compiled, Compiled) -> Compiled?): Compiled? =
	resolveOp2OrNull { lhs, resolvedName, rhs ->
		ifOrNull(name == resolvedName) {
			fn(lhs, rhs)
		}
	}

fun Compiled.resolveOp1OrNull(fn: (Compiled, String) -> Compiled?): Compiled? =
	resolveOp2OrNull { lhs, name, rhs ->
		ifOrNull(rhs.type.isEmpty) {
			fn(lhs, name)
		}
	}

fun Compiled.resolveOp2OrNull(fn: (Compiled, String, Compiled) -> Compiled?): Compiled? =
	structOrNull?.linkOrNull?.let { link ->
		link.head.fieldCompiledOrNull?.let { field ->
			fn(compiled(link.tail), field.field.name, field.rhsCompiled)
		}
	}

val Compiled.resolveTypeOrNull: Compiled?
	get() =
		resolveOp1OrNull("type") { lhs ->
			lhs.type.script.staticCompiled
		}
