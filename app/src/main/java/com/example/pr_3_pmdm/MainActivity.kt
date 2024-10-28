package com.example.pr_3_pmdm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
        seekBar = findViewById(R.id.seekBarSatisfaccion)
        textSatisfaccion = findViewById(R.id.textViewValorSatisfaccion)
        switchSuscribirse = findViewById(R.id.switchSuscribirse)
        checkBoxLectura = findViewById(R.id.checkBoxLectura)
        checkBoxDeporte = findViewById(R.id.checkBoxDeporte)
        checkBoxMusica = findViewById(R.id.checkBoxMusica)
        checkBoxArte = findViewById(R.id.checkBoxArte)
        textNombre = findViewById(R.id.editTextNombre)
        textApellido = findViewById(R.id.editTextApellido)
        textCorreo = findViewById(R.id.editTextTextCorreoElectronico)

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
    private fun visualizacion (){

        val eleccionPreferencia = usoCheckBox()

        //incorporamos a esta funcion el botonGuardar
        botonGuardar.setOnClickListener {

            //guardamos la informacion introducida en los textEdit del formulario
            val nombre = textNombre.text.toString()

            //controlamos que el campo nombre no este vacio
            val apellido = textApellido.text.toString()
            val correo = textCorreo.text.toString()
            val ciudad = spinner.selectedItem.toString()
            val eleccionPreferencia1 = eleccionPreferencia


            //vamos a controlar que los campos a introducir no esten vacios
            if (textNombre.text.toString().isEmpty()){
                mensajeError("El campo Nombre esta vacio")
            }else if (textApellido.text.toString().isEmpty()){
                mensajeError("El campo Apellido esta vacio")
            }else if (textApellido.text.toString().isEmpty()){
                mensajeError("El campo Apellido esta vacio")
            //vamos a introducir en este condicional la funcion de validarCorreo, si devuelve false esq no cumple con el validador
            }else if(validarCorrreo(textCorreo.text.toString())!= true){
                mensajeError("Correo introducido no valido")
            } else {
                val mensajeVisualizacion =
                    "Nombre: $nombre\nApellido: $apellido\nCorreo Electronico: $correo\nCiudad:" +
                            " $ciudad\nPreferencias: $eleccionPreferencia1"
                //enviamos la infomacion a la visuazliccion del textView
                textVisualizacion.setText(mensajeVisualizacion)
            }
        }
    }
    /**
     * Funcion que envia un mensaje de error usando snackbar
     * @param mensaje
     */
    private fun mensajeError (mensaje : String){
        Snackbar.make (findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show()
    }
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
     * Funcion que controla el uso de los checkBox
     * @return devuelve una lista de string con el contenido texto de los checkBox
     */
    private fun usoCheckBox () : String {
        //creamos un arrayList para guardar el texto de los checkBox seleccionados
        val eleccionesPreferencias = ArrayList<String>()

        //si alguno de los checkBox es seleccionado, llamamos a la funcion concatenarPreferencias para que gestione
        //la informacion del texto de los checkBox
        checkBoxLectura.setOnCheckedChangeListener { _, isChecked ->
           concatenarPreferencias(checkBoxLectura, eleccionesPreferencias)
        }
        checkBoxDeporte.setOnCheckedChangeListener { _, isChecked ->
            concatenarPreferencias(checkBoxDeporte, eleccionesPreferencias)
        }
        checkBoxMusica.setOnCheckedChangeListener { _, isChecked ->
            concatenarPreferencias(checkBoxMusica, eleccionesPreferencias)
        }
        checkBoxArte.setOnCheckedChangeListener { _, isChecked ->
            concatenarPreferencias(checkBoxArte, eleccionesPreferencias)
        }
        //devolvemos la lista y separamos cada elemento con una coma
        return eleccionesPreferencias.joinToString (", ")

    }

    /**
     * Funcion que guardara en un arrayList el contenido text de los checkBox al ser seleleccinados
     * @param checkBox
     * @param eleccionesPreferencias
     */
    private fun concatenarPreferencias (checkBox : CheckBox, eleccionesPreferencias : ArrayList<String>){
        if (checkBox.isChecked){
            eleccionesPreferencias.add(checkBox.text.toString())
        }
    }
}