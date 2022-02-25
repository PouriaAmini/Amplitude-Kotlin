package com.amplitude.core

import com.amplitude.core.events.BaseEvent
import com.amplitude.core.events.Identify
import com.amplitude.core.events.Revenue
import com.amplitude.core.events.RevenueEvent
import com.amplitude.core.platform.Plugin
import com.amplitude.core.platform.Timeline
import com.amplitude.core.platform.plugins.AmplitudeDestination
import com.amplitude.core.platform.plugins.ContextPlugin
import kotlinx.coroutines.*
import java.util.concurrent.Executors

open class Amplitude internal constructor(
    val configuration: Configuration,
    val store: State,
    val amplitudeScope: CoroutineScope = CoroutineScope(SupervisorJob()),
    val amplitudeDispatcher: CoroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher(),
    val networkIODispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
    val storageIODispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
){

    internal val timeline: Timeline
    val storage: Storage
    val logger: Logger

    init {
        require(configuration.isValid()) { "invalid configuration" }
        timeline = Timeline().also { it.amplitude = this }
        storage = configuration.storageProvider.getStorage(this)
        logger = configuration.loggerProvider.getLogger(this)
        build()
    }

    /**
     * Public Constructor
     */
    constructor(configuration: Configuration) : this(configuration, State())

    open fun build() {
        add(ContextPlugin())
        add(AmplitudeDestination())

        amplitudeScope.launch (amplitudeDispatcher) {

        }
    }

    @Deprecated("Please use 'track' instead.", ReplaceWith("track"))
    fun logEvent(event: BaseEvent) {
        track(event)
    }

    fun track(event: BaseEvent, callback: ((BaseEvent) -> Unit)? = null) {
        process(event)
    }

    fun identify(identify: Identify) {

    }

    fun identify(userId: String) {

    }

    fun groupIdentify(identify: Identify) {

    }

    fun setGroup(groupType: String, groupName: Array<String>) {

    }

    @Deprecated("Please use 'revenue' instead.", ReplaceWith("revenue"))
    fun logRevenue(revenue: Revenue) {
        revenue(revenue)
    }

    /**
     * Create a Revenue object to hold your revenue data and properties,
     * and log it as a revenue event using this method.
     */
    fun revenue(revenue: Revenue) {
        if (!revenue.isValid()) {
            return
        }
        revenue(revenue.toRevenueEvent())
    }

    /**
     * Log a Revenue Event
     */
    fun revenue(event: RevenueEvent) {
        process(event)
    }

    fun process(event: BaseEvent) {
        amplitudeScope.launch(amplitudeDispatcher) {
            timeline.process(event)
        }
    }

    fun add(plugin: Plugin) : Amplitude {
        this.timeline.add(plugin)
        return this
    }

    fun remove(plugin: Plugin): Amplitude {
        this.timeline.remove(plugin)
        return this
    }

    fun flush() {

    }
}