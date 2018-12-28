package com.example.dell.audiowish;

/**
 * Created by dell on 12/26/2018.
 */
public class Book {

    String name;
    String author;
    String downloadLink;

    Book(String name,String author,String downloadLink){
        this.name = name;
        this.author = author;
        this.downloadLink = downloadLink;
    }

    void setName(String name){
        this.name = name;
    }

    void setAuthor(String author){
        this.author = author;
    }

    void setDownloadLink(String downloadLink){
        this.downloadLink = downloadLink;
    }

    String getName(){
        return name;
    }

    String getAuthor(){
        return author;
    }

    String getDownloadLink(){
        return downloadLink;
    }
}
