import gg.rsmod.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.content.interfaces.options.OptionsTab

fun bind_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = child) {
        plugin(this)
    }
}

on_login {
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 6, range = 1..4, setting = 2) // Player option priority
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 7, range = 1..4, setting = 2) // Npc option priority
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 55, range = 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 84, range = 1..3, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 69, range = 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 82, range = 1..4, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 81, range = 1..5, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 83, range = 1..5, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 41, range = 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 23, range =  0..21, setting = InterfaceEvent.ClickOp1)
}

/**
 * Changing display modes (fixed, resizable).
 */
set_window_status_logic {
    val change = player.attr[DISPLAY_MODE_CHANGE_ATTR]
    val mode = when (change) {
        2 -> if (player.getVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT) == 1) DisplayMode.RESIZABLE_LIST else DisplayMode.RESIZABLE_NORMAL
        else -> DisplayMode.FIXED
    }
    player.toggleDisplayInterface(mode)
}

bind_setting(child = 84) {
    //val slot = player.getInteractingSlot()
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!
    val mode = when (slot) {
        2 -> {
            player.setVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT, 0)
            DisplayMode.RESIZABLE_NORMAL
        }
        3 -> {
            player.setVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT, 1)
            DisplayMode.RESIZABLE_LIST
        }
        else -> DisplayMode.FIXED
    }
    if(!(mode.isResizable() && player.interfaces.displayMode.isResizable()))
    player.runClientScript(3998, slot-1)
    player.toggleDisplayInterface(mode)
}

bind_setting(child = 81) {
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!.toInt()-1
    player.setVarp(Varp.PLAYER_ATTACK_PRIORITY_VARP, slot)
}
bind_setting(child = 82) {
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!.toInt()-1
    player.setVarp(Varp.NPC_ATTACK_PRIORITY_VARP, slot)
}


/**
 * This should be moved to a new class.
 * ['Bond_interface'] or something alike
 */
bind_setting(child = OptionsTab.BOND_BUTTON_ID) {
    with (player) {
        openInterface(interfaceId = 65, dest = InterfaceDestination.MAIN_SCREEN)
        runClientScript(2498, 1, 0, 0)
        runClientScript(2524, -1, -1)
        setComponentText(interfaceId = 65, component = 25, text = "Redeem bonds for packages of <col=ffffff>14, 29 or 45 days</col>, or<br>up to <col=ffffff>a year</col> of Premier Membership.")
        runClientScript(3650, 0)
        runClientScript(733, 0,0,0,0,0,0,0,0)
    }
}
on_button(65, 90) {
    player.setVarbit(Varbit.BOND_INTERFACE_FOCUS_TAB, 0)
}
on_button(65, 89) {
    player.setVarbit(Varbit.BOND_INTERFACE_FOCUS_TAB, 1)
}

val withDrawFromPouch = setOf(103, 99, 95, 110)
withDrawFromPouch.forEach {
    on_button(65, it) {
        player.message("This should say: Your inventory doesn't contain any of those bonds. But i'd would love to tell u fuck you.")
    }
}

/**
 * Toggle run mode
 */
bind_setting(child = OptionsTab.RUN_MODE_BUTTON_ID) {
    player.toggleVarp(Varp.RUN_MODE_VARP)
}
/**
 * Accept aid toggle.
 */
bind_setting(child = OptionsTab.ACCEPT_AID_BUTTON_ID) {
    player.toggleVarbit(Varbit.ACCEPT_AID_VARBIT)
}

/**
 * All settings button
 */
bind_setting(child = OptionsTab.ALL_SETTINGS_BUTTON_ID) {
    if (!player.lock.canInterfaceInteract()) {
        return@bind_setting
    }

    player.message("Opening All settings")

    val main_parent = getDisplayComponentId(player.interfaces.displayMode)
    val main_child = getChildId(InterfaceDestination.MAIN_SCREEN, player.interfaces.displayMode)
    val world_child = getChildId(InterfaceDestination.WORLD_MAP, player.interfaces.displayMode)

    if(player.interfaces.isOccupied(main_parent, main_child) || player.interfaces.isOccupied(main_parent, world_child)){
        player.message("Please finish what you are doing before opening the settings.")
        return@bind_setting
    }
    player.openInterface(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, parent = main_parent, child = world_child)
    player.setInterfaceEvents(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = 21, range = 0..147, setting = 2)
    player.setInterfaceEvents(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = 23, range = 0..7, setting = 2)
    player.setInterfaceEvents(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = 19, range = 0..264, setting = 2)
    player.setInterfaceEvents(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = 28, range = 0..41, setting = 2)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 5) {
    player.toggleVarbit(Varbit.PK_PREVENT_SKULL)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 87) {
    player.toggleVarbit(Varbit.DISABLE_ZOOM)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 30) {
    if (player.getVarp(Varp.AUDIO_MUSIC_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_MUSIC_VOLUME, player.getVarbit(Varbit.MUSIC_SLIDER_POSITION_LOCK))
    } else {
        player.setVarbit(Varbit.MUSIC_SLIDER_POSITION_LOCK, player.getVarp(Varp.AUDIO_MUSIC_VOLUME))
        player.setVarp(Varp.AUDIO_MUSIC_VOLUME, 0)
    }
}
on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 44) {
    if (player.getVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, player.getVarbit(Varbit.AUDIO_SOUND_SLIDER_POSITION_LOCK))
    } else {
        player.setVarbit(Varbit.AUDIO_SOUND_SLIDER_POSITION_LOCK, player.getVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME))
        player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, 0)
    }
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 58) {
    if (player.getVarp(Varp.AUDIO_AREA_SOUND_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, player.getVarbit(Varbit.AREA_SOUND_SLIDER_POSITION_LOCK))
    } else {
        player.setVarbit(Varbit.AREA_SOUND_SLIDER_POSITION_LOCK, player.getVarp(Varp.AUDIO_AREA_SOUND_VOLUME))
        player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, 0)
    }
}


on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 106) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 0)
}
on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 111) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 1)
}
on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 112) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 2)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 41) {
    player.playSound(Sound.INTERFACE_SELECT1,100)
    player.setVarp(Varp.AUDIO_MUSIC_VOLUME, player.getInteractingSlot() * 5)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 55) {
    player.playSound(Sound.INTERFACE_SELECT1,100)
    player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, player.getInteractingSlot() * 5)
}

on_button(OptionsTab.SETTINGS_INTERFACE_TAB, 69) {
    player.playSound(Sound.INTERFACE_SELECT1,100)
    player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, player.getInteractingSlot() * 5)
}
