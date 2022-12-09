package com.whacky.cs315finalcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.whacky.cs315finalcalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    var saveResult: String? = null

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
        binding.dataTv.text = ""

    }

    fun onAllClearClick(view: View) {

        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false

        binding.resultTv.visibility = View.GONE
        binding.btnSave.visibility = View.GONE

    }

    fun onDigitClick(view: View) {

        if (stateError) {

            binding.dataTv.text = (view as Button).text
            stateError = false

        } else {
            binding.dataTv.append((view as Button).text)
        }

        lastNumeric = true

    }

    fun onOperatorClick(view: View) {

        if (!stateError && lastNumeric) {

            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false

        }

    }

    fun onBackClick(view: View) {

        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try {
            val lastChar = binding.dataTv.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("Last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {

        binding.dataTv.text = ""
        lastNumeric = false

    }

    fun onSaveClick(view: View) {

        saveResult = binding.resultTv.text as String?

        binding.btnPaste.visibility = View.VISIBLE

        binding.btnSave.visibility = View.GONE

    }

    fun onPasteClick(view: View) {

        binding.dataTv.append(saveResult)

        binding.btnPaste.visibility = View.GONE

        lastNumeric = true

    }

    fun onEqual() {

        if (lastNumeric && !stateError) {

            val txt = binding.dataTv.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {

                val result = expression.evaluate()

                binding.resultTv.visibility = View.VISIBLE

                binding.resultTv.text = result.toString()
            } catch (ex: java.lang.ArithmeticException) {

                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }

        binding.btnSave.visibility = View.VISIBLE

        binding.dataTv.text = ""

    }

}