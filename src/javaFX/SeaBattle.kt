import javafx.application.Application
import tornadofx.App

class SeaBattle : App(SeaBattleView::class)

fun main() {
    Application.launch(SeaBattle::class.java)
}