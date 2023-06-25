package com.example.pjmk.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class IO_Product {
    public void WriteToggle(Context context, String filename, List<Toggle> list) throws Exception {
        FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void WriteButton(Context context, String filename, List<Button> list) throws Exception {
        FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void WriteThermal(Context context, String filename, Thermal thermal) throws Exception {
        FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(thermal);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public List<Toggle> ReadToggle(Context context, String filename) throws Exception {
        List<Toggle> list;
        FileInputStream fileInputStream = context.openFileInput(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        list = (List<Toggle>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return list;
    }

    public List<Button> ReadButton(Context context, String filename) throws Exception {
        List<Button> list;
        FileInputStream fileInputStream = context.openFileInput(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        list = (List<Button>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return list;
    }

    public Thermal ReadThermal(Context context, String filename) throws Exception {
        Thermal thermal;
        FileInputStream fileInputStream = context.openFileInput(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        thermal = (Thermal) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return thermal;
    }
}
