package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val url = dictionary_ {
	reflection.import

	import {
		dictionary {
			url.kotlin.class_
			is_ { "leo.base.UrlKt".text.name.class_ }

			url.class_
			is_ { "java.net.URL".text.name.class_ }

			string.url.constructor
			is_ {
				url.class_
				constructor {
					parameter {
						list {
							"java.lang.String".text.name.class_
						}
					}
				}
			}

			url.get.method.is_ {
				url.kotlin.class_
				method {
					name { "get".text }
					parameter { list { url.class_ } }
				}
			}
		}
	}

	any.text.url
	gives {
		string.url.constructor
		invoke { parameter { list { url.text.native } } }
		url
	}

	any.url.get
	gives {
		url.get.method
		invoke { parameter { list { get.url.native } } }
		text.get
	}
}