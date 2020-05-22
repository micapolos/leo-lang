package leo16.library

import leo15.dsl.*
import leo16.compile_

val help = compile_ {
	help
	is_ {
		help {
			command {
				list {
					item { quote { clear } }
					item { quote { leonardo } }
					item { quote { definition.list } }
				}
			}
			library {
				list {
					item { quote { use { reflection } } }
					item { quote { use { approximate.math } } }
				}
			}
			manual {
				url { "https://github.com/micapolos/leo-lang/wiki/Leonardo-language".text }
			}
		}
	}

	dictionary.help
	is_ {
		ok
	}
}