package com.example.mercadolaboral2.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.InputStream

class CsproJson(dictionaryStream: String) {

    private var stringBuilder = StringBuilder(dictionaryStream)
    private var dictionaryObj = Parser.default().parse(stringBuilder) as JsonObject
    private var records = dictionaryObj
        .obj("Dictionary")?.obj("Level")?.array<JsonObject>("Records")
    private var mainObject = JsonObject()

    fun getJson(stream: InputStream): String {
        mainObject = JsonObject()

        val streamLines = mutableListOf<String>()
        stream.bufferedReader().useLines { lines -> lines.forEach { streamLines.add(it) } }
        streamLines[0] = streamLines[0].replace("\uFEFF", "")
        streamLines.forEach { line ->
            val recordType = getRecordType(line.substring(0, 1))
            val mapa = JsonObject()
            recordType.array<JsonObject>("Items")?.forEach record@{
                run {
                    try {
                        val name = it.string("Name") ?: ""
                        val value = line.substring(
                            (it.int("Start") ?: 1) - 1,
                            (it.int("Start") ?: 1) + (it.int("Len") ?: 1) - 1
                        )
                        mapa[name] = value.trim()
                    } catch (e: IndexOutOfBoundsException) {
                        val name = it.string("Name") ?: ""
                        try {
                            val value = line.substring((it.int("Start") ?: 1) - 1, line.length)

//                            if (value.isNullOrBlank()) {
                            if (value.isBlank()) {
                                return@record
                            }
                            mapa[name] = value.trim()
                            return@record
                        } catch (e: IndexOutOfBoundsException) {
                            return@record
                        }
                    }
                }
            }

            if (recordType.string("RecordTypeValue") == "1") {
                guardarLineaPrincipal(recordType.string("Name") ?: "", mapa)
            } else {
                guardarLinea(recordType.string("Name") ?: "", mapa)
            }
        }

        return mainObject.toJsonString()
    }

    private fun getRecordType(typeNumber: String): JsonObject {
        //        val stringRec = Klaxon().toJsonString(rec)
//        val res = Klaxon().parse<CsproRecord>(stringRec)?: CsproRecord()
        return records?.find { rec -> rec["RecordTypeValue"] == typeNumber } ?: JsonObject()
    }

    private fun guardarLineaPrincipal(
        tipo: String,
        linea: JsonObject
    ) {
        mainObject[tipo] = linea
    }

    private fun guardarLinea(
        tipo: String,
        linea: JsonObject
    ) {
        var mObj = mainObject[tipo] as ArrayList<JsonObject>?
        if (mObj == null) {
            mObj = ArrayList()
        }
        mObj.add(linea)
        mainObject[tipo] = mObj
    }
}