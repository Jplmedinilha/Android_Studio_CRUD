package com.example.cruddojoao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlterActivity extends AppCompatActivity {

    private  EditText txtNome;
    private  EditText txtDia;
    private  EditText txtValor;
    private  Button botao;
    private  SQLiteDatabase bancoDados;

    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);

        txtNome = (EditText) findViewById(R.id.editTextNome);
        txtDia = (EditText) findViewById(R.id.txtDiaVencimento);
        txtValor = (EditText) findViewById(R.id.txtValor);
        botao = (Button) findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        carregarDados();

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }
    public void carregarDados(){
        try{

            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);

            Cursor cursor = bancoDados.rawQuery("SELECT id, nome, diaVencimento, valor FROM contas WHERE id =" + id.toString(), null);

            cursor.moveToFirst();

            txtNome.setText(cursor.getString(1));
            txtDia.setText(cursor.getString(2));
            txtValor.setText(cursor.getString(3));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(){
        String valueNome = txtNome.getText().toString();
        String valueDia = txtDia.getText().toString();
        String valueValor = txtValor.getText().toString();

        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE contas SET nome = ?, diaVencimento = ?, valor = ? WHERE id = ? ";

            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1, valueNome);
            stmt.bindString(2, valueDia);
            stmt.bindString(3, valueValor);
            stmt.bindLong(4, id);

            stmt.executeUpdateDelete();
            bancoDados.close();


        } catch (Exception e){
            e.printStackTrace();
        }

        finish();
    }
}