/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import java.io.IOException;
import lucene.IndexFile;
/**
 *
 * @author usuario1
 */
public class SearchLucene {
    
    public static void main(String[] args) throws IOException{
        
        IndexFile indexFile = new IndexFile();
    
        
        System.out.println("This is a man class!");
        
        indexFile.makeIndex();
        
        
        
    }
    
    
}
