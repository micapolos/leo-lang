package leo14.type.value

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.lambda.term
import leo14.type.*
import leo14.type.thunk.with
import leo14.typed.plus
import kotlin.test.Test

class UtilTest {
	val staticType = type()
	val dynamicType = type(native(type(), isStatic = false))

	@Test
	fun structurePlus_staticLhs_staticRhs() {
		term("lhs")
			.of(structure("lhs" fieldTo staticType) with scope())
			.plus(term("rhs") of ("rhs" fieldTo staticType with scope()))
			.assertEqualTo(
				id<String>() of
					(structure(
						"lhs" fieldTo staticType,
						"rhs" fieldTo staticType) with scope()))
	}

	@Test
	fun structurePlus_staticLhs() {
		term("lhs")
			.of(structure("lhs" fieldTo staticType) with scope())
			.plus(term("rhs") of ("rhs" fieldTo dynamicType with scope()))
			.assertEqualTo(
				term("rhs") of
					(structure(
						"lhs" fieldTo staticType,
						"rhs" fieldTo dynamicType) with scope()))
	}

	@Test
	fun structurePlus_staticRhs() {
		term("lhs")
			.of(structure("lhs" fieldTo dynamicType) with scope())
			.plus(term("rhs") of ("rhs" fieldTo staticType with scope()))
			.assertEqualTo(
				term("lhs") of
					(structure(
						"lhs" fieldTo dynamicType,
						"rhs" fieldTo staticType) with scope()))
	}

	@Test
	fun structurePlus_dynamic() {
		term("lhs")
			.of(structure("lhs" fieldTo dynamicType) with scope())
			.plus(term("rhs") of ("rhs" fieldTo dynamicType with scope()))
			.assertEqualTo(
				term("lhs")
					.plus(term("rhs"))
					.of(
						structure(
							"lhs" fieldTo dynamicType,
							"rhs" fieldTo dynamicType)
							.with(scope())))
	}

	@Test
	fun structurePlusSplit_staticLhs_staticRhs() {
		id<String>()
			.of(structure(
				"lhs" fieldTo staticType,
				"rhs" fieldTo staticType) with scope())
			.split!!
			.assertEqualTo(
				id<String>() of
					(structure("lhs" fieldTo staticType) with scope()) to
					(id<String>() of ("rhs" fieldTo staticType with scope())))
	}

	@Test
	fun structurePlusSplit_staticLhs() {
		term("rhs")
			.of(structure(
				"lhs" fieldTo staticType,
				"rhs" fieldTo dynamicType) with scope())
			.split!!
			.assertEqualTo(
				id<String>()
					.of(structure("lhs" fieldTo staticType) with scope()) to
					(term("rhs") of ("rhs" fieldTo dynamicType with scope())))
	}

	@Test
	fun structurePlusSplit_staticRhs() {
		term("lhs")
			.of(structure(
				"lhs" fieldTo dynamicType,
				"rhs" fieldTo staticType) with scope())
			.split!!
			.assertEqualTo(
				term("lhs")
					.of(structure("lhs" fieldTo dynamicType) with scope()) to
					(id<String>() of ("rhs" fieldTo staticType with scope())))
	}

	@Test
	fun structurePlusSplit() {
		term("lhs")
			.plus(term("rhs"))
			.of(structure(
				"lhs" fieldTo dynamicType,
				"rhs" fieldTo dynamicType) with scope())
			.split!!
			.assertEqualTo(
				term("lhs")
					.of(structure("lhs" fieldTo dynamicType) with scope()) to
					(term("rhs") of ("rhs" fieldTo dynamicType with scope())))
	}
}