package net.hungerstruck.renaissance.spec.mapspec

import net.hungerstruck.renaissance.spec.SpecBuilderClassLoader
import java.io.File

class MapBuilderClassLoader(path: File, override val className: String = "Map") : SpecBuilderClassLoader(path)
