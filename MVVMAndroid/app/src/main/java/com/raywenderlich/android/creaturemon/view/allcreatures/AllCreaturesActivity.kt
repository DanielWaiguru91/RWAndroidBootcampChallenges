package com.raywenderlich.android.creaturemon.view.allcreatures

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.raywenderlich.android.creaturemon.R
import com.raywenderlich.android.creaturemon.view.creature.CreatureActivity
import com.raywenderlich.android.creaturemon.viewmodels.AllCreaturesViewModel
import kotlinx.android.synthetic.main.activity_all_creatures.*
import kotlinx.android.synthetic.main.content_all_creatures.*

class AllCreaturesActivity : AppCompatActivity() {

  private val adapter = CreatureAdapter(mutableListOf())
  private val allCreaturesViewModel by lazy {
    ViewModelProviders.of(this).get(AllCreaturesViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_all_creatures)
    setSupportActionBar(toolbar)

    creaturesRecyclerView.layoutManager = LinearLayoutManager(this)
    creaturesRecyclerView.adapter = adapter

    fab.setOnClickListener {
      startActivity(Intent(this, CreatureActivity::class.java))
    }
    allCreaturesViewModel.getAllCreatures().observe(this, Observer { creatures ->
      creatures?.let {
        adapter.updateCreatures(it)
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_clear_all -> {
        allCreaturesViewModel.clearCreatures()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
