package com.example.luzsensor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.Color
/*
### Para que serve cada `import` neste código?

Os `imports` funcionam como uma lista de ferramentas que você está trazendo de fora para dentro do seu código. Sem eles, o Kotlin não saberia o que é um `TextView` ou um `Sensor`.

Aqui está a função de cada um, agrupados por categoria:

#### 1. Ciclo de Vida e Estrutura da Tela (AndroidX)

* **`import android.os.Bundle`**: O `Bundle` é como uma "sacola" que o Android usa para passar dados entre telas ou para salvar o estado do aplicativo (por exemplo, se você girar a tela, ele ajuda a não perder o que estava digitado).
* **`import androidx.appcompat.app.AppCompatActivity`**: É a classe base para as telas (Activities). O prefixo `AppCompat` significa *App Compatibility* (Compatibilidade de Aplicativo). Ela garante que os recursos visuais novos do Android funcionem direito mesmo em celulares bem antigos.

#### 2. Componentes de Interface Visual (UI)

* **`import android.widget.TextView`**: É a ferramenta que permite controlar componentes de texto na tela (para exibir o texto "Luminosidade: X lx").
* **`import androidx.constraintlayout.widget.ConstraintLayout`**: É o tipo de layout (o "esqueleto" da tela) mais moderno e recomendado para Android. Ele permite posicionar os elementos visualmente de forma flexível e responsiva.
* **`import android.graphics.Color`**: Uma classe utilitária para manipular cores no código. É graças a ela que você pode usar comandos amigáveis como `Color.WHITE` ou `Color.DKGRAY`.

#### 3. O Motor dos Sensores (Hardware)

* **`import android.content.Context`**: É o "contexto" do aplicativo. Ele dá acesso aos serviços globais do sistema operacional. No seu código, ele é usado para pedir ao celular o serviço de sensores (`Context.SENSOR_SERVICE`).
* **`import android.hardware.Sensor`**: A classe que representa o sensor físico em si (neste caso, o sensor de luz).
* **`import android.hardware.SensorEvent`**: Sempre que o sensor lê um dado novo, ele gera um "evento". Esse import traz esse evento, que carrega dentro dele o valor numérico em lux (`event.values[0]`).
* **`import android.hardware.SensorEventListener`**: É uma *interface* (um contrato). Ao colocar isso na sua classe, você está prometendo ao Android: *"Olha, meu código sabe escutar o sensor, e eu vou criar a função `onSensorChanged` para reagir quando a luz mudar"*.
* **`import android.hardware.SensorManager`**: O "Gerente dos Sensores". É ele quem liga, desliga e gerencia a comunicação entre o seu aplicativo e os sensores do aparelho.

---

### Qual é a diferença entre `android.*` e `androidx.*`?

Essa é uma das dúvidas mais comuns de quem está aprendendo Android! A diferença resume-se à **história e evolução** do sistema operacional.

#### O passado: Pacotes `android.*`

Os pacotes que começam puramente com `android.*` fazem parte do **Core (núcleo) do sistema operacional**. Eles vêm embutidos dentro do próprio celular do usuário (na imagem de fábrica do Android dele).

* **O problema:** Se a Google descobrisse um erro no `SensorManager` ou no `TextView` clássico, ela só conseguiria corrigir isso se o usuário atualizasse a versão do Android do celular inteiro (o que quase nunca acontece em modelos antigos).

#### O presente e futuro: Pacotes `androidx.*`

Para resolver isso, a Google criou o **AndroidX** (que faz parte do projeto *Jetpack*). O "X" vem de *eXtension* (Extensão).
Esses pacotes não ficam salvos dentro do celular do usuário; eles são **empacotados junto com o seu aplicativo** na hora que você gera o app para a Google Play.

| Característica | `android.*` (Padrão) | `androidx.*` (Moderno) |
| --- | --- | --- |
| **Onde fica?** | Direto no sistema operacional do celular. | Dentro do código do seu aplicativo. |
| **Atualizações** | Preso à versão do Android do aparelho. | Atualizado constantemente pela Google via bibliotecas. |
| **Compatibilidade** | Pode quebrar ou sumir em celulares muito antigos. | Garante que o recurso funcione igual em qualquer celular. |

**Um resumo simples:** Tudo o que for de hardware ou estrutura básica do sistema (como sensores e cores) continua vindo do pacote nativo `android.*`. Tudo o que for de interface moderna, comportamento de tela e compatibilidade (como o `ConstraintLayout` e a `AppCompatActivity`) usa o novo padrão `androidx.*`.
 */

//Comentário sobre o projeto par testar o Commit.
class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var sensorLuz: Sensor? = null
    private lateinit var textoLuz: TextView
    private lateinit var layoutPrincipal: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textoLuz = findViewById(R.id.text_luz)
        layoutPrincipal = findViewById(R.id.layout_principal)

        //Iniciar Gerencamento de Sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //Apontando para um sensor de Luz
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    //Esse metodo é chamado toda vez que o sensor detecta mudança
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null){
            val valorLuz = event.values[0] // valor de luz em lux
            textoLuz.text = "Luminosidade: $valorLuz lx"

            //mudar cor se estiver escuro
            if (valorLuz < 50){
                layoutPrincipal.setBackgroundColor(Color.DKGRAY)
                textoLuz.setTextColor(Color.WHITE)
            }else if (valorLuz < 500){
                layoutPrincipal.setBackgroundColor(Color.LTGRAY)
                textoLuz.setTextColor(Color.BLACK)
            }else{
                layoutPrincipal.setBackgroundColor(Color.WHITE)
                textoLuz.setTextColor(Color.BLUE)
            }
        }
    }
    // Registrar o sensor quando o app abre
    override fun onResume() {
        super.onResume()
        sensorLuz?.let{
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    // pausa o sensor quando sai
    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}
}