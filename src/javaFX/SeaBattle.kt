import javafx.application.Application
import tornadofx.App

class SeaBattle : App(SeaBattleView1::class)

fun main() {
    Application.launch(SeaBattle::class.java)
}