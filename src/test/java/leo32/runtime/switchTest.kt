package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class SwitchTest {
	@Test
	fun switchOrNull() {
		("switch" to term())
			.switchOrNull
			.assertEqualTo(empty.switch)

		("switch" to term(
			"case" to term(
				"foo" to term(),
				"to" to term("bar"))))
			.switchOrNull
			.assertEqualTo(
				switch(
					term("foo") caseTo term("bar")))

		("switch" to term(
			"case" to term(
				"foo" to term(),
				"to" to term("bar")),
			"case" to term(
				"zoo" to term(),
				"to" to term("zar"))))
			.switchOrNull
			.assertEqualTo(
				switch(
					term("foo") caseTo term("bar"),
					term("zoo") caseTo term("zar")))
	}

	@Test
	fun switchInvoke() {
		switch(
			term("one") caseTo term("jeden"),
			term("two") caseTo term("dwa"))
			.invoke(term("one"))
			.assertEqualTo(term("jeden"))

		switch(
			term("one") caseTo term("jeden"),
			term("two") caseTo term("dwa"))
			.invoke(term("two"))
			.assertEqualTo(term("dwa"))

		switch(
			term("one") caseTo term("jeden"),
			term("two") caseTo term("dwa"))
			.invoke(term("three"))
			.assertEqualTo(null)
	}
}