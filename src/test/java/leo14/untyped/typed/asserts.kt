package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.lambda.runtime.Value

fun Value.typedAssertEqualTo(other: Value) {
	this as Typed
	other as Typed
	type.assertEqualTo(other.type)
	value.assertEqualTo(other.value)
}
