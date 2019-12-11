package leo14.lib

import leo13.index
import leo14.lambda.*
import leo14.native.Native

typealias Term = leo14.lambda.Term<Native>

fun oneOf(n: Int, m: Int, obj: Obj) = choiceTerm(m.minus(n).index, m.index, obj.term)

fun <V1 : Obj, V2 : Obj, R : Obj> Term.switch(
	make1: Term.() -> V1,
	make2: Term.() -> V2,
	make: Term.() -> R,
	fn1: V1.() -> R,
	fn2: V2.() -> R): R =
	matchTerm(
		fn(arg0<Native>().make1().fn1().term),
		fn(arg0<Native>().make2().fn2().term))
		.nativeEval
		.make()

fun struct2(v1: Obj, v2: Obj) =
	pair(v1.term, v2.term)

fun <V1 : Obj> Obj.struct2get1(make: Term.() -> V1) =
	term.pair().first.make()

fun <V2 : Obj> Obj.struct2get2(make: Term.() -> V2) =
	term.pair().second.make()
