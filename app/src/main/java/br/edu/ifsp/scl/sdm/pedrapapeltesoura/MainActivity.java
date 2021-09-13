package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding activityMainBinding;

    private Integer numberOfOpponents;
    private Integer choiceOp1;
    private Integer[] hands = {R.mipmap.pedra, R.mipmap.papel, R.mipmap.tesoura};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btStone.setOnClickListener(this);
        activityMainBinding.btPaper.setOnClickListener(this);
        activityMainBinding.btScissor.setOnClickListener(this);

        numberOfOpponents = activityMainBinding.opponentRGroup.getCheckedRadioButtonId() == R.id.radio1Rb ? 1 : 2;
        cleanAll();

    }

    @Override
    public void onClick(View view) {
        numberOfOpponents = activityMainBinding.opponentRGroup.getCheckedRadioButtonId() == R.id.radio1Rb ? 1 : 2;
        cleanAll();

        switch (view.getId()) {
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

        if (numberOfOpponents == 1) {
            twoPlayers();
        } else {
            threePlayers();
        }
    }


    public void twoPlayers() {
        Random random = new Random(System.currentTimeMillis());
        int choiceOp2 = random.nextInt(3);

        activityMainBinding.opponent2.setImageResource(hands[choiceOp2]);
        activityMainBinding.resultSection.setVisibility(View.VISIBLE);
        activityMainBinding.imagesSection2.setVisibility(View.GONE);
        activityMainBinding.resultSection.setText(resultFor2(choiceOp1, choiceOp2));
        activityMainBinding.resultSection.setVisibility(View.VISIBLE);
    }


    public void threePlayers() {
        Random random = new Random(System.currentTimeMillis());
        int choiceOp2 = random.nextInt(3);
        int choiceOp3 = numberOfOpponents == 2? random.nextInt(3) : null;

        activityMainBinding.opponent2.setImageResource(hands[choiceOp2]);
        activityMainBinding.opponent3.setImageResource(hands[choiceOp3]);
        activityMainBinding.imagesSection2.setVisibility(View.VISIBLE);
        activityMainBinding.resultSection.setText(resultFor3(choiceOp1, choiceOp2, choiceOp3));
        activityMainBinding.resultSection.setVisibility(View.VISIBLE);
    }



    private String resultFor2(Integer i1, Integer i2) {
        Integer res;
        res = compareResult(i1, i2);
        switch (res) {
            case 1: return getString(R.string.win);
            case -1: return getString(R.string.lost);
            default: return getString(R.string.toTie);
        }
    }


    private String resultFor3(Integer v1, Integer v2, Integer v3) {
        Integer res1;
        Integer res2;
        Integer soma;
        res1 = compareResult(v1, v2);
        res2 = compareResult(v1, v3);
        soma = res1 + res2;

        switch (soma) {
            case 0: return getString(R.string.toTie);
            case 2: return getString(R.string.win);
            default: return getString(R.string.lost);
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


    public void cleanAll() {
        activityMainBinding.btStone.setBackgroundColor(Color.WHITE);
        activityMainBinding.btPaper.setBackgroundColor(Color.WHITE);
        activityMainBinding.btScissor.setBackgroundColor(Color.WHITE);
    }

}