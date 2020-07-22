package com.raywenderlich.android.creaturemon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import com.raywenderlich.android.creaturemon.model.room.RoomRepository

class AllCreaturesViewModel(private val repository: CreatureRepository = RoomRepository()): ViewModel() {
    private val _allCreatures = MutableLiveData<List<Creature>>()
    private val allCreatures = repository.getAllCreatures()
    fun getAllCreatures() = allCreatures
    fun clearCreatures() = repository.clearAllCreature()
}