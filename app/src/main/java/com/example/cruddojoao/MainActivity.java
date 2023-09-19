package com.example.cruddojoao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public ListView listViewContas;
    public Button botao;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;

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

        listViewContas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                confirmaExcluir();
                return true;
            }
        });

        listViewContas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirUpdate();
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
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);

            Cursor cursor = bancoDados.rawQuery("SELECT id, nome FROM contas;", null);

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
                arrayIds.add(cursor.getInt(0));
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

    public void excluir(){
        Toast.makeText(this, "Deleting " + idSelecionado.toString() + "...", Toast.LENGTH_LONG).show();
        try {

            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM contas WHERE id = ?";

            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindLong(1, idSelecionado);

            stmt.executeUpdateDelete();
            listarContas();

            bancoDados.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void confirmaExcluir(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja excluir esse registro?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                excluir();
                // listarDados();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void abrirUpdate(){
        Intent intent = new Intent ( this, AlterActivity.class);
        intent.putExtra("id", idSelecionado);
        startActivity(intent);
    }

}