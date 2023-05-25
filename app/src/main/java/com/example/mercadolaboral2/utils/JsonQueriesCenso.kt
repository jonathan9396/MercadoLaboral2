package com.example.mercadolaboral2.utils

import com.google.gson.JsonObject

object JsonQueriesCenso {

//    fun IsCondicionAndTipoProperlySet(cuestionario: JsonObject): Boolean {
//        val tipo = GetTipoVivienda(cuestionario)
//        val condicion = GetCondicionVivienda(cuestionario)
//        return if (tipo == null || condicion == null) {
//            false
//        } else {
//            val tipoInt = tipo.toInt()
//            val condicionInt = condicion.toInt()
//            if (tipoInt in 1..4) {
//                (condicionInt in 1..7)
//            } else {
//                false
//            }
//        }
//    }

    fun IsPersonasRelacionProperlySet(cuestionario: JsonObject): Boolean {
        val personas = cuestionario.getAsJsonArray("REC_PERSONA")
        return if (personas == null || personas.size() <= 0) {
            false
        } else {
            var isOk = true
            personas.remove(0)
            personas.forEach {
                val relacion = it.asJsonObject.get("P01_RELA")?.asString
                val isRelacionProperlySet = relacion != "1"
                isOk = isOk && isRelacionProperlySet
            }
            return isOk
        }
    }

    fun IsColectivaRelacionProperlySet(cuestionario: JsonObject): Boolean {
        val personas = cuestionario.getAsJsonArray("REC_PERSONA")
        return if (personas == null || personas.size() <= 0) {
            false
        } else {
            var isOk = true
            personas.forEach {
                val relacion = it.asJsonObject.get("P01_RELA")?.asString
                val isRelacionNoPariente = relacion == "13"
                isOk = isOk && isRelacionNoPariente
            }
            return isOk
        }
    } //tested

    fun IsViviendaParticularOcupada(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        val condicion = GetCondicionVivienda(cuestionario)
        return if (tipo == null || condicion == null) {
            false
        } else {
            val tipoInt = tipo.toInt()
            (tipoInt in 1..4 && condicion == "1")
        }
    } //tested

    fun IsViviendaParticularOcupadaReporte(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        val condicion = GetCondicionVivienda(cuestionario)
        return if (tipo == null || condicion == null || tipo == "") {
            false
        } else {
            val tipoInt = tipo.toInt()
            (tipoInt in 1..4 && condicion == "1" || tipoInt in 5..16)
        }
    } //tested

    fun IsViviendaOcupantesAusentes(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        val condicion = GetCondicionVivienda(cuestionario)
        return if (tipo == null || condicion == null || tipo == "") {
            false
        } else {
            val tipoInt = tipo.toInt()
            (tipoInt in 1..4 && condicion == "2")
        }
    }

    fun IsViviendaDesocupada(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        val condicion = GetCondicionVivienda(cuestionario)
        if (tipo == null || condicion == null || tipo == "") {
            return false
        } else {
            val tipoInt = if (tipo.isNullOrBlank()) {
                0
            } else {
                tipo.toInt()
            }

            val condicionInt = if (condicion.isNullOrBlank()) {
                0
            } else {
                condicion.toInt()
            }
            return (tipoInt in 1..4 && condicionInt in 3..8)
        }
    }

    fun IsViviendaColectiva(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        return if (tipo == null) {
            false
        } else {
            val tipoInt = tipo.toInt()
            (tipoInt in 9..16 || tipoInt == 7)
        }
    }

    fun IsViviendaEspecial(cuestionario: JsonObject): Boolean {
        val tipo = GetTipoVivienda(cuestionario)
        return if (tipo == null) {
            false
        } else {
            val tipoInt = tipo.toInt()
            (tipoInt in 5..8 && tipoInt != 7)
        }
    } //tested

    fun IsJefeDelHogarProperlySet(cuestionario: JsonObject): Boolean {
        val personas = cuestionario.getAsJsonArray("REC_PERSONA")
        val personasFiltradas =
            personas.filter { it.asJsonObject.get("NPERSONA")?.asString?.trim() == "01" }//Te lo dije
        val primeraPersona = if (personasFiltradas.isEmpty()) {
            null
        } else {
            personasFiltradas[0]?.asJsonObject
        }
        return if (primeraPersona != null) {
            val edad = primeraPersona.get("P03_EDAD")?.asString
            !edad.isNullOrBlank() && edad.toInt() >= 12 && primeraPersona.get("P01_RELA").asString == "01"
        } else {
            false
        }
    } //tested

    private fun GetTipoVivienda(cuestionario: JsonObject): String? {
        return cuestionario.getAsJsonObject("REC_VIVIENDA")?.get("V01_TIPO")?.asString
    } //tested

    private fun GetCondicionVivienda(cuestionario: JsonObject): String? {
        return cuestionario.getAsJsonObject("REC_VIVIENDA")?.get("V02_COND")?.asString
    } //tested

    fun HasViviendaParticularOcupadaData(cuestionario: JsonObject): Boolean {
        val vivienda = cuestionario.get("REC_VIVIENDA")
        if (vivienda == null) {
            return false
        } else {
            val viviendaObject = vivienda.asJsonObject
            val props = viviendaObject.keySet().toTypedArray()
            val fin = props.indexOf("V17_NHOG")
            val inicio = props.indexOf("V03_TENE")
            var contador = 0
            for (i in inicio..fin) {
                if (!viviendaObject.get(props.elementAt(i)).asString.isNullOrBlank()) {
                    contador++
                }
                //tieneValor =
                //    tieneValor //&& !viviendaObject.get(props.elementAt(i)).asString.isNullOrBlank()
            }
            return contador >= 2
        }
    }

//    fun HasViviendaColectivaData(cuestionario: JsonObject): Boolean {
//        val rec = cuestionario.get("REC_HOGAR")
//        val hogares = rec.asJsonArray
//        return if (hogares == null || hogares.size() <= 0) {
//            false
//        } else {
//            val hogar = hogares.get(0)
//            val props = hogar.asJsonObject.keySet()
//            val distance =
//                props.indexOf("H18A_ESTU") - props.indexOf("LO_PERADIC")
//            distance == 25
//        }
//    }

//    fun HasViviendaEspecialData(cuestionario: JsonObject): Boolean {
//        val vivienda = cuestionario.get("REC_VIVIENDA")
//        return if (vivienda == null) {
//            false
//        } else {
//            val props = vivienda.asJsonObject.keySet()
//            val distance = props.indexOf("V17_NHOG") - props.indexOf("V02_COND")
//            distance == 25
//        }
//    } //tested

    fun HasHogar(cuestionario: JsonObject): Boolean {
        return HasRecord(cuestionario, "REC_HOGAR")
    } //tested

    fun HasPersonas(cuestionario: JsonObject): Boolean {
        return HasRecord(cuestionario, "REC_PERSONA")
    } //tested

    fun HasOcupantes(cuestionario: JsonObject): Boolean {
        return HasRecord(cuestionario, "REC_LIST_OCUP")
    } //tested

    fun HasEmigracionInternacional(cuestionario: JsonObject): Boolean {
        return HasRecord(cuestionario, "REC_EMIGRA")
    }

/*    fun GetTotalHogaresDeclarados(cuestionario: JsonObject): Int {
        return getValueFromRecordInt(cuestionario, "REC_VIVIENDA", "V17_NHOG")
    } //tested*/

    fun GetTotalPersonasDeclaradas(cuestionario: JsonObject): Int {
        return getValueFromRecordInt(cuestionario, "REC_HOGAR", "H_PERSONAS")
    } //tested

    fun GetTotalMujeresDeclaradas(cuestionario: JsonObject): Int {
        return getValueFromRecordInt(cuestionario, "REC_HOGAR", "H_MUJERES")
    } //tested

    fun GetTotalHombresDeclarados(cuestionario: JsonObject): Int {
        return getValueFromRecordInt(cuestionario, "REC_HOGAR", "H_HOMBRES")
    } //tested

    fun GetTotalHombresRegistradosOcupantes(cuestionario: JsonObject): Int {
        return GetTotalFromListObject(cuestionario, "REC_LIST_OCUP", "LO_SEXO", "1")
    } //tested

    fun GetTotalMujeresRegistradasOcupantes(cuestionario: JsonObject): Int {
        return return GetTotalFromListObject(cuestionario, "REC_LIST_OCUP", "LO_SEXO", "2")
    } //tested

    fun GetTotalPersonasRegistradasOcupantes(cuestionario: JsonObject): Int {
        return return GetTotalFromListObject(cuestionario, "REC_LIST_OCUP", null, null)
    } //tested

    fun GetTotalHombresRegistradosPersonas(cuestionario: JsonObject): Int {
        return GetTotalFromListObject(cuestionario, "REC_PERSONA", "P02_SEXO", "1")
    } //tested

    fun GetTotalMujeresRegistradasPersonas(cuestionario: JsonObject): Int {
        return return GetTotalFromListObject(cuestionario, "REC_PERSONA", "P02_SEXO", "2")
    } //tested

    fun GetTotalPersonasRegistradasPersonas(cuestionario: JsonObject): Int {
        return return GetTotalFromListObject(cuestionario, "REC_PERSONA", null, null)
    } //tested

    //helpers
    private fun getValueFromRecordInt(
        cuestionario: JsonObject,
        record: String,
        value: String
    ): Int {
        val rec = cuestionario.get(record)
        val record = if (rec != null && rec.isJsonArray) {
            rec.asJsonArray?.get(0)?.asJsonObject
        } else {
            rec?.asJsonObject
        }
        val number = record?.get(value)?.asString
        return if (number.isNullOrBlank()) {
            0
        } else {
            number.toInt()
        }
    }

    private fun GetTotalFromListObject(
        cuestionario: JsonObject,
        record: String,
        field: String?,
        filter: String?
    ): Int {
        val ocupantes = cuestionario.getAsJsonArray(record)
        return if (ocupantes == null) {
            0
        } else {
            if (field == null || filter == null) {
                ocupantes.count()
            } else {
                ocupantes.filter {
                    it.asJsonObject.get(field)?.asString == filter
                }.size
            }
        }
    }

    private fun HasRecord(cuestionario: JsonObject, record: String): Boolean {
        val recordList = cuestionario.getAsJsonArray(record)
        return if (recordList == null) {
            false
        } else recordList.size() > 0
    }
}
