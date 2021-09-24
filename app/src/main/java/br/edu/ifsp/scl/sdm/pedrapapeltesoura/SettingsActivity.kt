package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    private lateinit var gameSettings: GameSettings
    private lateinit var gameSettingsBackup: GameSettings


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)
//        setContentView(R.layout.activity_settings)

        supportActionBar?.subtitle = "Configurações"

        // Recuperar o parâmetro que veio pela Intent da MainActivity
//        activitySettingsBinding = intent.getParcelableExtra(MainActivity.EXTRA_CONFIGURACOES) ?: Settings(false)

        gameSettings = (intent.getSerializableExtra(MainActivity.EXTRA_CONFIGURACOES) ?: GameSettings(true, 1)) as GameSettings
        gameSettingsBackup = (intent.getSerializableExtra(MainActivity.EXTRA_CONFIGURACOES) ?: GameSettings(true, 1)) as GameSettings

        Log.v(
            getString(R.string.app_name),
            "Roger | " + gameSettings.isTwoPlayers.toString()+" | " + gameSettings.round.toString()
        )

        activitySettingsBinding.opponentRGroup.check(if(gameSettings.isTwoPlayers) R.id.radio1Rb else R.id.radio2Rb)
        activitySettingsBinding.roundRGroup.check(if(gameSettings.round == 1) R.id.round1Rb else if(gameSettings.round == 3) R.id.round3Rb else R.id.round5Rb)
    }

    fun onClick(view: View) {
        if (view.id == R.id.saveBt) {
            // Enviar a configuração de volta para a MainActivity
            gameSettings.isTwoPlayers = activitySettingsBinding.radio1Rb.isChecked
            gameSettings.round = if(activitySettingsBinding.round1Rb.isChecked) 1 else if(activitySettingsBinding.round3Rb.isChecked) 3 else 5

            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_CONFIGURACOES, gameSettings)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        if(view.id == R.id.cancelBt){
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_CONFIGURACOES, gameSettingsBackup)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}