package leo32.runtime

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import leo32.base.list
import leo32.string32
import kotlin.test.Test

class TermTest {
	@Test
	fun simpleString() {
		term("color" to term("red"))
			.string
			.assertEqualTo("color red")

		term("pencil" to term("color" to term("red")))
			.string
			.assertEqualTo("pencil color red")

		term("pencil" to term("red" to term(), "color" to term()))
			.string
			.assertEqualTo("pencil(red, color)")

		term("red" to term(), "color" to term(), "pencil" to term())
			.string
			.assertEqualTo("red, color, pencil")

		term("x" to term("10"), "y" to term("12"))
			.string
			.assertEqualTo("x 10, y 12")

		term("vec" to term("x" to term("10"), "y" to term("12")))
			.string
			.assertEqualTo("vec(x 10, y 12)")

		term("center" to term("vec" to term("x" to term("10"), "y" to term("12"))))
			.string
			.assertEqualTo("center vec(x 10, y 12)")

		term("vec" to term("x" to term("percent" to term("10")), "y" to term("percent" to term("12"))))
			.string
			.assertEqualTo("vec(x percent 10, y percent 12)")

		term(
			"vec" to term("x" to term("10"), "y" to term("12")),
			"vec" to term("x" to term("13"), "y" to term("14")))
			.string
			.assertEqualTo("vec(x 10, y 12), vec(x 13, y 14)")

		term("red" to term(), "color" to term())
			.string
			.assertEqualTo("red, color")

		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("12"),
					"y" to term("13"))))
			.string
			.assertEqualTo("circle(radius 10, center(x 12, y 13))")
	}

	@Test
	fun leafPlus() {
		term()
			.leafPlus(term())
			.assertEqualTo(term())

		term()
			.leafPlus(term("one"))
			.assertEqualTo(term("one"))

		term("one")
			.leafPlus(term("two"))
			.assertEqualTo(term("one" to term("two")))

		term("one" to term("two"))
			.leafPlus(term("three"))
			.assertEqualTo(term("one" to term("two" to term("three"))))

		term(
			"anything" to term(),
			"one" to term())
			.leafPlus(term("two"))
			.assertEqualTo(
				term(
					"anything" to term(),
					"one" to term("two")))

		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("12"),
					"y" to term())))
			.leafPlus(term("13"))
			.assertEqualTo(
				term(
					"circle" to term(
						"radius" to term("10"),
						"center" to term(
							"x" to term("12"),
							"y" to term("13")))))
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

		term.at("param").assertEqualTo(list(term("0"), term("1")))
		term.at("result").assertEqualTo(list(term("i32")))
		term.at("body").assertEqualTo(list(empty.term))
	}

	@Test
	fun resolve() {
		lateinit var resolveFn: Term.() -> Term
		resolveFn = {
			if (isEmpty) this
			else term("resolved" to map(resolveFn))
		}

		empty.term.map(resolveFn).assertEqualTo(empty.term)

		term("one")
			.map(resolveFn)
			.assertEqualTo(term("one"))

		term(
			"one" to term("jeden"),
			"two" to term("dwa"))
			.map(resolveFn)
			.assertEqualTo(
				term(
					"resolved" to term(
						"one" to term(
							"resolved" to term("jeden"))),
					"two" to term(
						"resolved" to term("dwa"))))
	}

	@Test
	fun evalGet() {
		term()
			.plusMacroGet("one" to term())
			.assertEqualTo(null)

		term("one" to term())
			.plusMacroGet("one" to term())
			.assertEqualTo(term())

		term("one" to term("jeden"))
			.plusMacroGet("one" to term())
			.assertEqualTo(term("jeden"))

		term("one" to term("jeden"))
			.plusMacroGet("two" to term())
			.assertEqualTo(null)

		term("circle" to term("radius" to term("10")))
			.plusMacroGet("circle" to term())
			.assertEqualTo(term("radius" to term("10")))
	}

	@Test
	fun evalEquals() {
		term()
			.plusMacroEquals("equals" to term())
			.assertEqualTo(term(true))

		term("zero" to term())
			.plusMacroEquals("equals" to term("zero"))
			.assertEqualTo(term(true))

		term("zero" to term())
			.plusMacroEquals("equals" to term("one"))
			.assertEqualTo(term(false))
	}

	@Test
	fun seq32() {
		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("1.2"),
					"y" to term("1.3"))))
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
		term(0).intOrNull.assertEqualTo(0)
		term(-1).intOrNull.assertEqualTo(-1)
		term("int" to term("0")).intOrNull.assertEqualTo(0)
		term("int" to term("-1")).intOrNull.assertEqualTo(-1)
		term("int" to term("123123123123")).intOrNull.assertEqualTo(null)
	}

	@Test
	fun listTermSeqOrNull() {
		term().listTermSeqOrNull("foo")!!.assertContains()
		term("foo").listTermSeqOrNull("foo")!!.assertContains(term())
		term("foo" to term("bar")).listTermSeqOrNull("foo")!!.assertContains(term("bar"))
		term("foo" to term("bar"), "foo" to term("zar")).listTermSeqOrNull("foo")!!.assertContains(term("bar"), term("zar"))
		term("foo" to term(), "zoo" to term()).listTermSeqOrNull("foo").assertEqualTo(null)
	}

//	@Test
//	fun invoke() {
//		val bitType = term("bit")
//
//		val bitZero = term("bit" to term("zero")) of bitType
//		val bitOne = term("bit" to term("one")) of bitType
//
//		val notBitTemplate = template(
//			argument,
//			op(
//				switch(
//					term("not" to bitZero) caseTo bitOne,
//					term("not" to bitOne) caseTo bitZero)))
//
//		val notBitFunction = notBitTemplate of bitType
//
//		val notBitType = term("not" to term("bit")) gives notBitFunction
//
//		val notBitZero = term("not" to bitZero) of notBitType
//		val notBitOne = term("not" to bitOne) of notBitType
//
//		val bitAndBitTemplate = template(
//			argument,
//			op(
//				switch(
//					bitZero.plus("and" to bitZero) caseTo bitZero,
//					bitZero.plus("and" to bitOne) caseTo bitZero,
//					bitOne.plus("and" to bitZero) caseTo bitZero,
//					bitOne.plus("and" to bitOne) caseTo bitOne)))
//
//		val bitAndBitFunction = bitAndBitTemplate of bitType
//
//		val bitAndBitType = bitType.plus("and" to bitType) gives bitAndBitFunction
//
//		val bitZeroAndBitZero = bitZero.plus("and" to bitZero) of bitAndBitType
//		val bitZeroAndBitOne = bitZero.plus("and" to bitOne) of bitAndBitType
//		val bitOneAndBitZero = bitOne.plus("and" to bitZero) of bitAndBitType
//		val bitOneAndBitOne = bitOne.plus("and" to bitOne) of bitAndBitType
//
//		val bitOrBitTemplate = template(
//			op("not" to template(
//				op("not" to template(argument, op(get(lhs)))),
//				op(call(notBitTemplate)),
//				op("and" to template(
//					op("not" to template(argument, op(get(rhs)))),
//					op(call(notBitTemplate)))),
//				op(call(bitAndBitTemplate)))),
//			op(call(notBitTemplate)))
//
//		val bitOrBitFunction = bitOrBitTemplate of bitType
//
//		val bitOrBitType = bitType.plus("or" to bitType) gives bitOrBitFunction
//
//		val bitZeroOrBitZero = bitZero.plus("or" to bitZero) of bitOrBitType
//		val bitZeroOrBitOne = bitZero.plus("or" to bitOne) of bitOrBitType
//		val bitOneOrBitZero = bitOne.plus("or" to bitZero) of bitOrBitType
//		val bitOneOrBitOne = bitOne.plus("or" to bitOne) of bitOrBitType
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
				term("define" to term(
					"bit" to term(),
					"has" to term(
						"either" to term("zero"),
						"either" to term("one")))))

		term
			.invoke("bit" to term("zero"))
			.typeTerm
			.assertEqualTo(term("bit"))

		term
			.invoke("bit" to term("one"))
			.typeTerm
			.assertEqualTo(term("bit"))

		term
			.invoke("not" to term("bit" to term("one")))
			.typeTerm
			.assertEqualTo(term("not" to term("bit")))

		term
			.invoke("bit" to term("zero"))
			.invoke("and" to term("bit" to term("one")))
			.typeTerm
			.assertEqualTo(term("bit" to term(), "and" to term("bit")))

		term
			.invoke("bit" to term("zero"))
			.invoke("bit" to term("one"))
			.typeTerm
			.assertEqualTo(term("bit" to term(), "bit" to term("one")))
	}

	@Test
	fun invokeTermHasTermMacro() {
		val term0 = term()

		val term1 = term()
			.invoke(
				term(
					"define" to term(
						"circle" to term(),
						"has" to term("radius"))))

		term1
			.assertEqualTo(
				term()
					.invoke(
						term0.invoke(term("circle")) has term0.invoke(term("radius"))))

		val term2 = term1
			.invoke(
				term(
					"color" to term(),
					"has" to term("alpha")))

		term2
			.assertEqualTo(
				term1.invoke(term0.invoke(term("color")) has term0.invoke(term("alpha"))))
	}

	@Test
	fun argumentRaw() {
		term()
			.copy(argumentOrNull = term("zero"))
			.invoke(script("argument"))
			.script
			.assertEqualTo(script("zero"))

		term()
			.copy(argumentOrNull = term("zero"))
			.invoke(script("the" to script("argument")))
			.script
			.assertEqualTo(script("the" to script("zero")))
	}

	@Test
	fun argumentNotBound() {
		invoke("argument" to script())
			.assertEqualTo(script("argument"))
	}

	@Test
	fun givesArgument() {
		invoke(
			"define" to script(
				"zero" to script(),
				"gives" to script("argument")),
			"zero" to script())
			.assertEqualTo(script("zero"))
	}

	@Test
	fun defineGivesTheArgument() {
		invoke(
			"define" to script(
				"zero" to script(),
				"gives" to script("the" to script("argument"))),
			"zero" to script())
			.assertEqualTo(script("the" to script("zero")))
	}

	@Test
	fun simpleDefineGives() {
		invoke(
			"define" to script(
				"zero" to script(),
				"gives" to script("one")),
			"zero" to script())
			.assertEqualTo(script("one"))
	}

	@Test
	fun typeGivesSwitch() {
		val term = invokeTerm(
			"define" to script(
				"bit" to script(),
				"has" to script(
					"either" to script("zero"),
					"either" to script("one"))),
			"define" to script(
				"negate" to script("bit" to script()),
				"gives" to script(
					"argument" to script(),
					"switch" to script(
						"case" to script(
							"negate" to script("bit" to script("zero")),
							"to" to script("bit" to script("one"))),
						"case" to script(
							"negate" to script("bit" to script("one")),
							"to" to script("bit" to script("zero")))))))

		term.script.assertEqualTo(script())
		term
			.invoke("negate" to term("bit" to term("zero")))
			.script
			.assertEqualTo(script("bit" to script("one")))
		term
			.invoke("negate" to term("bit" to term("one")))
			.script
			.assertEqualTo(script("bit" to script("zero")))
		term
			.invoke("negate" to term("bit" to term("two")))
			.script
			.assertEqualTo(script("negate" to script("bit" to script("two"))))
	}

	@Test
	fun macroSwitch() {
		invoke(
			"one" to script(),
			"switch" to script(
				"case" to script(
					"one" to script(),
					"to" to script("jeden")),
				"case" to script(
					"two" to script(),
					"to" to script("dwa"))))
			.assertEqualTo(script("jeden"))

		invoke(
			"two" to script(),
			"switch" to script(
				"case" to script(
					"one" to script(),
					"to" to script("jeden")),
				"case" to script(
					"two" to script(),
					"to" to script("dwa"))))
			.assertEqualTo(script("dwa"))

		invoke(
			"three" to script(),
			"switch" to script(
				"case" to script(
					"one" to script(),
					"to" to script("jeden")),
				"case" to script(
					"two" to script(),
					"to" to script("dwa"))))
			.assertEqualTo(
				script(
					"three" to script(),
					"switch" to script(
						"case" to script(
							"one" to script(),
							"to" to script("jeden")),
						"case" to script(
							"two" to script(),
							"to" to script("dwa")))))
	}

	@Test
	fun termSwitchOrNull() {
		term("switch")
			.switchOrNull
			.assertEqualTo(switch())

		term(
			"switch" to term(
				"case" to term(
					"one" to term(),
					"to" to term("two"))))
			.switchOrNull
			.assertEqualTo(
				switch(
					term("one") caseTo term("two")))

		term(
			"foo" to term(),
			"switch" to term())
			.switchOrNull
			.assertEqualTo(null)
	}
}