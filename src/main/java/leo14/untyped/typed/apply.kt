package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.timesName
import leo14.untyped.typedName

fun Value.valueApply(value: Value): Value =
	when (value) {
		is Field ->
			when (value.rhs) {
				null -> null.valueApplyNormalized(value.name fieldTo this)
				else -> valueApplyNormalized(value)
			}
		else -> valueApplyNormalized(value)
	}

fun Value.valueApplyNormalized(value: Value): Value =
	when (this) {
		null ->
			when (value) {
				is Field ->
					when (value.name) {
						minusName ->
							when (value.rhs) {
								is Int -> -value.rhs
								is Number -> -value.rhs
								else -> valueAppend(value)
							}
						typedName -> value.rhs.valueTyped
						else -> valueAppend(value)
					}
				else -> valueAppend(value)
			}
		is String ->
			when (value) {
				is Field ->
					when (value.name) {
						plusName ->
							when (value.rhs) {
								is String -> this + value.rhs
								else -> valueAppend(value)
							}
						else -> valueAppend(value)
					}
				else -> valueAppend(value)
			}
		is Int ->
			when (value) {
				is Field ->
					when (value.name) {
						plusName ->
							when (value.rhs) {
								is Int -> this + value.rhs
								else -> valueAppend(value)
							}
						minusName ->
							when (value.rhs) {
								is Int -> this - value.rhs
								else -> valueAppend(value)
							}
						timesName ->
							when (value.rhs) {
								is Int -> this * value.rhs
								else -> valueAppend(value)
							}
						else -> valueAppend(value)
					}
				else -> valueAppend(value)
			}
		is Number ->
			when (value) {
				is Field ->
					when (value.name) {
						plusName ->
							when (value.rhs) {
								is Number -> this + value.rhs
								else -> valueAppend(value)
							}
						minusName ->
							when (value.rhs) {
								is Number -> this - value.rhs
								else -> valueAppend(value)
							}
						timesName ->
							when (value.rhs) {
								is Number -> this * value.rhs
								else -> valueAppend(value)
							}
						else -> valueAppend(value)
					}
				else -> valueAppend(value)
			}
		is Typed -> apply(value.valueTyped)
		else -> valueAppend(value)
	}

fun Value.valueAppend(value: Value): Value =
	if (this == null) value
	else this to value
