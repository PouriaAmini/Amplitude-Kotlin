package com.amplitude.android

import android.content.Context
import com.amplitude.android.utilities.AndroidLoggerProvider
import com.amplitude.android.utilities.AndroidStorageProvider
import com.amplitude.core.Configuration
import com.amplitude.core.EventCallBack
import com.amplitude.core.LoggerProvider
import com.amplitude.core.ServerZone
import com.amplitude.core.StorageProvider
import com.amplitude.core.events.IngestionMetadata
import com.amplitude.core.events.Plan
import com.amplitude.id.FileIdentityStorageProvider
import com.amplitude.id.IdentityStorageProvider

open class Configuration @JvmOverloads constructor(
    apiKey: String,
    val context: Context,
    override var flushQueueSize: Int = FLUSH_QUEUE_SIZE,
    override var flushIntervalMillis: Int = FLUSH_INTERVAL_MILLIS,
    override var instanceName: String = DEFAULT_INSTANCE,
    override var optOut: Boolean = false,
    override var storageProvider: StorageProvider = AndroidStorageProvider(),
    override var loggerProvider: LoggerProvider = AndroidLoggerProvider(),
    override var minIdLength: Int? = null,
    override var partnerId: String? = null,
    override var callback: EventCallBack? = null,
    override var flushMaxRetries: Int = FLUSH_MAX_RETRIES,
    override var useBatch: Boolean = false,
    override var serverZone: ServerZone = ServerZone.US,
    override var serverUrl: String? = null,
    override var plan: Plan? = null,
    override var ingestionMetadata: IngestionMetadata? = null,
    var useAdvertisingIdForDeviceId: Boolean = false,
    var useAppSetIdForDeviceId: Boolean = false,
    var newDeviceIdPerInstall: Boolean = false,
    var trackingOptions: TrackingOptions = TrackingOptions(),
    var enableCoppaControl: Boolean = false,
    var locationListening: Boolean = true,
    var flushEventsOnClose: Boolean = true,
    var minTimeBetweenSessionsMillis: Long = MIN_TIME_BETWEEN_SESSIONS_MILLIS,
    @Deprecated("Please use 'autocapture.sessions' instead.", ReplaceWith("autocapture.sessions"))
    var trackingSessionEvents: Boolean = true,
    @Suppress("DEPRECATION")
    @Deprecated("Use autocapture instead", ReplaceWith("autocapture"))
    var defaultTracking: DefaultTrackingOptions = DefaultTrackingOptions(),
    var autocapture: AutocaptureOptions = AutocaptureOptions(),
    override var identifyBatchIntervalMillis: Long = IDENTIFY_BATCH_INTERVAL_MILLIS,
    override var identifyInterceptStorageProvider: StorageProvider = AndroidStorageProvider(),
    override var identityStorageProvider: IdentityStorageProvider = FileIdentityStorageProvider(),
    var migrateLegacyData: Boolean = true,
    override var offline: Boolean? = false,
    override var deviceId: String? = null,
    override var sessionId: Long? = null,
) : Configuration(
    apiKey,
    flushQueueSize,
    flushIntervalMillis,
    instanceName,
    optOut,
    storageProvider,
    loggerProvider,
    minIdLength,
    partnerId,
    callback,
    flushMaxRetries,
    useBatch,
    serverZone,
    serverUrl,
    plan,
    ingestionMetadata,
    identifyBatchIntervalMillis,
    identifyInterceptStorageProvider,
    identityStorageProvider,
    offline,
    deviceId,
    sessionId,
) {
    companion object {
        const val MIN_TIME_BETWEEN_SESSIONS_MILLIS: Long = 300000
    }

    init {
        @Suppress("DEPRECATION")
        autocapture = AutocaptureOptions(
            // If trackingSessionEvents or defaultTracking.sessions are false (default value is true), it means they are manually set
            sessions = defaultTracking.sessions && trackingSessionEvents && autocapture.sessions,
            appLifecycles = defaultTracking.appLifecycles || autocapture.appLifecycles,
            deepLinks = defaultTracking.deepLinks || autocapture.deepLinks,
            screenViews = defaultTracking.screenViews || autocapture.screenViews,
            elementInteractions = autocapture.elementInteractions,
        )
    }
}
