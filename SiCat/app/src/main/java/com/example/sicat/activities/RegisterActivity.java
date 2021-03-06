package com.example.sicat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicat.R;
import com.example.sicat.controllers.Base_url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // deklarasi variable
    private Spinner jenkel_customer;
    String jenkel[] = {"Pilih Jenis Kelamin", "Pria", "Wanita"};
    ArrayAdapter<String> adapter;
    String record = "";

    private EditText nm_customer, almt_customer, no_hp, email, username, password, c_password;
    private Button btn_regist;
    private ProgressBar loading;

    Base_url base_url = new Base_url();
    String url = base_url.getUrl();

    private String URL_REGIST = url + "crud_customer/tbl_customer/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // inisialisasi
        nm_customer = findViewById(R.id.nm_customer);
        almt_customer = findViewById(R.id.almt_customer);
        no_hp = findViewById(R.id.no_hp);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);

        // select option
        jenkel_customer = (Spinner) findViewById(R.id.jenkel_customer);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jenkel);
        jenkel_customer.setAdapter(adapter);
        jenkel_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:

                        record = "";

                        break;

                    case 1:

                        record = "pria";

                        break;

                    case 2:

                        record = "wanita";

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_regist = findViewById(R.id.btn_regist);

        loading = findViewById(R.id.loading);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                regist();
            }
        });

        ButterKnife.bind(this);
        initActionBar();

    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void regist() {
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);

        // mengambil nilai didalam inputan
        final String nm_customer = this.nm_customer.getText().toString().trim();
        final String almt_customer = this.almt_customer.getText().toString().trim();
        final String jenkel_customer = record;
        final String no_hp = this.no_hp.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String username = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        // http request disini
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // membuat json objek
                            JSONObject jsonObject = new JSONObject(response);
                            // mengambil data didalam json objek yang berindeks assoc success
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Register Berhasil!", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_regist.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {

                            // jika gagal untuk menerima response
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Gagal!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // jika terjadi kesalahan didalam volley
                        Toast.makeText(RegisterActivity.this, "Register Telah Gagal!" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                }) {
            // data yang akan dikirim ke file api
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nm_customer", nm_customer);
                params.put("almt_customer", almt_customer);
                params.put("jenkel_customer", jenkel_customer);
                params.put("no_hp", no_hp);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // disini untuk menjalankan atau mengeksekusi stringRequest ()
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
