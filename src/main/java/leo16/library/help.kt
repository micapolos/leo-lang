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
						item { debug }
						item { help }
						item { leonardo }
					}
				}
				library {
					list {
						item { use { approximate.math } }
						item { use { reflection } }
					}
				}
			}
		}
	}
}