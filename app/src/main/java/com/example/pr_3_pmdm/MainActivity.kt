package com.example.pr_3_pmdm

import android.annotation.SuppressLint
import android.content.Context
import android.media.audiofx.BassBoost.OnParameterChangeListener
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //usaremos estas variables mas adelante y fuera del metodo onCreate()
    lateinit var spinner: Spinner
    lateinit var radioGroup: RadioGroup
    lateinit var seekBar : SeekBar
    lateinit var textSatisfaccion : TextView
    lateinit var switchSuscribirse: Switch
    lateinit var botonGuardar : Button
    lateinit var textVisualizacion : TextView
    lateinit var checkBoxLectura : CheckBox
    lateinit var checkBoxDeporte : CheckBox
    lateinit var checkBoxMusica : CheckBox
    lateinit var checkBoxArte : CheckBox
    lateinit var textNombre : EditText
    lateinit var textApellido : EditText
    lateinit var textCorreo : EditText
    var eleccionSuscrito = ".."


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //instanciamos elementos graficos
        radioGroup = findViewById(R.id.radioGroupSexo)
        botonGuardar = findViewById(R.id.buttonGuardar)
        textVisualizacion = findViewById(R.id.textViewVisualizacionPerfil)
        checkBoxLectura = findViewById(R.id.checkBoxLectura)
        checkBoxDeporte = findViewById(R.id.checkBoxDeporte)
        checkBoxMusica = findViewById(R.id.checkBoxMusica)
        checkBoxArte = findViewById(R.id.checkBoxArte)

        textNombre = findViewById(R.id.editTextNombre)
        //nos aseguramos que al iniciar la app, el campo nombre este seleccionado por defecto
        textNombre.requestFocus()

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(textNombre, InputMethodManager.SHOW_IMPLICIT)

        textApellido = findViewById(R.id.editTextApellido)
        textCorreo = findViewById(R.id.editTextTextCorreoElectronico)

        //Spinner y su adaptador
        spinner = findViewById(R.id.spinnerPaises)
        //Al adaptador le pasao como parametro esta actividad y el array de ciudades
        //guardado en String
        ArrayAdapter.createFromResource(
            this,
            R.array.ciudades_array,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            //se indica el layaout para visualizar en dropView
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //se aplica el spinner al adaptador
            spinner.adapter = adapter
        }
        //fijacion del listener
        spinner.onItemSelectedListener = this

        //seekBar y su visualizacion
        seekBar = findViewById(R.id.seekBarSatisfaccion)
        textSatisfaccion = findViewById(R.id.textViewValorSatisfaccion)

        //indicamos el rango de valor del seekBar
        seekBar.max = 10
        //fijacion del listener:
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            //progeso : Int es el valor al ir moviendo el seekBar, guardamos aqui el valor
            override fun onProgressChanged (SeekBar : SeekBar?, progreso : Int,fromUser: Boolean ){
                //actualizamos el textSatisfaccion:
                textSatisfaccion.setText("$progreso/10")
            }
            //metodo abstracto que notifica cuando el usuario ha empezado a tocar el seekbar -> no lo uso
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            //metodo abstracto que notifica cuando el usuario ha terminado de tocar el seekbar -> no lo uso
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        switchSuscribirse = findViewById(R.id.switchSuscribirse)
        switchSuscribirse.setOnCheckedChangeListener{_,isChecked ->

            if (isChecked){
                eleccionSuscrito = "Suscrito"
            }else {
                eleccionSuscrito = "No Suscrito"
            }
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        visualizacion()

        //
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /**
     * Funcion que mostrara la informacion al presionar el botonGuardar
     */
    @SuppressLint("ServiceCast")
    private fun visualizacion (){
        //incorporamos a esta funcion el botonGuardar
        botonGuardar.setOnClickListener {

            val preferencia = validadorCheckBox()
            //guardamos la informacion introducida en los textEdit del formulario
            val nombre = textNombre.text.toString()
            val apellido = textApellido.text.toString()
            val correo = textCorreo.text.toString()
            val ciudad = spinner.selectedItem.toString()
            val sexo = usoRadioBoton()
            val nivelSatisfaccion = textSatisfaccion.text.toString()

            //vamos a controlar que los campos a introducir no esten vacios
            if (textNombre.text.toString().isEmpty()){
                mensajeError("El campo Nombre esta vacio")

            }else if (textApellido.text.toString().isEmpty()){
                mensajeError("El campo Apellido esta vacio")

            //vamos a introducir en este condicional la funcion de validarCorreo, si devuelve false esq no cumple con el validador
            }else if(validarCorrreo(textCorreo.text.toString())!= true){
                mensajeError("Correo introducido no valido")
            } else {
                val mensajeVisualizacion =
                    "Nombre: $nombre\nApellido: $apellido\nCorreo Electronico: $correo\nCiudad:" +
                            " $ciudad\nPreferencias: $preferencia\nSexo: $sexo\nNivel Satisfaccion: $nivelSatisfaccion\nSuscripción: $eleccionSuscrito "

                //enviamos la infomacion a la visuazliccion del textView
                textVisualizacion.setText(mensajeVisualizacion)
            }
        }
    }

    /**
     *Funcion que cuya logica controla si los checkBox son activados o no
     * @return string
     */
    private fun validadorCheckBox() : String{
        //Log.d("CHECK", "${checkBoxLectura.isChecked}-${checkBoxDeporte.isChecked}-${checkBoxMusica.isChecked}-${checkBoxArte.isChecked}")
        var preferenciasConcatenadas = ","
        if (checkBoxLectura.isChecked){
            preferenciasConcatenadas += checkBoxLectura.text.toString() + " , "
        }
        if (checkBoxMusica.isChecked){
            preferenciasConcatenadas += checkBoxMusica.text.toString()+ " , "
        }
        if(checkBoxDeporte.isChecked){
            preferenciasConcatenadas += checkBoxDeporte.text.toString() + " , "
        }
        if(checkBoxArte.isChecked){
            preferenciasConcatenadas += checkBoxArte.text.toString()
        }
        return preferenciasConcatenadas
    }

    /**
     * Funcion que envia un mensaje de error usando snackbar cuando el campo se deja vacio
     * @param mensaje
     * @return String
     */
    private fun mensajeError (mensaje : String){
        Snackbar.make (findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Funcion que envia un mensaje de error usando snackbar cuando el correo no es validado
     * @param email
     * @return Boolean
     */
    private fun validarCorrreo (email : String): Boolean{
        //^ : indica lel inicio de cadena
        // A-Za-z : permite letras minus y mayus (Aa -> zZ),
        // 0-9 : permite numeros del 0 al 9,
        // +_.- : permite signo + _.- ,
        // + (final):  puede repetirse los caracteres anteriores
        //@ : debe estar presente @ para la parte del dominio
        //(.+)$ : representa el dominio, coincide con cualquier caracter salvo un salto de linea hasta
        // el final de la cadena
        val validadorRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

        // Verificamos si el correo electrónico coincide con la expresión regular y devuelve true en ese caso
        return validadorRegex.matches(email)
    }

    /**
     * Funcion que controla el uso de los radioButon
     * @return devuelve una lista de string con el contenido texto de los radioButon
     */
//
    private fun usoRadioBoton (): String{

        //guardamos la opcion que se ha seleccionado
        //radioGroup.checkedRadioButtonId devuelve -1 cuando no se ha seleccionado nada
        val seleccionRadioButtonId = radioGroup.checkedRadioButtonId
        //si la opccion no es vacia:
        if (seleccionRadioButtonId != -1){
            //tomamos la opcion seleccionada
            val seleccionRadioButton = findViewById<RadioButton>(seleccionRadioButtonId)
            //Escribo en una variable string la opcion seleccionada
            val opcionSeleccionada = seleccionRadioButton.text.toString()

            return opcionSeleccionada
        }else{
            return "No ha seleccionado un sexo"
        }
    }

}