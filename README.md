# 一割貯金箱
 [「Compose を用いた Android アプリ開発の基礎」](https://developer.android.com/courses/android-basics-compose/course?hl=ja)のユニット６の学習内容を主に使用しました。
<br>  

## アプリ説明
「一割貯金箱」は、毎月の収入の10%を自動で貯金し、貯金目標の達成をサポートするシンプルなアプリです。
<br>  

## 使用言語・技術
kotlin / Android Studio / Jetpack Compose / Room
<br>  

## アプリを作った目的
子どもの頃から「収入の一割は貯金すると良い」と言われていましたが、管理が難しく忘れがちでした。
そこで、収入の10%を自動で貯金できるアプリを作成しました。
<br>  

## 機能一覧
 - 貯金入力機能
 - 自動で貯金予定額を計算
 - ×100 ×1000 ×10000 のボタンで入力を簡略化
 - 貯金額を保存（DataStoreを使用）
<br>  

## こだわった点
### **データ保存にDataStore**
最初はシンプルなアプリを目指していましたが、作成していく中で、
ユーザーがアプリを閉じてもデータが保存できるように、DataStoreを追加しました。
Room も考えましたが、調べると単純な Key-Value 形式の保存がこのアプリに適していると判断し、DataStoreを選びました。
<br>  

## 苦労した点
初めてのアプリ開発だったため、多くの新しい技術に触れました。
特に DataStore の導入には苦労しましたが、公式ドキュメントを読み込み、実際にコードを書いて試行錯誤しながら理解を深めました。
<br>  

### **学んだこと**
 - Jetpack Composeの基本的な UI実装
 - DataStore を用いたデータ保存
<br>  


![10%PiggyBank](https://github.com/user-attachments/assets/daa4b65f-d83c-4b6b-ae61-f4fc27785ed1)

