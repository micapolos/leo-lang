package leo21.compiled

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.leonardoScript
import leo21.prim.DoubleCosinusPrim
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoubleSinusPrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.token.processor.compiled
import leo21.type.Type
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.plus
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

val Compiled.resolve: Compiled
	get() =
		resolveOrNull ?: this

val Type.fn2Compiled: Compiled get() = fn(arg<Prim>(0)) of this

val Compiled.resolveLeonardoOrNull: Compiled?
	get() =
		notNullIf(type == type("leonardo" lineTo type())) {
			leonardoScript.compiled
		}
val Compiled.resolveOrNull: Compiled?
	get() =
		null
			?: resolveGetOrNull
			?: resolveAsOrNull
			?: resolveLeonardoOrNull
			?: resolveFn1OrNull(doubleType, "sinus", DoubleSinusPrim, doubleType)
			?: resolveFn1OrNull(doubleType, "cosinus", DoubleCosinusPrim, doubleType)
			?: resolveFn2OrNull(doubleType, "plus", doubleType, DoublePlusDoublePrim, doubleType)
			?: resolveFn2OrNull(doubleType, "minus", doubleType, DoubleMinusDoublePrim, doubleType)
			?: resolveFn2OrNull(doubleType, "times", doubleType, DoubleTimesDoublePrim, doubleType)
			?: resolveFn2OrNull(stringType, "plus", stringType, StringPlusStringPrim, stringType)

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