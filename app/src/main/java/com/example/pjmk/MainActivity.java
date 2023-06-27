package com.example.pjmk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pjmk.model.IO_Product;
import com.example.pjmk.model.Thermal;
import com.example.pjmk.model.Toggle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView Nhiet, Am;
    private Blynk blynk;
    private final List<ToggleButton> tg = new ArrayList<>();
    private ImageView Status;
    private Button btn_0, btn_1;
    private static final int UPDATE_INTERVAL = 1000; // Thời gian cập nhật (1 giây)
    static Thermal thermal;
    static int vNumberThermal;
    static int vNumberHumidity;
    boolean send, get, token;
    private boolean online = false;
    private boolean isFetchingData = false;
    static List<Boolean> saveATV = new ArrayList<>();
    static List<String> saveNAME = new ArrayList<>();
    static List<Integer> saveVNUMBER = new ArrayList<>();
    private Handler handler;
    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        appContext = getApplicationContext();
        blynk = Blynk.getInstance(this);
        blynk.getToken(this);


        //gán id các widget
        Nhiet = findViewById(R.id.Nhiet);
        Am = findViewById(R.id.Am);

        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);

        Button btn_ChangeToken = findViewById(R.id.btn_ChangeToken);
        Status = findViewById(R.id.Status);
        handler = new Handler();

        for (int i = 0; i < 4; i++) {
            ToggleButton swNumber = findViewById(getResources().getIdentifier("tg_" + i, "id", getPackageName()));
            tg.add(swNumber);
        }

        Sync();
        fetchDataAndUpdateUI();

        //Đăng ký bộ lắng nghe sự kiện nhấn button
/**
 * -------------------------------------------------------------------------------------------------
 */
        btn_0.setOnClickListener(v -> {
            if (saveATV.get(4) && !send) {
                vibrator.vibrate(12);
                sendbtn_0();
            }
        });

        btn_1.setOnClickListener(v -> {
            if (saveATV.get(5) && !send) {
                vibrator.vibrate(12);
                sendbtn_1();
            }
        });
/**
 * -------------------------------------------------------------------------------------------------
 */

//         Đăng ký bộ lắng nghe sự kiện thay đổi trạng thái của Toggle tg number
        tg.get(0).setOnClickListener(v -> {
            pauseFetchingData();
            boolean isChecked = tg.get(0).isChecked();
            try {
                if (!get && !send && saveATV.get(0)) {
                    String value;
                    if (isChecked) {
                        value = "1";
                    } else {
                        value = "0";
                    }
                    vibrator.vibrate(12);
                    send = true;
                    blynk.sendData(saveVNUMBER.get(0), value);
                    System.out.println(value);
                    send = false;
                }
            } catch (Exception e) {
                showToast("Lỗi xảy ra: " + e.getMessage());
            }
        });

        tg.get(1).setOnClickListener(v -> {
            pauseFetchingData();
            boolean isChecked = tg.get(1).isChecked();
            try {

                if (!get && !send && saveATV.get(1)) {
                    String value;
                    if (isChecked) {
                        value = "1";
                    } else {
                        value = "0";
                    }
                    vibrator.vibrate(12);
                    send = true;
                    blynk.sendData(saveVNUMBER.get(1), value);
                    System.out.println(value);
                    send = false;
                }
            } catch (Exception e) {
                showToast("Lỗi xảy ra: " + e.getMessage());
            }
        });

        tg.get(2).setOnClickListener(v -> {
            pauseFetchingData();
            boolean isChecked = tg.get(2).isChecked();
            try {

                if (!get && !send && saveATV.get(2)) {
                    String value;
                    if (isChecked) {
                        value = "1";
                    } else {
                        value = "0";
                    }
                    vibrator.vibrate(12);
                    send = true;
                    blynk.sendData(saveVNUMBER.get(2), value);
                    System.out.println(value);
                    send = false;
                }
            } catch (Exception e) {
                showToast("Lỗi xảy ra: " + e.getMessage());
            }
        });

        tg.get(3).setOnClickListener(v -> {
            pauseFetchingData();
            boolean isChecked = tg.get(3).isChecked();
            try {

                if (!get && !send && saveATV.get(3)) {
                    String value;
                    if (isChecked) {
                        value = "1";
                    } else {
                        value = "0";
                    }
                    vibrator.vibrate(12);
                    send = true;
                    blynk.sendData(saveVNUMBER.get(3), value);
//                    System.out.println(value);
                    send = false;
                }
            } catch (Exception e) {
                showToast("Lỗi xảy ra: " + e.getMessage());
            }
        });
        /**
         * -------------------------------------------------------------------------------------------------
         */
        //Đăng ký bộ lắng nghe sự kiện nhấn button ChangeToken
        btn_ChangeToken.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SaveTokenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    public static void Sync() {
        IO_Product io_product = new IO_Product();
        saveATV.clear();
        saveNAME.clear();
        saveVNUMBER.clear();

        try {
            List<Toggle> toggle = io_product.ReadToggle(appContext, "Toggle");
            for (int i = 0; i < 4; i++) {
                saveATV.add(toggle.get(i).isActivate());
                saveNAME.add(toggle.get(i).getName());
                saveVNUMBER.add(toggle.get(i).getvNumber());
            }
            List<com.example.pjmk.model.Button> button = io_product.ReadButton(appContext, "Button");
            for (int i = 0; i < 2; i++) {
                saveATV.add(button.get(i).isActivate());
                saveVNUMBER.add(button.get(i).getvNumber());
                saveNAME.add(button.get(i).getName());
            }
            thermal = io_product.ReadThermal(appContext, "Thermal");

        } catch (Exception e) {
            for (int i = 0; i < 6; i++) {
                Toast.makeText(appContext, e.toString(), Toast.LENGTH_SHORT).show();
                saveATV.add(false);
                saveVNUMBER.add(10);
                saveNAME.add("");
            }
            thermal = new Thermal(0, 1);
        }
        vNumberThermal = thermal.getvNumberNhiet();
        vNumberHumidity = thermal.getvNumberAm();
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateDataRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateDataRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    private void startFetchingData() {
        handler.postDelayed(updateDataRunnable, UPDATE_INTERVAL);
    }

    private void pauseFetchingData() {
        // Tạm dừng handler trong 10 giây
        handler.removeCallbacks(updateDataRunnable); // Ngừng việc lập lịch chạy updateDataRunnable (nếu đang chạy)
        handler.postDelayed(this::startFetchingData, 1100); // Tạm dừng trong 10 giây (10000 milliseconds)
    }

    private final Runnable updateDataRunnable = new Runnable() {
        @Override
        public void run() {
            fetchDataAndUpdateUI();
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    /**
     * -------------------------------------------------------------------------------------------------
     */
    private void fetchDataAndUpdateUI() {
//        Sync();
        for (int i = 0; i < 4; i++) {
            tg.get(i).setActivated(saveATV.get(i));
            tg.get(i).setTextOn(saveNAME.get(i));
            tg.get(i).setTextOff(saveNAME.get(i));
        }

        btn_0.setText(saveNAME.get(4));
        btn_0.setActivated(saveATV.get(4));
        btn_1.setText(saveNAME.get(5));
        btn_1.setActivated(saveATV.get(5));

        if (!send) {
            send = true;
            if (!isFetchingData) {
                isFetchingData = true;
                isOnline();
                if (online) {
                    getNhietDo();
                    getDoAm();
                    for (int i = 0; i < 4; i++) {
                        gettg(saveATV.get(i), i, saveVNUMBER.get(i));
                    }
                }
                isFetchingData = false;
            }
            send = false;
        }
    }

    /**
     * Hiển thị Toast trên luồng giao diện chính.
     */
    private void showToast(String message) {
        // Tạo một Handler mới để hiển thị Toast trên luồng giao diện chính.
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    /**
     * Hiển thị dialog lỗi cho người dùng
     */
    private void log(String e) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Lỗi");
            builder.setMessage(String.valueOf(e));
            // Thêm nút "OK" vào hộp thoại để đóng nó.
            builder.setPositiveButton("OK", (dialog, id) -> {
                // Đóng hộp thoại và trở lại màn hình chính.
                dialog.dismiss();
            });
            // Hiển thị hộp thoại báo lỗi cho người dùng.
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    /**
     * các hàm kiểm tra trạng thái
     */
    public void getNhietDo() {
        blynk.fetchData(vNumberThermal, new Blynk.DataCallback() {
            @Override
            public void onSuccess(String data) {
                Nhiet.setText(data + " °C");
            }

            @Override
            public void onError(String errorMessage) {
                log(errorMessage);
            }
        });
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    public void getDoAm() {
        blynk.fetchData(vNumberHumidity, new Blynk.DataCallback() {
            @Override
            public void onSuccess(String data) {
                Am.setText(data + " %");
            }

            @Override
            public void onError(String errorMessage) {
                log(errorMessage);
            }
        });
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    public void gettg(boolean active, int number_tg, int VNumber) {
//        tg.get(number_tg).setActivated(saveATV.get(number_tg));
        if (active) {
            blynk.fetchData(VNumber, new Blynk.DataCallback() {
                @Override
                public void onSuccess(String data) {
                    tg.get(number_tg).setChecked(data.equals("1"));
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);

                    tg.get(number_tg).setBackgroundColor(Color.argb(1, 72, 72, 72));
                }
            });
        } else {
            tg.get(number_tg).setBackgroundColor(Color.argb(1, 72, 72, 72));
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    public void sendbtn_0() {
        send = true;
        try {
            blynk.sendData(saveVNUMBER.get(4), "1");
            Thread.sleep(500);
            blynk.sendData(saveVNUMBER.get(5), "0");
        } catch (Exception e) {
            Toast.makeText(this, "Gửi lệnh WOL thất bại", Toast.LENGTH_SHORT).show();
        }
        send = false;
    }

    public void sendbtn_1() {
        send = true;
        try {
            blynk.sendData(saveVNUMBER.get(5), "1");
            Thread.sleep(3000);
            blynk.sendData(saveVNUMBER.get(5), "0");
        } catch (Exception e) {
            Toast.makeText(this, "Gửi lệnh btn_1 thất bại", Toast.LENGTH_SHORT).show();
        }
        send = false;
    }

    /**
     * -------------------------------------------------------------------------------------------------
     */
    public void isOnline() {
        blynk.isOnline(new Blynk.DataCallback() {
            @Override
            public void onSuccess(String data) {
                if (Boolean.parseBoolean(data)) {
                    Status.setBackground(getDrawable(R.drawable.is_online));
                    token = true;
                    online = true;
                } else if (Boolean.parseBoolean(data)) {
                    Status.setBackground(getDrawable(R.drawable.is_offline));
                    token = true;
                    online = false;
                } else {
                    System.out.println(data);
                    Toast.makeText(MainActivity.this, "Token sai hoặc lỗi kết nối\n" + data, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SaveTokenActivity.class);
                    startActivity(intent);
                    token = false;
                    online = false;
                }
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });

    }
}
