package leo14.typed

import leo14.*

fun Type.plusCompiler(ret: Ret<Type>): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken ->
				error("$token")
			is BeginToken ->
				when (token.begin.string) {
					"native" ->
						endCompiler {
							plus(nativeLine).plusCompiler(ret)
						}
					"choice" ->
						emptyChoice.plusCompiler { choice ->
							plus(line(choice)).plusCompiler(ret)
						}
					"function" ->
						beginCompiler("from") {
							emptyType.plusCompiler { lhs ->
								beginCompiler("to") {
									emptyType.plusCompiler { rhs ->
										endCompiler {
											plus(line(lhs arrowTo rhs)).plusCompiler(ret)
										}
									}
								}
							}
						}
					else ->
						emptyType.plusCompiler { rhsType ->
							plus(token.begin.string fieldTo rhsType).plusCompiler(ret)
						}
				}
			is EndToken ->
				ret(this)
		}
	}

fun Choice.plusCompiler(ret: Ret<Choice>): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken ->
				error("$token")
			is BeginToken ->
				emptyType.plusCompiler { rhs ->
					plus(token.begin.string fieldTo rhs).plusCompiler(ret)
				}
			is EndToken ->
				ret(this)
		}
	}
