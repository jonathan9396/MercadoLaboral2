package com.example.mercadolaboral2.utils


import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class ValidacionDeEstructuraCenso(vivienda: String, json: JsonObject) {
    private val _vivienda = vivienda
    val gson = Gson()
    private val cuestionario = json

    //7. Para las viviendas particulares ocupadas
    fun validarViviendaParticularOcupada(): ArrayList<String> {
        val isParticularOcupada = JsonQueriesCenso.IsViviendaParticularOcupada(cuestionario)
        if (isParticularOcupada) {
            val erroresDeEsctructura = ArrayList<String>()
            //	Desde la Preg.3 ¿Es su vivienda? hasta la Preg.17-Número de hogares en la vivienda, deben tener información.
            try {
                if (!JsonQueriesCenso.HasViviendaParticularOcupadaData(cuestionario)) {
                    erroresDeEsctructura.add("Desde la Preg.3 ¿Es su vivienda? hasta la Preg.17-Número de hogares en la vivienda, deben tener información. ")
                }

                //	Debe existir hogar y población asociada
                if (!JsonQueriesCenso.HasHogar(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de hogar. ")
                }

                if (!JsonQueriesCenso.HasOcupantes(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir lista de ocupantes. ")
                }

                if (!JsonQueriesCenso.HasPersonas(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de personas. ")
                }

                //	El total de personas, hombres y mujeres registrados en la lista de ocupantes, debe ser igual a la cantidad de registros personas, hombres y mujeres en el cuestionario.
                if (JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de personas declaradas debe ser igual a la cantidad registrada. ")
                }

                if (JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario)
                    != JsonQueriesCenso.GetTotalHombresRegistradosOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario) != JsonQueriesCenso.GetTotalHombresRegistradosPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Hombres declarados debe ser igual a la cantidad registrada. ")
                }

                if (JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Mujeres declaradas debe ser igual a la cantidad registrada. ")
                }

                //	Si es la primera persona, la relación de parentesco debe ser igual a “1” (Jefe).
                if (!JsonQueriesCenso.IsJefeDelHogarProperlySet(cuestionario)) {
                    erroresDeEsctructura.add("La información del jefe del hogar esta erronea, revise la edad o la relación de parentesco. ")
                }

                //	Si no es la primera persona, la relación de parentesco debe ser diferente de “1”.
                if (!JsonQueriesCenso.IsPersonasRelacionProperlySet(cuestionario)) {
                    erroresDeEsctructura.add("Si no es la primera persona, la relación de parentesco debe ser diferente de “1”. ")
                }
            } catch (e: Exception) {
                return erroresDeEsctructura
            }
            return erroresDeEsctructura
        } else {
            return ArrayList()
        }
    }

    //8. Para las viviendas con ocupantes ausentes o desocupadas
    fun validarViviendaConOcupantesAusentesODesocupadas(): ArrayList<String> {
        val isParticularAusentes = JsonQueriesCenso.IsViviendaOcupantesAusentes(cuestionario)
                || JsonQueriesCenso.IsViviendaDesocupada(cuestionario)
        if (isParticularAusentes) {
            val erroresDeEsctructura = ArrayList<String>()

            try {//	Desde la Preg.3 Tenencia de la vivienda, hasta la Preg.17-Número de hogares en la vivienda, deben estar en blanco.
                if (JsonQueriesCenso.HasViviendaParticularOcupadaData(cuestionario)) {
                    erroresDeEsctructura.add("Desde la Preg.3 Tenencia de la vivienda, hasta la Preg.17-Número de hogares en la vivienda, deben estar en blanco. ")
                }

                //	No deben existir registros de hogar ni población asociados.
                if (JsonQueriesCenso.HasPersonas(cuestionario)
                    || JsonQueriesCenso.HasHogar(cuestionario)
                    || JsonQueriesCenso.HasOcupantes(cuestionario)
                    || JsonQueriesCenso.HasEmigracionInternacional(cuestionario)
                ) {
                    erroresDeEsctructura.add("No deben existir registros de hogar ni población asociados. ")
                }
            } catch (e: Exception) {
                return erroresDeEsctructura
            }
            return erroresDeEsctructura
        } else {
            return ArrayList()
        }
    }

    //9. Para las viviendas con tipo de vivienda=5 (local no destinado), 6 (damnificado) u 8 (hogar particular en vivienda colectiva)
    fun validarViviendaEspecial(): ArrayList<String> {
        val isEspecial = JsonQueriesCenso.IsViviendaEspecial(cuestionario)
        if (isEspecial) {
            val erroresDeEsctructura = ArrayList<String>()
            //	Desde Preg.2. Condición de la vivienda hasta Preg.17-Número de hogares en la vivienda, deben estar en blanco.
            /* if (JsonQueriesCenso.HasViviendaEspecialData(cuestionario)) {
                 erroresDeEsctructura.add("Desde Preg.2. Condición de la vivienda hasta Preg.17-Número de hogares en la vivienda, deben estar en blanco.")
             }*/

            try {//	Debe existir hogar y población asociada
                if (!JsonQueriesCenso.HasHogar(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de hogar. ")
                }

                if (!JsonQueriesCenso.HasOcupantes(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir lista de ocupantes. ")
                }

                if (!JsonQueriesCenso.HasPersonas(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de personas. ")
                }

                //	El total de personas, hombres y mujeres registrados en la lista de ocupantes, debe ser igual a la cantidad de registros personas, hombres y mujeres en el cuestionario.
                if (JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de personas declaradas debe ser igual a la cantidad registrada. ")
                }

                if (JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario) != JsonQueriesCenso.GetTotalHombresRegistradosOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario) != JsonQueriesCenso.GetTotalHombresRegistradosPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Hombres declarados debe ser igual a la cantidad registrada. ")
                }

                if (JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasOcupantes(
                        cuestionario
                    )
                    || JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Mujeres declaradas debe ser igual a la cantidad registrada. ")
                }

                //	Si es la primera persona, la relación de parentesco debe ser igual a “1” (Jefe).
                if (!JsonQueriesCenso.IsJefeDelHogarProperlySet(cuestionario)) {
                    erroresDeEsctructura.add(
                        "La información del jefe del hogar está errónea, revise la edad o la relación " +
                                "de parentesco. "
                    )
                }

                //	Si no es la primera persona, la relación de parentesco debe ser diferente de “1”.
                if (!JsonQueriesCenso.IsPersonasRelacionProperlySet(cuestionario)) { //todo: aun no esta especificado
                    erroresDeEsctructura.add("Si no es la primera persona, la relación de parentesco debe ser diferente de “1”. ")
                }
            } catch (e: Exception) {
                return erroresDeEsctructura
            }
            return erroresDeEsctructura
        } else {
            return ArrayList()
        }

    }

    //10. Para las viviendas con tipo de vivienda = 7 (indigente) o del 9 al 16 (colectivas)//TODO PREGUNTAR MAGA
    fun validarViviendaColecctiva(): ArrayList<String> {
        val isColectiva = JsonQueriesCenso.IsViviendaColectiva(cuestionario)
        if (isColectiva) {
            val erroresDeEsctructura = ArrayList<String>()
            //	Desde la Preg.2 Condición de la vivienda, hasta la Preg.17-Número de hogares en la vivienda, deben estar en blanco.
            /*if (JsonQueriesCenso.HasViviendaEspecialData(cuestionario)) {
                erroresDeEsctructura.add("Desde la Preg.2 Condición de la vivienda, hasta la Preg.17-Número de hogares en la vivienda, deben estar en blanco")
            }*/
            //	No debe existir información de hogar (existe el registro solo con el total de personas).
            /* if (JsonQueriesCenso.HasViviendaColectivaData(cuestionario)) {
                 erroresDeEsctructura.add("No debe existir información de hogar (existe el registro solo con el total de personas)")
             }*/

            try {//	La relación de parentesco debe ser igual a 13 (no pariente) para todas las personas del cuestionario.
                if (!JsonQueriesCenso.IsColectivaRelacionProperlySet(cuestionario)) {
                    erroresDeEsctructura.add("La relación de parentesco debe ser igual a 13 (no pariente) para todas las personas del cuestionario. ")
                }

                //	Debe existir hogar y población asociada
                if (!JsonQueriesCenso.HasHogar(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de hogar. ")
                }
/*            if (!JsonQueriesCenso.HasOcupantes(cuestionario)) {
                erroresDeEsctructura.add("Deben existir Ocupantes")
            }*/

                if (!JsonQueriesCenso.HasPersonas(cuestionario)) {
                    erroresDeEsctructura.add("Debe existir registro de personas. ")
                }

                //	El total de personas, hombres y mujeres registrados en la lista de ocupantes, debe ser igual a la cantidad de registros personas, hombres y mujeres en el cuestionario.
                if (
                /*  JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasOcupantes(
                      cuestionario
                  )
                  ||*/
                    JsonQueriesCenso.GetTotalPersonasDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalPersonasRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de personas declaradas debe ser igual a la cantidad registrada. ")
                }

                if (
                /* JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario) != JsonQueriesCenso.GetTotalHombresRegistradosOcupantes(
                     cuestionario
                 )
                 ||*/
                    JsonQueriesCenso.GetTotalHombresDeclarados(cuestionario) != JsonQueriesCenso.GetTotalHombresRegistradosPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Hombres declarados debe ser igual a la cantidad registrada. ")
                }

                if (
                /* JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasOcupantes(
                     cuestionario
                 )
                 || */
                    JsonQueriesCenso.GetTotalMujeresDeclaradas(cuestionario) != JsonQueriesCenso.GetTotalMujeresRegistradasPersonas(
                        cuestionario
                    )
                ) {
                    erroresDeEsctructura.add("Total de Mujeres declaradas debe ser igual a la cantidad registrada.")
                }
                /*//	Si es la primera persona, la relación de parentesco debe ser igual a “1” (Jefe).
                    if (!JsonQueriesCenso.IsJefeDelHogarProperlySet(cuestionario)) {
                        erroresDeEsctructura.add("La información del jefe del hogar esta erronea, revise la edad o la relación de parentesco")
                    }*/
            } catch (e: Exception) {
                return erroresDeEsctructura
            }
            return erroresDeEsctructura
        } else {
            return ArrayList()
        }
    }

    //EXTRA. validaciones a las viviendas
    fun validarCantidadDeHogaresEnVivienda(cuestionariosViviendas: List<List<Cuestionarios>>): String? {
        //buscar la lista segun viviendas
        var cuestionariosFiltrados = cuestionariosViviendas[0]
        for (num in 0..cuestionariosViviendas.size) {
            if (cuestionariosViviendas[num][0].jefe.toInt() == (_vivienda.toInt() - 1)) {
                cuestionariosFiltrados = cuestionariosViviendas[num]
                break
            }
        }

//        val cuestionariosFiltrados = cuestionariosViviendas[_vivienda.toInt() - 1]
        val viviendaCuestionario =
            cuestionariosFiltrados.firstOrNull { it.otraReferencia == "1" } ?: return null

        if (viviendaCuestionario.datosJson != null) {
            val viviendaObject = JsonParser().parse(viviendaCuestionario.datosJson)
                .asJsonObject
                .getAsJsonObject("REC_VIVIENDA")

            if (!JsonQueriesCenso.IsViviendaParticularOcupada(viviendaObject)) {
                return null
            }

            val hogaresDeclarados = viviendaObject.get("V17_NHOG").asString.toInt()
            return if (hogaresDeclarados == cuestionariosFiltrados.size) {
                viviendaCuestionario.entrevistaId
            } else {
                null
            }
        } else
            return null
    }

    /*//EXTRA. validaciones a las viviendas
    fun validarCantidadDeHogaresEnVivienda2(*//*cuestionariosViviendas: List<List<Cuestionarios>>,*//*
        viviendaCuestionario: Cuestionarios
    ): String? {
        //buscar la lista segun viviendas
//        val cuestionariosFiltrados = cuestionariosViviendas[_vivienda.toInt() - 1]
//        val viviendaCuestionario =
//            viviendaCuestionario.firstOrNull { it.hogar == "1" } ?: return null
        if (viviendaCuestionario.hogar == "1")
            return null

        if (viviendaCuestionario.datosJson != null) {
            val viviendaObject = JsonParser().parse(viviendaCuestionario.datosJson)
                .asJsonObject
                .getAsJsonObject("REC_VIVIENDA")

            if (viviendaObject == null)
                return null

            if (!JsonQueriesCenso.IsViviendaParticularOcupada(viviendaObject)) {
                return null
            }

            val hogaresDeclarados = viviendaObject.get("V17_NHOG").asString.toInt()
            return if (hogaresDeclarados > 1) {
                viviendaCuestionario.llave
            } else {
                null
            }
        } else
            return null
    }*/
}
