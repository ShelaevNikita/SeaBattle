import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App

class SeaBattle : App(SeaBattleView::class) {

}

fun main(args: Array<String>) {
    Application.launch(SeaBattle::class.java, *args)
}