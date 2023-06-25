package com.example.pjmk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjmk.model.IO_Product;
import com.example.pjmk.model.Thermal;
import com.example.pjmk.model.Toggle;

import java.util.ArrayList;
import java.util.List;

public class SaveTokenActivity extends AppCompatActivity {
    private EditText editToken, edtThermal, edtHumidity;
    private final ArrayList<Switch> sw_Number = new ArrayList<>();
    private final ArrayList<EditText> edt_Name = new ArrayList<>();
    private final ArrayList<EditText> edt_VNumber = new ArrayList<>();
    private final ArrayList<Switch> swbtn = new ArrayList<>();
    private final ArrayList<EditText> edtnamebtn = new ArrayList<>();
    private final ArrayList<EditText> edtvNumberbtn = new ArrayList<>();
    private List<Toggle> toggleList = new ArrayList<>();
    private List<com.example.pjmk.model.Button> buttonList = new ArrayList<>();
    private Thermal thermal;
    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_token);
        appContext = getApplicationContext();
        /**
         * Đọc file chứa token
         */
        SharedPreferences sharedPreferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
        String savedToken = sharedPreferences.getString("token", "");

        /**
         * Đọc file chứa các button và toggle, trạng tắt enable hay disable của các đối tượng đó
         * Nếu không đọc được thì đặt tất cả giá trị về mặc định
         */
        try {
            toggleList = new IO_Product().ReadToggle(this, "Toggle");
            buttonList = new IO_Product().ReadButton(this, "Button");
            thermal = new IO_Product().ReadThermal(this, "Thermal");
        } catch (Exception e) {
            Toast.makeText(getAppContext(), "Đọc dữ liệu lưu các đối tượng thất bại", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 4; i++) {
                toggleList.add(new Toggle(false, "Toggle" + i, 10));
            }
            buttonList.add(new com.example.pjmk.model.Button(false, "Button0", 10));
            buttonList.add(new com.example.pjmk.model.Button(false, "Button1", 10));
            thermal = new Thermal(0, 1);
        }

        /**
         * Ánh xạ các widget
         */
        editToken = findViewById(R.id.editToken);
        edtThermal = findViewById(R.id.edtThermal);
        edtHumidity = findViewById(R.id.edtHumidity);
        Button btnSaveToken = findViewById(R.id.btnSaveToken);
        // ánh xạ các widget có tên theo trình tự swbtn0, 1
        for (int i = 0; i < 4; i++) {
            Switch swNumber = findViewById(getResources().getIdentifier("swbtn" + i, "id", getPackageName()));
            swbtn.add(swNumber);
            EditText edtNameBtn = findViewById(getResources().getIdentifier("edtnamebtn" + i, "id", getPackageName()));
            edtnamebtn.add(edtNameBtn);
            EditText edtVNumber = findViewById(getResources().getIdentifier("edtvNumberbtn" + i, "id", getPackageName()));
            edtvNumberbtn.add(edtVNumber);
        }
        // ánh xạ các widget edittext của toggle theo trình tự từ 0 đến 3 edt_vNumber và edt_Name
        for (int i = 0; i < 4; i++) {
            Switch swNumber = findViewById(getResources().getIdentifier("swtg" + i, "id", getPackageName()));
            sw_Number.add(swNumber);
            EditText edtVNumber = findViewById(getResources().getIdentifier("edt_vNumber" + i, "id", getPackageName()));
            edt_VNumber.add(edtVNumber);
            EditText edtName = findViewById(getResources().getIdentifier("edt_Name" + i, "id", getPackageName()));
            edt_Name.add(edtName);
        }
        editToken.setText(savedToken);
        for (int i = 0; i<4; i++){
            sw_Number.get(i).setChecked(toggleList.get(i).isActivate());
            edt_Name.get(i).setText(toggleList.get(i).getName());
            edt_VNumber.get(i).setText(String.valueOf(toggleList.get(i).getvNumber()));
        }
        for (int i = 0; i<2; i++){
            swbtn.get(i).setChecked(buttonList.get(i).isActivate());
            edtnamebtn.get(i).setText(buttonList.get(i).getName());
            edtvNumberbtn.get(i).setText(String.valueOf(buttonList.get(i).getvNumber()));
        }
        edtThermal.setText(String.valueOf(thermal.getvNumberNhiet()));
        edtHumidity.setText(String.valueOf(thermal.getvNumberAm()));
        /**
         * tạo sự kiện khi nhấn vào button save token
         */
        btnSaveToken.setOnClickListener(v -> {
            String token = editToken.getText().toString();
            saveToken(token);

            for (int i = 0; i < 4; i++) {
                Toggle toggle = new Toggle(sw_Number.get(i).isChecked(),
                        edt_Name.get(i).getText().toString(),
                        Integer.parseInt(edt_VNumber.get(i).getText().toString()));
                toggleList.set(i, toggle);
            }
            for (int i = 0; i < 2; i++) {
                com.example.pjmk.model.Button button = new com.example.pjmk.model.Button(swbtn.get(i).isChecked(),
                        edtnamebtn.get(i).getText().toString(),
                        Integer.parseInt(edtvNumberbtn.get(i).getText().toString()));
                buttonList.set(i, button);
            }
            thermal = new Thermal(Integer.parseInt(edtThermal.getText().toString()),
                    Integer.parseInt(edtHumidity.getText().toString()));

            IO_Product io_product = new IO_Product();
            try {
                io_product.WriteButton(appContext, "Button", buttonList);
                io_product.WriteToggle(appContext, "Toggle", toggleList);
                io_product.WriteThermal(appContext, "Thermal", thermal);
            } catch (Exception e) {
//                    throw new RuntimeException(e);
                Toast.makeText(SaveTokenActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(SaveTokenActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getAppContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }


    public static Context getAppContext() {
        return appContext;
    }
}
