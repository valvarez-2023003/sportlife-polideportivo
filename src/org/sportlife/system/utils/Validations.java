/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sportlife.system.utils;

/**
 *
 * @author Cristofer Ramos
 */

public class Validations {
    public Validations(){
    }
    
    public Boolean equalsText(String textOriginal, String textCompare){
        return textOriginal.equals(textCompare);
    }
    
    public Boolean emptyText(String text){
        boolean isEmpty = false;
        if(text.isEmpty() || text.isBlank()){
            isEmpty = true;
        }
        return isEmpty;
    }
    
    public Boolean validateLengthText(String text, int lengthMax){
        return text.length() <= lengthMax;
    }
    
    public Boolean validateEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        String emailLimpio = email.trim();
        if(emailLimpio.contains(" ")){
            return false;
        }
        if(emailLimpio.contains("..")){
            return false;
        }
        int indiceArroba = emailLimpio.indexOf('@');
        if(indiceArroba <= 0 || indiceArroba != emailLimpio.lastIndexOf('@')){
            return false;
        }
        String usuario = emailLimpio.substring(0, indiceArroba);
        String dominio = emailLimpio.substring(indiceArroba + 1);
        if(dominio.startsWith(".") || !dominio.contains(".")){
            return false;
        }
        String[] partesDominio = dominio.split("\\.");
        String extension = partesDominio[partesDominio.length - 1];
        if(extension.length() < 2 || !extension.matches("[a-zA-Z]+")){
            return false;
        }
        if(!usuario.matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$")){
            return false;
        }
        return true;
    } 
}
