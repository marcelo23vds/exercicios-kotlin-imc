package com.aulasandroid.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aulasandroid.calculadoraimc.ui.theme.CalculadoraIMCTheme
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraIMCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IMCScreen(modifier: Modifier = Modifier) {

    // O remember "diz" ao Compose:
    // "quando você for desenhar essa tela de novo, lembra do valor que estava aqui"

    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imcValor by remember { mutableStateOf(0.0) }
    var classificacao by remember { mutableStateOf("") }
    var corResultado by remember { mutableStateOf(Color.Transparent) }
    var mostrarResultado by remember { mutableStateOf(false) }

    val corVerde = Color(0xFF43A047)
    val corLaranja = Color(0xFFFB8C00)
    val corVermelha = Color(0xFFE53935)
    val corCabecalho = Color(0xFF56B5DD)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)) // Fundo cinza bem clarinho
    ) {
        // Cabeçalho azul
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(corCabecalho)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.bmi),
                contentDescription = "IMC Icon",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Calculadora IMC",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Card Formulário
        Column(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seus dados",
                        fontSize = 20.sp,
                        color = corCabecalho,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = altura,
                        onValueChange = { altura = it },
                        label = { Text("Altura (cm)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botão Calcular
                    Button(
                        onClick = {
                            val pesoNum = peso.replace(",", ".").toDoubleOrNull()
                            val alturaCm = altura.replace(",", ".").toDoubleOrNull()

                            if (pesoNum != null && alturaCm != null && alturaCm > 0) {
                                // Converte cm para metros
                                val alturaM = alturaCm / 100
                                val imc = pesoNum / (alturaM * alturaM)
                                imcValor = imc

                                when {
                                    imc < 18.5 -> {
                                        classificacao = "Abaixo do peso."
                                        corResultado = corVermelha
                                    }
                                    imc < 25.0 -> {
                                        classificacao = "Peso ideal."
                                        corResultado = corVerde
                                    }
                                    imc < 30.0 -> {
                                        classificacao = "Levemente acima do peso."
                                        corResultado = corLaranja
                                    }
                                    imc < 35.0 -> {
                                        classificacao = "Obesidade grau I."
                                        corResultado = corVermelha
                                    }
                                    imc < 40.0 -> {
                                        classificacao = "Obesidade grau II."
                                        corResultado = corVermelha
                                    }
                                    else -> {
                                        classificacao = "Obesidade grau III."
                                        corResultado = corVermelha
                                    }
                                }
                                mostrarResultado = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = corCabecalho)
                    ) {
                        Text("CALCULAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão Limpar
                    OutlinedButton(
                        onClick = {
                            peso = ""
                            altura = ""
                            mostrarResultado = false
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("LIMPAR", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Card Resultado
        if (mostrarResultado) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                // cor dinâmica
                colors = CardDefaults.cardColors(containerColor = corResultado),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val formatador = DecimalFormat("#.0")
                    Text(
                        text = "${formatador.format(imcValor)}   $classificacao",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}