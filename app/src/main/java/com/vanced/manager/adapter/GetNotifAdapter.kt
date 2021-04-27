package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewNotificationSettingBinding
import com.vanced.manager.model.NotifModel
import com.vanced.manager.utils.defPrefs

class GetNotifAdapter(private val context: Context) :
    RecyclerView.Adapter<GetNotifAdapter.GetNotifViewHolder>() {

    private val prefs = context.defPrefs

    private val vanced = NotifModel(
        "Vanced-Update",
        context.getString(R.string.push_notifications, context.getString(R.string.vanced)),
        context.getString(R.string.push_notifications_summary, context.getString(R.string.vanced)),
        "vanced_notifs"
    )
    private val music = NotifModel(
        "Music-Update",
        context.getString(R.string.push_notifications, context.getString(R.string.music)),
        context.getString(R.string.push_notifications_summary, context.getString(R.string.music)),
        "music_notifs"
    )
    private val microg = NotifModel(
        "MicroG-Update",
        context.getString(R.string.push_notifications, context.getString(R.string.microg)),
        context.getString(R.string.push_notifications_summary, context.getString(R.string.microg)),
        "microg_notifs"
    )

    private val apps = arrayOf(vanced, music, microg)

    inner class GetNotifViewHolder(val binding: ViewNotificationSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val switch = binding.notifSwitch
        fun bind(position: Int) {
            val app = apps[position]
            with(binding.notifSwitch) {
                setKey(app.key)
                setSummary(app.switchSummary)
                setTitle(app.switchTitle)
                setDefaultValue(true)
                with(prefs) {
                    setChecked(
                        getBoolean(
                            "enable_" + app.key.substringBefore("_"),
                            true
                        ) && getBoolean(app.key, true)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetNotifViewHolder {
        val view =
            ViewNotificationSettingBinding.inflate(LayoutInflater.from(context), parent, false)
        return GetNotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GetNotifViewHolder, position: Int) {
        holder.bind(position)
        holder.switch.setOnCheckedListener { _, isChecked ->
            when (isChecked) {
                true -> FirebaseMessaging.getInstance().subscribeToTopic(apps[position].topic)
                false -> FirebaseMessaging.getInstance().unsubscribeFromTopic(apps[position].topic)
            }
        }
    }

    override fun getItemCount(): Int = 3

}