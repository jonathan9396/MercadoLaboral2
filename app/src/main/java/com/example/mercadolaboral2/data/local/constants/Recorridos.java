package com.example.mercadolaboral2.data.local.constants;

public class Recorridos {
    private String id;
    private String recorrido;
    private String condicionVivienda;
    private String totalPersonas;
    private String totHombres;
    private String totMujeres;

    public Recorridos(String id, String recorrido, String condicionVivienda, String totalPersonas, String totHombres, String totMujeres) {
        this.id = id;
        this.recorrido = recorrido;
        this.condicionVivienda = condicionVivienda;
        this.totalPersonas = totalPersonas;
        this.totHombres = totHombres;
        this.totMujeres = totMujeres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(String recorrido) {
        this.recorrido = recorrido;
    }

    public String getCondicionVivienda() {
        return condicionVivienda;
    }

    public void setCondicionVivienda(String condicionVivienda) {
        this.condicionVivienda = condicionVivienda;
    }

    public String getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(String totalPersonas) {
        this.totalPersonas = totalPersonas;
    }

    public String getTotHombres() {
        return totHombres;
    }

    public void setTotHombres(String totHombres) {
        this.totHombres = totHombres;
    }

    public String getTotMujeres() {
        return totMujeres;
    }

    public void setTotMujeres(String totMujeres) {
        this.totMujeres = totMujeres;
    }
}
