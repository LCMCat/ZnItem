package tech.ccat.znitem

import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import tech.ccat.calevel.api.CaLevelAPI
import tech.ccat.kstats.api.KStatsAPI
import tech.ccat.naskill.api.NaSkillAPI
import tech.ccat.znitem.api.ZnItemAPI
import tech.ccat.znitem.api.ZnItemAPIImpl
import tech.ccat.znitem.command.*
import tech.ccat.znitem.config.ConfigManager
import tech.ccat.znitem.data.PlayerDataManager
import tech.ccat.znitem.enchant.ZnEnchantRegistry
import tech.ccat.znitem.listener.*
import tech.ccat.znitem.lore.LoreRenderer
import tech.ccat.znitem.lore.LoreUpdateManager
import tech.ccat.znitem.provider.VanillaItemProvider
import tech.ccat.znitem.provider.ZnItemStatProvider
import tech.ccat.znitem.reforge.ReforgeRegistry
import tech.ccat.znitem.skill.SkillRegistry
import tech.ccat.znitem.storage.H2Database
import tech.ccat.znitem.storage.PlayerBackupDao
import tech.ccat.znitem.storage.PlayerBackupManager
import tech.ccat.znitem.memento.MementoEditionManager
import tech.ccat.znitem.validation.UUIDRegistry

class ZnItem : JavaPlugin() {

    companion object {
        lateinit var instance: ZnItem
    }

    lateinit var configManager: ConfigManager
    lateinit var dataManager: PlayerDataManager
    lateinit var uuidRegistry: UUIDRegistry
    lateinit var backupManager: PlayerBackupManager
    lateinit var loreUpdateManager: LoreUpdateManager
    lateinit var mementoEditionManager: MementoEditionManager

    lateinit var znItemAPI: ZnItemAPIImpl

    var kstatsAPI: KStatsAPI? = null
    var naSkillAPI: NaSkillAPI? = null
    var caLevelAPI: CaLevelAPI? = null

    private lateinit var h2Database: H2Database
    private lateinit var backupDao: PlayerBackupDao

    private lateinit var znItemStatProvider: ZnItemStatProvider
    private lateinit var vanillaItemProvider: VanillaItemProvider

    private var autoBackupTaskId: Int = -1

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        configManager = ConfigManager().apply { setup() }
        dataManager = PlayerDataManager()
        uuidRegistry = UUIDRegistry()
        loreUpdateManager = LoreUpdateManager().apply { setup() }
        mementoEditionManager = MementoEditionManager()

        h2Database = H2Database()
        h2Database.connect()
        backupDao = PlayerBackupDao(h2Database)
        backupManager = PlayerBackupManager(backupDao)

        ZnEnchantRegistry.setup()
        ReforgeRegistry.setup()
        SkillRegistry.setup()
        LoreRenderer.setup()

        registerListeners()
        registerCommands()

        znItemAPI = ZnItemAPIImpl(this)
        server.servicesManager.register(
            ZnItemAPI::class.java,
            znItemAPI,
            this,
            ServicePriority.Normal
        )

        connectToExternalServices()

        startAutoBackupTask()

        logger.info("ZnItem物品系统已成功启用")

        if (Bukkit.getOnlinePlayers().isNotEmpty()) {
            midInitPlayerData()
        }
    }

    override fun onDisable() {
        stopAutoBackupTask()

        Bukkit.getOnlinePlayers().forEach { player ->
            backupManager.backupPlayer(player, "SHUTDOWN")
        }

        disconnectFromExternalServices()

        h2Database.disconnect()

        server.servicesManager.unregisterAll(this)

        logger.info("ZnItem物品系统已成功禁用")
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(EquipmentListener(), this)
        pm.registerEvents(SkillTriggerListener(), this)
        pm.registerEvents(EnchantDamageListener(), this)
        pm.registerEvents(PlayerJoinListener(), this)
        pm.registerEvents(LoreUpdateListener(), this)
        pm.registerEvents(ItemRestrictionListener(), this)
        pm.registerEvents(DurabilityProtectionListener(), this)
        pm.registerEvents(ItemBreakListener(), this)
        pm.registerEvents(LifeStealListener(), this)
        pm.registerEvents(ConsumableListener(), this)
    }

    private fun registerCommands() {
        getCommand("summonitem")?.setExecutor(SummonItemCommand())
        getCommand("summonitem")?.tabCompleter = SummonItemCommand()
        getCommand("vanillatoznitem")?.setExecutor(VanillaToZnItemCommand())
        getCommand("renameitem")?.setExecutor(RenameItemCommand())
        getCommand("refactoritem")?.setExecutor(RefactorItemCommand())
        getCommand("znench")?.setExecutor(ZnEnchantCommand())
        getCommand("znench")?.tabCompleter = ZnEnchantCommand()
        getCommand("hotpowerbook")?.setExecutor(HotPowerBookCommand())
        getCommand("itemgem")?.setExecutor(ItemGemCommand())
        getCommand("viewnbt")?.setExecutor(ViewNbtCommand())
        getCommand("creativemind")?.setExecutor(CreativeMindCommand())
        getCommand("menditem")?.setExecutor(MendItemCommand())
    }

    private fun connectToExternalServices() {
        kstatsAPI = server.servicesManager.getRegistration(KStatsAPI::class.java)?.provider
        kstatsAPI?.let { api ->
            znItemStatProvider = ZnItemStatProvider()
            api.registerProvider(znItemStatProvider)
            vanillaItemProvider = VanillaItemProvider()
            api.registerProvider(vanillaItemProvider)
            logger.info("已连接到KStats")
        }

        naSkillAPI = server.servicesManager.getRegistration(NaSkillAPI::class.java)?.provider
        naSkillAPI?.let {
            logger.info("已连接到NaSkill")
        }

        caLevelAPI = server.servicesManager.getRegistration(CaLevelAPI::class.java)?.provider
        caLevelAPI?.let {
            logger.info("已连接到CaLevel")
        }
    }

    private fun disconnectFromExternalServices() {
        kstatsAPI?.let { api ->
            if (::znItemStatProvider.isInitialized) {
                api.unregisterProvider(znItemStatProvider)
            }
            if (::vanillaItemProvider.isInitialized) {
                api.unregisterProvider(vanillaItemProvider)
            }
        }
    }

    private fun midInitPlayerData() {
        Bukkit.getOnlinePlayers().forEach { player ->
            dataManager.loadPlayer(player)
            kstatsAPI?.requestUpdate(player)
        }
    }

    private fun startAutoBackupTask() {
        val interval = configManager.pluginConfig.autoBackupInterval * 1200L
        autoBackupTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            this,
            {
                Bukkit.getOnlinePlayers().forEach { player ->
                    backupManager.backupPlayer(player, "AUTO")
                }
            },
            interval,
            interval
        )
    }

    private fun stopAutoBackupTask() {
        if (autoBackupTaskId != -1) {
            Bukkit.getScheduler().cancelTask(autoBackupTaskId)
            autoBackupTaskId = -1
        }
    }
}
