package leo32.runtime

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import leo32.base.list
import leo32.base.put
import leo32.string32
import kotlin.test.Test

class TermTest {
	@Test
	fun simpleString() {
		term(colorSymbol to term(redSymbol))
			.string
			.assertEqualTo("color red")

		term(pencilSymbol to term(colorSymbol to term(redSymbol)))
			.string
			.assertEqualTo("pencil color red")

		term(pencilSymbol to term(redSymbol to term(), colorSymbol to term()))
			.string
			.assertEqualTo("pencil(red, color)")

		term(redSymbol to term(), colorSymbol to term(), pencilSymbol to term())
			.string
			.assertEqualTo("red, color, pencil")

		term(xSymbol to term("10"), ySymbol to term("12"))
			.string
			.assertEqualTo("x 10, y 12")

		term(vecSymbol to term(xSymbol to term("10"), ySymbol to term("12")))
			.string
			.assertEqualTo("vec(x 10, y 12)")

		term(centerSymbol to term(vecSymbol to term(xSymbol to term("10"), ySymbol to term("12"))))
			.string
			.assertEqualTo("center vec(x 10, y 12)")

		term(vecSymbol to term(xSymbol to term("percent" to term("10")), ySymbol to term("percent" to term("12"))))
			.string
			.assertEqualTo("vec(x percent 10, y percent 12)")

		term(
			vecSymbol to term(xSymbol to term("10"), ySymbol to term("12")),
			vecSymbol to term(xSymbol to term("13"), ySymbol to term("14")))
			.string
			.assertEqualTo("vec(x 10, y 12), vec(x 13, y 14)")

		term(redSymbol to term(), colorSymbol to term())
			.string
			.assertEqualTo("red, color")

		term(
			circleSymbol to term(
				radiusSymbol to term("10"),
				centerSymbol to term(
					xSymbol to term("12"),
					ySymbol to term("13"))))
			.string
			.assertEqualTo("circle(radius 10, center(x 12, y 13))")
	}

	@Test
	fun leafPlus() {
		term()
			.leafPlus(term())
			.assertEqualTo(term())

		term()
			.leafPlus(term(oneSymbol))
			.assertEqualTo(term(oneSymbol))

		term(oneSymbol)
			.leafPlus(term(twoSymbol))
			.assertEqualTo(term(oneSymbol to term(twoSymbol)))

		term(oneSymbol to term(twoSymbol))
			.leafPlus(term(threeSymbol))
			.assertEqualTo(term(oneSymbol to term(twoSymbol to term(threeSymbol))))

		term(
			anythingSymbol to term(),
			oneSymbol to term())
			.leafPlus(term(twoSymbol))
			.assertEqualTo(
				term(
					anythingSymbol to term(),
					oneSymbol to term(twoSymbol)))

		term(
			circleSymbol to term(
				radiusSymbol to term("10"),
				centerSymbol to term(
					xSymbol to term("12"),
					ySymbol to term())))
			.leafPlus(term("13"))
			.assertEqualTo(
				term(
					circleSymbol to term(
						radiusSymbol to term("10"),
						centerSymbol to term(
							xSymbol to term("12"),
							ySymbol to term("13")))))
	}

	@Test
	fun at() {
		val term = term(
			"param" to term("0"),
			"param" to term("1"),
			"result" to term("i32"),
			"body" to term())

		term.fieldList.assertEqualTo(
			list(
				"param" to term("0"),
				"param" to term("1"),
				"result" to term("i32"),
				"body" to term()))

		term.at(symbol("param")).assertEqualTo(list(term("0"), term("1")))
		term.at(symbol("result")).assertEqualTo(list(term("i32")))
		term.at(bodySymbol).assertEqualTo(list(empty.term))
	}

	@Test
	fun resolve() {
		lateinit var resolveFn: Term.() -> Term
		resolveFn = {
			if (isEmpty) this
			else term(resolvedSymbol to map(resolveFn))
		}

		empty.term.map(resolveFn).assertEqualTo(empty.term)

		term(oneSymbol)
			.map(resolveFn)
			.assertEqualTo(term(oneSymbol))

		term(
			oneSymbol to term(jedenSymbol),
			twoSymbol to term(dwaSymbol))
			.map(resolveFn)
			.assertEqualTo(
				term(
					resolvedSymbol to term(
						oneSymbol to term(
							resolvedSymbol to term(jedenSymbol))),
					twoSymbol to term(
						resolvedSymbol to term(dwaSymbol))))
	}

	@Test
	fun evalGet() {
		term()
			.plusMacroGet(oneSymbol to term())
			.assertEqualTo(null)

		term(oneSymbol to term())
			.plusMacroGet(oneSymbol to term())
			.assertEqualTo(null)

		term()
			.plusMacroGet(oneSymbol to term())
			.assertEqualTo(null)

		term()
			.plusMacroGet(oneSymbol to term(oneSymbol to term(jedenSymbol)))
			.assertEqualTo(term(jedenSymbol))

		term()
			.plusMacroGet(oneSymbol to term(twoSymbol to term(jedenSymbol)))
			.assertEqualTo(null)

		term()
			.plusMacroGet(circleSymbol to term(circleSymbol to term(radiusSymbol to term("10"))))
			.assertEqualTo(term(radiusSymbol to term("10")))
	}

	@Test
	fun evalEquals() {
		term()
			.plusMacroEquals(equalsSymbol to term())
			.assertEqualTo(term(true))

		term(zeroSymbol to term())
			.plusMacroEquals(equalsSymbol to term(zeroSymbol))
			.assertEqualTo(term(true))

		term(zeroSymbol to term())
			.plusMacroEquals(equalsSymbol to term(oneSymbol))
			.assertEqualTo(term(false))
	}

	@Test
	fun seq32() {
		term(
			circleSymbol to term(
				radiusSymbol to term("10"),
				centerSymbol to term(
					xSymbol to term("1.2"),
					ySymbol to term("1.3"))))
			.seq32
			.string32
			.assertEqualTo("circle(radius(10())center(x(1.2())y(1.3())))")
	}

	@Test
	fun seq32_escaping() {
		term("\\()").seq32.string32.assertEqualTo("\\\\\\(\\)()")
	}

	@Test
	fun intOrNull() {
		term(0).assertEqualTo(term("int" to term("0")))
		term(0).intOrNull.assertEqualTo(0)
		term(-1).intOrNull.assertEqualTo(-1)
		term(intSymbol to term("0")).intOrNull.assertEqualTo(0)
		term(intSymbol to term("-1")).intOrNull.assertEqualTo(-1)
		term(intSymbol to term("123123123123")).intOrNull.assertEqualTo(null)
	}

	@Test
	fun listTermSeqOrNull() {
		term().listTermSeqOrNull(fooSymbol)!!.assertContains()
		term(fooSymbol).listTermSeqOrNull(fooSymbol)!!.assertContains(term())
		term(fooSymbol to term(barSymbol)).listTermSeqOrNull(fooSymbol)!!.assertContains(term(barSymbol))
		term(fooSymbol to term(barSymbol), fooSymbol to term("zar")).listTermSeqOrNull(fooSymbol)!!.assertContains(term(barSymbol), term("zar"))
		term(fooSymbol to term(), "zoo" to term()).listTermSeqOrNull(fooSymbol).assertEqualTo(null)
	}

//	@Test
//	fun invoke() {
//		val bitType = term(bitSymbol)
//
//		val bitZero = term(bitSymbol to term(zeroSymbol)) of bitType
//		val bitOne = term(bitSymbol to term(oneSymbol)) of bitType
//
//		val notBitTemplate = template(
//			argument,
//			op(
//				switch(
//					term(notSymbol to bitZero) caseTo bitOne,
//					term(notSymbol to bitOne) caseTo bitZero)))
//
//		val notBitFunction = notBitTemplate of bitType
//
//		val notBitType = term(notSymbol to term(bitSymbol)) gives notBitFunction
//
//		val notBitZero = term(notSymbol to bitZero) of notBitType
//		val notBitOne = term(notSymbol to bitOne) of notBitType
//
//		val bitAndBitTemplate = template(
//			argument,
//			op(
//				switch(
//					bitZero.plus(andSymbol to bitZero) caseTo bitZero,
//					bitZero.plus(andSymbol to bitOne) caseTo bitZero,
//					bitOne.plus(andSymbol to bitZero) caseTo bitZero,
//					bitOne.plus(andSymbol to bitOne) caseTo bitOne)))
//
//		val bitAndBitFunction = bitAndBitTemplate of bitType
//
//		val bitAndBitType = bitType.plus(andSymbol to bitType) gives bitAndBitFunction
//
//		val bitZeroAndBitZero = bitZero.plus(andSymbol to bitZero) of bitAndBitType
//		val bitZeroAndBitOne = bitZero.plus(andSymbol to bitOne) of bitAndBitType
//		val bitOneAndBitZero = bitOne.plus(andSymbol to bitZero) of bitAndBitType
//		val bitOneAndBitOne = bitOne.plus(andSymbol to bitOne) of bitAndBitType
//
//		val bitOrBitTemplate = template(
//			op(notSymbol to template(
//				op(notSymbol to template(argument, op(get(lhs)))),
//				op(call(notBitTemplate)),
//				op(andSymbol to template(
//					op(notSymbol to template(argument, op(get(rhs)))),
//					op(call(notBitTemplate)))),
//				op(call(bitAndBitTemplate)))),
//			op(call(notBitTemplate)))
//
//		val bitOrBitFunction = bitOrBitTemplate of bitType
//
//		val bitOrBitType = bitType.plus(orSymbol to bitType) gives bitOrBitFunction
//
//		val bitZeroOrBitZero = bitZero.plus(orSymbol to bitZero) of bitOrBitType
//		val bitZeroOrBitOne = bitZero.plus(orSymbol to bitOne) of bitOrBitType
//		val bitOneOrBitZero = bitOne.plus(orSymbol to bitZero) of bitOrBitType
//		val bitOneOrBitOne = bitOne.plus(orSymbol to bitOne) of bitOrBitType
//
//		notBitZero.invoke.assertEqualTo(bitOne)
//		notBitOne.invoke.assertEqualTo(bitZero)
//
//		bitZeroAndBitZero.invoke.assertEqualTo(bitZero)
//		bitZeroAndBitOne.invoke.assertEqualTo(bitZero)
//		bitOneAndBitZero.invoke.assertEqualTo(bitZero)
//		bitOneAndBitOne.invoke.assertEqualTo(bitOne)
//
//		bitZeroOrBitZero.invoke.assertEqualTo(bitZero)
//		bitZeroOrBitOne.invoke.assertEqualTo(bitOne)
//		bitOneOrBitZero.invoke.assertEqualTo(bitOne)
//		bitOneOrBitOne.invoke.assertEqualTo(bitOne)
//	}

	@Test
	fun invokeTermHasTerm() {
		val term = term()
			.invoke(
				term(defineSymbol to term(
					bitSymbol to term(),
					"has" to term(
						"either" to term(zeroSymbol),
						"either" to term(oneSymbol)))))

		term
			.invoke(bitSymbol to term(zeroSymbol))
			.typeTerm
			.assertEqualTo(term(bitSymbol))

		term
			.invoke(bitSymbol to term(oneSymbol))
			.typeTerm
			.assertEqualTo(term(bitSymbol))

		term
			.invoke(notSymbol to term(bitSymbol to term(oneSymbol)))
			.typeTerm
			.assertEqualTo(term(notSymbol to term(bitSymbol)))

		term
			.invoke(bitSymbol to term(zeroSymbol))
			.invoke(andSymbol to term(bitSymbol to term(oneSymbol)))
			.typeTerm
			.assertEqualTo(term(bitSymbol to term(), andSymbol to term(bitSymbol)))

		term
			.invoke(bitSymbol to term(zeroSymbol))
			.invoke(bitSymbol to term(oneSymbol))
			.typeTerm
			.script
			.assertEqualTo(script(bitSymbol to script(), bitSymbol to script(oneSymbol)))
	}

	@Test
	fun argumentRaw() {
		term()
			.copy(selfOrNull = term(zeroSymbol))
			.invoke(script(selfSymbol))
			.script
			.assertEqualTo(script(zeroSymbol))

		term()
			.copy(selfOrNull = term(zeroSymbol))
			.invoke(script(theSymbol to script(selfSymbol)))
			.script
			.assertEqualTo(script(theSymbol to script(zeroSymbol)))
	}

	@Test
	fun argumentNotBound() {
		invoke(selfSymbol to script())
			.assertEqualTo(script(selfSymbol))
	}

	@Test
	fun givesArgument() {
		invoke(
			defineSymbol to script(
				zeroSymbol to script(),
				givesSymbol to script(selfSymbol)),
			zeroSymbol to script())
			.assertEqualTo(script(zeroSymbol))
	}

	@Test
	fun defineGivesTheArgument() {
		invoke(
			defineSymbol to script(
				zeroSymbol to script(),
				givesSymbol to script(theSymbol to script(selfSymbol))),
			zeroSymbol to script())
			.assertEqualTo(script(theSymbol to script(zeroSymbol)))
	}

	@Test
	fun simpleDefineGives() {
		invoke(
			defineSymbol to script(
				zeroSymbol to script(),
				givesSymbol to script(oneSymbol)),
			zeroSymbol to script())
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun typeGivesSwitch() {
		val term = invokeTerm(
			defineSymbol to script(
				bitSymbol to script(),
				hasSymbol to script(
					eitherSymbol to script(zeroSymbol),
					eitherSymbol to script(oneSymbol))),
			defineSymbol to script(
				negateSymbol to script(bitSymbol to script()),
				givesSymbol to script(
					selfSymbol to script(),
					switchSymbol to script(
						caseSymbol to script(
							negateSymbol to script(bitSymbol to script(zeroSymbol)),
							toSymbol to script(bitSymbol to script(oneSymbol))),
						caseSymbol to script(
							negateSymbol to script(bitSymbol to script(oneSymbol)),
							toSymbol to script(bitSymbol to script(zeroSymbol)))))))
	}

	@Test
	fun macroSwitch() {
		invoke(
			oneSymbol to script(),
			switchSymbol to script(
				caseSymbol to script(
					oneSymbol to script(),
					givesSymbol to script(jedenSymbol)),
				caseSymbol to script(
					twoSymbol to script(),
					givesSymbol to script(dwaSymbol))))
			.assertEqualTo(script(jedenSymbol))

		invoke(
			twoSymbol to script(),
			switchSymbol to script(
				caseSymbol to script(
					oneSymbol to script(),
					givesSymbol to script(jedenSymbol)),
				caseSymbol to script(
					twoSymbol to script(),
					givesSymbol to script(dwaSymbol))))
			.assertEqualTo(script(dwaSymbol))

		invoke(
			threeSymbol to script(),
			switchSymbol to script(
				caseSymbol to script(
					oneSymbol to script(),
					toSymbol to script(jedenSymbol)),
				caseSymbol to script(
					twoSymbol to script(),
					toSymbol to script(dwaSymbol))))
			.assertEqualTo(
				script(
					threeSymbol to script(),
					switchSymbol to script(
						caseSymbol to script(
							oneSymbol to script(),
							toSymbol to script(jedenSymbol)),
						caseSymbol to script(
							twoSymbol to script(),
							toSymbol to script(dwaSymbol)))))
	}

	@Test
	fun termSwitchOrNull() {
		term(switchSymbol)
			.switchOrNull
			.assertEqualTo(switch())

		term(
			switchSymbol to term(
				caseSymbol to term(
					oneSymbol to term(),
					givesSymbol to term(twoSymbol))))
			.switchOrNull
			.assertEqualTo(
				switch(
					term(oneSymbol) caseTo term(twoSymbol)))

		term(
			fooSymbol to term(),
			switchSymbol to term())
			.switchOrNull
			.assertEqualTo(null)
	}

	@Test
	fun eitherDict() {
		term()
			.eitherDictOrNull
			.assertEqualTo(empty.symbolDict())

		term(zeroSymbol to term())
			.eitherDictOrNull
			.assertEqualTo(null)

		term(eitherSymbol to term())
			.eitherDictOrNull
			.assertEqualTo(null)

		term(eitherSymbol to term(zeroSymbol))
			.eitherDictOrNull
			.assertEqualTo(empty.symbolDict<Term>().put(zeroSymbol, term()))

		term(eitherSymbol to term(zeroSymbol to term(oneSymbol)))
			.eitherDictOrNull
			.assertEqualTo(empty.symbolDict<Term>().put(zeroSymbol, term(oneSymbol)))

		term(
			eitherSymbol to term(
				zeroSymbol to term(),
				oneSymbol to term()))
			.eitherDictOrNull
			.assertEqualTo(null)

		term(
			eitherSymbol to term(zeroSymbol),
			eitherSymbol to term(oneSymbol))
			.eitherDictOrNull
			.assertEqualTo(
				empty
					.symbolDict<Term>()
					.put(zeroSymbol, term())
					.put(oneSymbol, term()))

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)))
			.eitherDictOrNull
			.assertEqualTo(
				empty
					.symbolDict<Term>()
					.put(xSymbol, term(zeroSymbol))
					.put(ySymbol, term(oneSymbol)))

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)),
			eitherSymbol to term())
			.eitherDictOrNull
			.assertEqualTo(null)

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)),
			zeroSymbol to term())
			.eitherDictOrNull
			.assertEqualTo(null)
	}
}