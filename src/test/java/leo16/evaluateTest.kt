package leo16

import leo.base.assertEqualTo
import leo14.Script
import leo15.dsl.*
import leo16.names.*
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

fun Script.assertEquals(f: F) {
	assertEqualTo(read_(f))
}

class EvaluateTest {
	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertEquals { negate { zero } }
	}

	@Test
	fun comment() {
		evaluate_ { comment { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { comment { zero } }.assertEquals { nothing_ }
		evaluate_ {
			comment { two { dimensional { point } } }
			point {
				comment { x.coordinate }
				x { 10.number }
				comment { y.coordinate }
				y { 20.number }
			}
		}.assertEquals {
			point {
				x { 10.number }
				y { 20.number }
			}
		}
	}

	@Test
	fun nothing() {
		evaluate_ { nothing }.assertEquals { nothing_ }
		evaluate_ { x { nothing } }.assertEquals { x { nothing_ } }
		evaluate_ { x.nothing }.assertEquals { nothing { x } }
		evaluate_ { x.nothing { y } }.assertEquals { x.nothing { y } }
	}

	@Test
	fun sentences() {
		evaluate_ { nothing_ }.assertEquals { nothing_ }
		evaluate_ { zero }.assertEquals { zero }
		evaluate_ { zero.plus { one } }.assertEquals { zero.plus { one } }
	}

	@Test
	fun literal() {
		evaluate_ { "foo".text }.assertEquals { "foo".text }
		evaluate_ { 123.number }.assertEquals { 123.number }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate } }.assertEquals { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertEquals { zero.is_ { one } }
		evaluate_ { zero.quote { one.two } }.assertEquals { zero.quote { one.two } }
		evaluate_ { zero.one.quote { two } }.assertEquals { one { zero }.quote { two } }
	}

	@Test
	fun meta() {
		evaluate_ { meta }.assertEquals { meta }
		evaluate_ { meta { zero } }.assertEquals { zero }
		evaluate_ { meta { zero { one } } }.assertEquals { zero { one } }
		evaluate_ { meta { zero.one } }.assertEquals { meta { zero.one } }
		evaluate_ { meta { zero.is_ { one } } }.assertEquals { meta { zero.is_ { one } } }
		evaluate_ { zero.meta { is_ { one } } }.assertEquals { zero.is_ { one } }
		evaluate_ { meta { quote { zero.is_ { one }.zero } } }.assertEquals { quote { one } }
	}

	@Test
	fun leonardo() {
		evaluate_ { leonardo.author.text }.assertEquals { "Michał Pociecha-Łoś".text }
	}

	@Test
	fun this_() {
		evaluate_ { this_ }.assertEquals { this_ }
		evaluate_ { x { zero }.this_ { nothing_ } }.assertEquals { x { zero } }
		evaluate_ { x { zero }.this_ { y { one } } }.assertEquals { x { zero }; y { one } }
		evaluate_ { x { zero }.this_ { y { one }; z { two } } }.assertEquals { x { zero }; this_ { y { one }; z { two } } }
		evaluate_ { this_ { zero }; this_ { one } }.assertEquals { zero; one }
	}

	@Test
	fun equals_() {
		evaluate_ { zero.equals_ { zero } }.assertEquals { boolean { true_ } }
		evaluate_ { zero.equals_ { one } }.assertEquals { boolean { false_ } }
		evaluate_ { function { does { zero } }.equals_ { function { does { zero } } } }.assertEquals { boolean { true_ } }
		evaluate_ { function { does { zero } }.equals_ { function { does { one } } } }.assertEquals { boolean { false_ } }
	}

	@Test
	fun evaluateBegin() {
		evaluate_ {
			is_ { new { line } }
			zero
		}.assertEquals { zero { new { line } } }

		evaluate_ {
			is_ { new { line } }
			x { zero }
			y { one }
		}.assertEquals {
			x {
				new { line }
				zero { new { line } }
			}
			y {
				new { line }
				one { new { line } }
			}
		}
	}

	@Test
	fun hash() {
		evaluate_ { zero.hash }.assertEquals { hash { value(_zero()).hashBigDecimal.number } }
	}

	@Test
	fun asText() {
		evaluate_ { 123.number.as_ { text } }.assertEquals { "123".text }
	}

	@Test
	fun lazy() {
		evaluate_ {
			color.is_ { color { red } }
			lazy_ { color }
		}.assertEquals { lazy_ { color } }
	}

	@Test
	fun force() {
		evaluate_ {
			force { zero }
		}.assertEquals { force { zero } }
	}

	@Test
	fun lazyForce() {
		evaluate_ {
			point.has {
				x { zero }
				y { one }
			}
			lazy_ { point }
			force
		}.assertEquals {
			point {
				x { zero }
				y { one }
			}
		}
	}

	@Test
	fun apply() {
		evaluate_ {
			x.is_ { zero }
			quote { x }
			apply
		}.assertEquals { zero }

		evaluate_ {
			x.is_ { zero }
			quote { the { x } }
			apply
		}.assertEquals { apply { the { x } } }
	}

	@Test
	fun evaluate() {
		evaluate_ { quote { nothing_ }.evaluate }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate }.evaluate }.assertEquals { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.evaluate }.assertEquals { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.evaluate }.assertEquals { one }
		evaluate_ { quote { zero.is_ { one } }.evaluate.zero }.assertEquals { zero }
	}

	@Test
	fun compile() {
		evaluate_ { quote { nothing_ }.compile }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate }.compile }.assertEquals { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.compile }.assertEquals { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.compile }.assertEquals { one }
		evaluate_ { quote { zero.is_ { one } }.compile.zero }.assertEquals { one }
	}

	@Test
	fun getField() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertEquals { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertEquals { y { one } }
	}

	@Test
	fun get() {
		evaluate_ { point { x { zero }; y { one } }.get { x } }.assertEquals { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.get { y } }.assertEquals { y { one } }
		evaluate_ { the { function { zero.does { one } } }.get { function } }.assertEquals { function { zero.does { one } } }
	}

	@Test
	fun getSpecial() {
		evaluate_ { the { "foo".text }.text }.assertEquals { "foo".text }
		evaluate_ { the { 123.number }.number }.assertEquals { 123.number }
		evaluate_ { the { function { zero.does { one } } }.function }.assertEquals { function { zero.does { one } } }
	}

	@Test
	fun getNative() {
		evaluate_ { "foo".text.native.as_ { text } }.assertEquals { "foo".nativeText }
	}

	@Test
	fun make() {
		evaluate_ { zero.make { number } }.assertEquals { number { zero } }
	}

	@Test
	fun thing() {
		evaluate_ { thing }.assertEquals { thing }
		evaluate_ { point { x { zero }; y { one } }.thing }.assertEquals { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; thing }.assertEquals { thing { x { zero }; y { one } } }
	}

	@Test
	fun functionFunction() {
		evaluate_ {
			function { function { zero }.does { ok } }
			take { function { zero.does { one } } }
		}.assertEquals { ok }
	}

	@Test
	fun functionAsField() {
		evaluate_ {
			the {
				0.number
				"hello".text
				function { zero.does { one } }
			}
			function.take { zero }
		}.assertEquals {
			function { zero.does { one } }
			take { zero }
		}
	}

	@Test
	fun functionInsideField() {
		evaluate_ {
			the {
				0.number
				"hello".text
				my { function { zero.does { one } } }
			}
			my.function.take { zero }
		}.assertEquals { one }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertEquals { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertEquals { one }
		evaluate_ { any.is_ { one }.any }.assertEquals { one }
	}

	@Test
	fun has() {
		evaluate_ { zero.has { one } }.assertEquals { nothing_ }
		evaluate_ { zero.has { one }.zero }.assertEquals { zero { one } }
		evaluate_ { any.has { one }.any }.assertEquals { any { one } }
	}

	@Test
	fun does() {
		evaluate_ { zero.does { one } }.assertEquals { nothing_ }
		evaluate_ { zero.does { one }.zero }.assertEquals { one }
		evaluate_ { zero.does { one }.one }.assertEquals { one }

		evaluate_ { native.any.text.does { ok }; "hello".text }.assertEquals { ok }

		evaluate_ { any.x.does { x }.zero.x }.assertEquals { x { zero } }
		evaluate_ { any.x.does { x }.zero.y }.assertEquals { y { zero } }

		evaluate_ { any.text.does { text }; "foo".text }.assertEquals { "foo".text }
		evaluate_ { any.number.does { number }; 123.number }.assertEquals { 123.number }
	}

	@Test
	fun matchSentence() {
		evaluate_ { zero.bit.match { zero { one } } }
			.assertEquals { one }
		evaluate_ { circle { radius { zero } }.match { radius { radius } } }
			.assertEquals { radius { zero } }

		evaluate_ { zero.bit.match { nothing_ } }
			.assertEquals { match { bit { zero } } }
		evaluate_ { zero.bit.match { one } }
			.assertEquals { bit { zero }.match { one } }
	}

	@Test
	fun matchEmpty() {
		evaluate_ { empty.list.match { empty { ok } } }
			.assertEquals { ok }
	}

	@Test
	fun matchLink() {
		evaluate_ {
			list {
				item { zero }
				item { one }
				item { two }
			}
			match { link { link } }
		}.assertEquals {
			link {
				previous {
					list {
						item { zero }
						item { one }
					}
				}
				last { two }
			}
		}
	}

	@Test
	fun function() {
		evaluate_ {
			function { zero.does { one } }
		}.assertEquals {
			function { zero.does { one } }
		}

		evaluate_ {
			function { zero.does { one } }
			zero
		}.assertEquals {
			zero { function { zero.does { one } } }
		}

		evaluate_ {
			function { zero.plus { one } }
			zero
		}.assertEquals { zero }
	}

	@Test
	fun take() {
		evaluate_ {
			function { zero.does { one } }
			take { zero }
		}.assertEquals { one }

		evaluate_ {
			function { anything.bit.does { ok } }
			take { zero.bit }
		}.assertEquals { ok }

		evaluate_ {
			function { anything.x.does { x } }
			take { y { zero } }
		}.assertEquals {
			function { x { anything }.does { x } }
			take { y { zero } }
		}

		evaluate_ {
			function { does { zero } }.take
		}.assertEquals { zero }
	}

	@Test
	fun do_() {
		evaluate_ {
			x { zero }
			y { one }
			do_ { x.and { y } }
		}.assertEquals { x { zero }.and { y { one } } }
	}

	@Test
	fun doThing() {
		evaluate_ {
			x { zero }
			y { one }
			do_ { thing }
		}.assertEquals {
			x { zero }
			y { one }
		}
	}

	@Test
	fun doRepeat() {
		evaluate_ {
			one.bit
			do_ {
				bit.match {
					zero { bit }
					one { zero.bit.repeat }
				}
			}
		}.assertEquals { bit { zero } }
	}

	@Test
	fun expands() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
		}.assertEquals { nothing_ }
	}

	@Test
	fun expandsApply() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
		}.assertEquals { nothing_ }
	}

	@Test
	fun expandsApplyUse() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
			zero
		}.assertEquals { one }
	}

	@Test
	fun expandsLocals() {
		evaluate_ {
			expand.expands {
				zero.is_ { one }
				zero
			}
			expand
		}.assertEquals { one }
	}

	@Test
	fun useLoaded() {
		evaluate_ { use { ping.testing }.ping }.assertEquals { pong }
	}

	@Test
	fun useInline() {
		evaluate_ {
			use {
				ping.is_ { pong }
			}
			ping
		}.assertEquals { pong }
	}

	@Test
	fun useDeepInline() {
		evaluate_ {
			use {
				use {
					ping.is_ { pong }
				}
			}
			ping
		}.assertEquals { ping }
	}

	@Test
	fun useNothing() {
		evaluate_ {
			zero
			use { nothing }
		}.assertEquals { zero }
	}

	@Test
	fun mode() {
		evaluate_ {
			word { zero }.mode
			is_ { meta { quote }.mode }
			zero { zero.is_ { one } }
		}.assertEquals { zero { zero.is_ { one } } }
	}

	@Test
	fun nativePatternMatch() {
		evaluate_ {
			"hello".text
			is_ { "world".text }
			"hello".text
		}.assertEquals { "world".text }
	}

	@Test
	fun nativePattern() {
		evaluate_ {
			native.any.text.check.does { ok }
			"hello".text.check
		}.assertEquals { ok }
	}

	@Test
	fun useSomething() {
		evaluate_ {
			zero
			use { x.is_ { one } }
			plus { x }
		}.assertEquals { zero.plus { one } }
	}

	@Test
	fun useCapturesScope() {
		evaluate_ {
			x.is_ { zero }
			x
			use { y.is_ { x.done } }
			and { y }
		}.assertEquals { zero.and { done { zero } } }
	}

	@Test
	fun useInvalid() {
		evaluate_ {
			use { x }
		}.assertEquals { use { x } }
	}

	@Test
	fun useDoesNotExport() {
		evaluate_ {
			use {
				use {
					x.is_ { zero }
				}
			}
			x
		}.assertEquals { x }
	}

	@Test
	fun useListRead() {
		evaluate_ {
			use {
				x
				is_ {
					list {
						item { zero }
						item { one }
					}
				}
			}
			x.link.last.thing
		}.assertEquals { one }
	}

	@Test
	fun testTest() {
		evaluate_ {
			test { zero.equals_ { zero } }
		}.assertEquals { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { one.equals_ { result } }
		}.assertEquals { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { result.equals_ { one } }
		}.assertEquals { nothing_ }

		assertFailsWith(AssertionError::class) {
			evaluate_ { test { zero.equals_ { one } } }
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { zero.equals_ { result } }
			}
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { result.equals_ { zero } }
			}
		}
	}

	@Test
	fun matchesSentence() {
		evaluate_ { zero.matches { zero } }.assertEquals { true.boolean }
		evaluate_ { zero.matches { one } }.assertEquals { false.boolean }
	}

	@Test
	fun matchesChoice() {
		evaluate_ {
			zero.or { one }
			matches { zero }
		}.assertEquals { true.boolean }

		evaluate_ {
			zero.or { one }
			matches { one }
		}.assertEquals { true.boolean }

		evaluate_ {
			zero.or { one }
			matches { two }
		}.assertEquals { false.boolean }

		evaluate_ {
			zero.or { one }
			matches { zero.or { one } }
		}.assertEquals { false.boolean }

		evaluate_ {
			zero.meta { meta { or { one } } }
			matches { zero.or { one } }
		}.assertEquals { true.boolean }
	}

	@Test
	fun matchesFunction() {
		evaluate_ {
			function { zero }
			matches { function { zero.does { one } } }
		}.assertEquals { true.boolean }

		evaluate_ {
			function { anything }
			matches { function { anything.does { one } } }
		}.assertEquals { true.boolean }

		evaluate_ {
			function { zero.does { one } }
			matches { function { one } }
		}.assertEquals { false.boolean }
	}

	@Test
	fun matchesQuote() {
		evaluate_ {
			meta { quote { zero.or { one } } }
			matches { zero.or { one } }
		}.assertEquals { true.boolean }
	}

	@Test
	fun matchesRepeating() {
		evaluate_ {
			do_ {
				natural {
					zero
					or { next { lazy_ { repeat } } }
				}
			}
			matches { zero.natural }
		}.assertEquals { true.boolean }

		evaluate_ {
			do_ {
				natural {
					zero
					or { next { lazy_ { repeat } } }
				}
			}
			matches { zero.natural.next.natural }
		}.assertEquals { true.boolean }
	}

	@Test
	fun matchesLazy() {
		evaluate_ {
			lazy_ { zero }
			matches { zero }
		}.assertEquals { true.boolean }

		evaluate_ {
			lazy_ { zero }
			matches { lazy_ { zero } }
		}.assertEquals { false.boolean }

		evaluate_ {
			zero
			matches { lazy_ { zero } }
		}.assertEquals { false.boolean }
	}

	@Test
	fun matching() {
		evaluate_ {
			zero
			matching { zero.or { one } }
			equals_ { zero }
		}

		evaluate_ {
			one
			matching { zero.or { one } }
			equals_ { one }
		}

		assertFails {
			evaluate_ {
				two
				matching { zero.or { one } }
			}
		}
	}

	@Test
	fun listRead() {
		evaluate_ {
			list {
				item { zero }
				item { one }
			}
			link.last.thing
		}.assertEquals { one }
	}

	@Test
	fun read() {
		evaluate_ {
			zero { one }.read
			is_ { two { three } }
			one
		}.assertEquals { one }

		evaluate_ {
			zero { one }.read
			is_ { two { three } }
			zero { one }
		}.assertEquals { two { three } }

		evaluate_ {
			zero { one }.read
			is_ { two { three } }
			test { zero { one }.equals_ { two { three } } }
		}.assertEquals { nothing_ }
	}

	@Test
	fun emptyDefinition() {
		evaluate_ {
			does { zero }
			x
		}.assertEquals { x { zero } }
	}
}