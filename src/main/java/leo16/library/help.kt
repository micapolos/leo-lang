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
						item { help }
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
}