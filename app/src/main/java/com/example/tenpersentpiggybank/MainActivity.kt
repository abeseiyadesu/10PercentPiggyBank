package com.example.tenpersentpiggybank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenpersentpiggybank.ui.theme.TenPersentPiggyBankTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.height
import java.lang.Integer.parseInt

// DataStoreという名前で　データを保存できる場所　ができる
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TenPersentPiggyBankTheme {
                TenPersentPiggyBankApp()

            }
        }
    }
}

// Datastoreを使う
object PreferencesKeys {
    val SAVINGAMOUNT = intPreferencesKey("savingAmount")
    val NOW_SAVINGAMOUNT = intPreferencesKey("NowSavingAmount")
}

// 保存処理
suspend fun saveSavingAmount(context: Context, SavingAmount: Int) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.SAVINGAMOUNT] = SavingAmount
    }
}

suspend fun saveNowSavingAmount(context: Context, NowSavingAmount: Int) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.NOW_SAVINGAMOUNT] = NowSavingAmount
    }
}

// データ取得
fun getSavingAmount(context: Context): Flow<Int?> {
    return context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SAVINGAMOUNT] // 取得
        }
}

fun getNowSavingAmount(context: Context): Flow<Int?> {
    return context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NOW_SAVINGAMOUNT] // 取得
        }
}

// データを削除
suspend fun deleteSavingAmount(context: Context) {
    context.dataStore.edit { preferences ->
        preferences.remove(PreferencesKeys.SAVINGAMOUNT)
    }
}

suspend fun deleteNowSavingAmount(context: Context) {
    context.dataStore.edit { preferences ->
        preferences.remove(PreferencesKeys.NOW_SAVINGAMOUNT)
    }
}

//　アプリ関数
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TenPersentPiggyBankApp() {

    // 非同期でしなければ バグやクラッシュの原因になるため
    // コールチンでで保存・削除を実行
    val scope = rememberCoroutineScope()
    val context = LocalContext.current // 現在のコンテクストを取得
    var text by remember { mutableStateOf("") }

    // Datastoreからデータを取得
    val savingAmountFlow: Flow<Int?> = getSavingAmount(context)
    val nowSavingAmountFlow: Flow<Int?> = getNowSavingAmount(context)
    // 貯金額
    val SavingAmountAsState by savingAmountFlow.collectAsState(initial = 0)
    // 貯金する予定の額
    val nowSavingAmountAsState by nowSavingAmountFlow.collectAsState(initial = 0)

    // 仮想キーボードの表示・非表示を制御するためのコントローラー
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("一割貯金箱")
                }
            )
        },
        modifier = Modifier.fillMaxSize(),

        // メインコンテンツ
        content = { paddingValues ->
            // 全体Column
            Column(
                // トップバーの padding分を 渡す
                modifier = Modifier.padding(paddingValues)
            ) {
                // 現在の貯金額表示
                Text(
                    text = "貯金額",
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 20.dp,
                    )
                )
                Text(
                    text = SavingAmountAsState.toString(),
                    modifier = Modifier.padding(
                        start = 20.dp,
                        bottom = 16.dp
                    )
                )
                Row {
                    // 入力欄
                    OutlinedTextField(
                        value = text,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() }) {
                                text = newText
                            }
                        },
                        // 入力されるまで表示されている
                        label = { Text("入力欄") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number, // 最初に出て来るキーボードを数字に設定
                            imeAction = ImeAction.Done          // 決定を押したときどうするか
                        ),

                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Button(
                        onClick = {
                            // 貯金予定額を計算
                            var total = parseInt(text) / 10
                            total += nowSavingAmountAsState ?: 0
                            scope.launch {
                                // datastoreを更新
                                saveNowSavingAmount(context, total)
                            }
                            keyboardController?.hide()  // キーボードを閉じて実行
                            text = "" // 初期化する
                        },
                        modifier = Modifier.padding(end = 16.dp, top = 16.dp)
                    ) {
                        Text("貯金する")
                    }
                }

                // ×100 1000 10000 ボタン
                Row {
                    Button(
                        onClick = {
                            text += "00"
                        }
                    ) {
                        Text("百")
                    }

                    Button(
                        onClick = {
                            text += "000"
                        }
                    ) {
                        Text("千")
                    }
                    Button(
                        onClick = {
                            text += "0000"
                        }
                    ) {
                        Text("万")
                    }
                }

                // 貯金予定額
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Column {
                        Text(
                            text = "貯金する予定額",
                            modifier = Modifier.padding(
                                top = 8.dp,
                                start = 20.dp,
                            )
                        )
                        Text(
                            text = "$nowSavingAmountAsState",
                            modifier = Modifier.padding(
                                start = 20.dp,
                                bottom = 16.dp
                            )
                        )
                    }
                    // 貯金するボタン
                    Button(
                        onClick = {
                            val total = (SavingAmountAsState ?: 0) + (nowSavingAmountAsState ?: 0)
                            scope.launch {
                                saveSavingAmount(context, total)
                                saveNowSavingAmount(context, 0)
                            }

                        },
                        modifier = Modifier.padding(start = 30.dp)

                    ) {
                        Text("貯金した")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // 貯金リセットボタン
                Button(
                    onClick = {
                        scope.launch {
                            saveSavingAmount(context, 0)
                            saveNowSavingAmount(context, 0)
                        }
                    }
                ) {
                    Text(text = "貯金をリセット")
                }
            }
        }
    )

}

@Preview
@Composable
fun TenPersentPiggyBankPreview() {
    TenPersentPiggyBankApp()
}