package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewSponsorBinding
import com.vanced.manager.model.SponsorModel
import com.vanced.manager.ui.viewmodels.HomeViewModel

class SponsorAdapter(
    context: Context,
    private val viewModel: HomeViewModel,
    //private val json: ObservableField<JsonObject?>
) : RecyclerView.Adapter<SponsorAdapter.LinkViewHolder>() {

    private val brave = SponsorModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_brave),
        "Brave",
        "https://vancedapp.com/brave"
    )

    private val adguard = SponsorModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_adguard),
        "AdGuard",
        "https://adguard.com/?aid=31141&source=manager"
    )

    val sponsors = arrayListOf(brave, adguard)

    inner class LinkViewHolder(private val binding: ViewSponsorBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        val logo = binding.sponsorLogo
        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.sponsor = sponsors[position]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSponsorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(position)
        holder.logo.setImageDrawable(sponsors[position].logo)
    }

    override fun getItemCount(): Int = 2

//    fun getCountryFromIP(ipAddress: String?): String? {
//        val db = context.assets.open("GeoLite2-Country.mmdb")
//        val reader = DatabaseReader.Builder(db).build()
//        val inetIp = InetAddress.getByName(ipAddress)
//        return reader.country(inetIp).country.isoCode
//    }
//
//    init {
//        json.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
//            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                val wm = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
//                val ip: String = formatIpAddress(wm!!.connectionInfo.ipAddress)
//                val promotedTiers = json.get()?.array<String>("tier2")?.value!! + json.get()?.array<String>("tier3")?.value!!
//                if (promotedTiers.any { getCountryFromIP(ip)?.contains(it)!! })
//                    sponsors.removeAt(1)
//            }
//
//        })
//    }

}