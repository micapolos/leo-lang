package leo14.untyped

val stackOverflowErrorProgram =
	program(
		"error" valueTo program(
			"overflow" valueTo program(
				"stack" valueTo program())))