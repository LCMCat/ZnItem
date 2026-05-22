package tech.ccat.znitem.memento

import org.bukkit.configuration.file.YamlConfiguration
import tech.ccat.znitem.ZnItem
import java.io.File

class MementoEditionManager {
    private val plugin = ZnItem.instance
    private val file: File
    private val config: YamlConfiguration
    
    var creativeMindEdition: Int = 0
        private set
    
    init {
        file = File(plugin.dataFolder, "edition.yml")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        config = YamlConfiguration.loadConfiguration(file)
        load()
    }
    
    private fun load() {
        creativeMindEdition = config.getInt("creative-mind", 0)
    }
    
    private fun save() {
        config.set("creative-mind", creativeMindEdition)
        config.save(file)
    }
    
    fun incrementCreativeMind(): Int {
        creativeMindEdition++
        save()
        return creativeMindEdition
    }
}
