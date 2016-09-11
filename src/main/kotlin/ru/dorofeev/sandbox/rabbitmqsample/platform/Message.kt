package ru.dorofeev.sandbox.rabbitmqsample.platform

interface Message {

}

class Failure(val type: String) : Message {

}

