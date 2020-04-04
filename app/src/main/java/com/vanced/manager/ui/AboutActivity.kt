package com.vanced.manager.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.vanced.manager.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar!!.title = "About"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mattisLigma : TextView = findViewById(R.id.mattis_ligma)

        mattisLigma.setOnClickListener{
            val ligmaurl = "https://youtu.be/LDU_Txk06tM"
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.YT))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(ligmaurl))
        }
    }
}
