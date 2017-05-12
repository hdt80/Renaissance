package net.hungerstruck.renaissance.util

import com.google.common.io.Files
import net.hungerstruck.renaissance.RLogger
import java.io.File
import java.io.IOException

/**
 * MapUtils ported to Kotlin.
 */
object FileUtil {

    /**
     * Copy a file to another location
     *
     * @param source File to be copied from
     * @param destination File to be copied to. If force is not true this File should not exist
     * @param force If creation of the destination be forced if destination already exists
     *
     * @throws IllegalAccessException If the source doesn't exist, or if destination does exist with the force flag false
     */
    fun copy(source: File, destination: File, force: Boolean = false) {
        if (!source.exists()) {
            throw IllegalArgumentException("Source (" + source.path + ") doesn't exist.")
        }

        if (!force && destination.exists()) {
            throw IllegalArgumentException("Destination (" + destination.path + ") exists.")
        }

        if (source.isDirectory) {
            copyDirectory(source, destination)
        } else {
            copyFile(source, destination)
        }
    }

    /**
     * Copy a world folder to a new location
     *
     * @param from File where the world exists
     * @param to Where the world should be copied to
     */
    fun copyWorldFolder(from: File, to: File) {
        FileUtil.copy(from, to)
        FileUtil.delete(File(to, "session.lock"))
        FileUtil.delete(File(to, "uid.dat"))
        FileUtil.delete(File(to, "players"))
    }

    private fun copyDirectory(source: File, destination: File) {
        if (!destination.mkdirs()) {
            throw IOException("Failed to create destination directories")
        }

        val files = source.listFiles()

        for (file in files) {
            if (file.isDirectory) {
                copyDirectory(file, File(destination, file.name))
            } else {
                copyFile(file, File(destination, file.name))
            }
        }
    }

    private fun copyFile(source: File, destination: File) {
        Files.copy(source, destination)
    }

    /**
     * Delete a file. If f is a directory each file within the directory will also be deleted
     *
     * @param f File to delete
     */
    fun delete(f: File) {
        if (!f.exists()) {
            return
        }

        if (f.isDirectory) {
            for (c in f.listFiles()!!) {
                delete(c)
            }
        }

        try {
            java.nio.file.Files.delete(f.toPath())
        } catch (e: IOException) {
            RLogger.error("IOException: ${e.message}")
        }
    }
}
