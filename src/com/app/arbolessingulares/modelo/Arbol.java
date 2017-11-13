package com.app.arbolessingulares.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Arbol implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String nombre;
	private String descripcion;
	private String foto;
	private String provincia;	
	private String tmunicipal;	
	private String latitud;
	private String longitud;	
	private String direccion;
	private String especie;
	private String altura;
	private String diametro;
	private String edad;
	private String comun;
	private String distancia;
	//private String ratings_average;


public Arbol(String ID)
	{
		this.ID = ID;
		this.distancia = "0";
		
	}
public String getNombre(){
	return nombre;
}

public String getID(){
	return ID;
}



public void setNombre(String name)
{
	this.nombre=name;
}
public void setID(String ID)
{
	this.ID=ID;
}


public String getDireccion() {
	return direccion;
}
public void setDireccion(String direccion) {
	this.direccion = direccion;
}


public String getProvincia() {
	return provincia;
}
public void setProvincia(String provincia) {
	this.provincia = provincia;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public String getFoto() {
	return foto;
}
public void setFoto(String foto) {
	this.foto = foto;
}
public String getTmunicipal() {
	return tmunicipal;
}
public void setTmunicipal(String tmunicipal) {
	this.tmunicipal = tmunicipal;
}
public String getLatitud() {
	return latitud;
}
public void setLatitud(String latitud) {
	this.latitud = latitud;
}
public String getLongitud() {
	return longitud;
}
public void setLongitud(String longitud) {
	this.longitud = longitud;
}
public String getEspecie() {
	return especie;
}
public void setEspecie(String especie) {
	this.especie = especie;
}
public String getAltura() {
	return altura;
}
public void setAltura(String altura) {
	this.altura = altura;
}
public String getDiametro() {
	return diametro;
}
public void setDiametro(String diametro) {
	this.diametro = diametro;
}
public String getEdad() {
	return edad;
}
public void setEdad(String edad) {
	this.edad = edad;
}
public String getComun() {
	return comun;
}
public void setComun(String comun) {
	this.comun = comun;
}
public String getDistancia() {
	return distancia;
}
public void setDistancia(String distancia) {
	this.distancia = distancia;
}


}
