define
	if: any
	then: any
	else: any
	writes: given if body meta: switch
		meta: true
		gives: given then body
		meta: false
		gives: given else body
if: boolean: false
then: dupa
else: zupa
