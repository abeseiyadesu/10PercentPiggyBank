# 一割貯金箱

「Compose を用いた Android アプリ開発の基礎」のユニット６の学習内容を主に使用しました。

## アプリ説明
「一割貯金箱」は、毎月の収入の10%を自動で貯金し、貯金目標の達成をサポートするシンプルなアプリです。

## 使用言語・技術
kotlin / Android Studio / Jetpack Compose / Room

## アプリを作った目的
「得たお金の一割は貯金したほうがいいよ」と小さな頃から親に言われていて、それの管理をよく忘れることがありました。
それをアプリで管理できると便利だなと思い作成しました

## 機能一覧
・貯金入力機能
・自動で貯金予定額を計算
・×100 ×1000 ×10000 のボタンで入力を簡略化
・貯金額を保存（DataStoreを使用）

## アピールポイント
初めて作成したアプリなのでUIなど


## 苦労した点
初めてアプリを作成したので、作っていく中で初めての体験が多く苦戦しました。（DataStore、）

最初はDataStore(データベース)を使う予定はなく、シンプルなアプリを目指していましたが、
作成していく中で、アプリを閉じてもデータが保存されているとユーザー体験がより良いものになるなと思い、DataStoreを追加しました。


