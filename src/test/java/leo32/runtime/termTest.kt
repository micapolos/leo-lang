package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import leo32.base.list
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
			.plusMacroGet(oneSymbol to term(theSymbol to term(oneSymbol to term(jedenSymbol))))
			.assertEqualTo(term(oneSymbol to term(jedenSymbol)))

		term()
			.plusMacroGet(oneSymbol to term(twoSymbol to term(jedenSymbol)))
			.assertEqualTo(null)

		term()
			.plusMacroGet(radiusSymbol to term(circleSymbol to term(radiusSymbol to term("10"))))
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
	fun argumentRaw() {
		empty.scope
			.bindSelf(term(zeroSymbol))
			.emptyTerm
			.invoke(script(selfSymbol))
			.script
			.assertEqualTo(script(zeroSymbol))

		empty.scope
			.bindSelf(term(zeroSymbol))
			.emptyTerm
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
			zeroSymbol to script(),
			givesSymbol to script(selfSymbol),
			zeroSymbol to script())
			.assertEqualTo(script(selfSymbol to script(zeroSymbol)))
	}

	@Test
	fun defineGivesTheArgument() {
		invoke(
			theSymbol to script(zeroSymbol to script()),
			givesSymbol to script(theSymbol to script(selfSymbol)),
			theSymbol to script(zeroSymbol to script()))
			.assertEqualTo(script(theSymbol to script(zeroSymbol)))
	}

	@Test
	fun simpleDefineGives() {
		invoke(
			zeroSymbol to script(),
			givesSymbol to script(oneSymbol),
			zeroSymbol to script())
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun alternativesTermOrNull() {
		term()
			.alternativesTermOrNull
			.assertEqualTo(null)

		term(zeroSymbol to term())
			.alternativesTermOrNull
			.assertEqualTo(null)

		term(eitherSymbol to term())
			.alternativesTermOrNull
			.assertEqualTo(null)

		term(eitherSymbol to term(zeroSymbol))
			.alternativesTermOrNull
			.assertEqualTo(term(zeroSymbol))

		term(eitherSymbol to term(zeroSymbol to term(oneSymbol)))
			.alternativesTermOrNull
			.assertEqualTo(term(zeroSymbol to term(oneSymbol)))

		term(
			eitherSymbol to term(
				zeroSymbol to term(),
				oneSymbol to term()))
			.alternativesTermOrNull
			.assertEqualTo(null)

		term(
			eitherSymbol to term(zeroSymbol),
			eitherSymbol to term(oneSymbol))
			.alternativesTermOrNull
			.assertEqualTo(
				term(
					zeroSymbol to term(),
					oneSymbol to term()))

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)))
			.alternativesTermOrNull
			.assertEqualTo(
				term(
					xSymbol to term(zeroSymbol),
					ySymbol to term(oneSymbol)))

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)),
			eitherSymbol to term())
			.alternativesTermOrNull
			.assertEqualTo(null)

		term(
			eitherSymbol to term(xSymbol to term(zeroSymbol)),
			eitherSymbol to term(ySymbol to term(oneSymbol)),
			zeroSymbol to term())
			.alternativesTermOrNull
			.assertEqualTo(null)
	}

	@Test
	fun functionScoping() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					givesSymbol to script(oneSymbol)))

		scopedTerm
			.scope
			.assertEqualTo(empty.scope.define(term("zero") caseTo term("one")))

		scopedTerm
			.invoke(zeroSymbol to script())
			.assertEqualTo(scopedTerm.invoke("one" to term()))
	}

	@Test
	fun functionScoping2() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					givesSymbol to script(oneSymbol)))

		val scopedTerm2 = scopedTerm
			.invoke(
				script(
					oneSymbol to script(),
					givesSymbol to script(twoSymbol)))

		scopedTerm2
			.invoke(zeroSymbol to script())
			.assertEqualTo(scopedTerm2.scope.bring(term(oneSymbol)))
	}

	@Test
	fun functionScoping_self() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					givesSymbol to script(selfSymbol)))

		scopedTerm
			.invoke(zeroSymbol to script())
			.assertEqualTo(scopedTerm.scope.bring(term(selfSymbol to term(zeroSymbol))))
	}

	@Test
	fun functionScoping_get() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					negateSymbol to script(),
					givesSymbol to script(oneSymbol)))

		scopedTerm
			.invoke(
				script(
					theSymbol to script(zeroSymbol to script()),
					zeroSymbol to script(),
					negateSymbol to script()))
			.assertEqualTo(scopedTerm.scope.bring(term(oneSymbol)))
	}

	@Test
	fun functionScoping_wrap() {
		val scopedTerm = term()
			.invoke(
				script(
					theSymbol to script(zeroSymbol to script()),
					negateSymbol to script(),
					givesSymbol to script(oneSymbol)))

		scopedTerm
			.invoke(
				script(
					zeroSymbol to script(),
					theSymbol to script(),
					negateSymbol to script()))
			.assertEqualTo(scopedTerm.scope.bring(term(oneSymbol)))
	}

	@Test
	fun functionScoping_self2() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					negateSymbol to script(),
					givesSymbol to script(oneSymbol)))

		val scopedTerm2 = scopedTerm
			.invoke(
				script(
					theSymbol to script(zeroSymbol to script()),
					givesSymbol to script(
						selfSymbol to script(),
						theSymbol to script(),
						zeroSymbol to script(),
						negateSymbol to script())))

		scopedTerm2
			.invoke(theSymbol to script(zeroSymbol to script()))
			.assertEqualTo(scopedTerm2.scope.bring(term(oneSymbol)))
	}

	@Test
	fun functionScoping_self3() {
		val scopedTerm = term()
			.invoke(
				script(
					zeroSymbol to script(),
					givesSymbol to script(
						selfSymbol to script(),
						andSymbol to script(selfSymbol))))

		scopedTerm
			.invoke(zeroSymbol to script())
			.assertEqualTo(
				scopedTerm
					.scope
					.bring(
						term(
							selfSymbol to term(zeroSymbol),
							andSymbol to term(selfSymbol to term(zeroSymbol)))))
	}
}