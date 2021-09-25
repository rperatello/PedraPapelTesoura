package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> settingsActivityResultLauncher;


    public static final String EXTRA_CONFIGURACOES = "EXTRA_SETTINGS";

    public final String IS_TWO_PLAYERS = "IS_TWO_PLAYERS";
    public final String NUMBER_OF_ROUNDS = "NUMBER_OF_ROUNDS";
    public final String POINTS_OPPONENT_1 = "POINTS_OPPONENT_1";
    public final String POINTS_OPPONENT_2 = "POINTS_OPPONENT_2";
    public final String POINTS_OPPONENT_3 = "POINTS_OPPONENT_3";
    public final String ROUNDS_FINISHED = "ROUNDS_FINISHED";


    private Integer p1 = 0;
    private Integer p2 = 0;
    private Integer p3 = 0;
    private Integer roundsFinished = 0;
    private Integer choiceOp1;
    private Integer[] hands = {R.mipmap.pedra, R.mipmap.papel, R.mipmap.tesoura};
    private Boolean endGame = false;


    private GameSettings gameSettings = new GameSettings(true, 1);
    private GameSettings gameSettingsBackup = new GameSettings(true, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btStart.setOnClickListener(this);
        activityMainBinding.btStone.setOnClickListener(this);
        activityMainBinding.btPaper.setOnClickListener(this);
        activityMainBinding.btScissor.setOnClickListener(this);

        Log.v(getString(R.string.app_name), "onCreate: Iniciando ciclo de vida completo");

        settingsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result != null) {
                            Intent intent = result.getData();
                            gameSettings = (GameSettings) intent.getSerializableExtra(EXTRA_CONFIGURACOES);
                            if (gameSettings != null) {
                                //Toast.makeText(MainActivity.this, gameSettings.toString(), Toast.LENGTH_LONG).show();
                                boolean isSameConfig = gameSettingsBackup.equals(gameSettings);
                                if(!isSameConfig)
                                    resetGame();
                            }
                            if (gameSettings != null && gameSettings.getIsTwoPlayers()) {
                                findViewById(R.id.imagesSection2).setVisibility(View.GONE);
                            }
                            else {
                                findViewById(R.id.imagesSection2).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );

        cleanAll();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btStart:
                startGame();
                Log.v(getString(R.string.app_name), "onClick - passou startGame");
                return;
            case R.id.btStone:
                choiceOp1 = 0;
                activityMainBinding.btStone.setBackgroundColor(Color.GRAY);
                break;
            case R.id.btPaper:
                choiceOp1 = 1;
                activityMainBinding.btPaper.setBackgroundColor(Color.GRAY);
                break;
            case R.id.btScissor:
                choiceOp1 = 2;
                activityMainBinding.btScissor.setBackgroundColor(Color.GRAY);
                break;
        }

        activityMainBinding.opponent1.setImageResource(hands[choiceOp1]);

        if (gameSettings.getIsTwoPlayers()) {
            twoPlayers();
        } else {
            threePlayers();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(getString(R.string.app_name), "onSaveInstanceState executado - salvando dados de instância");
        outState.putBoolean(IS_TWO_PLAYERS, gameSettings.getIsTwoPlayers());
        outState.putInt(NUMBER_OF_ROUNDS, gameSettings.getRound());
        outState.putInt(POINTS_OPPONENT_1, p1);
        outState.putInt(POINTS_OPPONENT_2, p2);
        outState.putInt(POINTS_OPPONENT_3, p3);
        outState.putInt(ROUNDS_FINISHED, roundsFinished);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(getString(R.string.app_name), "onRestoreInstanceState executado - restaurando dados de instância");
        if(savedInstanceState.getBoolean(IS_TWO_PLAYERS) && gameSettings != null){
            gameSettings.setIsTwoPlayers(true);
            findViewById(R.id.imagesSection2).setVisibility(View.GONE);
        };

        if(!savedInstanceState.getBoolean(IS_TWO_PLAYERS) && gameSettings != null){
            gameSettings.setIsTwoPlayers(false);
            findViewById(R.id.imagesSection2).setVisibility(View.VISIBLE);
        };

        if(gameSettings != null){
            gameSettings.setRound(savedInstanceState.getInt(NUMBER_OF_ROUNDS, 0));
        };

        p1 = savedInstanceState.getInt(POINTS_OPPONENT_1, 0);
        p2 = savedInstanceState.getInt(POINTS_OPPONENT_2, 0);
        p3 = savedInstanceState.getInt(POINTS_OPPONENT_3, 0);
        roundsFinished = savedInstanceState.getInt(ROUNDS_FINISHED, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingMi:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(EXTRA_CONFIGURACOES, gameSettings);
                settingsActivityResultLauncher.launch(settingsIntent);
                return true;
            default:
                return false;
        }
    }

    public void twoPlayers() {
        Random random = new Random(System.currentTimeMillis());
        int choiceOp2 = random.nextInt(3);

        activityMainBinding.opponent2.setImageResource(hands[choiceOp2]);
        resultFor2(choiceOp1, choiceOp2);
        return;
    }


    public void threePlayers() {
        Random random = new Random(System.currentTimeMillis());
        int choiceOp2 = random.nextInt(3);
        int choiceOp3 = !gameSettings.getIsTwoPlayers() ? random.nextInt(3) : null;

        activityMainBinding.opponent2.setImageResource(hands[choiceOp2]);
        activityMainBinding.opponent3.setImageResource(hands[choiceOp3]);
        activityMainBinding.imagesSection2.setVisibility(View.VISIBLE);
        resultFor3(choiceOp1, choiceOp2, choiceOp3);
    }

    private void checkGame(){
//        Log.v(getString(R.string.app_name), "Entrou checkGame: " + gameSettings.toString() + " roundsFinished: " + roundsFinished.toString());
        if(roundsFinished >= gameSettings.getRound()){
            endGame = true;
        }
        return;
    };

    private void resultFor2(Integer op1, Integer op2) {
        Integer res;
        res = compareResult(op1, op2);
        switch (res) {
            case 1:
                p1 += 1;
                roundsFinished += 1;
                checkGame();
                if(endGame)
                    SendResult();
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.win),Toast.LENGTH_SHORT).show();
                    cleanAll();
                }
                break;
            case -1:
                p2 += 1;
                roundsFinished += 1;
                checkGame();
                if(endGame)
                    SendResult();
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.lost),Toast.LENGTH_SHORT).show();
                    cleanAll();
                }
                break;
            default:
                roundsFinished += 1;
                checkGame();
                if(endGame)
                    SendResult();
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.toTie),Toast.LENGTH_SHORT).show();
                    cleanAll();
                }
                break;
        }
    }

    private void resultFor3(Integer op1, Integer op2, Integer op3) {
        Integer res1 = compareResult(op1, op2);
        Integer res2 = compareResult(op1, op3);
        Integer res3 = compareResult(op2, op3);

        if(op1 != op2 && op1 != op3 && op2 != op2) {
            roundsFinished += 1;
            checkGame();
            if (endGame)
                SendResult();
            else {
                Toast.makeText(MainActivity.this, "Vocês empataram!", Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 0 && res3 == 0){
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Vocês empataram!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 1 && res3 == 0){
            p1 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Você ganhou!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 0 && res3 == -1){
            p3 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Oponente 2 ganhou!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 1 && res2 == 1){
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Vocês empataram!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 0 && res2 == 1){
            p1 += 1;
            p2 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Você e o Oponente 1 ganharam a jogada!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == -1 && res3 == 1){
            p2 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Oponente 1 ganhou a jogada!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == -1 && res3 == 0){
            p2 += 1;
            p3 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Oponente 1 e Oponente 2 ganharam a jogada!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else if(res1 == 1 && res2 == 0){
            p1 += 1;
            p3 += 1;
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Você e o Oponente 3 ganharam a jogada!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
        else {
            roundsFinished += 1;
            checkGame();
            if(endGame)
                SendResult();
            else{
                Toast.makeText(MainActivity.this, "Vocês emparatam!",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        }
    }

    private Integer compareResult(Integer i1, Integer i2) {
        if (i1 == i2) {
            return 0;
        } else if ((i1 - i2 == -2) || (i1 - i2 == 1)) {
            return 1;
        } else {
            return -1;
        }
    }

    private String ckWinner(){
//        Log.v(getString(R.string.app_name), "Entrou ckWinner");
        if(p1 == p2 && p1 == p3){ return "Os jogadores empataram!"; }
        else if(p1 > p2 && p1 > p3){ return "Você venceu o jogo!"; }
        else if(p1 > p2 && p1 == p3){ return "Você e o Oponente 2 empataram!"; }
        else if(p1 > p2 && p1 < p3){ return "Oponente 2 venceu o jogo!"; }
        else if(p1 == p2 && p1 > p3){ return "Você e o Oponente 1 empataram!"; }
        else if(p1 < p2 && p2 > p3){ return "Oponente 1 venceu o jogo!"; }
        else if(p1 < p2 && p2 == p3){ return "Oponente 1 e Oponente 2 empataram!"; }
        else if(p1 < p2 && p2 < p3){ return "Oponente 2 venceu o jogo!"; }
        return "Vocês empataram!";
    }

    private void SendResult(){
        Log.v(getString(R.string.app_name), "Entrou SendResult");
        resetGame();
        activityMainBinding.resultSection.setText(ckWinner());
        activityMainBinding.resultSection.setVisibility(View.VISIBLE);
    }

    public void cleanAll() {
        Log.v(getString(R.string.app_name), "Entrou cleanAll");
        activityMainBinding.btStone.setBackgroundColor(Color.WHITE);
        activityMainBinding.btPaper.setBackgroundColor(Color.WHITE);
        activityMainBinding.btScissor.setBackgroundColor(Color.WHITE);
        return;
    }

    private void startGame() {
        Log.v(getString(R.string.app_name), "Entrou startGame");
        activityMainBinding.resultSection.setVisibility(View.GONE);
        activityMainBinding.btStart.setVisibility(View.GONE);
        activityMainBinding.lbBtSection.setVisibility(View.VISIBLE);
        activityMainBinding.btSection.setVisibility(View.VISIBLE);
        p1 = 0;
        p2 = 0;
        p3 = 0;
        setNewMatch();
        return;
    }

    private void setNewMatch() {
        Log.v(getString(R.string.app_name), "Entrou setNewMatch");
        activityMainBinding.opponent1.setImageResource(hands[0]);
        activityMainBinding.opponent2.setImageResource(hands[0]);
        activityMainBinding.opponent3.setImageResource(hands[0]);
        cleanAll();
        return;
    }

    private void resetGame(){
        if(gameSettings.getIsTwoPlayers())
            activityMainBinding.imagesSection2.setVisibility(View.GONE);
        else
            activityMainBinding.imagesSection2.setVisibility(View.VISIBLE);
        activityMainBinding.resultSection.setVisibility(View.GONE);
        activityMainBinding.lbBtSection.setVisibility(View.GONE);
        activityMainBinding.btSection.setVisibility(View.GONE);
        activityMainBinding.btStart.setVisibility(View.VISIBLE);
        roundsFinished = 0;
        endGame = false;
        return;
    }

}