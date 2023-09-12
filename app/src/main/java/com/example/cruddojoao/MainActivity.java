package com.example.cruddojoao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public ListView listViewContas;
    public Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContas = (ListView) findViewById(R.id.listViewContas);
        botao = (Button) findViewById(R.id.buttonCadastrar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });

        criarDb();

        listarContas();
    }

    @Override
    protected void onResume(){
        super.onResume();
        listarContas();
    }

    public void criarDb(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS contas("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR, " +
                    "diaVencimento VARCHAR, " +
                    "valor VARCHAR)");
            bancoDados.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listarContas(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);

            Cursor cursor = bancoDados.rawQuery("SELECT nome FROM contas;", null);

            ArrayList<String> linhas = new ArrayList<String>();

            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );

            listViewContas.setAdapter(meuAdapter);
            cursor.moveToFirst();
            while(cursor != null){
                linhas.add(cursor.getString(1));
                cursor.moveToNext();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirCadastro(){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

}