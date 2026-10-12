package com.example.fastfoodorderingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fastfoodorderingapp.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var selectedMainMeal: String? = null
    private var selectedSideDishes: ArrayList<String> = arrayListOf()
    private var selectedDrink: String? = null

    private val mainMealLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedMainMeal = result.data?.getStringExtra(MainMealActivity.EXTRA_MAIN_MEAL)
            updateUI()
        }
    }

    private val sideDishesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val list = result.data?.getStringArrayListExtra(SideDishesActivity.EXTRA_SIDE_DISHES)
            selectedSideDishes = list ?: arrayListOf()
            updateUI()
        }
    }

    private val drinkLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedDrink = result.data?.getStringExtra(DrinkActivity.EXTRA_DRINK)
            updateUI()
        }
    }

    private val confirmLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseMainMeal.setOnClickListener {
            val intent = Intent(this, MainMealActivity::class.java).apply {
                putExtra(MainMealActivity.EXTRA_MAIN_MEAL, selectedMainMeal)
            }
            mainMealLauncher.launch(intent)
        }

        binding.btnChooseSideDishes.setOnClickListener {
            val intent = Intent(this, SideDishesActivity::class.java).apply {
                putStringArrayListExtra(SideDishesActivity.EXTRA_SIDE_DISHES, selectedSideDishes)
            }
            sideDishesLauncher.launch(intent)
        }

        binding.btnChooseDrink.setOnClickListener {
            val intent = Intent(this, DrinkActivity::class.java).apply {
                putExtra(DrinkActivity.EXTRA_DRINK, selectedDrink)
            }
            drinkLauncher.launch(intent)
        }

        binding.btnOrder.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java).apply {
                putExtra(ConfirmActivity.EXTRA_MAIN_MEAL, selectedMainMeal)
                putStringArrayListExtra(ConfirmActivity.EXTRA_SIDE_DISHES, selectedSideDishes)
                putExtra(ConfirmActivity.EXTRA_DRINK, selectedDrink)
            }
            confirmLauncher.launch(intent)
        }

        updateUI()
    }

    private fun updateUI() {
        val mainText = selectedMainMeal ?: "—"
        val sidesText = if (selectedSideDishes.isNotEmpty()) selectedSideDishes.joinToString(", ") else "—"
        val drinkText = selectedDrink ?: "—"

        binding.tvCurrentSelection.text = "Main: $mainText\n\nSides: $sidesText\n\nDrink: $drinkText"
    }
}
