package leo13.js.compiler

fun function(functions: Functions, ret: (Function) -> Compiler): Compiler =
	begin("given") {
		types { parameterTypes ->
			begin("gives") {
				typed(functions) { bodyTyped ->
					end {
						ret(parameterTypes gives bodyTyped)
					}
				}
			}
		}
	}
