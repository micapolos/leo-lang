package leo16.library.native

import leo15.dsl.*
import leo16.compile_

val url = compile_ {
	use { reflection.library }

	url.class_
	is_ { "java.net.URL".text.name.class_ }

	url.kotlin.class_
	is_ { "leo.base.UrlKt".text.name.class_ }

	string.url.constructor
	is_ {
		url.class_
		constructor {
			parameter {
				list {
					item { "java.lang.String".text.name.class_ }
				}
			}
		}
	}

	url.get.protocol.method.is_ {
		url.class_.method {
			name { "getProtocol".text }
			parameter { empty.list }
		}
	}

	url.get.user.info.method.is_ {
		url.class_.method {
			name { "getUserInfo".text }
			parameter { empty.list }
		}
	}

	url.get.host.method.is_ {
		url.class_.method {
			name { "getHost".text }
			parameter { empty.list }
		}
	}

	url.get.port.method.is_ {
		url.class_.method {
			name { "getPort".text }
			parameter { empty.list }
		}
	}

	url.get.path.method.is_ {
		url.class_.method {
			name { "getPath".text }
			parameter { empty.list }
		}
	}

	url.get.query.method.is_ {
		url.class_.method {
			name { "getQuery".text }
			parameter { empty.list }
		}
	}

	url.get.ref.method.is_ {
		url.class_.method {
			name { "getRef".text }
			parameter { empty.list }
		}
	}

	url.get.method.is_ {
		url.kotlin.class_
		method {
			name { "get".text }
			parameter { list { item { url.class_ } } }
		}
	}
}