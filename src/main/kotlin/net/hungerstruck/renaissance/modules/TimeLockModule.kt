package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import net.hungerstruck.renaissance.xml.toBool
import org.jdom2.Document

/**
 * Created by molenzwiebel on 21-12-15.
 */
class TimeLockModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    val locked: Boolean

    init {
        locked = document.rootElement?.getChild("timelock")?.textNormalize.toBool(defaultValue = false)
    }
}