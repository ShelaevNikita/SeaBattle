import javafx.application.Application
import tornadofx.App

class SeaBattle1 : App(SeaBattleView1::class)

fun main() {
    Application.launch(SeaBattle1::class.java)
}