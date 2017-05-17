package net.hungerstruck.renaissance.spec

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class inject(val fieldName: String = "")