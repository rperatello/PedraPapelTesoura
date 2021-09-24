package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class GameSettings implements Serializable {
    private boolean isTwoPlayers;
    private int round;

    public GameSettings(boolean isTwoPlayers, int round) {
        this.isTwoPlayers = isTwoPlayers;
        this.round = round;
    }

    public boolean getIsTwoPlayers() {
        return isTwoPlayers;
    }

    public void setIsTwoPlayers(boolean twoPlayers) {
        isTwoPlayers = twoPlayers;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @NonNull
    @Override
    public String toString() {
        return "Oponentes: " + (this.isTwoPlayers == true ? "2": "3") + " | Jogadas: " + this.round;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSettings gameSettings = (GameSettings) o;
        return isTwoPlayers == gameSettings.isTwoPlayers &&
                round == gameSettings.round;
    }
}
