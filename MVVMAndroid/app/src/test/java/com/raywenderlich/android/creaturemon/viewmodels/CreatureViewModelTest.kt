package com.raywenderlich.android.creaturemon.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureAttributes
import com.raywenderlich.android.creaturemon.model.CreatureGenerator
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CreatureViewModelTest {
    private lateinit var creatureViewModel: CreatureViewModel
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockGenerator: CreatureGenerator

    @Mock
    lateinit var repository: CreatureRepository
    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        creatureViewModel = CreatureViewModel(mockGenerator, repository)
    }

    @Test
    fun testSetupCreature(){
        val attributes = CreatureAttributes(10, 3, 7)
        val stubCreature = Creature(attributes, 87, "Test creature")
        `when`(mockGenerator.generateCreature(attributes)).thenReturn(stubCreature)
        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7

        creatureViewModel.updateCreature()

        assertEquals(stubCreature, creatureViewModel.creature)
    }
    @Test
    fun textFailureOnCreatureWithBlankName(){
        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7
        creatureViewModel.name = ""
        val failureSavingCreature = creatureViewModel.failureSavingCreature()
        assertEquals(false, failureSavingCreature)
    }
    @Test
    fun testCantSaveCreatureWithoutStrength(){
        creatureViewModel.intelligence = 10
        creatureViewModel.strength = 0
        creatureViewModel.endurance = 7
        creatureViewModel.name = "Creature Without Strength"
        val creatureWithoutStrength = creatureViewModel.failureSavingCreature()
        assertEquals(false, creatureWithoutStrength)
    }
}