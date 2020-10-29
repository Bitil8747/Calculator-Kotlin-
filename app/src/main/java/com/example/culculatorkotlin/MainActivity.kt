package com.example.culculatorkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var resultField : TextView         // поле для вывода результата
    lateinit var numberField : EditText         // поле для ввода числа
    lateinit var operationField : TextView      // поле для вывода знака операции
    var operand : Double = 0.0                  // операнд операции
    var lastOperation : String = "="            // последняя операция
    var prosentOk: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultField = findViewById(R.id.resultField) as TextView
        numberField = findViewById(R.id.numberField) as EditText
        operationField = findViewById(R.id.operationField) as TextView
        numberField.setFocusable(false) //чтобы нельзя было вводить числа с клавиатуры, только со встроенных кнопок
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState : Bundle) {
        outState.putString("OPERATION", lastOperation)
        if(operand!=0.0)
            outState.putDouble("OPERAND", operand)
        super.onSaveInstanceState(outState)
    }

    // получение ранее сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastOperation = savedInstanceState.getString("OPERATION").toString()
        operand= savedInstanceState.getDouble("OPERAND")
        resultField.setText(operand.toString())
        operationField.setText(lastOperation)
    }

    // Нажатие на кнопки с цифрами и кнопки %
    fun onNumberClick(view: View){
        val button: Button = view as Button

        if (button.getText() == "%" && resultField.getText().length > 0 && numberField.getText().length > 0){
            val num: String = numberField.text.toString().replace(',', '.')
            val res: String = resultField.text.toString().replace(',', '.')
            val prosetn: Double = num.toDouble() * res.toDouble() / 100.0
            numberField.setText(prosetn.toString().replace('.', ','))
            //prosentOk = true
        }else {
                numberField.append(button.getText())
        }


        if(lastOperation.equals("=") && operand!=0.0){
            operand = 0.0;
        }
    }

    // Нажатие на кнопку AC
    fun onSpecialClick1(view: View){
        numberField.setText("");
        resultField.setText("");
        operationField.setText("")
    }

    // Нажатие на кнопку +/-
    fun onSpecialClick2(view: View){
        numberField.setText(numberField.getText().toString().replace(',', '.'));
        try{
            if((numberField.getText().toString()).toDouble() >= 0 && numberField.getText().toString() != null){
                numberField.setText(numberField.getText().toString().replace('.', '.'));
                numberField.setText("-" + numberField.getText().toString());
            }
        }catch (ex: Exception) {
            numberField.setText(numberField.getText().toString());
        }
        numberField.setText(numberField.getText().toString().replace('.', ','));
    }

    // Нажатие на кнопку математической операции
    fun onOperationClick(view: View){
        val button: Button = view as Button
        val operation: String = button.getText().toString()
        var number: String = numberField.getText().toString()
        if(number.length > 0){
            number = number.replace(',', '.')
            try{
                Operation(number.toDouble(), operation);
            }catch (ex: NumberFormatException){
                numberField.setText("");
            }
        }
        lastOperation = operation;
        operationField.setText(lastOperation);
    }

    // Действие для каждой математической операции
    fun Operation(number: Double, operation: String){

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if(operand == 0.0){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            when(lastOperation){
                "=" ->
                    operand = number
                "/" ->
                    if(number == 0.0){
                        operand = 0.0;
                    }
                    else{
                        operand = operand / number
                    }
                "*" ->
                    operand = operand * number
                "+" ->
                    operand = operand + number
                "-" ->
                    operand = operand - number
                "%" ->
                    operand = (operand * number) / 100.0
            }
        }
        resultField.setText(operand.toString().replace('.', ','));
        numberField.setText("");
    }
}


/*public class MainActivity extends AppCompatActivity {

    TextView resultField;           // поле для вывода результата
    EditText numberField;           // поле для ввода числа
    TextView operationField;        // поле для вывода знака операции
    Double operand = null;          // операнд операции
    String lastOperation = "=";     // последняя операция

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultField =(TextView) findViewById(R.id.resultField);
        numberField = (EditText) findViewById(R.id.numberField); //поле ввода чисел
        operationField = (TextView) findViewById(R.id.operationField);

        numberField.setFocusable(false);
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }
    // Нажатие на кнопки с цифрами
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }

    // Нажатие на кнопку AC
    public void onSpecialClick1(View view){
        numberField.setText("");
    }

    // Нажатие на кнопку +/-
    public void onSpecialClick2(View view){
        numberField.setText(numberField.getText().toString().replace(',', '.'));
        try{
            if(Double.parseDouble(numberField.getText().toString()) >= 0 && numberField.getText().toString() != null){
                numberField.setText(numberField.getText().toString().replace('.', '.'));
                numberField.setText("-" + numberField.getText().toString());
            }
        }catch (Exception ex) {
            numberField.setText(numberField.getText().toString());
        }
        numberField.setText(numberField.getText().toString().replace('.', '.'));
    }

    // Нажатие на кнопку математической операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String operation = button.getText().toString();
        String number = numberField.getText().toString();
        if(number.length()>0){
            number = number.replace('.', '.');
            try{
                Operation(Double.valueOf(number), operation);
            }catch (NumberFormatException ex){
                numberField.setText("");
            }
        }
        lastOperation = operation;
        operationField.setText(lastOperation);
    }

    // Действие для каждой математической операции
    private void Operation(Double number, String operation){

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            switch(lastOperation){
                case "=":
                operand =number;
                break;
                case "/":
                if(number==0){
                    operand =0.0;
                }
                else{
                    operand /=number;
                }
                break;
                case "*":
                operand *=number;
                break;
                case "+":
                operand +=number;
                break;
                case "-":
                operand -=number;
                break;
                case "%":
                operand = (number * operand) / 100;
                break;
            }
        }
        resultField.setText(operand.toString().replace('.', '.'));
        numberField.setText("");
    }
}

//////////////////////////////////////////Начало изучения Kotlin//////////////////////////////////
val a : Int = 10
var b = when(a){
    in 1..2-> 1.5
    in 7..10 -> 8.5
}

var c = 1..10 step 2
var d = 1 downTo -10 step 1
var f = 1 until 10

fun factorial(n : Int) : Int{
    var result = 1
    for (i in 1..n){
        result *= i
    }
    return result
}

class car constructor(_brend : String){
    var brend : String
        get() {
            return "Car brend is $brend"
        }
        init {
            brend = _brend
        }
    var model : String = "Unknown"
        get() {
            return "Car model is $model"
        }
    var maxSpeed : Double = 0.0
        set(value) {
            if(value > 0)
            field = value
        }
}

val viecal : car = car("Audi")
println(Car.brend)

interface Movable {
    fun move()
    fun stop(){
        println("Остановилась")
    }
}

class Car(mod:String, num:Int) : Movable , Info{
    override val model: String
    override val number: Int
    init {
        model = mod
        number = num
    }
    override fun move() {
        println("Машина едет")
    }
}
class Aircraft : Movable{
    override fun move() {
        println("Самолет детит")
    }

    override fun stop() {
        println("Приземлился")
    }
}

val m1 : Car = Car("Toyota", 11563440)
val m2 : Movable = Aircraft()
m1.move
m1

interface Info {
    val model : String
        get() {
            return ("Undefinded")
        }
    val number : Int
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//Наследование класса
open class Person (val name:String)

class Employee (name: String): Person(name)

class Employee2 (val company:String, name: String): Person(name)

class Employee3 : Person{
    var company: String = "undefined"
    constructor (name:String, company: String): super(name){
        this.company = company
    }
}

val kate : Employee2 = Employee2("Amazon", "Katerin")
val anna : Person = Employee2("Apple", "Anna")
////////////////////////////////////////////////////////////////////////////////////////////////////
//...переопределение методов
open class Person1 (val name:String){

    open val fullInfo: String
        get() = "Name: $name"

    open fun displey(){
        println("Name $name")
    }
}

open class Employee4 (name: String, val company: String) : Person1(name){

    override val fullInfo: String
        get() = "Name: $name  Company: $company"

    final override fun displey() {
        println("Name: $name; company: $company")
    }
}

class Manager(name: String, company: String) : Employee4 (name, company){
    override val fullInfo: String
        get() = "${super.fullInfo}; company: $company; position: manager"
    fun displey(){
        super.displey()
        println("Company: $company")
        println("Position: manager")
    }
    // теперь функцию нельзя переопределить
    /*override fun display() {
        println("Name: $name Company: $company  Position: Manager")
    }*/
}

open class Video {
    open fun play() { println("Play video") }
}

interface AudioPlayable {
    fun play() { println("Play audio") }
}
class MediaPlayer() : Video(), AudioPlayable {
    // Функцию play обязательно надо переопределить
    override fun play() {
        super<Video>.play()         // вызываем Video.play()
        super<AudioPlayable>.play() // вызываем AudioPlayable.play()
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
*/