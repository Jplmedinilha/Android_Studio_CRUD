package com.example.cruddojoao;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {

    EditText txtNome;
    EditText txtDia;
    EditText txtValor;
    Button botao;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtNome = (EditText) findViewById(R.id.editTextNome);
        txtDia = (EditText) findViewById(R.id.txtDiaVencimento);
        txtValor = (EditText) findViewById(R.id.txtValor);
        botao = (Button) findViewById(R.id.btnInserir);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

    }

    public void cadastrar(){
        if(!TextUtils.isEmpty(txtNome.getText().toString())){
            try {
                bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);

                String sql = "INSERT INTO contas (nome, diaVencimento, valor) VALUES (?, ?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                stmt.bindString(1, txtNome.getText().toString());
                stmt.bindString(2, txtDia.getText().toString());
                stmt.bindString(3, txtValor.getText().toString());

                stmt.executeInsert();

                bancoDados.close();
                finish();

            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}