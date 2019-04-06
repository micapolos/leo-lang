package leo.base

fun Appendable.tryAppend(tryFn: Appendable.() -> Appendable?): Appendable? =
	StringBuilder().let { stringBuilder ->
		stringBuilder.tryFn().let { triedAppendable ->
			if (triedAppendable == null) null
			else append(stringBuilder.toString())
		}
	}