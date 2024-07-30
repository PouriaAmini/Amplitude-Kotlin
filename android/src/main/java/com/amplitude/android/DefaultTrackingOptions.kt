package com.amplitude.android

@Suppress("DEPRECATION")
@Deprecated("Use AutocaptureOptions instead", ReplaceWith("AutocaptureOptions"))
open class DefaultTrackingOptions
@JvmOverloads
constructor(
    sessions: Boolean = true,
    appLifecycles: Boolean = false,
    deepLinks: Boolean = false,
    screenViews: Boolean = false,
) {
    // Prebuilt options for easier usage
    companion object {
        @JvmField
        @Deprecated("Use AutocaptureOptions instead", ReplaceWith("AutocaptureOptions(appLifecycles = true, deepLinks = true, screenViews = true, elementInteractions = true)"))
        val ALL =
            DefaultTrackingOptions(
                sessions = true,
                appLifecycles = true,
                deepLinks = true,
                screenViews = true,
            )

        @JvmField
        @Deprecated("Use AutocaptureOptions instead", ReplaceWith("AutocaptureOptions(sessions = false)"))
        val NONE =
            DefaultTrackingOptions(
                sessions = false,
                appLifecycles = false,
                deepLinks = false,
                screenViews = false,
            )
    }

    var sessions: Boolean = sessions
        set(value) {
            field = value
            if (value) {
                autocapture?.add(AutocaptureOption.SESSIONS)
            } else {
                autocapture?.remove(AutocaptureOption.SESSIONS)
            }
        }

    var appLifecycles: Boolean = appLifecycles
        set(value) {
            field = value
            if (value) {
                autocapture?.add(AutocaptureOption.APP_LIFECYCLES)
            } else {
                autocapture?.remove(AutocaptureOption.APP_LIFECYCLES)
            }
        }

    var deepLinks: Boolean = deepLinks
        set(value) {
            field = value
            if (value) {
                autocapture?.add(AutocaptureOption.DEEP_LINKS)
            } else {
                autocapture?.remove(AutocaptureOption.DEEP_LINKS)
            }
        }

    var screenViews: Boolean = screenViews
        set(value) {
            field = value
            if (value) {
                autocapture?.add(AutocaptureOption.SCREEN_VIEWS)
            } else {
                autocapture?.remove(AutocaptureOption.SCREEN_VIEWS)
            }
        }

    private var autocapture: MutableSet<AutocaptureOption>? = null

    internal fun withAutocaptureOptions(autocapture: MutableSet<AutocaptureOption>): DefaultTrackingOptions {
        this.autocapture = autocapture
        return this
    }
}
