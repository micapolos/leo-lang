package leo16.library

import leo15.dsl.*
import leo16.compile_

val help = compile_ {
	help
	is_ {
		quote {
			help {
				command {
					list {
						item { clear }
						item { leonardo }
						item { definition.list }
					}
				}
				library {
					list {
						item { use { reflection } }
						item { use { approximate.math } }
					}
				}
				manual {
					url { "https://github.com/micapolos/leo-lang/wiki/Leonardo-language".text }
				}
			}
		}
	}

	dictionary.help
	is_ {
		ok
	}
}